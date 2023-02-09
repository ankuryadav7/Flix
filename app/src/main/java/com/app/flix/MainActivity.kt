package com.app.flix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.app.flix.list.ui.ListActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            /** launching the intent for listing activity*/
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)


    }
}