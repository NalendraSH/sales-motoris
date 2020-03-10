package com.salesmotoris.activity.detailtransaction

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salesmotoris.R
import com.salesmotoris.formatCurrency
import com.salesmotoris.model.DetailTransaction
import kotlinx.android.synthetic.main.item_detail_transaction.view.*

class DetailTransactionAdapter : RecyclerView.Adapter<DetailTransactionAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val detailTransactions: MutableList<DetailTransaction.DetailTransaction> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_detail_transaction, parent, false))
    }

    override fun getItemCount(): Int = detailTransactions.size

    fun addAllDetailTransaction(detailTransactions: MutableList<DetailTransaction.DetailTransaction>) {
        this.detailTransactions.clear()
        this.detailTransactions.addAll(detailTransactions)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(detailTransactions[position], context)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bindView(detailTransaction: DetailTransaction.DetailTransaction, context: Context) {
            val product = "${detailTransaction.product} ${detailTransaction.quantity} ${detailTransaction.unit}"
            itemView.textview_item_detail_transaction_product_name.text = product
            itemView.textview_item_detail_transaction_price.text = context.getString(R.string.display_currency, detailTransaction.sub_total?.toFloat()?.formatCurrency())
        }

    }
}