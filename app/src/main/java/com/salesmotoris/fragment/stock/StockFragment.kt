package com.salesmotoris.fragment.stock

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.salesmotoris.R
import com.salesmotoris.activity.home.HomeActivity
import com.salesmotoris.activity.takestock.TakeStockActivity
import com.salesmotoris.library.SalesMotorisPref
import com.salesmotoris.model.Stock
import com.salesmotoris.mvp.BaseMvpFragment
import kotlinx.android.synthetic.main.fragment_stock.view.*

/**
 * A simple [Fragment] subclass.
 */
class StockFragment : BaseMvpFragment<StockContract.View, StockContract.Presenter>(), StockContract.View {

    private lateinit var v: View
    private val adapter = StockAdapter()

    override var mPresenter: StockContract.Presenter = StockPresenter()
    private val sharedPref: SalesMotorisPref by lazy { SalesMotorisPref(context) }

    override fun showResponse(response: Stock.StockResponse) {
        v.progress_stock.visibility = View.GONE
        v.container_stock.visibility = View.VISIBLE

        adapter.addAllStock(response.data)
    }

    override fun showError() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_stock, container, false)

        return v
    }

    override fun onResume() {
        super.onResume()

        initRecyler()
        loadStock()

        (activity as HomeActivity).updateStock(object : HomeActivity.RefreshStock{
            override fun refresh() {
                loadStock()
            }
        })

        v.fab_stock.setOnClickListener {
            activity?.startActivityForResult(Intent(context, TakeStockActivity::class.java), 2)
        }
    }

    private fun loadStock() {
        v.progress_stock.visibility = View.VISIBLE
        v.container_stock.visibility = View.GONE
        mPresenter.getStock(sharedPref.accessToken!!, sharedPref.id!!)
    }

    private fun initRecyler() {
        v.recyclerview_stock.adapter = adapter
        v.recyclerview_stock.layoutManager = LinearLayoutManager(context)
    }

}
