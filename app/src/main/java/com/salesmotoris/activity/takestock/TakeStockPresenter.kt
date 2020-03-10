package com.salesmotoris.activity.takestock

import com.salesmotoris.api.ApiManager
import com.salesmotoris.api.ErrorResponseHandler
import com.salesmotoris.mvp.BaseMvpPresenterImpl
import rx.functions.Action1

class TakeStockPresenter : BaseMvpPresenterImpl<TakeStockContract.View>(), TakeStockContract.Presenter {

    override fun getProducts(accessToken: String) {
        ApiManager.getProducts(accessToken)
            .doOnError { mView?.showMessage(it.toString()) }
            .subscribe(
                Action1 { mView?.showProducts(it) },
                ErrorResponseHandler(mView, true) {_, _, _ -> mView?.hideMainProgress()}
            )
    }

    override fun submitTakeStock(accessToken: String, productId: String, quantity: String) {
        ApiManager.submitTakeStock(accessToken, productId, quantity)
            .doOnError { mView?.showMessage(it.toString()) }
            .subscribe(
                Action1 { mView?.showSubmitResponse(it) },
                ErrorResponseHandler(mView, true) {_, _, _ -> mView?.hideButtonProgress()}
            )
    }

}