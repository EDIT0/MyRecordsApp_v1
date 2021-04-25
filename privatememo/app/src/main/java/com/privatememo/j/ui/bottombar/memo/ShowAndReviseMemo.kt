package com.privatememo.j.ui.bottombar.memo

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.privatememo.j.R
import com.privatememo.j.databinding.ShowandrevisememoBinding
import com.privatememo.j.model.retrofit.Retrofit2Module
import com.privatememo.j.utility.*
import com.privatememo.j.viewmodel.ShowAndReviseMemoViewModel
import kotlinx.android.synthetic.main.showandrevisememo.*
import kotlinx.android.synthetic.main.showandrevisememo.addImage
import kotlinx.android.synthetic.main.showandrevisememo.backbutton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class ShowAndReviseMemo : AppCompatActivity() {

    lateinit var ShowAndReviseMemoBinding: ShowandrevisememoBinding
    var showAndReviseMemoViewModel = ShowAndReviseMemoViewModel()

    lateinit var one: ImageView
    lateinit var two: ImageView
    lateinit var three: ImageView
    lateinit var four: ImageView
    lateinit var five: ImageView
    var count = 0

    var Original_UriArray = ArrayList<Uri>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(ApplyFontModule.a.FontCall())

        showAndReviseMemoViewModel = ViewModelProvider(this).get(ShowAndReviseMemoViewModel::class.java)
        ShowAndReviseMemoBinding = DataBindingUtil.setContentView(this, R.layout.showandrevisememo)
        ShowAndReviseMemoBinding.setLifecycleOwner(this)
        ShowAndReviseMemoBinding.showAndReviseMemoViewModel = showAndReviseMemoViewModel



        var getintent = getIntent()
        var getbundle = getintent.getExtras()

        showAndReviseMemoViewModel.contentNum.set(getbundle?.getInt("contentNum"))
        showAndReviseMemoViewModel.title = getbundle?.getString("title")!!
        showAndReviseMemoViewModel.memo = getbundle?.getString("memo")!!
        showAndReviseMemoViewModel.date = getbundle?.getString("date")!!
        showAndReviseMemoViewModel.revisedate = getbundle?.getString("revisedate")!!
        showAndReviseMemoViewModel.time = getbundle?.getString("time")!!
        showAndReviseMemoViewModel.revisetime = getbundle?.getString("revisetime")!!
        showAndReviseMemoViewModel.ConBookmark = getbundle?.getString("ConBookmark")!!
        showAndReviseMemoViewModel.email = getbundle?.getString("email")!!
        showAndReviseMemoViewModel.cateNum.set(getbundle?.getInt("cateNum"))

        Log.i("tag","타이틀 ${showAndReviseMemoViewModel.title}")
        Log.i("tag","이메일 ${getbundle?.getString("email")}")
        Log.i("tag","카테 넘버 ${getbundle?.getInt("cateNum")}")

        showAndReviseMemoViewModel.getMemoImage_call()

        ShowAndReviseMemoBinding.justshowmemo.setTextSize(MemberSettingModule.ContentSize.toFloat())
        ShowAndReviseMemoBinding.title.setTextSize(MemberSettingModule.TitleSize.toFloat())
        ShowAndReviseMemoBinding.textMemo.setTextSize(MemberSettingModule.ContentSize.toFloat())

        backbutton.setOnClickListener {
            finish()
        }

        oneButton.setOnClickListener {
            val intent = Intent(this, WholeImageActivity::class.java)
            intent.putExtra("imageUri", showAndReviseMemoViewModel.items.get(0).imagePath)
            startActivity(intent)
        }

        twoButton.setOnClickListener {
            val intent = Intent(this, WholeImageActivity::class.java)
            intent.putExtra("imageUri", showAndReviseMemoViewModel.items.get(1).imagePath)
            startActivity(intent)
        }

        threeButton.setOnClickListener {
            val intent = Intent(this, WholeImageActivity::class.java)
            intent.putExtra("imageUri", showAndReviseMemoViewModel.items.get(2).imagePath)
            startActivity(intent)
        }

        fourButton.setOnClickListener {
            val intent = Intent(this, WholeImageActivity::class.java)
            intent.putExtra("imageUri", showAndReviseMemoViewModel.items.get(3).imagePath)
            startActivity(intent)
        }

        fiveButton.setOnClickListener {
            val intent = Intent(this, WholeImageActivity::class.java)
            intent.putExtra("imageUri", showAndReviseMemoViewModel.items.get(4).imagePath)
            startActivity(intent)
        }



        var connectionFinish = Observer<Boolean> { result ->
            if(result == true){
                for(i in 0 until showAndReviseMemoViewModel.items.size){
                    if(i==0){
                        imagearea.visibility = View.VISIBLE
                    }
                    when(i){
                        0 -> {
                            oneButton.visibility = View.VISIBLE
                            Glide.with(this).load(showAndReviseMemoViewModel.items.get(0).imagePath+ "? ${Date().getTime()}").override(1000,1000).into(oneButton)

                            showAndReviseMemoViewModel.uriHash.put("one",Uri.parse(showAndReviseMemoViewModel.items.get(0).imagePath))
                            showAndReviseMemoViewModel.ViewArray.add(one)
                            Glide.with(this).load(showAndReviseMemoViewModel.items.get(0).imagePath+ "? ${Date().getTime()}").override(1000,1000).into(one)
                            showAndReviseMemoViewModel.showImageView.addView(one)
                        }
                        1 -> {
                            twoButton.visibility = View.VISIBLE
                            Glide.with(this).load(showAndReviseMemoViewModel.items.get(1).imagePath+ "? ${Date().getTime()}").override(1000,1000).into(twoButton)

                            showAndReviseMemoViewModel.uriHash.put("two",Uri.parse(showAndReviseMemoViewModel.items.get(1).imagePath))
                            showAndReviseMemoViewModel.ViewArray.add(two)
                            Glide.with(this).load(showAndReviseMemoViewModel.items.get(1).imagePath+ "? ${Date().getTime()}").override(1000,1000).into(two)
                            showAndReviseMemoViewModel.showImageView.addView(two)
                        }
                        2 -> {
                            threeButton.visibility = View.VISIBLE
                            Glide.with(this).load(showAndReviseMemoViewModel.items.get(2).imagePath+ "? ${Date().getTime()}").override(1000,1000).into(threeButton)

                            showAndReviseMemoViewModel.uriHash.put("three",Uri.parse(showAndReviseMemoViewModel.items.get(2).imagePath))
                            showAndReviseMemoViewModel.ViewArray.add(three)
                            Glide.with(this).load(showAndReviseMemoViewModel.items.get(2).imagePath+ "? ${Date().getTime()}").override(1000,1000).into(three)
                            showAndReviseMemoViewModel.showImageView.addView(three)
                        }
                        3 -> {
                            fourButton.visibility = View.VISIBLE
                            Glide.with(this).load(showAndReviseMemoViewModel.items.get(3).imagePath+ "? ${Date().getTime()}").override(1000,1000).into(fourButton)

                            showAndReviseMemoViewModel.uriHash.put("four",Uri.parse(showAndReviseMemoViewModel.items.get(3).imagePath))
                            showAndReviseMemoViewModel.ViewArray.add(four)
                            Glide.with(this).load(showAndReviseMemoViewModel.items.get(3).imagePath+ "? ${Date().getTime()}").override(1000,1000).into(four)
                            showAndReviseMemoViewModel.showImageView.addView(four)
                        }
                        4 -> {
                            fiveButton.visibility = View.VISIBLE
                            Glide.with(this).load(showAndReviseMemoViewModel.items.get(4).imagePath+ "? ${Date().getTime()}").override(1000,1000).into(fiveButton)

                            showAndReviseMemoViewModel.uriHash.put("five",Uri.parse(showAndReviseMemoViewModel.items.get(4).imagePath))
                            showAndReviseMemoViewModel.ViewArray.add(five)
                            Glide.with(this).load(showAndReviseMemoViewModel.items.get(4).imagePath+ "? ${Date().getTime()}").override(1000,1000).into(five)
                            showAndReviseMemoViewModel.showImageView.addView(five)
                        }
                    }
                }
            }
        }
        showAndReviseMemoViewModel?.connectionFinish?.observe(ShowAndReviseMemoBinding.lifecycleOwner!!, connectionFinish)


        deletememo.setOnClickListener {
            lateinit var ShowAndReviseMemoDialog: Dialog

            ShowAndReviseMemoDialog = Utility.NormalDialogSetting(ShowAndReviseMemoBinding.root.context, R.layout.onlypiccustomdialog, 600)
            ShowAndReviseMemoDialog.show();

            ShowAndReviseMemoDialog.findViewById<TextView>(R.id.onlypicDelete).setOnClickListener {
                showAndReviseMemoViewModel.deleteMemo_call(showAndReviseMemoViewModel.contentNum.get()!!)
                ShowAndReviseMemoDialog.dismiss()
                setResult(153)
                finish()
            }
            ShowAndReviseMemoDialog.findViewById<TextView>(R.id.finish).setOnClickListener {
                ShowAndReviseMemoDialog.dismiss()
            }
        }


        //수정 부분
        showAndReviseMemoViewModel.showImageView = findViewById(R.id.imagelayout)
        one = ImageView(this)
        two = ImageView(this)
        three = ImageView(this)
        four = ImageView(this)
        five = ImageView(this)
        one.scaleType = ImageView.ScaleType.FIT_XY
        two.scaleType = ImageView.ScaleType.FIT_XY
        three.scaleType = ImageView.ScaleType.FIT_XY
        four.scaleType = ImageView.ScaleType.FIT_XY
        five.scaleType = ImageView.ScaleType.FIT_XY
        one.setBackgroundColor(Color.WHITE)
        two.setBackgroundColor(Color.WHITE)
        three.setBackgroundColor(Color.WHITE)
        four.setBackgroundColor(Color.WHITE)
        five.setBackgroundColor(Color.WHITE)
        var layoutParams = LinearLayout.LayoutParams(300, 300)
        one.layoutParams = layoutParams
        two.layoutParams = layoutParams
        three.layoutParams = layoutParams
        four.layoutParams = layoutParams
        five.layoutParams = layoutParams

        backbutton.setOnClickListener {
            setResult(154)
            finish()
        }

        addImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(Intent.createChooser(intent, "선택"), 111)
        }


        one.setOnClickListener {
            showAndReviseMemoViewModel.ViewArray.remove(one)
            showAndReviseMemoViewModel.uriHash.remove("one")
            RecallImage()
            Log.i("tag", "1번 지워짐")
        }

        two.setOnClickListener {
            showAndReviseMemoViewModel.ViewArray.remove(two)
            showAndReviseMemoViewModel.uriHash.remove("two")
            RecallImage()
            Log.i("tag", "2번 지워짐")
        }

        three.setOnClickListener {
            showAndReviseMemoViewModel.ViewArray.remove(three)
            showAndReviseMemoViewModel.uriHash.remove("three")
            RecallImage()
            Log.i("tag", "3번 지워짐")
        }

        four.setOnClickListener {
            showAndReviseMemoViewModel.ViewArray.remove(four)
            showAndReviseMemoViewModel.uriHash.remove("four")
            RecallImage()
            Log.i("tag", "4번 지워짐")
        }

        five.setOnClickListener {
            showAndReviseMemoViewModel.ViewArray.remove(five)
            showAndReviseMemoViewModel.uriHash.remove("five")
            RecallImage()
            Log.i("tag", "5번 지워짐")
        }

        var sendImageToServer = Observer<String> { result ->

            if(result == "image_yes") {

                for(i in showAndReviseMemoViewModel.uriHash) {

                    try {
                        if (i.value == showAndReviseMemoViewModel.save_original_pic_array.get(0)
                            || i.value == showAndReviseMemoViewModel.save_original_pic_array.get(1)
                            || i.value == showAndReviseMemoViewModel.save_original_pic_array.get(2)
                            || i.value == showAndReviseMemoViewModel.save_original_pic_array.get(3)
                            || i.value == showAndReviseMemoViewModel.save_original_pic_array.get(4)
                        ) {

                            Original_UriArray.add(i.value)

                            Log.i("tag", "중복 이미지")
                        }
                    }catch (e:Exception){}

                    try {
                        if (i.value != showAndReviseMemoViewModel.save_original_pic_array.get(0)
                            || i.value != showAndReviseMemoViewModel.save_original_pic_array.get(1)
                            || i.value != showAndReviseMemoViewModel.save_original_pic_array.get(2)
                            || i.value != showAndReviseMemoViewModel.save_original_pic_array.get(3)
                            || i.value != showAndReviseMemoViewModel.save_original_pic_array.get(4)
                        ) {
                            send_images_Retrofit(i.value)
                            Log.i("tag", "이미지 보내자")
                        }
                    }catch (e:Exception){}

                    Log.i("tag","키 벨류 : "+i.value +" / "+ i.key)
                }

                for(i in showAndReviseMemoViewModel.save_original_pic_array){
                    if(Original_UriArray.contains(i)){

                    }else{
                        showAndReviseMemoViewModel.DeleteImage(i.toString())
                        Log.i("tag","삭제될 이미지 uri ${i.toString()}")
                    }
                }

            }
            else if(result == "image_no") {

            }
            //Utility.OnlyPicLoadMore.isChanged = 1
            Thread.sleep(500)
            setResult(153)
            finish()

            /*showAndReviseMemoViewModel.ViewArray.clear()
            showAndReviseMemoViewModel.uriHash.clear()
            showAndReviseMemoViewModel.showImageView.removeAllViews()
            showAndReviseMemoViewModel.connectionFinish.value = false
            //showAndReviseMemoViewModel.save_original_pic_array.clear()
            showAndReviseMemoViewModel.items.clear()
            justshowtitle.text = showAndReviseMemoViewModel.title
            justshowmemo.text = showAndReviseMemoViewModel.memo
            showAndReviseMemoViewModel.getMemoImage_call()
            Thread.sleep(2000)*/
            //showAndReviseMemoViewModel.controler.value = false

        }
        showAndReviseMemoViewModel?.sendImageToServer?.observe(ShowAndReviseMemoBinding.lifecycleOwner!!, sendImageToServer)

    }



    fun RecallImage(){
        showAndReviseMemoViewModel.showImageView.removeAllViews()
        for(i in 0 until showAndReviseMemoViewModel.ViewArray.size){
            showAndReviseMemoViewModel.showImageView.addView(showAndReviseMemoViewModel.ViewArray.get(i))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 111 && resultCode == RESULT_OK && data != null) {
            var result = data.getData()

            if(resultCode == Activity.RESULT_OK){
                if(!showAndReviseMemoViewModel.ViewArray.contains(one)){
                    showAndReviseMemoViewModel.uriHash.put("one",result!!)
                    showAndReviseMemoViewModel.ViewArray.add(one)
                    Glide.with(this).load(result).override(1000,1000).into(one)
                    showAndReviseMemoViewModel.showImageView.addView(one)
                    println("1 / ${result}")
                }
                else if(!showAndReviseMemoViewModel.ViewArray.contains(two)){
                    showAndReviseMemoViewModel.uriHash.put("two",result!!)
                    showAndReviseMemoViewModel.ViewArray.add(two)
                    Glide.with(this).load(result).override(1000,1000).into(two)
                    showAndReviseMemoViewModel.showImageView.addView(two)
                    println("2 / ${result}")
                }
                else if(!showAndReviseMemoViewModel.ViewArray.contains(three)){
                    showAndReviseMemoViewModel.uriHash.put("three",result!!)
                    showAndReviseMemoViewModel.ViewArray.add(three)
                    Glide.with(this).load(result).override(1000,1000).into(three)
                    showAndReviseMemoViewModel.showImageView.addView(three)
                    println("3 / ${result}")
                }
                else if(!showAndReviseMemoViewModel.ViewArray.contains(four)){
                    showAndReviseMemoViewModel.uriHash.put("four",result!!)
                    showAndReviseMemoViewModel.ViewArray.add(four)
                    Glide.with(this).load(result).override(1000,1000).into(four)
                    showAndReviseMemoViewModel.showImageView.addView(four)
                    println("4 / ${result}")
                }
                else if(!showAndReviseMemoViewModel.ViewArray.contains(five)){
                    showAndReviseMemoViewModel.uriHash.put("five",result!!)
                    showAndReviseMemoViewModel.ViewArray.add(five)
                    Glide.with(this).load(result).override(1000,1000).into(five)
                    showAndReviseMemoViewModel.showImageView.addView(five)
                    println("5 / ${result}")
                }
            }
        }
        else if(requestCode == 111 && resultCode == RESULT_CANCELED){
        }

    }

    fun absolutelyPath(path: Uri): String {

        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = contentResolver.query(path, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }

    fun send_images_Retrofit(uri: Uri){

        count++

        var path = absolutelyPath(uri)
        println("경로 받아라 ! "+path)
        val file = File(path)
        var fileName = file.getName()
        var random = Random
        var randomNum = 0
        randomNum = random.nextInt(100000, 999999)

        fileName = "${showAndReviseMemoViewModel.email}_${randomNum}_"+fileName+"_${count}.png"
        Log.i("tag","파일네임: ${fileName}  이메일: ${showAndReviseMemoViewModel.email}")

        InsertImageAddressToServer(fileName)//DB에 주소 넣기

        val retrofit2module = Retrofit2Module.getInstance()
        var (server, body) = retrofit2module.SendImageModule(file, fileName)
        server.MemoImageSender("name2.png",body).enqueue(object: Callback<String> {
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

    fun InsertImageAddressToServer(fileName: String){
        val retrofit2module = Retrofit2Module.getInstance()

        var imageAddress = "http://edit0@edit0.dothome.co.kr/MyRecords/OnlyImage/Memo/"
        val call: Call<String> = retrofit2module.BaseModule().InsertImageAddressToServer(imageAddress+fileName, showAndReviseMemoViewModel.email,
                Integer.parseInt(showAndReviseMemoViewModel.contentNum.get().toString()))

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val result: String? = response.body()
                Log.i("tag","디비로 이미지 전송 완료")
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("??","error!!")
            }
        })
    }
}