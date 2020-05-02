package com.salesmotoris.api

import com.salesmotoris.BuildConfig
import com.salesmotoris.model.AddStore
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

object ApiManager{
    private const val SERVER: String = BuildConfig.BASE_URL

    private lateinit var mApiService: ApiService

    init {
        val retrofit = initRetrofit()
        initServices(retrofit)
    }

    private fun initRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).apply {
            networkInterceptors().add(Interceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            })
            addInterceptor(interceptor)
        }

        return Retrofit.Builder().baseUrl(SERVER)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create().asLenient().withNullSerialization())
            .client(client.build())
            .build()
    }

    private fun initServices(retrofit: Retrofit) {
        mApiService = retrofit.create(ApiService::class.java)
    }

    fun submitLoginData(username: String, password: String) =
        mApiService.submitLoginData(username, password, "sales")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun getTwoDaysReport(accessToken: String, idSales: String) =
        mApiService.getTwoDaysReport("Bearer $accessToken", idSales)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun getVisitation(accessToken: String, idSales: String) =
        mApiService.getVisitation("Bearer $accessToken", idSales)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun getStock(accessToken: String, idSales: String) =
        mApiService.getStock("Bearer $accessToken", idSales)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun getProducts(accessToken: String) =
        mApiService.getProducts("Bearer $accessToken")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun getProductsCoroutines(accessToken: String) =
        mApiService.getProducts("Bearer $accessToken")

    fun submitTakeStock(accessToken: String, productId: String, quantity: String) =
        mApiService.submitTakeStock(accessToken, productId, quantity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun getTransactions(accessToken: String, idSales: String) =
        mApiService.getTransactions("Bearer $accessToken", idSales)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun getDetailTransactions(accessToken: String, transactionId: String, idSales: String) =
        mApiService.getDetailTransactions("Bearer $accessToken", transactionId, idSales)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun submitDetailTransaction(accessToken: String,
                                idSales: String,
                                detailTransaction: RequestBody,
                                totalIncome: RequestBody,
                                totalItems: RequestBody,
                                transactionId: RequestBody,
                                visitationId: RequestBody,
                                storeId: RequestBody,
                                currentLocation: RequestBody,
                                image: MultipartBody.Part) =
        mApiService.submitDetailTransaction("Bearer $accessToken", idSales, detailTransaction, totalIncome, totalItems, transactionId, visitationId, storeId, currentLocation, image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun submitStore(accessToken: String, locationDetail: AddStore.AddStoreJson) =
        mApiService.submitStore("Bearer $accessToken", locationDetail)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())!!

}