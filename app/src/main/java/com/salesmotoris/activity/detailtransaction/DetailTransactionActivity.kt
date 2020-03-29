package com.salesmotoris.activity.detailtransaction

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.salesmotoris.R
import com.salesmotoris.activity.inputdetailtransaction.InputDetailTransactionActivity
import com.salesmotoris.formatCurrency
import com.salesmotoris.library.SalesMotorisPref
import com.salesmotoris.model.DetailTransaction
import com.salesmotoris.mvp.BaseMvpActivity
import kotlinx.android.synthetic.main.activity_detail_transaction.*
import org.jetbrains.anko.intentFor

class DetailTransactionActivity : BaseMvpActivity<DetailTransactionContract.View, DetailTransactionContract.Presenter>(), DetailTransactionContract.View {

    private val adapter = DetailTransactionAdapter()
    private lateinit var transactionId: String
    private lateinit var visitationId: String
    private lateinit var storeId: String
    private val sharedPref: SalesMotorisPref by lazy { SalesMotorisPref(this) }

    override var mPresenter: DetailTransactionContract.Presenter = DetailTransactionPresenter()

    override fun showResponse(response: DetailTransaction.DetailTransactionResponse) {
        progress_detail_transaction.visibility = View.GONE
        container_detail_transaction.visibility = View.VISIBLE

        if (response.data.detail_transaction.isEmpty()) {
            container_detail_transaction_content.visibility = View.GONE
            container_detail_transaction_empty.visibility = View.VISIBLE
        } else {
            adapter.addAllDetailTransaction(response.data.detail_transaction)
            textview_detail_transaction_total.text = getString(
                R.string.display_currency,
                response.data.total_income.toFloat().formatCurrency()
            )
        }
    }

    override fun showError() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaction)

        setToolbar()
        initRecycler()

        loadDetailTransaction()
    }

    private fun loadDetailTransaction() {
        progress_detail_transaction.visibility = View.VISIBLE
        container_detail_transaction.visibility = View.GONE
        transactionId = intent.getStringExtra("transaction_id")!!
        visitationId = intent.getStringExtra("visitation_id")!!
        storeId = intent.getStringExtra("store_id")!!
        mPresenter.getDetailTransactions(sharedPref.accessToken!!, transactionId, sharedPref.id!!)
    }

    private fun initRecycler() {
        recyclerview_detail_transaction.adapter = adapter
        recyclerview_detail_transaction.layoutManager = LinearLayoutManager(this)
    }

    private fun setToolbar() {
        toolbar_detail_transaction.title = intent.getStringExtra("store")
        toolbar_detail_transaction.setNavigationIcon(R.drawable.ic_nav_back)
        toolbar_detail_transaction.setNavigationOnClickListener { finish() }
        val editButton = toolbar_detail_transaction.findViewById<ImageView>(R.id.toolbar_detail_transaction_edit)
        editButton.setOnClickListener {
            startActivity(intentFor<InputDetailTransactionActivity>(
                "transaction_id" to transactionId,
                "visitation_id" to visitationId,
                "store_id" to storeId
            ))
        }
    }

    override fun onResume() {
        super.onResume()
        loadDetailTransaction()
    }
}
