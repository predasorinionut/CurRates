package com.github.predasorinionut.currates.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.predasorinionut.currates.R
import com.github.predasorinionut.currates.business.models.CurrencyModel
import com.github.predasorinionut.currates.common.GlideApp
import kotlinx.android.synthetic.main.currency_rate_item.view.*
import java.math.MathContext

class RateListAdapter : RecyclerView.Adapter<RateListAdapter.CurrencyViewHolder>() {
    private var recyclerView: RecyclerView? = null
    var currencyModelList = mutableListOf<CurrencyModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder =
        CurrencyViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.currency_rate_item, parent, false)
        )

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bindCurrencyRate(currencyModelList[position])
    }

    override fun getItemCount(): Int = currencyModelList.size

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    fun updateCurrencyModels(rates: Map<String, String>) {
        for (i in 1 until currencyModelList.size) {
            currencyModelList[i].rate = rates[currencyModelList[i].code]?.toBigDecimal()?.multiply(
                currencyModelList[0].rate,
                MathContext.DECIMAL64
            ) ?: currencyModelList[i].rate
        }
        notifyItemRangeChanged(1, itemCount - 1)
    }

    inner class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            with(itemView.currencyAmount) {
                addTextChangedListener(object : TextWatcher {
                    val inputRegex =
                        """(0(.[0-9]?[1-9]?)?)|([1-9][0-9]{0,6}((.[0-9]?[1-9]?)|([0-9]{0,2}))?)""".toRegex()
                    val ratesRegex = """(0(.[0-9]?[1-9])?)|([1-9][0-9]*(.[0-9]?[1-9])?)""".toRegex()

                    override fun afterTextChanged(source: Editable?) {
                        source?.let {

                            val result: String = if (adapterPosition == 0) {
                                inputRegex.find(source)?.value ?: ""
                            } else {
                                ratesRegex.find(source)?.value ?: ""
                            }

                            if (result != it.toString())
                                it.replace(0, it.length, result)
                        }
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(source: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (adapterPosition == 0)
                            source?.toString()?.toBigDecimalOrNull().let {
                                currencyModelList[0].rate = it ?: java.math.BigDecimal.ZERO
                                recyclerView?.post {
                                    notifyItemRangeChanged(1, itemCount - 1)
                                }
                            }
                    }
                })
                onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                    if (hasFocus && adapterPosition != 0) {
                        val rate = currencyModelList[adapterPosition]
                        currencyModelList.removeAt(adapterPosition)
                        currencyModelList.add(0, rate)
                        notifyItemMoved(adapterPosition, 0)
                        (context as MainActivity).subscribeToRates()
                    }
                }
            }
        }

        fun bindCurrencyRate(currency: CurrencyModel) {
            with(currency) {
                GlideApp.with(itemView.context.applicationContext)
                    .load(flagId)
                    .placeholder(R.drawable.flag_placeholder)
                    .into(itemView.currencyCountryFlag)

                itemView.currencyCountryCode.text = this.code
                itemView.currencyName.text = name
                if (adapterPosition == 0) {
                    itemView.currencyAmount.setText("${currencyModelList[0].rate}")
                } else {
                    itemView.currencyAmount.setText(if (rate > java.math.BigDecimal.ZERO) rate.toString() else "")
                }
            }
        }
    }
}