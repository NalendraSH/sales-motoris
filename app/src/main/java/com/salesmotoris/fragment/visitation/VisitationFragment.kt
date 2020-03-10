package com.salesmotoris.fragment.visitation


import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.salesmotoris.R
import com.salesmotoris.activity.addstore.AddStoreActivity
import com.salesmotoris.library.SalesMotorisPref
import com.salesmotoris.model.Visitation
import com.salesmotoris.mvp.BaseMvpFragment
import kotlinx.android.synthetic.main.fragment_visitation.view.*

/**
 * A simple [Fragment] subclass.
 */
class VisitationFragment : BaseMvpFragment<VisitationContract.View, VisitationContract.Presenter>(), VisitationContract.View {

    private lateinit var v: View

    override var mPresenter: VisitationContract.Presenter = VisitationPresenter()

    override fun showResponse(response: Visitation.VisitationResponse) {
        v.progress_visitation.visibility = View.GONE
        v.container_visitation.visibility = View.VISIBLE

        val bulletList = Html.fromHtml("&#8226;")
        //senin
        var mondayVisitation = ""
        for (store in response.data.Senin){
            mondayVisitation += "$bulletList $store\n"
        }
        v.textview_visitation_senin.text = mondayVisitation.substring(0, mondayVisitation.length - 1)
        //selasa
        var tuesdayVisitation = ""
        for (store in response.data.Selasa){
            tuesdayVisitation += "$bulletList $store\n"
        }
        v.textview_visitation_selasa.text = tuesdayVisitation.substring(0, tuesdayVisitation.length - 1)
        //rabu
        var wednesdayVisitation = ""
        for (store in response.data.Rabu){
            wednesdayVisitation += "$bulletList $store\n"
        }
        v.textview_visitation_rabu.text = wednesdayVisitation.substring(0, wednesdayVisitation.length - 1)
        //kamis
        var thursdayVisitation = ""
        for (store in response.data.Kamis){
            thursdayVisitation += "$bulletList $store\n"
        }
        v.textview_visitation_kamis.text = thursdayVisitation.substring(0, thursdayVisitation.length - 1)
        //friday
        var fridayVisitation = ""
        for (store in response.data.Jumat){
            fridayVisitation += "$bulletList $store\n"
        }
        v.textview_visitation_jumat.text = fridayVisitation.substring(0, fridayVisitation.length - 1)
    }

    override fun showError() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_visitation, container, false)

        v.progress_visitation.visibility = View.VISIBLE
        v.container_visitation.visibility = View.GONE
        val pref = SalesMotorisPref(context)
        mPresenter.getVisitation(pref.accessToken!!, pref.id!!)

        v.fab_visitation.setOnClickListener {
            startActivity(Intent(context, AddStoreActivity::class.java))
        }

        return v
    }

}
