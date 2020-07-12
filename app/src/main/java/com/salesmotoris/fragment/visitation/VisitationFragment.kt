package com.salesmotoris.fragment.visitation


import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
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
import org.jetbrains.anko.collections.forEachByIndex

/**
 * A simple [Fragment] subclass.
 */
class VisitationFragment : BaseMvpFragment<VisitationContract.View, VisitationContract.Presenter>(), VisitationContract.View {

    private lateinit var v: View

    override var mPresenter: VisitationContract.Presenter = VisitationPresenter()

    override fun showResponse(response: Visitation.VisitationResponse) {
        v.progress_visitation.visibility = View.GONE
        v.container_visitation.visibility = View.VISIBLE

        val mondaySchedule = response.data.Senin
        val tuesdaySchedule = response.data.Selasa
        val wednesdaySchedule = response.data.Rabu
        val thursdaySchedule = response.data.Kamis
        val fridaySchedule = response.data.Jumat
        Log.d("OkHttp", "schedule jumat 1 $fridaySchedule")

        val bulletList = Html.fromHtml("&#8226;")
        //senin
        var mondayVisitation = ""
        mondaySchedule.forEach{ store ->
            mondayVisitation += "$bulletList $store\n"
        }
        v.textview_visitation_senin.text = mondayVisitation.trim()
        //selasa
        var tuesdayVisitation = ""
        tuesdaySchedule.forEach{ store ->
            tuesdayVisitation += "$bulletList $store\n"
        }
        v.textview_visitation_selasa.text = tuesdayVisitation.trim()
        //rabu
        var wednesdayVisitation = ""
        wednesdaySchedule.forEach{ store ->
            wednesdayVisitation += "$bulletList $store\n"
        }
        v.textview_visitation_rabu.text = wednesdayVisitation.trim()
        //kamis
        var thursdayVisitation = ""
        thursdaySchedule.forEach{ store ->
            thursdayVisitation += "$bulletList $store\n"
        }
        v.textview_visitation_kamis.text = thursdayVisitation.trim()
        //friday
        var fridayVisitation = ""
        fridaySchedule.forEach{ store ->
            fridayVisitation += "$bulletList $store\n"
        }
        v.textview_visitation_jumat.text = fridayVisitation.trim()
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
