package com.privatememo.j.ui.login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.privatememo.j.R
import com.privatememo.j.utility.Utility

class LoadingActivity : AppCompatActivity() {
    lateinit var NetworkDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loadingactivity)

        startLoading()
    }

    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed({

            NetworkDialog = Utility.NetworkDialogSetting(this)
            var state = Utility.CheckNetworkState(applicationContext)

            if(state == true) {
                var intent = Intent(this,WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            else if(state == false){
                NetworkDialog.show();
            }

            NetworkDialog.findViewById<TextView>(R.id.retry).setOnClickListener {
                var state = Utility.CheckNetworkState(applicationContext)

                if(state == true) {
                    NetworkDialog.dismiss()
                    var intent = Intent(this,WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else if(state == false){
                    NetworkDialog.dismiss()
                    NetworkDialog.show()
                }
            }
        }, 2000)
    }
}