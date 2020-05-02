package com.salesmotoris.api

import com.salesmotoris.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import rx.Observable

interface ApiService{

    @POST("auth/login")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    fun submitLoginData(@Field("username")username: String,
                        @Field("password")password: String,
                        @Field("level")level: String): Observable<Login.LoginResponse>

    @GET("report")
    fun getTwoDaysReport(@Header("Authorization")accessToken: String,
                         @Query("id_sales")idSales: String): Observable<Report.ReportResponse>

    @GET("visitation")
    fun getVisitation(@Header("Authorization")accessToken: String,
                      @Query("id_sales")idSales: String): Observable<Visitation.VisitationResponse>

    @GET("stock")
    fun getStock(@Header("Authorization")accessToken: String,
                 @Query("id_sales")idSales: String): Observable<Stock.StockResponse>

    @GET("product")
    fun getProducts(@Header("Authorization")accessToken: String): Observable<Product.ProductResponse>

    @POST("stock")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    fun submitTakeStock(@Header("Authorization")accessToken: String,
                        @Field("product_id")productId: String,
                        @Field("number_of_fetches")quantity: String): Observable<Meta>

    @GET("transaction")
    fun getTransactions(@Header("Authorization")accessToken: String,
                        @Query("id_sales")idSales: String): Observable<Transaction.TransactionResponse>

    @GET("transaction/{transaction_id}")
    fun getDetailTransactions(@Header("Authorization")accessToken: String,
                              @Path("transaction_id")transactionId: String,
                              @Query("id_sales")idSales: String): Observable<DetailTransaction.DetailTransactionResponse>

    @POST("transaction")
    @Multipart
    fun submitDetailTransaction(@Header("Authorization")accessToken: String,
                                @Query("id_sales")idSales: String,
                                @Part("detail_transaction")detailTransaction: RequestBody,
                                @Part("total_income")totalIncome: RequestBody,
                                @Part("total_items")totalItems: RequestBody,
                                @Part("transaction_id")transactionId: RequestBody,
                                @Part("id_visitation")visitationId: RequestBody,
                                @Part("id_store")storeId: RequestBody,
                                @Part("coordinate")currentLocation: RequestBody,
                                @Part image: MultipartBody.Part): Observable<Meta>

    @POST("store")
    @Headers("Content-Type: application/json")
    fun submitStore(@Header("Authorization")accessToken: String,
                    @Body locationDetail: AddStore.AddStoreJson): Observable<AddStore.AddStoreResponse>

}