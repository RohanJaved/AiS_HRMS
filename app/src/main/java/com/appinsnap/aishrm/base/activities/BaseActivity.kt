package com.appinsnap.aishrm.base.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.appinsnap.aishrm.MyApplication

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.get()!!.activity = this@BaseActivity

    }
}