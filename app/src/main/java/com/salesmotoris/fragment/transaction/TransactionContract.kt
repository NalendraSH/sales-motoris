package com.salesmotoris.fragment.transaction

import com.salesmotoris.model.Transaction
import com.salesmotoris.mvp.BaseMvpPresenter
import com.salesmotoris.mvp.BaseMvpView

interface TransactionContract {
    interface View: BaseMvpView {
        fun showResponse(response: Transaction.TransactionResponse)

        fun showError()
    }
    interface Presenter: BaseMvpPresenter<View> {
        fun getTransactions(accessToken: String, idSales: String)
    }
}