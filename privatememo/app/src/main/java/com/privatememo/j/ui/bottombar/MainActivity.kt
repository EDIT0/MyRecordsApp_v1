package com.privatememo.j.ui.bottombar

import android.app.Activity
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.privatememo.j.R
import com.privatememo.j.database.table.EntityMemberSetting
import com.privatememo.j.databinding.ActivityMainBinding
import com.privatememo.j.ui.bottombar.calendar.CalendarFragment
import com.privatememo.j.ui.bottombar.memo.MemoFragment
import com.privatememo.j.ui.bottombar.search.SearchFragment
import com.privatememo.j.ui.bottombar.setting.SettingFragment
import com.privatememo.j.utility.AccessDatabase
import com.privatememo.j.utility.ApplyFontModule
import com.privatememo.j.utility.MemberSettingModule
import com.privatememo.j.utility.Utility
import com.privatememo.j.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    var fm: FragmentManager = supportFragmentManager
    lateinit var MainBinding: ActivityMainBinding
    var mainViewModel = MainViewModel(Utility.repositoryModule.repositorymodule)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(ApplyFontModule.a.FontCall())

        //mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        MainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        MainBinding.setLifecycleOwner(this)
        MainBinding.mainViewModel = mainViewModel



        //fm.beginTransaction().replace(R.id.framelayout, fragment2()).commit()
        with(fm.beginTransaction()){
            replace(
                R.id.framelayout,
                    MemoFragment()
            )
            commit()
        }

        bottom_tab.setOnNavigationItemSelectedListener(this)

        var getintent = getIntent();
        var getbundle = getintent.getExtras();

        mainViewModel.email.setValue(getbundle?.getString("email"))
        mainViewModel.nickname.setValue(getbundle?.getString("nickname"))
        mainViewModel.motto.setValue(getbundle?.getString("motto"))
        mainViewModel.picPath.setValue(getbundle?.getString("picPath"))
        mainViewModel.password = getbundle?.getString("password")!!

        Log.i("TAG", "넘어온 데이터1 ${getbundle?.getString("email")}, ${getbundle?.getString("nickname")} " +
                "${getbundle?.getString("motto")} ${getbundle?.getString("picPath")}")
        Log.i("TAG", "넘어온 데이터2 ${mainViewModel.email.getValue()}, ${mainViewModel.nickname.getValue()} ${mainViewModel.motto.getValue()} " +
                "${mainViewModel.picPath.getValue()}")

        Check_MemberSetting()


        //AutoLogin
        thread(start = true){
            val sp = getSharedPreferences("AutoLogin", Activity.MODE_PRIVATE)
            val editor = sp.edit() //edit 메소드 호출
            editor.putString("id", mainViewModel.email.value.toString())
            editor.putString("password", mainViewModel.password)
            editor.commit()
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("혹시","이거 호출됨?")
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId){
            R.id.toolbar_item_memo ->{
                fm.beginTransaction().replace(
                    R.id.framelayout,
                        MemoFragment()
                ).commit()

                return true
            }
            R.id.toolbar_item_search -> {
                fm.beginTransaction().replace(
                    R.id.framelayout,
                        SearchFragment()
                ).commit()
                return true
            }
            R.id.toolbar_item_calendar -> {
                fm.beginTransaction().replace(
                    R.id.framelayout,
                    CalendarFragment()
                ).commit()
                return true
            }
            R.id.toolbar_item_setting -> {
                fm.beginTransaction().replace(
                    R.id.framelayout,
                    SettingFragment()
                ).commit()
                return true
            }
        }
        return false
    }


    fun Check_MemberSetting(){
        var DBinstance = AccessDatabase.getInstance()
        var accessDB = DBinstance.MemberSetting(this)

        var checkEmailValue = accessDB.DaoMemberSetting().checkEmail(mainViewModel.email.value.toString())
        if(checkEmailValue == null){
            Log.i("tag","과연 값은? ${checkEmailValue} / ${mainViewModel.email.value}")
            accessDB.DaoMemberSetting().insert(
                EntityMemberSetting(
                    mainViewModel.email.value.toString(),
                    MemberSettingModule.TitleSize,
                    MemberSettingModule.ContentSize,
                    MemberSettingModule.Font
                )
            )
            Log.i("tag","디비 호출")

            var data = accessDB.DaoMemberSetting().getEmailData(mainViewModel.email.value.toString())
            for(i in data){
                MemberSettingModule.TitleSize = i.TitleTextSize
                MemberSettingModule.ContentSize = i.ContentTextSize
                MemberSettingModule.Font = i.Font
                Log.i("tag","배열 넣기 null인 경우")
            }
        }
        else{
            var data = accessDB.DaoMemberSetting().getEmailData(mainViewModel.email.value.toString())
            for(i in data){
                MemberSettingModule.TitleSize = i.TitleTextSize
                MemberSettingModule.ContentSize = i.ContentTextSize
                MemberSettingModule.Font = i.Font
                Log.i("tag","배열 넣기 null이 아닌 경우")
                Log.i("tag","${accessDB.DaoMemberSetting().getAll()}")
            }
        }
    }

    private var backBtnTime: Long = 0
    override fun onBackPressed() {
        val curTime = System.currentTimeMillis()
        val gapTime = curTime - backBtnTime
        if (0 <= gapTime && 1000 >= gapTime) {
            moveTaskToBack(true)
            finish()
            Process.killProcess(Process.myPid())
        } else {
            backBtnTime = curTime
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }
}