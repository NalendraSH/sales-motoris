package com.salesmotoris.fragment.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.salesmotoris.R
import com.salesmotoris.customFormat
import com.salesmotoris.formatCurrency
import com.salesmotoris.library.SalesMotorisPref
import com.salesmotoris.model.Report
import com.salesmotoris.mvp.BaseMvpFragment
import kotlinx.android.synthetic.main.fragment_home.view.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseMvpFragment<HomeContract.View, HomeContract.Presenter>(), HomeContract.View {

    private lateinit var v: View

    override var mPresenter: HomeContract.Presenter = HomePresenter()

    override fun showResponse(response: Report.ReportResponse) {
        v.progress_home.visibility = View.GONE
        v.container_home.visibility = View.VISIBLE

        v.textview_home_yesterday_date.text = getString(R.string.display_day_date, response.data[0].days, response.data[0].date.customFormat("yyyy-MM-dd", "dd MMMM yyyy"))
        v.textview_home_yesterday_income.text = getString(R.string.display_currency, response.data[0].total_income.toFloat().formatCurrency())

        v.textview_home_today_date.text = getString(R.string.display_day_date, response.data[1].days, response.data[1].date.customFormat("yyyy-MM-dd", "dd MMMM yyyy"))
        v.textview_home_today_store.text = getString(R.string.display_store, response.data[1].completed_visitation)
        v.textview_home_today_income.text = getString(R.string.display_currency, response.data[1].total_income.toFloat().formatCurrency())
    }

    override fun showError() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)

        //get current date and yesterday's date
//        var day1 = ""
//        var day2 = ""
//        setDate({day1 = it}, {day2 = it})

        v.progress_home.visibility = View.VISIBLE
        v.container_home.visibility = View.GONE
        val accessToken = SalesMotorisPref(context).accessToken
        accessToken?.let {
            mPresenter.getTwoDaysReport(it)
        }

        return v
    }

//    private fun setDate(yesterday: (String) -> Unit, today: (String) -> Unit){
//        val localeId = Locale("in", "ID")
//
//        //current date
//        val currentDate = Calendar.getInstance()
//        v.textview_home_today_date.text = SimpleDateFormat("dd MMMM yyyy", localeId).format(currentDate.time)
//        today(SimpleDateFormat("EEEE", localeId).format(currentDate.time))
//
//        //yesterday's date
//        currentDate.add(Calendar.DATE, -1)
//        v.textview_home_yesterday_date.text = SimpleDateFormat("dd MMMM yyyy", localeId).format(currentDate.time)
//        yesterday(SimpleDateFormat("EEEE", localeId).format(currentDate.time))
//
//    }

}
