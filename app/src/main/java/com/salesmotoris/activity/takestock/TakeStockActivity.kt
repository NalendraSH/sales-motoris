package com.salesmotoris.activity.takestock

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.salesmotoris.R
import com.salesmotoris.library.SalesMotorisPref
import com.salesmotoris.model.Meta
import com.salesmotoris.model.Product
import com.salesmotoris.mvp.BaseMvpActivity
import kotlinx.android.synthetic.main.activity_take_stock.*
import org.jetbrains.anko.toast


class TakeStockActivity : BaseMvpActivity<TakeStockContract.View, TakeStockContract.Presenter>(), TakeStockContract.View {

    override var mPresenter: TakeStockContract.Presenter = TakeStockPresenter()
    private var productId = ""

    override fun showProducts(response: Product.ProductResponse) {
        progress_take_stock_main.visibility = View.GONE
        container_take_stock.visibility = View.VISIBLE

        spinner_take_stock.adapter = ProductAdapter(this, response.data)
        spinner_take_stock.setSelection(0)
        spinner_take_stock.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                textview_take_stock_unit.text = response.data[position].unit
                productId = response.data[position].id
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }
    }

    override fun showSubmitResponse(response: Meta) {
        progress_take_stock_submit.visibility = View.GONE
        button_take_stock.visibility = View.VISIBLE

        toast(response.message)
        resetUi()
    }

    override fun hideMainProgress() {
        progress_take_stock_main.visibility = View.GONE
        container_take_stock.visibility = View.VISIBLE
    }

    override fun hideButtonProgress() {
        progress_take_stock_submit.visibility = View.GONE
        button_take_stock.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_stock)

        setBackNav()

        progress_take_stock_main.visibility = View.VISIBLE
        container_take_stock.visibility = View.GONE
        val accessToken = SalesMotorisPref(this).accessToken
        accessToken?.let {
            mPresenter.getProducts(it)
        }
        button_take_stock.setOnClickListener {
            progress_take_stock_submit.visibility = View.VISIBLE
            button_take_stock.visibility = View.GONE
            accessToken?.let {
                mPresenter.submitTakeStock(it, productId, edittext_take_stock_qty.text.toString())
            }
        }
    }

    private fun resetUi(){
        spinner_take_stock.setSelection(0)
        edittext_take_stock_qty.text = null
    }

    private fun setBackNav() {
        toolbar_take_stock.setNavigationIcon(R.drawable.ic_nav_back)
        toolbar_take_stock.setNavigationOnClickListener {
            setResult(Activity.RESULT_OK, Intent())
            finish()
        }
    }
}
