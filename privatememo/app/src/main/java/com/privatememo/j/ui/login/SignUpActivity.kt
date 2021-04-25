package com.privatememo.j.ui.login

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.privatememo.j.R
import com.privatememo.j.databinding.SignupactivityBinding
import com.privatememo.j.utility.Retrofit2Module
import com.privatememo.j.viewmodel.SignUpViewModel
import kotlinx.android.synthetic.main.signupactivity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SignUpActivity : AppCompatActivity() {
    lateinit var SignUpBinding: SignupactivityBinding
    lateinit var signupViewModel: SignUpViewModel
    lateinit var UriResult: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signupViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        SignUpBinding = DataBindingUtil.setContentView(this, R.layout.signupactivity)
        SignUpBinding.setLifecycleOwner(this)
        SignUpBinding.signViewModel = signupViewModel


        SignUpBinding.backbutton.setOnClickListener {
            setResult(RESULT_OK)

            finish();
        }

        SignUpBinding.picture.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(Intent.createChooser(intent, "선택"), 101)
        }


        var checkNum = Observer<String> { result ->
            if(result == signupViewModel.randomNum.toString()){
                SignUpBinding.checknumber.setText("$result 인증완료")
                SignUpBinding.checknumber.setFocusable(false)
            }
        }
        signupViewModel?.checkNum?.observe(SignUpBinding.lifecycleOwner!!, checkNum)

        var duplicate_email_check = Observer<String> { result ->
            if(signupViewModel.duplicate_email_check.getValue() == "false"){
                signupViewModel.emailStatus.set(false)
                signupViewModel.email_certificate.set(true)
                SignUpBinding.emailComment.text = "이메일이 확인되었습니다. 인증번호를 받으세요."
            }
            else{
                SignUpBinding.emailComment.text = "이미 가입된 이메일 입니다."
            }
        }
        signupViewModel?.duplicate_email_check?.observe(SignUpBinding.lifecycleOwner!!, duplicate_email_check)

        var sendImageToServer = Observer<String> { result ->
            if(result == "image_yes") {
                send_images_testRetrofit()
            }
            else if(result == "image_no") {

            }
            setResult(RESULT_OK)
            finish()
        }
        signupViewModel?.sendImageToServer?.observe(SignUpBinding.lifecycleOwner!!, sendImageToServer)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            UriResult = data.getData()!!

            if (resultCode == Activity.RESULT_OK) {
                Glide.with(this).load(UriResult).override(1000,1000).into(picture)
                signupViewModel.pictureUri.set(UriResult)

            } else if (requestCode == 101 && resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
            }
        }
    }


    fun send_images_testRetrofit(){

        Log.i("TAG","아아"+signupViewModel.pictureUri.get()!!)
        var path = absolutelyPath(signupViewModel.pictureUri.get()!!)
        println("경로 "+path)
        val file = File(path)
        var fileName = file.getName()
        fileName = "${signupViewModel.email.get().toString()}.png"
        Log.i("tag","사진 제목 ${fileName}")
        signupViewModel.pictureAddress.set(fileName)


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