package com.salesmotoris.activity.inputdetailtransaction

import com.salesmotoris.model.DetailTransaction
import com.salesmotoris.model.Meta
import com.salesmotoris.mvp.BaseMvpPresenter
import com.salesmotoris.mvp.BaseMvpView
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface InputDetailTransactionContract {
    interface View: BaseMvpView {
        fun showResponse(response: DetailTransaction.DetailTransactionResponse)

        fun showSubmitResponse(response: DetailTransaction.SubmitTransactionResponse)

        fun showError()

        fun hideButtonProgress()
    }
    interface Presenter: BaseMvpPresenter<View> {
        fun getDetailTransactions(accessToken: String, transactionId: String, idSales: String)

        fun submitDetailTransaction(accessToken: String,
                                    idSales: String,
                                    detailTransaction: RequestBody,
                                    totalIncome: RequestBody,
                                    totalItems: RequestBody,
                                    transactionId: RequestBody,
                                    visitationId: RequestBody,
                                    storeId: RequestBody,
                                    currentLocation: RequestBody,
                                    image: MultipartBody.Part)
    }
}