package com.privatememo.j.ui.bottombar.setting

import android.animation.ObjectAnimator
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.privatememo.j.R
import com.privatememo.j.databinding.ChangetextsizeBinding
import com.privatememo.j.utility.AccessDatabase
import com.privatememo.j.utility.ApplyFontModule
import com.privatememo.j.utility.MemberSettingModule
import com.privatememo.j.viewmodel.ChangeTextSizeViewModel


class ChangeTextSize : AppCompatActivity() {

    lateinit var ChangeTextSizeBinding: ChangetextsizeBinding
    var changeTextSizeViewModel = ChangeTextSizeViewModel()

    var title_list = ArrayList<String>()
    var content_list = ArrayList<String>()
    var textSizeModule = MemberSettingModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(ApplyFontModule.a.FontCall())

        val display = windowManager.defaultDisplay // in case of Activity
        /* val display = activity!!.windowManaver.defaultDisplay */ // in case of Fragment
        val size = Point()
        display.getRealSize(size) // or getSize(size)
        val width = size.x
        val height = size.y

        title_list.add("12")
        title_list.add("13")
        title_list.add("14")
        title_list.add("15")
        title_list.add("16")
        title_list.add("17")
        title_list.add("18")
        title_list.add("19")
        title_list.add("20 (기본)")
        title_list.add("21")
        title_list.add("22")
        title_list.add("23")
        title_list.add("24")
        title_list.add("25")
        title_list.add("26")

        content_list.add("12")
        content_list.add("13")
        content_list.add("14")
        content_list.add("15")
        content_list.add("16")
        content_list.add("17")
        content_list.add("18 (기본)")
        content_list.add("19")
        content_list.add("20")
        content_list.add("21")
        content_list.add("22")
        content_list.add("23")
        content_list.add("24")
        content_list.add("25")
        content_list.add("26")



        changeTextSizeViewModel = ViewModelProvider(this).get(ChangeTextSizeViewModel::class.java)
        ChangeTextSizeBinding = DataBindingUtil.setContentView(this,
            R.layout.changetextsize
        )
        ChangeTextSizeBinding.setLifecycleOwner(this)
        ChangeTextSizeBinding.changeTextSizeViewModel = changeTextSizeViewModel

        var getintent = getIntent()
        changeTextSizeViewModel.email = getintent.getStringExtra("email")!!

        ChangeTextSizeBinding.backbutton.setOnClickListener {
            setResult(602)
            finish()
        }

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            height/2
        )
        layoutParams.leftMargin = 50
        layoutParams.rightMargin = 50
        ChangeTextSizeBinding.Titlecontent.layoutParams = layoutParams
        ChangeTextSizeBinding.Memocontent.layoutParams = layoutParams


        //제목
        var titleToggle = Observer<Boolean>{ result ->
            if(result == false){
                var objectAnimator: ObjectAnimator =
                    ObjectAnimator.ofFloat(ChangeTextSizeBinding.Titlecontent, "translationY",0.toFloat())
                objectAnimator.duration = 300
                objectAnimator.start()
            }
            else if(result == true){
                changeTextSizeViewModel.contentToggle.value = false
                var objectAnimator: ObjectAnimator =
                    ObjectAnimator.ofFloat(ChangeTextSizeBinding.Titlecontent,"translationY",-height/2.toFloat())
                objectAnimator.duration = 300
                objectAnimator.start()
            }
        }
        changeTextSizeViewModel.titleToggle.observe(ChangeTextSizeBinding.lifecycleOwner!!,titleToggle)

        ChangeTextSizeBinding.titlelistView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    0 -> {
                        textSizeModule.TitleSize = 12
                    }
                    1 -> {
                        textSizeModule.TitleSize = 13
                    }
                    2 -> {
                        textSizeModule.TitleSize = 14
                    }
                    3 -> {
                        textSizeModule.TitleSize = 15
                    }
                    4 -> {
                        textSizeModule.TitleSize = 16
                    }
                    5 -> {
                        textSizeModule.TitleSize = 17
                    }
                    6 -> {
                        textSizeModule.TitleSize = 18
                    }
                    7 -> {
                        textSizeModule.TitleSize = 19
                    }
                    8 -> {
                        textSizeModule.TitleSize = 20
                    }
                    9 -> {
                        textSizeModule.TitleSize = 21
                    }
                    10 -> {
                        textSizeModule.TitleSize = 22
                    }
                    11 -> {
                        textSizeModule.TitleSize = 23
                    }
                    12 -> {
                        textSizeModule.TitleSize = 24
                    }
                    13 -> {
                        textSizeModule.TitleSize = 25
                    }
                    14 -> {
                        textSizeModule.TitleSize = 26
                    }
                }
                Toast.makeText(ChangeTextSizeBinding.root.context,"${textSizeModule.TitleSize} 크기로 변경",Toast.LENGTH_SHORT).show()
                changeTextSizeViewModel.titleToggle.value = false
                ChangeTextSetting("title")
            }
        })
        val ChangeTextSizeTitle_adapter: ArrayAdapter<String> = ArrayAdapter<String>(ChangeTextSizeBinding.root.context, android.R.layout.simple_list_item_1, title_list)
        ChangeTextSizeBinding.titlelistView.setAdapter(ChangeTextSizeTitle_adapter)


        //내용
        var contentToggle = Observer<Boolean>{ result ->
            if(result == false){
                var objectAnimator: ObjectAnimator =
                    ObjectAnimator.ofFloat(ChangeTextSizeBinding.Memocontent, "translationY",0.toFloat())
                objectAnimator.duration = 300
                objectAnimator.start()
            }
            else if(result == true){
                changeTextSizeViewModel.titleToggle.value = false
                var objectAnimator: ObjectAnimator =
                    ObjectAnimator.ofFloat(ChangeTextSizeBinding.Memocontent,"translationY",-height/2-(height/2).toFloat())
                objectAnimator.duration = 300
                objectAnimator.start()
            }
        }
        changeTextSizeViewModel.contentToggle.observe(ChangeTextSizeBinding.lifecycleOwner!!,contentToggle)

        ChangeTextSizeBinding.contentlistView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    0 -> {
                        textSizeModule.ContentSize = 12
                    }
                    1 -> {
                        textSizeModule.ContentSize = 13
                    }
                    2 -> {
                        textSizeModule.ContentSize = 14
                    }
                    3 -> {
                        textSizeModule.ContentSize = 15
                    }
                    4 -> {
                        textSizeModule.ContentSize = 16
                    }
                    5 -> {
                        textSizeModule.ContentSize = 17
                    }
                    6 -> {
                        textSizeModule.ContentSize = 18
                    }
                    7 -> {
                        textSizeModule.ContentSize = 19
                    }
                    8 -> {
                        textSizeModule.ContentSize = 20
                    }
                    9 -> {
                        textSizeModule.ContentSize = 21
                    }
                    10 -> {
                        textSizeModule.ContentSize = 22
                    }
                    11 -> {
                        textSizeModule.ContentSize = 23
                    }
                    12 -> {
                        textSizeModule.ContentSize = 24
                    }
                    13 -> {
                        textSizeModule.ContentSize = 25
                    }
                    14 -> {
                        textSizeModule.ContentSize = 26
                    }
                }
                Toast.makeText(ChangeTextSizeBinding.root.context,"${textSizeModule.ContentSize} 크기로 변경",Toast.LENGTH_SHORT).show()
                changeTextSizeViewModel.contentToggle.value = false
                ChangeTextSetting("content")
            }
        })
        val ChangeTextSizeContent_adapter: ArrayAdapter<String> = ArrayAdapter<String>(ChangeTextSizeBinding.root.context, android.R.layout.simple_list_item_1, content_list)
        ChangeTextSizeBinding.contentlistView.setAdapter(ChangeTextSizeContent_adapter)

    }

    fun ChangeTextSetting(str: String){
        var DBinstance = AccessDatabase.getInstance()
        var accessDB = DBinstance.MemberSetting(this)

        when(str){
            "title" -> {
                accessDB.DaoMemberSetting().updateMemberSetting(textSizeModule.TitleSize, textSizeModule.ContentSize, textSizeModule.Font, changeTextSizeViewModel.email)
            }
            "content" -> {
                accessDB.DaoMemberSetting().updateMemberSetting(textSizeModule.TitleSize, textSizeModule.ContentSize, textSizeModule.Font, changeTextSizeViewModel.email)
            }
        }
    }
}