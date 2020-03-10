package com.salesmotoris.activity.takestock

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.salesmotoris.R
import com.salesmotoris.model.Product
import kotlinx.android.synthetic.main.item_products.view.*

class ProductAdapter (internal var context: Context, private var products: MutableList<Product.Data>): BaseAdapter() {

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_products, null)
        view.textview_item_take_stock_product_name.text = products[position].name
        return view
    }

    override fun getItem(position: Int): Product.Data = products[position]

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = products.size

}