package com.salesmotoris.activity.inputdetailtransaction

import com.salesmotoris.api.ApiManager
import com.salesmotoris.api.ErrorResponseHandler
import com.salesmotoris.model.DetailTransaction
import com.salesmotoris.mvp.BaseMvpPresenterImpl
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.functions.Action1

class InputDetailTransactionPresenter : BaseMvpPresenterImpl<InputDetailTransactionContract.View>(), InputDetailTransactionContract.Presenter {

    override fun getDetailTransactions(accessToken: String, transactionId: String, idSales: String) {
        ApiManager.getDetailTransactions(accessToken, transactionId, idSales)
            .doOnError { mView?.showMessage(it.toString()) }
            .subscribe(
                Action1 { mView?.showResponse(it) },
                ErrorResponseHandler(mView, true) {_, _, _ -> mView?.showError()}
            )
    }

    override fun submitDetailTransaction(
        accessToken: String,
        idSales: String,
        detailTransaction: RequestBody,
        totalIncome: RequestBody,
        totalItems: RequestBody,
        transactionId: RequestBody,
        visitationId: RequestBody,
        storeId: RequestBody,
        currentLocation: RequestBody,
        image: MultipartBody.Part
    ) {
        ApiManager.submitDetailTransaction(accessToken, idSales, detailTransaction, totalIncome, totalItems, transactionId, visitationId, storeId, currentLocation, image)
            .doOnError { mView?.showMessage(it.toString()) }
            .subscribe(
                Action1 { mView?.showSubmitResponse(it) },
                ErrorResponseHandler(mView, true) {_, _, _ -> mView?.hideButtonProgress()}
            )
    }

}