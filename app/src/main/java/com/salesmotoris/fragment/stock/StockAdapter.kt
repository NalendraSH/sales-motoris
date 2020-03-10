package com.salesmotoris.fragment.stock

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salesmotoris.R
import com.salesmotoris.model.Stock
import kotlinx.android.synthetic.main.item_stock.view.*

class StockAdapter : RecyclerView.Adapter<StockAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val stock: MutableList<Stock.Data> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_stock, parent, false))
    }

    override fun getItemCount(): Int = stock.size

    fun addAllStock(stocks: MutableList<Stock.Data>){
        stock.clear()
        stock.addAll(stocks)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(stock[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bindView(stock: Stock.Data){
            itemView.textview_item_stock_product_name.text = stock.product
            itemView.textview_item_stock_qty.text = stock.quantity
        }

    }
}