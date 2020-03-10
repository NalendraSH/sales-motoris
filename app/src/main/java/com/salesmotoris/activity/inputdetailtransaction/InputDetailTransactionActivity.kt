package com.salesmotoris.activity.inputdetailtransaction

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.salesmotoris.R
import com.salesmotoris.activity.detailtransaction.DetailTransactionContract
import com.salesmotoris.activity.detailtransaction.DetailTransactionPresenter
import com.salesmotoris.library.SalesMotorisPref
import com.salesmotoris.model.DetailTransaction
import com.salesmotoris.model.Meta
import com.salesmotoris.mvp.BaseMvpActivity
import kotlinx.android.synthetic.main.activity_input_detail_transaction.*
import org.jetbrains.anko.toast

class InputDetailTransactionActivity : BaseMvpActivity<InputDetailTransactionContract.View, InputDetailTransactionContract.Presenter>(), InputDetailTransactionContract.View {

    private val adapter = InputDetailTransactionAdapter()
    private lateinit var transactionId: String
    private lateinit var accessToken: String

    override var mPresenter: InputDetailTransactionContract.Presenter = InputDetailTransactionPresenter()

    override fun showResponse(response: DetailTransaction.DetailTransactionResponse) {
        progress_input_detail_transaction.visibility = View.GONE
        container_input_detail_transaction.visibility = View.VISIBLE

        adapter.addDetailTransactions(response.data.detail_transaction)
    }

    override fun showSubmitResponse(response: Meta) {
        progress_input_detail_transaction_save.visibility = View.GONE
        button_input_detail_transaction_save.visibility = View.VISIBLE

        toast(response.message)
    }

    override fun hideButtonProgress() {
        progress_input_detail_transaction_save.visibility = View.GONE
        button_input_detail_transaction_save.visibility = View.VISIBLE
    }

    override fun showError() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_detail_transaction)

        setNavBack()
        initRecycler()

        progress_input_detail_transaction.visibility = View.VISIBLE
        container_input_detail_transaction.visibility = View.GONE
        accessToken = SalesMotorisPref(this).accessToken!!
        transactionId = intent.getStringExtra("transaction_id")!!
        mPresenter.getDetailTransactions(accessToken, transactionId)

        button_input_detail_transaction_add.setOnClickListener {
            val detailTransaction = DetailTransaction.DetailTransaction(
                "Richeese Nabati",
                null,
                null,
                "0",
                null
            )
            adapter.addDetailTransaction(detailTransaction)
        }

        button_input_detail_transaction_save.setOnClickListener {
            val detailTransaction = adapter.getDetailTransaction()
            val dataTransactionBody: MutableList<DetailTransaction.DataTransactionBody> = mutableListOf()

//            progress_input_detail_transaction_save.visibility = View.VISIBLE
//            button_input_detail_transaction_save.visibility = View.GONE
//            mPresenter.submitDetailTransaction(accessToken, detailTransactionBody)
//            Log.d("detail_transaction_data", Gson().toJson(detailTransactionBody))
        }
    }

    private fun initRecycler() {
        recylerview_input_detail_transaction.adapter = adapter
        recylerview_input_detail_transaction.layoutManager = LinearLayoutManager(this)
    }

    private fun setNavBack() {
        toolbar_input_detail_transaksi.setNavigationIcon(R.drawable.ic_nav_back)
        toolbar_input_detail_transaksi.setNavigationOnClickListener { finish() }
    }
}
