package com.salesmotoris.fragment.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.salesmotoris.R
import com.salesmotoris.library.SalesMotorisPref
import com.salesmotoris.model.Transaction
import com.salesmotoris.mvp.BaseMvpFragment
import kotlinx.android.synthetic.main.fragment_transaction.view.*

/**
 * A simple [Fragment] subclass.
 */
class TransactionFragment : BaseMvpFragment<TransactionContract.View, TransactionContract.Presenter>(), TransactionContract.View {

    private lateinit var v: View

    override var mPresenter: TransactionContract.Presenter = TransactionPresenter()
    private val adapter = TransactionAdapter()

    private val sharedPref: SalesMotorisPref by lazy { SalesMotorisPref(context) }

    override fun showResponse(response: Transaction.TransactionResponse) {
        v.progress_transaction.visibility = View.GONE
        v.container_transaction.visibility = View.VISIBLE

        v.textview_transaction_current_day.text = response.data[0].days
        adapter.addAllTransaction(response.data)
    }

    override fun showError() {
        v.progress_transaction.visibility = View.GONE
        v.container_transaction.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_transaction, container, false)

        initRecyclerView()

        //get current date
//        val localeId = Locale("in", "ID")
//        val currentDate = SimpleDateFormat("EEEE", localeId).format(Calendar.getInstance().time)
        v.progress_transaction.visibility = View.VISIBLE
        v.container_transaction.visibility = View.GONE
        mPresenter.getTransactions(sharedPref.accessToken!!, sharedPref.id!!)

        return v
    }

    override fun onResume() {
        super.onResume()

        v.progress_transaction.visibility = View.VISIBLE
        v.container_transaction.visibility = View.GONE
        mPresenter.getTransactions(sharedPref.accessToken!!, sharedPref.id!!)
    }

    private fun initRecyclerView() {
        v.recyclerview_transaction.adapter = adapter
        v.recyclerview_transaction.layoutManager = LinearLayoutManager(context)
    }

}
