package com.privatememo.j.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.privatememo.j.FindAccount
import com.privatememo.j.R
import com.privatememo.j.databinding.WelcomeactivityBinding
import com.privatememo.j.service.NetworkService
import com.privatememo.j.ui.bottombar.MainActivity
import com.privatememo.j.viewmodel.WelcomeViewModel
import kotlinx.android.synthetic.main.welcomeactivity.*


class WelcomeActivity : AppCompatActivity() {

    lateinit var WelcomeBinding: WelcomeactivityBinding
    var welcomeViewModel = WelcomeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingPermission()

        welcomeViewModel = ViewModelProvider(this).get(WelcomeViewModel::class.java)
        WelcomeBinding = DataBindingUtil.setContentView(this, R.layout.welcomeactivity)
        WelcomeBinding.setLifecycleOwner(this)
        WelcomeBinding.welcomeViewModel = welcomeViewModel

        var Serviceintent = Intent(applicationContext,NetworkService::class.java)
        startService(Serviceintent)

        //AutoLogin
        val sp = getSharedPreferences("AutoLogin", Activity.MODE_PRIVATE)
        if (sp != null) {
            val id = sp.getString("id", "No data")
            val password = sp.getString("password", "No data")
            if(id == "No data" && password == "No data"){ }
            else{
                welcomeViewModel.Login_call(id!!, password!!)
            }
        }

        WelcomeBinding.signup.setOnClickListener{
            var intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RESULT_OK)
        }

        WelcomeBinding.findidpw.setOnClickListener {
            var intent = Intent(this, FindAccount::class.java)
            startActivityForResult(intent, 100)
        }

        var communication_check = Observer<Boolean>{ result ->
            if(result == true && (welcomeViewModel.login_check.get() == "true")){
                var intent = Intent(this, MainActivity::class.java)
                var bundle = Bundle()
                bundle.putString("email",welcomeViewModel.emailfromServer.get().toString())
                bundle.putString("nickname",welcomeViewModel.nicknamefromServer.get().toString())
                bundle.putString("motto",welcomeViewModel.mottofromServer.get().toString())
                bundle.putString("picPath",welcomeViewModel.picPathfromServer.get().toString())
                bundle.putString("password",welcomeViewModel.getPasswordfromMember.get().toString())

                Log.i("tag", "보내는 데이터 ${welcomeViewModel.emailfromServer.get().toString()}")
                intent.putExtras(bundle)
                startActivity(intent)
            }else if(result == false || (welcomeViewModel.login_check.get() == "false")){
                logincomment.text = "올바른 ID / Password를 입력하세요."
            }
        }
        welcomeViewModel?.communication_check?.observe(WelcomeBinding.lifecycleOwner!!, communication_check)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Activity.RESULT_OK){
            Log.i("TAG", "웰컴")
        }
    }

    fun settingPermission(){
        var permis = object  : PermissionListener {
            override fun onPermissionGranted() {
            }
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                ActivityCompat.finishAffinity(this@WelcomeActivity) // 권한 거부시 앱 종료
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permis)
            .setDeniedMessage("권한이 허가되지 않는다면, 서비스를 이용하실 수 없습니다.\n\n [설정] > [권한]에서 권한을 허가해주세요.")
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()
    }
}