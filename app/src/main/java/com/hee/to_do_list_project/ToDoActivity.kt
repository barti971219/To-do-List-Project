package com.hee.to_do_list_project

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ContentView
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ToDoActivity : AppCompatActivity() {
    lateinit var todoRecyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)

        findViewById<ImageView>(R.id.write).setOnClickListener{
            startActivity(Intent(this, ToDoWriteActivity::class.java))
        }

        todoRecyclerView = findViewById(R.id.todo_list)
        getToDoList()
    }

    fun getToDoList(){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitService = retrofit.create(RetrofitService::class.java)

        val header = HashMap<String, String>()
        val sp = this.getSharedPreferences(
            "user_info",
            Context.MODE_PRIVATE
        )
        val token = sp.getString("token", "")
        header.put("Authorization", "token " + token!!)

        retrofitService.getToDoList(header).enqueue(object:Callback<ArrayList<ToDo>>{
            override fun onResponse(
                call: Call<ArrayList<ToDo>>,
                response: Response<ArrayList<ToDo>>
            ) {
                if(response.isSuccessful){
                    val todoList = response.body()
                    for(i:Int in 0 until todoList!!.size){
                        Log.d("todoo", todoList.get(i).content)
                    }
                    todoRecyclerView.adapter =
                        ToDoListRecyclerViewAdapter(
                            todoList!!,
                            LayoutInflater.from(this@ToDoActivity),
                            resources
                        )
                }
            }

            override fun onFailure(call: Call<ArrayList<ToDo>>, t: Throwable) {
            }
        })
    }
}

class ToDoListRecyclerViewAdapter(
   val todoList : ArrayList<ToDo>,
   val inflater : LayoutInflater,
   val resource : Resources
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var previousDate : String = ""
    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val dateTextView : TextView

        init {
            dateTextView = itemView.findViewById(R.id.date)
        }
    }

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val content : TextView
        val isComplete : ImageView

        init {
            content = itemView.findViewById(R.id.content)
            isComplete = itemView.findViewById(R.id.is_complete)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val todo = todoList.get(position)
        val tempDate = todo.created.split("T")[0]
        if(previousDate == tempDate){
            return 0
        }else{
            previousDate = tempDate
            return 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            1 -> return DateViewHolder(inflater.inflate(R.layout.todo_date, parent, false))
            else -> return ContentViewHolder(inflater.inflate(R.layout.todo_content, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val todo = todoList.get(position)
        if(holder is DateViewHolder){
            (holder as DateViewHolder).dateTextView.text = todo.created.split("T")[0]
        }else{
            (holder as ContentViewHolder).content.text = todo.content
            if(todo.is_complete){
                (holder as ContentViewHolder).isComplete.setImageDrawable(resource.getDrawable(R.drawable.btn_radio_check))
            }else{
                (holder as ContentViewHolder).isComplete.setImageDrawable(resource.getDrawable(R.drawable.btn_radio))
            }
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
}