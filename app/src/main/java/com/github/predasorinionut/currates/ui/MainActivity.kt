package com.github.predasorinionut.currates.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.predasorinionut.currates.R
import com.github.predasorinionut.currates.common.GlideApp
import com.github.predasorinionut.currates.di.qualifiers.ForCurrencyFlags
import com.github.predasorinionut.currates.vm.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    @ForCurrencyFlags
    lateinit var currencyFlagsMap: Map<String, Int>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var mainViewModel: MainViewModel
    private val currencyListAdapter = CurrencyListAdapter()
    private var ratesDisposable: Disposable? = null
    private var currenciesDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        preloadFlags()
        setupCurrenciesRecyclerView()
        showUIStateLayout()

        // TODO: handle the case when the subscribe is made for the first time and
        //  there is no internet connection or an error occurs
        subscribeToCurrencies()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeCurrenciesSubscription()
    }

    override fun onResume() {
        super.onResume()
        subscribeToRates()
    }

    override fun onPause() {
        super.onPause()
        disposeRatesSubscription()
    }

    private fun preloadFlags() {
        val currencyFlagDimension = resources.getDimensionPixelSize(R.dimen.currency_flag_dimension)

        currencyFlagsMap.forEach {
            GlideApp.with(applicationContext)
                .load(it.value)
                .placeholder(R.drawable.flag_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .fitCenter()
                .preload(currencyFlagDimension, currencyFlagDimension)
        }
    }

    private fun setupCurrenciesRecyclerView() {
        rates?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rates?.itemAnimator = null
        rates?.setItemViewCacheSize(2)
        rates.adapter = currencyListAdapter
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

    private fun subscribeToCurrencies() {
        disposeCurrenciesSubscription()

        if (currencyListAdapter.currencyModelList.size > 0) return

        currenciesDisposable = mainViewModel.getCurrencyModels()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { uiState ->
                when {
                    uiState.isError() -> {
                        showUIStateLayout(false, getString(R.string.general_error_message))
                        Toast.makeText(this, uiState.getErrorMessage(), Toast.LENGTH_SHORT).show()
                    }
                    uiState.isSuccess() -> {
                        currencyListAdapter.currencyModelList = uiState.getData().toMutableList()
                        currencyListAdapter.notifyDataSetChanged()
                        subscribeToRates()
                        showUIStateLayout(false)
                    }
                    uiState.isEmpty() -> {
                        showUIStateLayout(false, getString(R.string.no_items_message))
                    }
                }
            }
    }

    private fun disposeCurrenciesSubscription() {
        currenciesDisposable?.dispose()
        currenciesDisposable = null
    }

    fun subscribeToRates() {
        disposeRatesSubscription()

        if (currencyListAdapter.currencyModelList.size == 0) return

        ratesDisposable = mainViewModel.updateRates(currencyListAdapter.currencyModelList[0].code)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                currencyListAdapter.updateCurrencyModels(mainViewModel.rates)
            }
    }

    private fun disposeRatesSubscription() {
        ratesDisposable?.dispose()
        ratesDisposable = null
    }
}