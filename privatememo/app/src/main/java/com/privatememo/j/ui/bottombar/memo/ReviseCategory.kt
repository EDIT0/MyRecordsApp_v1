package com.privatememo.j.ui.bottombar.memo

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.privatememo.j.R
import com.privatememo.j.databinding.RevisecategoryBinding
import com.privatememo.j.utility.ApplyFontModule
import com.privatememo.j.model.retrofit.Retrofit2Module
import com.privatememo.j.viewmodel.ReviseCategoryViewModel
import kotlinx.android.synthetic.main.makecategory.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ReviseCategory : AppCompatActivity() {

    lateinit var RevisecategoryBinding: RevisecategoryBinding
    var reviseMemoViewModel = ReviseCategoryViewModel()

    lateinit var UriResult: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(ApplyFontModule.a.FontCall())

        reviseMemoViewModel = ViewModelProvider(this).get(ReviseCategoryViewModel::class.java)
        RevisecategoryBinding = DataBindingUtil.setContentView(this,
            R.layout.revisecategory
        )
        RevisecategoryBinding.setLifecycleOwner(this)
        RevisecategoryBinding.reviewCategoryViewModel = reviseMemoViewModel

        var getintent = getIntent()
        var getbundle = getintent.getExtras()

        reviseMemoViewModel.cateNum.set(getbundle?.getInt("cateNum",0))
        reviseMemoViewModel.cateName = getbundle?.getString("catename")!!
        reviseMemoViewModel.cateExplanation = getbundle?.getString("explanation")!!
        reviseMemoViewModel.catepicPath = getbundle?.getString("catepicPath")!!
        reviseMemoViewModel.email = getbundle?.getString("email")!!
        reviseMemoViewModel.CateBookmark = getbundle?.getString("CateBookmark")!!

        Glide.with(this).load(reviseMemoViewModel.catepicPath+ "? ${Date().getTime()}").override(1000,1000).error(
            R.drawable.ic_baseline_block_24
        ).into(picture)

        if(getbundle?.getString("catepicPath")!!.length < 70){
            UriResult = Uri.parse("")
            reviseMemoViewModel.pictureUri.set(UriResult)
        }

        backbutton.setOnClickListener {
            finish()
        }

        RevisecategoryBinding.picture.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(Intent.createChooser(intent, "선택"), 101)
        }

        RevisecategoryBinding.deleteimage.setOnClickListener {
            picture.setImageDrawable(null)
            UriResult = Uri.parse("")
            reviseMemoViewModel.pictureUri.set(UriResult)

            //이거 작동 안함..
            Glide.with(this).load(R.drawable.ic_baseline_block_24).error(
                R.drawable.ic_baseline_block_24
            ).override(1000,1000).into(picture)
        }

        var sendImageToServer = Observer<String> { result ->
            if(result == "image_yes") {
                send_images_testRetrofit()
            }
            else if(result == "image_no") {

            }
            finish()
        }
        reviseMemoViewModel?.sendImageToServer?.observe(RevisecategoryBinding.lifecycleOwner!!, sendImageToServer)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            UriResult = data.getData()!!

            if (resultCode == Activity.RESULT_OK) {
                Glide.with(this).load(UriResult).override(1000,1000).into(picture)
                reviseMemoViewModel.pictureUri.set(UriResult)

            } else if (requestCode == 101 && resultCode == RESULT_CANCELED) {
            }
        }
    }

    fun send_images_testRetrofit(){
        var date = SimpleDateFormat()
        var dt = Date()
        date = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
        println(date.format(dt).toString())
        //var date_filename = date.format(dt).toString()
        Log.i("TAG","아아"+reviseMemoViewModel.pictureUri.get()!!)
        var path = absolutelyPath(reviseMemoViewModel.pictureUri.get()!!)
        println("경로 "+path)
        val file = File(path)
        var fileName = file.getName()
        fileName = "Category_${reviseMemoViewModel.email}_${date.format(dt).toString()}_.png"
        Log.i("tag","사진 제목 ${fileName}")
        reviseMemoViewModel.pictureAddress.set(fileName)


        val retrofit2module = Retrofit2Module.getInstance()
        var (server, body) = retrofit2module.SendImageModule(file, fileName)
        server.CategoryImageSender("name2.png",body).enqueue(object: Callback<String> {
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