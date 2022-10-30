package com.hee.to_do_list_project

import retrofit2.Call
import retrofit2.http.*

class ToDo(
    val id : Int,
    val content : String,
    val is_complete : Boolean,
    val created : String
)
interface RetrofitService {

    @GET("to-do/")
    fun getToDoList(
        @HeaderMap headers: Map<String, String>
    ):Call<ArrayList<ToDo>>

    @POST("to-do/")
    @FormUrlEncoded
    fun makeToDo(
        @HeaderMap headers: Map<String, String>,
        @FieldMap params: HashMap<String, Any>
    ): Call<Any>
}