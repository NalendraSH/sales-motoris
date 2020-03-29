package com.salesmotoris.activity.inputdetailtransaction

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.salesmotoris.R
import com.salesmotoris.api.ApiManager
import com.salesmotoris.library.RecyclerItemTouchHelper
import com.salesmotoris.library.SalesMotorisPref
import com.salesmotoris.model.DetailTransaction
import com.salesmotoris.model.Meta
import com.salesmotoris.mvp.BaseMvpActivity
import kotlinx.android.synthetic.main.activity_input_detail_transaction.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException


class InputDetailTransactionActivity : BaseMvpActivity<InputDetailTransactionContract.View, InputDetailTransactionContract.Presenter>(), InputDetailTransactionContract.View, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    companion object {
        private const val IMAGE_REQUEST_CODE = 1
    }
    private lateinit var transactionImage: File
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
        progress_input_detail_transaction.visibility = View.GONE
        container_input_detail_transaction.visibility = View.VISIBLE

        adapter.addDetailTransactions(response.data.detail_transaction)
    }

    override fun showSubmitResponse(response: DetailTransaction.SubmitTransactionResponse) {
        progress_input_detail_transaction_save.visibility = View.GONE
        button_input_detail_transaction_save.visibility = View.VISIBLE

        toast(response.meta.message)
        if (response.meta.code == 200) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_detail_transaction)

        setNavBack()
        initRecycler()

        val transactionId = intent.getStringExtra("transaction_id")!!
        if (transactionId == "0") {
            toolbar_input_detail_transaksi.title = "Buat Transaksi Baru"
            adapter.addDetailTransaction(defaultDetailTransaction)
        } else {
            toolbar_input_detail_transaksi.title = "Edit Transaksi"
            progress_input_detail_transaction.visibility = View.VISIBLE
            container_input_detail_transaction.visibility = View.GONE
            mPresenter.getDetailTransactions(sharedPref.accessToken!!, transactionId, sharedPref.id!!)
        }

        imageview_input_detail_transaction.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), IMAGE_REQUEST_CODE)
        }

        button_input_detail_transaction_add.setOnClickListener {
            adapter.addDetailTransaction(defaultDetailTransaction)
            adapter.addScrollListener(object : InputDetailTransactionAdapter.OnScroll {
                override fun onScrollToBottom() {
                    scrollToBottom()
                }
            })
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
//                    val gsonBuilder = GsonBuilder().disableHtmlEscaping().create()

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
                    val currentLocation = DetailTransaction.Coordinate(-7.2372388, 112.7404682)
                    val currentLocationJson = Gson().toJson(currentLocation)
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    val fileReqBody = transactionImage.asRequestBody("image/*".toMediaTypeOrNull())
                    val image =
                        MultipartBody.Part.createFormData("image", transactionImage.name, fileReqBody)

                    if (::transactionImage.isInitialized) {
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
                        toast("Mohon pilih gambar terlebih dahulu")
                    }
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            val imageUri = data.data
            try {
                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
//                transactionImage = File(imageUri?.path)
                transactionImage = File(getRealPathFromURI(imageUri))
                val bitmap = Images.Media.getBitmap(contentResolver, imageUri)
                imageview_input_detail_transaction.setImageBitmap(bitmap)
                linear_placeholder_input_detail_transaction.visibility = View.GONE
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

//    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
//        val bytes = ByteArrayOutputStream()
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//        val path =
//            Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
//        return Uri.parse(path)
//    }

    private fun getRealPathFromURI(uri: Uri?): String? {
        val cursor = contentResolver.query(uri!!, null, null, null, null)
        cursor?.moveToFirst()
        val idx = cursor?.getColumnIndex(Images.ImageColumns.DATA)
        val path = cursor?.getString(idx!!)
        cursor?.close()
        return path
    }

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
