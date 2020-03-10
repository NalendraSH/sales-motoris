package com.salesmotoris.activity.addstore

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.salesmotoris.R
import com.salesmotoris.library.GPSTracker
import com.salesmotoris.library.SalesMotorisPref
import com.salesmotoris.model.AddStore
import com.salesmotoris.mvp.BaseMvpActivity
import com.sucho.placepicker.AddressData
import com.sucho.placepicker.Constants
import com.sucho.placepicker.MapType
import com.sucho.placepicker.PlacePicker
import kotlinx.android.synthetic.main.activity_add_store.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class AddStoreActivity : BaseMvpActivity<AddStoreContract.View, AddStoreContract.Presenter>(), AddStoreContract.View {

    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null
    private var selectedLatitude: Double? = null
    private var selectedLongitude: Double? = null

    override var mPresenter: AddStoreContract.Presenter = AddStorePresenter()

    override fun showResponse(response: AddStore.AddStoreResponse) {
        button_add_store_save.visibility = View.VISIBLE
        progress_add_store.visibility = View.GONE

        if (response.code == 200){
            toast(response.message)
            finish()
        } else {
            toast(response.message)
        }
    }

    override fun hideProgress() {
        button_add_store_save.visibility = View.VISIBLE
        progress_add_store.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_store)

        setNavBack()
    }

    override fun onResume() {
        super.onResume()

        val gpsTracker = GPSTracker(this)
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            val currentLocation = "${gpsTracker.getLatitude()}\n${gpsTracker.getLongitude()}\n${gpsTracker.getAddressLine(this)}"
            Log.d("current_location", currentLocation)

            currentLatitude = gpsTracker.getLatitude()
            currentLongitude = gpsTracker.getLongitude()

            button_add_store_select_location.setOnClickListener {
                val intent = PlacePicker.IntentBuilder()
                    .setLatLong(gpsTracker.getLatitude(), gpsTracker.getLongitude())
                    .showLatLong(true)
                    .setMapRawResourceStyle(R.raw.map_style)
                    .setMapType(MapType.NORMAL)
                    .build(this)
                startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST)
            }
        } else {
            gpsTracker.showSettingsAlert()
        }

        button_add_store_save.setOnClickListener {
            if (currentLatitude != 0.0 && currentLongitude != 0.0 && selectedLatitude != 0.0 && selectedLongitude != 0.0) {
                val storeName = edittext_add_store_name.text.toString()
                val storeAddress = edittext_add_store_address.text.toString()
                val addStoreJson = AddStore.AddStoreJson(
                    currentLatitude!!,
                    selectedLatitude!!,
                    currentLongitude!!,
                    selectedLongitude!!,
                    storeName,
                    storeAddress
                )

                button_add_store_save.visibility = View.GONE
                progress_add_store.visibility = View.VISIBLE
                Log.d("detail_loc_json", addStoreJson.toString())
                SalesMotorisPref(this).accessToken?.let { it1 ->
                    mPresenter.submitStore(it1, addStoreJson)
                }
            } else {
                longToast("Mohon untuk memilih lokasi toko atau menyalakan gps & data anda terlebih dahulu")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val addressData = data?.getParcelableExtra<AddressData>(Constants.ADDRESS_INTENT)
                    Log.d("selected_location", addressData.toString())
                    val address = addressData?.addressList!![0].getAddressLine(0)
                    selectedLatitude = addressData.latitude
                    selectedLongitude = addressData.longitude

                    edittext_add_store_address.setText(address)
                } catch (e: Exception) {
                    Log.e("MainActivity", e.message.toString())
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setNavBack() {
        toolbar_add_store.setNavigationIcon(R.drawable.ic_nav_back)
        toolbar_add_store.setNavigationOnClickListener { finish() }
    }
}
