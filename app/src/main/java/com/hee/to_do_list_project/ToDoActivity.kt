package com.hee.to_do_list_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class ToDoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)

        findViewById<ImageView>(R.id.write).setOnClickListener{
            startActivity(Intent(this, ToDoWriteActivity::class.java))
        }
    }
}