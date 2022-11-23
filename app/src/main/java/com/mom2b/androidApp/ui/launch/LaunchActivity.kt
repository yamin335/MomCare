package com.mom2b.androidApp.ui.launch

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mom2b.androidApp.R
import com.mom2b.androidApp.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking {
            launch {
                delay(500L)
            }

            startActivity(
                Intent(this@LaunchActivity, MainActivity::class.java)
            )
            overridePendingTransition(R.anim.fade_in, R.anim.slide_out_left)
            finish()
        }
    }
}