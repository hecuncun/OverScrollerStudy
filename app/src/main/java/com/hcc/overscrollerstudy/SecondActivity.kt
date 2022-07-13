package com.hcc.overscrollerstudy

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SecondActivity:AppCompatActivity() {
    private var distance = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val tv = findViewById<JellyTextView>(R.id.tv_jelly)

        findViewById<Button>(R.id.btn_scroll_by).setOnClickListener {
            tv.scrollTo(distance,0)
            distance+=10
        }
        findViewById<Button>(R.id.btn_scroll_to).setOnClickListener {
            tv.scrollTo(30,0)
        }
        findViewById<Button>(R.id.btn_spring_back).setOnClickListener {
           tv.springBack()
        }

    }
}