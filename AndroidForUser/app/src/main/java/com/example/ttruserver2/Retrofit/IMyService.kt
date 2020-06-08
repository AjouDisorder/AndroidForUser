package com.example.ttruserver2.Retrofit

import android.text.Editable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface IMyService{
    @FormUrlEncoded
    @POST("user/signup")
    fun signUpUser(@Field("userId") userId: String,
                   @Field("password") password: String,
                   @Field("name") nickname: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("user/login")
    fun loginUser(@Field("userId") email: String,
                  @Field("password") password: String): Call<ResponseBody>

    //Search Services
    @GET("/user/getMenuListOfRestaurant")
    fun getMenuListOfRestaurant(@Query("restaurant_id") restaurant_id:String): Call<ResponseBody>
    @GET("/user/getRestaurantDetail")
    fun getRestaurantDetail(@Query("restaurant_id") restaurant_id:String): Call<ResponseBody>

    @GET("/user/getMenuByTime")
    fun getMenuByTime(@Query("year") year:Int,
                      @Query("month") month:Int,
                      @Query("date") date:Int,
                      @Query("hour") hour:Int,
                      @Query("min") min:Int,
                      @Query("lat") lat:Double?,
                      @Query("lng") lng:Double?): Call<ResponseBody>

    @GET("/user/getMenuByCategory")
    fun getMenuByCategory(@Query("type") menuType:String,
                          @Query("lat") lat:Double?,
                          @Query("lng") lng:Double?): Call<ResponseBody>

    @GET("/user/getRestaurantByCategory")
    fun getRestaurantByCategory(  @Query("type") restaurantType:String,
                                  @Query("lat") lat:Double?,
                                  @Query("lng") lng:Double?): Call<ResponseBody>

    @GET("getMenuBySearchBar")
    fun getMenuBySearchBar(  @Query("title") menuTitle:String,
                             @Query("lat") lat:Double?,
                             @Query("lng") lng:Double?): Call<ResponseBody>

    @GET("/user/getRestaurantBySearchBar")
    fun getRestaurantBySearchBar(  @Query("title") restaurantTitle: String,
                                   @Query("lat") lat:Double?,
                                   @Query("lng") lng:Double?): Call<ResponseBody>

    @GET("/user/getMenuByTime")
    fun getMenuByTime(@Query("year") year:Int,
                      @Query("month") month:Int,
                      @Query("date") date:Int,
                      @Query("hour") hour:Int,
                      @Query("min") min:Int,
                      @Query("lat") lat:Double,
                      @Query("lng") lng:Double): Call<ResponseBody>


    //Ticket Services
    @FormUrlEncoded
    @POST("/user/createTicket")
    fun createTicket(@Field("menu_id") menu_id: String,
                     @Field("quantity") quantity: Int,
                     @Field("user_id") user_id: String?,
                     @Field("totalPrice") totalPrice: String,
                     @Field("method") method: String,
                     @Field("messageForBoss") messageForBoss: Editable,
                     @Field("restaurantTitle") restaurantTitle: String): Call<ResponseBody>

    @GET("/user/getTicketList")
    fun getTicketList(@Query("user_id") user_id : String): Call<ResponseBody>

}