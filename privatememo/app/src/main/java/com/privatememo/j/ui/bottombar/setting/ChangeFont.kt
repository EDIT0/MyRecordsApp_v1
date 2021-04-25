package com.privatememo.j.ui.bottombar.setting

import android.animation.ObjectAnimator
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.privatememo.j.R
import com.privatememo.j.databinding.ChangefontBinding
import com.privatememo.j.utility.AccessDatabase
import com.privatememo.j.utility.ApplyFontModule
import com.privatememo.j.utility.MemberSettingModule
import com.privatememo.j.viewmodel.ChangeFontViewModel

class ChangeFont : AppCompatActivity() {

    lateinit var ChangeFontBinding: ChangefontBinding
    var changeFontViewModel = ChangeFontViewModel()

    var font_list = ArrayList<String>()
    var FontModule = MemberSettingModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(ApplyFontModule.a.FontCall())

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        val width = size.x
        val height = size.y

        font_list.add("기본체")
        font_list.add("휴먼편지체")
        font_list.add("나눔바른고딕체")
        font_list.add("맑은 고딕체")

        changeFontViewModel = ViewModelProvider(this).get(ChangeFontViewModel::class.java)
        ChangeFontBinding = DataBindingUtil.setContentView(this,
                R.layout.changefont
        )
        ChangeFontBinding.setLifecycleOwner(this)
        ChangeFontBinding.changeFontViewModel = changeFontViewModel

        var getintent = getIntent()
        changeFontViewModel.email = getintent.getStringExtra("email")!!

        ChangeFontBinding.backbutton.setOnClickListener {
            setResult(603)
            finish()
        }

        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                height/2
        )
        layoutParams.leftMargin = 50
        layoutParams.rightMargin = 50
        ChangeFontBinding.Fontcontent.layoutParams = layoutParams


        //제목
        var fontToggle = Observer<Boolean>{ result ->
            if(result == false){
                var objectAnimator: ObjectAnimator =
                        ObjectAnimator.ofFloat(ChangeFontBinding.Fontcontent, "translationY",0.toFloat())
                objectAnimator.duration = 300
                objectAnimator.start()
            }
            else if(result == true){
                var objectAnimator: ObjectAnimator =
                        ObjectAnimator.ofFloat(ChangeFontBinding.Fontcontent,"translationY",-height/2.toFloat())
                objectAnimator.duration = 300
                objectAnimator.start()
            }
        }
        changeFontViewModel.fontToggle.observe(ChangeFontBinding.lifecycleOwner!!,fontToggle)

        ChangeFontBinding.fontlistView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    0 -> {
                        FontModule.Font = "기본체"
                    }
                    1 -> {
                        FontModule.Font = "휴먼편지체"
                    }
                    2 -> {
                        FontModule.Font = "나눔바른고딕체"
                    }
                    3 -> {
                        FontModule.Font = "맑은 고딕체"
                    }
                }
                Toast.makeText(ChangeFontBinding.root.context,"${FontModule.Font}로 변경", Toast.LENGTH_SHORT).show()
                changeFontViewModel.fontToggle.value = false
                ChangeFontSetting()

                setResult(6031)
                finish()
            }
        })
        val ChangeTextSizeTitle_adapter: ArrayAdapter<String> = ArrayAdapter<String>(ChangeFontBinding.root.context, android.R.layout.simple_list_item_1, font_list)
        ChangeFontBinding.fontlistView.setAdapter(ChangeTextSizeTitle_adapter)


    }

    fun ChangeFontSetting(){
        var DBinstance = AccessDatabase.getInstance()
        var accessDB = DBinstance.MemberSetting(this)
        accessDB.DaoMemberSetting().updateMemberSetting(FontModule.TitleSize, FontModule.ContentSize, FontModule.Font, changeFontViewModel.email)
    }
}