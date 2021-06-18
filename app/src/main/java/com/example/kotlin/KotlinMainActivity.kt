package com.example.kotlin

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mrslibutterknife.R
import kotlinx.coroutines.*


class KotlinMainActivity : AppCompatActivity() {
    var num = 0
    lateinit var tvNum:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_main)
        tvNum = findViewById<TextView>(R.id.tv_num)
        var run = findViewById<TextView>(R.id.run)

        val handler: Handler = Handler {
            tvNum.text = it.what.toString()
            true
        }
//        run.setOnClickListener{
//            GlobalScope.launch {
//                dplyNum()
//                var message = Message()
//                message.what = num
//                handler.sendMessage(message)
//            }
//        }

        run.setOnClickListener{
            GlobalScope.launch {
                runMainIONum()
            }
        }
    }

    suspend fun runMainIONum(){
       withContext(Dispatchers.Main){
           num++
           tvNum.text = num.toString()
       }
    }

    suspend fun dplyNum(){
        delay(10000)
        num++
    }
}