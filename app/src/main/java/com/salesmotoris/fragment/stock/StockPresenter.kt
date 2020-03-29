package com.salesmotoris.fragment.stock

import com.salesmotoris.api.ApiManager
import com.salesmotoris.api.ErrorResponseHandler
import com.salesmotoris.mvp.BaseMvpPresenterImpl
import rx.functions.Action1

class StockPresenter : BaseMvpPresenterImpl<StockContract.View>(), StockContract.Presenter {

    override fun getStock(accessToken: String, idSales: String) {
        ApiManager.getStock(accessToken, idSales)
            .doOnError { mView?.showMessage(it.toString()) }
            .subscribe(
                Action1 { mView?.showResponse(it) },
                ErrorResponseHandler(mView, true) {_, _, _ -> mView?.showError()}
            )
    }

}