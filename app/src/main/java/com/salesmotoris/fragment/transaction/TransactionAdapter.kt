package com.salesmotoris.fragment.transaction

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salesmotoris.R
import com.salesmotoris.activity.detailtransaction.DetailTransactionActivity
import com.salesmotoris.formatCurrency
import com.salesmotoris.model.Transaction
import kotlinx.android.synthetic.main.item_transaction.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.intentFor

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val transactions: MutableList<Transaction.Item> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false))
    }

    override fun getItemCount(): Int = transactions.size

    fun addAllTransaction(transactions: MutableList<Transaction.Item>){
        this.transactions.clear()
        this.transactions.addAll(transactions)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(transactions[position], context)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailTransactionActivity::class.java)
            intent.putExtra("transaction_id", transactions[position].id)
            intent.putExtra("store", transactions[position].store)
            (context as Activity).startActivityForResult(intent, 1)
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bindView(transaction: Transaction.Item, context: Context){
            itemView.textview_item_transaction_store.text = transaction.store
            if (transaction.visitation_status == "DONE") {
                itemView.textview_item_transaction_status.text = context.getString(R.string.visitation_status_done)
                if (transaction.total_items != "0") {
                    //DONE
                    itemView.container_item_transaction_header.backgroundColor = context.resources.getColor(R.color.colorDone)
                    itemView.textview_item_transaction_items.text = context.getString(R.string.display_item_transactions_items, transaction.total_items)
                    itemView.textview_item_transaction_income.text = context.getString(R.string.display_item_transactions_income, transaction.total_income.toFloat().formatCurrency())
                } else {
                    //EMPTY
                    itemView.container_item_transaction_header.backgroundColor = context.resources.getColor(R.color.colorEmpty)
                    itemView.container_item_transaction_content.visibility = View.GONE
                    itemView.textview_item_transaction_empty.visibility = View.VISIBLE
                }
            } else {
                //NOT YET
                itemView.textview_item_transaction_status.text = context.getString(R.string.visitation_status_not_yet)
                itemView.container_item_transaction_header.backgroundColor = context.resources.getColor(R.color.colorNotYet)
                itemView.container_item_transaction_content.visibility = View.GONE
                itemView.textview_item_transaction_empty.visibility = View.INVISIBLE
            }
        }

    }
}