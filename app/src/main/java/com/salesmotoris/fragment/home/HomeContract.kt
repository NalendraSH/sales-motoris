package com.salesmotoris.fragment.home

import com.salesmotoris.model.Report
import com.salesmotoris.mvp.BaseMvpPresenter
import com.salesmotoris.mvp.BaseMvpView

interface HomeContract {
    interface View: BaseMvpView{
        fun showResponse(response: Report.ReportResponse)

        fun showError()
    }
    interface Presenter: BaseMvpPresenter<View>{
        fun getTwoDaysReport(accessToken: String, idSales: String)
    }
}