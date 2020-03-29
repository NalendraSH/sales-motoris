package com.salesmotoris.activity.detailtransaction

import com.salesmotoris.model.DetailTransaction
import com.salesmotoris.mvp.BaseMvpPresenter
import com.salesmotoris.mvp.BaseMvpView

interface DetailTransactionContract {
    interface View: BaseMvpView {
        fun showResponse(response: DetailTransaction.DetailTransactionResponse)

        fun showError()
    }
    interface Presenter: BaseMvpPresenter<View> {
        fun getDetailTransactions(accessToken: String, transactionId: String, idSales: String)
    }
}