package com.salesmotoris.activity.inputdetailtransaction

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.salesmotoris.R
import com.salesmotoris.activity.takestock.ProductAdapter
import com.salesmotoris.api.ApiManager
import com.salesmotoris.formatCurrency
import com.salesmotoris.library.SalesMotorisPref
import com.salesmotoris.model.DetailTransaction
import kotlinx.android.synthetic.main.activity_take_stock.view.*
import kotlinx.android.synthetic.main.item_input_detail_transaction.view.*
import org.jetbrains.anko.toast
import java.lang.NumberFormatException

class InputDetailTransactionAdapter : RecyclerView.Adapter<InputDetailTransactionAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val detailTransactions: MutableList<DetailTransaction.DetailTransaction> = mutableListOf()
    private lateinit var listener: OnQuantityChanged

    interface OnQuantityChanged {
        fun onQuantityChanged(position: Int, quantity: String)

        fun onItemChange(position: Int, productName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_input_detail_transaction, parent, false))
    }

    override fun getItemCount(): Int = detailTransactions.size

    fun addDetailTransactions(detailTransactions: MutableList<DetailTransaction.DetailTransaction>) {
        this.detailTransactions.clear()
        this.detailTransactions.addAll(detailTransactions)
    }

    fun addDetailTransaction(detailTransaction: DetailTransaction.DetailTransaction) {
        this.detailTransactions.add(detailTransaction)
        notifyDataSetChanged()
    }

    fun addChangeListener(onQuantityChanged: OnQuantityChanged) {
        listener = onQuantityChanged
    }

    fun getDetailTransaction(): MutableList<DetailTransaction.DetailTransaction>{
        return this.detailTransactions
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(detailTransactions[position], context)
        holder.itemView.edittext_take_stock_qty.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                listener.onQuantityChanged(position, charSequence.toString())
            }
        })
        holder.itemView.spinner_input_detail_transaction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                listener.onItemChange(position, detailTransactions[position].product)
            }
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bindView(detailTransaction: DetailTransaction.DetailTransaction, context: Context) {
            ApiManager.getProducts(SalesMotorisPref(context).accessToken!!)
                .doOnError { context.toast(it.toString()) }
                .subscribe {
                    var selectedPosition = 0
                    for (product in it.data) {
                        if (product.name == detailTransaction.product) {
                            selectedPosition = product.id.toInt() - 1
                        }
                    }
                    itemView.spinner_input_detail_transaction.adapter = ProductAdapter(context, it.data)
                    itemView.spinner_input_detail_transaction.setSelection(selectedPosition)
                    itemView.spinner_input_detail_transaction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }

                        override fun onItemSelected(
                            parentView: AdapterView<*>?,
                            selectedItemView: View?,
                            position: Int,
                            id: Long
                        ) {
                            itemView.textview_input_detail_transaction_unit.text = it.data[position].unit
                            itemView.textview_input_detail_transaction_price.text = context.getString(R.string.display_currency, it.data[position].price.toFloat().formatCurrency())
                        }

                    }
                    itemView.edittext_input_detail_transaction_qty.setText(detailTransaction.quantity)
                }
        }

    }
}