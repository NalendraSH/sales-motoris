package com.salesmotoris.fragment.visitation

import com.salesmotoris.model.Visitation
import com.salesmotoris.mvp.BaseMvpPresenter
import com.salesmotoris.mvp.BaseMvpView

interface VisitationContract {
    interface View: BaseMvpView {
        fun showResponse(response: Visitation.VisitationResponse)

        fun showError()
    }
    interface Presenter: BaseMvpPresenter<View> {
        fun getVisitation(accessToken: String, idSales: String)
    }
}