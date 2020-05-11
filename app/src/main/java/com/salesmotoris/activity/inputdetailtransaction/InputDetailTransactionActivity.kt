package com.salesmotoris.activity.inputdetailtransaction

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.salesmotoris.R
import com.salesmotoris.api.ApiManager
import com.salesmotoris.library.GPSTracker
import com.salesmotoris.library.PathFromUri
import com.salesmotoris.library.RecyclerItemTouchHelper
import com.salesmotoris.library.SalesMotorisPref
import com.salesmotoris.model.DetailTransaction
import com.salesmotoris.model.Meta
import com.salesmotoris.mvp.BaseMvpActivity
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import kotlinx.android.synthetic.main.activity_input_detail_transaction.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import java.io.File


class InputDetailTransactionActivity : BaseMvpActivity<InputDetailTransactionContract.View, InputDetailTransactionContract.Presenter>(), InputDetailTransactionContract.View, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, IPickResult {

    companion object {
        private const val IMAGE_REQUEST_CODE = 1
    }
    private lateinit var transactionImage: File
    private var currentLatitude = 0.0
    private var currentLongitude = 0.0
    private val adapter = InputDetailTransactionAdapter()
    private val sharedPref: SalesMotorisPref by lazy { SalesMotorisPref(this) }
    private val detailTransactions: MutableList<DetailTransaction.DetailTransaction> = mutableListOf()
    private val defaultDetailTransaction: DetailTransaction.DetailTransaction by lazy {
        DetailTransaction.DetailTransaction(
            "Richeese Nabati",
            null,
            null,
            1,
            null
        )
    }

    override var mPresenter: InputDetailTransactionContract.Presenter = InputDetailTransactionPresenter()

    override fun showResponse(response: DetailTransaction.DetailTransactionResponse) {
        ApiManager.getProducts(SalesMotorisPref(this).accessToken!!)
                .doOnError { Log.d("product_error", it.message.toString()) }
                .subscribe {
                    adapter.addDetailTransactionsAndProduct(response.data.detail_transaction, it.data)
                    progress_input_detail_transaction.visibility = View.GONE
                    container_input_detail_transaction.visibility = View.VISIBLE
                    scrollToBottom()
                }
    }

    override fun showSubmitResponse(response: Meta) {
        progress_input_detail_transaction_save.visibility = View.GONE
        button_input_detail_transaction_save.visibility = View.VISIBLE

        toast(response.message)
        if (response.code == 200) {
            finish()
        }
    }

    override fun hideButtonProgress() {
        progress_input_detail_transaction_save.visibility = View.GONE
        button_input_detail_transaction_save.visibility = View.VISIBLE
    }

    override fun showError() {
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        if (viewHolder is InputDetailTransactionAdapter.ViewHolder) {
            adapter.removeTransaction(viewHolder.adapterPosition)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onPickResult(result: PickResult?) {
        result?.let {
            if (result.error == null) {
                Log.d("image_uri", "image uri ${result.uri}")
                transactionImage = File(PathFromUri.getPathFromUri(this, result.uri))
                val bitmap = result.bitmap
                imageview_input_detail_transaction.setImageBitmap(bitmap)
                linear_placeholder_input_detail_transaction.visibility = View.GONE
            } else {
                toast("Terjadi kesalahan")
                Log.d("file picker error", "${result.error.message}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_detail_transaction)

        setNavBack()
        initRecycler()

        val transactionId = intent.getStringExtra("transaction_id")!!
        if (transactionId == "0") {
            toolbar_input_detail_transaksi.title = "Buat Transaksi Baru"
            ApiManager.getProducts(SalesMotorisPref(this).accessToken!!)
                .doOnError { Log.d("product_error", it.message.toString()) }
                .subscribe {
                    adapter.addDetailTransactionAndProduct(defaultDetailTransaction, it.data)
                }
        } else {
            toolbar_input_detail_transaksi.title = "Edit Transaksi"
            progress_input_detail_transaction.visibility = View.VISIBLE
            container_input_detail_transaction.visibility = View.GONE
            mPresenter.getDetailTransactions(sharedPref.accessToken!!, transactionId, sharedPref.id!!)
        }

        imageview_input_detail_transaction.setOnClickListener {
//            val intent = Intent()
//            intent.type = "*/*"
//            intent.action = Intent.ACTION_GET_CONTENT
//            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), IMAGE_REQUEST_CODE)
            PickImageDialog.build(
                PickSetup().setSystemDialog(true)
            ).show(this)
        }

        button_input_detail_transaction_add.setOnClickListener {
            ApiManager.getProducts(SalesMotorisPref(this).accessToken!!)
                .doOnError { Log.d("product_error", it.message.toString()) }
                .subscribe {
                    adapter.addDetailTransactionAndProduct(defaultDetailTransaction, it.data)
                    adapter.addScrollListener(object : InputDetailTransactionAdapter.OnScroll {
                        override fun onScrollToBottom() {
                            scrollToBottom()
                        }
                    })
                }
        }

        adapter.addProductListener(object : InputDetailTransactionAdapter.OnProductModified {
            override fun onProductModified(
                detailTransaction: DetailTransaction.DetailTransaction?,
                position: Int,
                isQuantityUpdated: Boolean) {
                if (detailTransaction != null) {
                    if (position > detailTransactions.size - 1) {
                        detailTransactions.add(detailTransaction)
                    } else {
                        if (!isQuantityUpdated) {
                            detailTransactions[position] = detailTransaction
                        } else {
                            detailTransactions[position].quantity = detailTransaction.quantity
                            detailTransactions[position].sub_total =
                                detailTransactions[position].price?.times(detailTransaction.quantity)
                        }
                    }
                } else {
                    detailTransactions.removeAt(position)
                }
                Log.d("detail_transaction", Gson().toJson(detailTransactions))
            }
        })

        button_input_detail_transaction_save.setOnClickListener {
            val dataTransactionBody: MutableList<DetailTransaction.DataTransactionBody> = mutableListOf()
            ApiManager.getProducts(sharedPref.accessToken!!)
                .doOnError { Log.d("product_error", it.message.toString()) }
                .subscribe {
                    it.data.forEach { product ->
                        detailTransactions.forEach { detail ->
                            if (product.name == detail.product) {
                                dataTransactionBody.add(
                                    DetailTransaction.DataTransactionBody(product.id, detail.quantity, detail.sub_total!!)
                                )
                            }
                        }
                    }

//                    Log.d("detail_transaction", Gson().toJson(dataTransactionBody))

                    val dataTransactionJson =
                        Gson().toJson(dataTransactionBody).toRequestBody("text/plain".toMediaTypeOrNull())
                    val totalItems = dataTransactionBody.sumBy { transactionBody -> transactionBody.quantity }
                        .toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    val totalIncome = dataTransactionBody.sumBy { transactionBody -> transactionBody.sub_total }
                        .toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    val visitationId = intent.getStringExtra("visitation_id")!!
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val storeId = intent.getStringExtra("store_id")!!
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    if (::transactionImage.isInitialized) {
                        val fileReqBody = transactionImage.asRequestBody("image/*".toMediaTypeOrNull())
                        val image = MultipartBody.Part.createFormData(
                            "image",
                            transactionImage.name,
                            fileReqBody
                        )
                        if (currentLatitude != 0.0 && currentLongitude != 0.0) {
                            val currentLocation = DetailTransaction.Coordinate(currentLatitude, currentLongitude)
                            val currentLocationJson = Gson().toJson(currentLocation)
                                .toRequestBody("text/plain".toMediaTypeOrNull())

                            progress_input_detail_transaction_save.visibility = View.VISIBLE
                            button_input_detail_transaction_save.visibility = View.GONE
                            mPresenter.submitDetailTransaction(
                                sharedPref.accessToken!!,
                                sharedPref.id!!,
                                dataTransactionJson,
                                totalIncome,
                                totalItems,
                                transactionId.toRequestBody("text/plain".toMediaTypeOrNull()),
                                visitationId,
                                storeId,
                                currentLocationJson,
                                image
                            )
                        } else {
                            longToast("Mohon aktifkan gps atau koneksi anda terlebih dahulu")
                        }
                    } else {
                        toast("Mohon pilih gambar terlebih dahulu")
                    }
                }
        }
    }

    override fun onResume() {
        super.onResume()

        val gpsTracker = GPSTracker(this)
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            val currentLocation = "${gpsTracker.getLatitude()}\n${gpsTracker.getLongitude()}\n${gpsTracker.getAddressLine(this)}"
            Log.d("current_location", currentLocation)

            currentLatitude = gpsTracker.getLatitude()
            currentLongitude = gpsTracker.getLongitude()
        } else {
            gpsTracker.showSettingsAlert()
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null){
//            val imageUri = data.data
//            try {
//                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
////                transactionImage = File(imageUri?.path)
//                imageUri?.let {
//                    transactionImage = File(PathFromUri.getPathFromUri(this, imageUri))
//                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
//                    imageview_input_detail_transaction.setImageBitmap(bitmap)
//                    linear_placeholder_input_detail_transaction.visibility = View.GONE
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//    }

//    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
//        val bytes = ByteArrayOutputStream()
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//        val path =
//            Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
//        return Uri.parse(path)
//    }

//    private fun getRealPathFromURI(uri: Uri?): String? {
//        val cursor = contentResolver.query(uri!!, null, null, null, null)
//        cursor?.moveToFirst()
//        val idx = cursor?.getColumnIndex(Images.ImageColumns.DATA)
//        val path = cursor?.getString(idx!!)
//        cursor?.close()
//        return path
//    }

    private fun scrollToBottom() {
        container_input_detail_transaction.post {
            container_input_detail_transaction.fullScroll(View.FOCUS_DOWN)
        }
    }

    private fun initRecycler() {
        recylerview_input_detail_transaction.adapter = adapter
        recylerview_input_detail_transaction.layoutManager = LinearLayoutManager(this)
        recylerview_input_detail_transaction.isNestedScrollingEnabled = true
        recylerview_input_detail_transaction.itemAnimator = DefaultItemAnimator()
        val itemTouchHelperCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recylerview_input_detail_transaction)
    }

    private fun setNavBack() {
        toolbar_input_detail_transaksi.setNavigationIcon(R.drawable.ic_nav_back)
        toolbar_input_detail_transaksi.setNavigationOnClickListener { finish() }
    }
}
