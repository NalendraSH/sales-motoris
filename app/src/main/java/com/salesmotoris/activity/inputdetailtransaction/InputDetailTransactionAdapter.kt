package com.salesmotoris.activity.inputdetailtransaction

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.salesmotoris.R
import com.salesmotoris.activity.takestock.ProductAdapter
import com.salesmotoris.api.ApiManager
import com.salesmotoris.formatCurrency
import com.salesmotoris.library.SalesMotorisPref
import com.salesmotoris.model.DetailTransaction
import kotlinx.android.synthetic.main.item_input_detail_transaction.view.*

class InputDetailTransactionAdapter : RecyclerView.Adapter<InputDetailTransactionAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val detailTransactions: MutableList<DetailTransaction.DetailTransaction> = mutableListOf()
    private lateinit var scrollListener: OnScroll
    private lateinit var productListener: OnProductModified

    interface OnScroll {
        fun onScrollToBottom()
    }

    interface OnProductModified {
        fun onProductModified(detailTransaction: DetailTransaction.DetailTransaction?, position: Int, isQuantityUpdated: Boolean)
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
        notifyItemInserted(detailTransactions.size - 1)
        notifyItemRangeInserted(detailTransactions.size - 1, detailTransactions.size)
    }

    fun addScrollListener(onScroll: OnScroll) {
        scrollListener = onScroll
    }

    fun addProductListener(onModified: OnProductModified) {
        productListener = onModified
    }

    fun removeTransaction(position: Int) {
        detailTransactions.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, detailTransactions.size)
        productListener.onProductModified(null, position, false)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (::scrollListener.isInitialized) {
            holder.bindView(detailTransactions[position], position, context, scrollListener, productListener)
        } else {
            holder.bindView(detailTransactions[position], position, context, null, productListener)
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        val viewBackground: ConstraintLayout by lazy { itemView.constraint_item_input_detail_transaction_background }
        val viewForeground: ConstraintLayout by lazy { itemView.constraint_item_input_detail_transaction_foreground }

        fun bindView(
            detailTransaction: DetailTransaction.DetailTransaction,
            adapterPosition: Int,
            context: Context,
            scrollListener: OnScroll?,
            productListener: OnProductModified)
        {
            itemView.constraint_item_input_detail_transaction_background.visibility = View.GONE
            itemView.constraint_item_input_detail_transaction_foreground.visibility = View.GONE
            itemView.progress_item_input_detail_transaction.visibility = View.VISIBLE
            ApiManager.getProducts(SalesMotorisPref(context).accessToken!!)
                .doOnError { Log.d("product_error", it.message.toString()) }
                .subscribe {
                    itemView.constraint_item_input_detail_transaction_background.visibility = View.VISIBLE
                    itemView.constraint_item_input_detail_transaction_foreground.visibility = View.VISIBLE
                    itemView.progress_item_input_detail_transaction.visibility = View.GONE
                    var selectedPosition = 0
                    it.data.forEach {product ->
                        if (product.name == detailTransaction.product) {
                            selectedPosition = product.id - 1
                        }
                    }
                    itemView.spinner_input_detail_transaction.adapter = ProductAdapter(context, it.data)
                    itemView.spinner_input_detail_transaction.setSelection(selectedPosition)
                    itemView.edittext_input_detail_transaction_qty.setText(detailTransaction.quantity.toString())
                    itemView.edittext_input_detail_transaction_qty.requestFocus()
                    scrollListener?.onScrollToBottom()

                    //spinner listener
                    itemView.spinner_input_detail_transaction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }

                        override fun onItemSelected(
                            parentView: AdapterView<*>?,
                            selectedItemView: View?,
                            spinnerPosition: Int,
                            id: Long
                        ) {
                            itemView.textview_input_detail_transaction_unit.text = it.data[spinnerPosition].unit
                            itemView.textview_input_detail_transaction_price.text = context.getString(R.string.display_currency, it.data[spinnerPosition].price.toFloat().formatCurrency())

                            val qty: Int = try {
                                itemView.edittext_input_detail_transaction_qty.text.toString().toInt()
                            } catch (e: NumberFormatException) {
                                0
                            }
                            val detail = DetailTransaction.DetailTransaction(
                                it.data[spinnerPosition].name,
                                it.data[spinnerPosition].price,
                                it.data[spinnerPosition].unit,
                                qty,
                                (it.data[spinnerPosition].price * qty)
                            )
                            productListener.onProductModified(detail, adapterPosition, false)
                        }
                    }
                    //quantity listener
                    itemView.edittext_input_detail_transaction_qty.addTextChangedListener { quantity ->
                        val qty: Int = try {
                            quantity.toString().toInt()
                        }catch (e: NumberFormatException) {
                            0
                        }
                        val detail = DetailTransaction.DetailTransaction(
                            null,
                            null,
                            null,
                            qty,
                            null
                        )
                        productListener.onProductModified(detail, adapterPosition, true)
                    }
                }
        }

    }
}