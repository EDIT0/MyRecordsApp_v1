package com.privatememo.j.ui.bottombar.setting

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.privatememo.j.R
import com.privatememo.j.databinding.ProfilemanagementBinding
import com.privatememo.j.utility.ApplyFontModule
import com.privatememo.j.utility.Retrofit2Module
import com.privatememo.j.viewmodel.ProfileManagementViewModel
import kotlinx.android.synthetic.main.profilemanagement.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class ProfileManagement : AppCompatActivity() {

    lateinit var ProfilemanageBinding: ProfilemanagementBinding
    var profileManagementViewModel = ProfileManagementViewModel()

    lateinit var UriResult: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(ApplyFontModule.a.FontCall())

        profileManagementViewModel = ViewModelProvider(this).get(ProfileManagementViewModel::class.java)
        ProfilemanageBinding = DataBindingUtil.setContentView(this,
            R.layout.profilemanagement
        )
        ProfilemanageBinding.setLifecycleOwner(this)
        ProfilemanageBinding.profileManageViewModel = profileManagementViewModel

        ProfilemanageBinding.backbutton.setOnClickListener {
            setResult(601)
            finish()
        }

        ProfilemanageBinding.picture.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(Intent.createChooser(intent, "선택"), 101)
        }
        Log.i("tag","픽쳐유알아이: ${profileManagementViewModel.pictureUri.get()}")

        var getintent = getIntent()
        profileManagementViewModel.email = getintent.getStringExtra("email")!!
        profileManagementViewModel.getMemberProfile_call()

        var nickname = Observer<String>{ result ->
            ProfilemanageBinding.nickname.setText(profileManagementViewModel.nickname.value)
        }
        profileManagementViewModel.picPath.observe(ProfilemanageBinding.lifecycleOwner!!,nickname)

        var motto = Observer<String>{ result ->
            ProfilemanageBinding.motto.setText(profileManagementViewModel.motto.value)
        }
        profileManagementViewModel.picPath.observe(ProfilemanageBinding.lifecycleOwner!!,motto)

        var picPath = Observer<String>{ result ->
            Glide.with(this).load(result+ "? ${Date().getTime()}").error(R.drawable.ic_baseline_add_circle_outline_24).into(picture)
        }
        profileManagementViewModel.picPath.observe(ProfilemanageBinding.lifecycleOwner!!,picPath)

        var completeWorking = Observer<Boolean>{ result ->
            if(result == true) {
                if(profileManagementViewModel.pictureUri.get() != null) {
                    send_images_testRetrofit()
                }
                var intent = Intent(ProfilemanageBinding.root.context, SettingFragment::class.java);
                intent.putExtra("nickname", profileManagementViewModel.nickname.value);
                intent.putExtra("motto", profileManagementViewModel.motto.value);
                setResult(601,intent)
                finish()
            }
        }
        profileManagementViewModel.completeWorking.observe(ProfilemanageBinding.lifecycleOwner!!,completeWorking)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            UriResult = data.getData()!!

            if (resultCode == Activity.RESULT_OK) {
                Glide.with(this).load(UriResult).override(1000,1000).into(picture)
                profileManagementViewModel.pictureUri.set(UriResult)
                Log.i("tag","pictureUri: ${profileManagementViewModel.pictureUri.get().toString()}")

            } else if (requestCode == 101 && resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
            }
        }
    }


    fun send_images_testRetrofit(){

        Log.i("TAG","아아"+profileManagementViewModel.pictureUri.get()!!)
        var path = absolutelyPath(profileManagementViewModel.pictureUri.get()!!)
        println("경로 "+path)
        val file = File(path)
        var fileName = file.getName()
        fileName = "${profileManagementViewModel.email}.png"
        Log.i("tag","사진 제목: ${fileName}")
        profileManagementViewModel.pictureAddress.set(fileName)


        val retrofit2module = Retrofit2Module.getInstance()
        var (server, body) = retrofit2module.SendImageModule(file, fileName)
        server.ProfileImageSender("name2.png",body).enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("레트로핏 결과1","에러")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response?.isSuccessful) {
                    Log.d("레트로핏 결과2",""+response?.body().toString())
                } else {
                }
            }
        })
    }

    fun absolutelyPath(path: Uri): String {

        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = contentResolver.query(path, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }
}