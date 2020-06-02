package com.example.ttruserver2.Retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

data class ResponseDTO(var userId:String? = null, var result:String? = null)

interface IMyService{
    @FormUrlEncoded
    @POST("user/signup")
    fun signUpUser(@Field("userId") userId: String,
                 @Field("password") password: String,
                 @Field("nickname") nickname: String
        //@Field("age") age: String,
        //@Field("sex") sex: String,
        //@Field("phone") phone: String
    ): Call<ResponseDTO>

    @FormUrlEncoded
    @POST("user/login")
    fun loginUser(@Field("userId") email: String,
                  @Field("password") password: String): Call<ResponseDTO>

    //Search Services
    @GET("/user/getMenuListOfRestaurant")
    fun getMenuListOfRestaurant(@Query("restaurant_id") restaurant_id:String): Call<ResponseBody>

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

    //Ticket Services
    @FormUrlEncoded
    @POST("/user/createTicket")
    fun createTicket(@Field("menu_id") menu_id: String,
                     @Field("quantity") quantity: Int,
                     @Field("user_id") user_id: String,
                     @Field("totalPrice") totalPrice: Int,
                     @Field("method") method: String,
                     @Field("messageForBoss") messageForBoss: String): Call<ResponseBody>



}