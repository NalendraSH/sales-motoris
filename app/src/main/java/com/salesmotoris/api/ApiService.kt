package com.salesmotoris.api

import com.salesmotoris.model.*
import retrofit2.http.*
import rx.Observable

interface ApiService{

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("auth/login")
    fun submitLoginData(@Field("username")username: String,
                        @Field("password")password: String,
                        @Field("level")level: String): Observable<Login.LoginResponse>

    @GET("report")
    fun getTwoDaysReport(@Header("Authorization")accessToken: String): Observable<Report.ReportResponse>

    @GET("visitation")
    fun getVisitation(@Header("Authorization")accessToken: String,
                      @Query("id_sales")idSales: String): Observable<Visitation.VisitationResponse>

    @GET("stock")
    fun getStock(@Header("Authorization")accessToken: String): Observable<Stock.StockResponse>

    @GET("product")
    fun getProducts(@Header("Authorization")accessToken: String): Observable<Product.ProductResponse>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("stock")
    fun submitTakeStock(@Header("Authorization")accessToken: String,
                        @Field("product_id")productId: String,
                        @Field("number_of_fetches")quantity: String): Observable<Meta>

    @GET("transaction")
    fun getTransactions(@Header("Authorization")accessToken: String,
                        @Query("day")day: String): Observable<Transaction.TransactionResponse>

    @GET("transaction/{transaction_id}")
    fun getDetailTransactions(@Header("Authorization")accessToken: String,
                              @Path("transaction_id")transactionId: String): Observable<DetailTransaction.DetailTransactionResponse>

    @POST("transaction/detail_transaction")
    fun submitDetailTransaction(@Header("Authorization")accessToken: String,
                                @Body detailTransaction: DetailTransaction.DetailTransactionBody): Observable<Meta>

    @POST("store")
    @Headers("Content-Type: application/json")
    fun submitStore(@Header("Authorization")accessToken: String,
                    @Body locationDetail: AddStore.AddStoreJson): Observable<AddStore.AddStoreResponse>

}