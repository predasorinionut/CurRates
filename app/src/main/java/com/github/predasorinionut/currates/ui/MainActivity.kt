package com.github.predasorinionut.currates.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.predasorinionut.currates.R
import com.github.predasorinionut.currates.vm.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var mainViewModel: MainViewModel
    private val rateListAdapter = RateListAdapter()
    private var ratesDisposable: Disposable? = null

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        rates?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rates?.itemAnimator = null
        rates?.setItemViewCacheSize(2)
        rates.adapter = rateListAdapter

        showUIStateLayout()

        mainViewModel.getCurrencyModels("EUR", 0.0)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { uiState ->
                when {
                    uiState.isError() -> {
                        showUIStateLayout(false)
                        Toast.makeText(this, uiState.getErrorMessage(), Toast.LENGTH_LONG).show()
                    }
                    uiState.isSuccess() -> {
                        rateListAdapter.currencyModelList = uiState.getData().toMutableList()
                        rateListAdapter.notifyItemRangeChanged(1, rateListAdapter.itemCount - 1)
                        subscribeToRates()
                        showUIStateLayout(false)
                    }
                    uiState.isEmpty() -> {
                        showUIStateLayout(false, getString(R.string.no_items_message))
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        if (ratesDisposable == null)
            subscribeToRates()
    }

    override fun onPause() {
        super.onPause()
        disposeRatesSubscription()
    }

    /**
     * By default it shows loading state
     */
    private fun showUIStateLayout(isLoading: Boolean = true, message: String = "") {
        uiStateProgress?.visibility = if (message.isBlank() && isLoading) View.VISIBLE else View.INVISIBLE
        uiStateMessage?.text = message
        uiStateMessage?.visibility = if (message.isNotBlank()) View.VISIBLE else View.INVISIBLE
        uiStateLayout?.visibility =
            if (
                uiStateProgress?.visibility == View.VISIBLE ||
                uiStateMessage?.visibility == View.VISIBLE
            )
                View.VISIBLE
            else
                View.INVISIBLE
    }

    fun subscribeToRates() {
        disposeRatesSubscription()

        if (rateListAdapter.currencyModelList.size == 0) return

        ratesDisposable = mainViewModel.updateRates(rateListAdapter.currencyModelList[0].code)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                rateListAdapter.updateCurrencyModels(mainViewModel.rates)
            }
    }

    private fun disposeRatesSubscription() {
        ratesDisposable?.dispose()
        ratesDisposable = null
    }
}