package com.salesmotoris.activity.inputdetailtransaction

import com.salesmotoris.model.DetailTransaction
import com.salesmotoris.model.Meta
import com.salesmotoris.mvp.BaseMvpPresenter
import com.salesmotoris.mvp.BaseMvpView

interface InputDetailTransactionContract {
    interface View: BaseMvpView {
        fun showResponse(response: DetailTransaction.DetailTransactionResponse)

        fun showSubmitResponse(response: Meta)

        fun showError()

        fun hideButtonProgress()
    }
    interface Presenter: BaseMvpPresenter<View> {
        fun getDetailTransactions(accessToken: String, transactionId: String)

        fun submitDetailTransaction(accessToken: String, detailTransaction: DetailTransaction.DetailTransactionBody)
    }
}