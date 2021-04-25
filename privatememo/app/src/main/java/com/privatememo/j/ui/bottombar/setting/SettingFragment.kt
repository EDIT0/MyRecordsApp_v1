package com.privatememo.j.ui.bottombar.setting

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.privatememo.j.R
import com.privatememo.j.model.datamodel.CheckPWMatching
import com.privatememo.j.ui.bottombar.MainActivity
import com.privatememo.j.ui.login.WelcomeActivity
import com.privatememo.j.utility.ApplyFontModule
import com.privatememo.j.utility.Retrofit2Module
import com.privatememo.j.utility.Utility
import kotlinx.android.synthetic.main.settingfragment.*
import kotlinx.android.synthetic.main.settingfragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread


class SettingFragment : Fragment() {

    val retrofit2module = Retrofit2Module.getInstance()

    var email = ""
    lateinit var rootView: View

    lateinit var LogoutDialog: Dialog
    lateinit var DeleteDialog: Dialog
    lateinit var GoodbyeDialog: Dialog
    lateinit var NotMatchDialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getContext()?.getTheme()?.applyStyle(ApplyFontModule.a.FontCall(), true)

        rootView = inflater.inflate(R.layout.settingfragment, settingfrag, false)

        var act = activity as MainActivity
        email = act.mainViewModel.email.value?:""

        rootView.profile.setOnClickListener {
            var gotoProfile = Intent(rootView.context, ProfileManagement::class.java)
            gotoProfile.putExtra("email",email)
            startActivityForResult(gotoProfile,601)
        }

        rootView.changeTextSize.setOnClickListener {
            var gotoChangeTextSize = Intent(rootView.context, ChangeTextSize::class.java)
            gotoChangeTextSize.putExtra("email",email)
            startActivityForResult(gotoChangeTextSize,602)
        }

        rootView.changeFont.setOnClickListener {
            var gotoChangeFont = Intent(rootView.context, ChangeFont::class.java)
            gotoChangeFont.putExtra("email",email)
            startActivityForResult(gotoChangeFont,603)
        }

        rootView.logout.setOnClickListener {
            showLogoutDialog()
        }

        rootView.deleteMemeber.setOnClickListener {
            showDeleteMemberDialog(email)
        }



        return rootView
    }

    override fun onStart() {
        super.onStart()
        Log.i("tag","셋팅프래그먼트 온 스타트")

    }

    override fun onResume() {
        super.onResume()
        Log.i("tag","셋팅프래그먼트 온 리숨")
        if(Utility.NetworkState.off == true){
            Utility.NetworkUnavailable(rootView.context)
        }
    }

    fun showLogoutDialog(){
        LogoutDialog = Utility.NormalDialogSetting(rootView.context, R.layout.logoutcustomdialog, 800)
        LogoutDialog.show();

        LogoutDialog.findViewById<TextView>(R.id.yes).setOnClickListener {
            //AutoLogin
            thread(start = true){
                val sp = activity!!.getSharedPreferences("AutoLogin", Activity.MODE_PRIVATE)
                val editor = sp.edit()
                editor.clear()
                editor.commit()
            }


            var intent = Intent(context, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_SINGLE_TOP + Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            LogoutDialog.dismiss()
        }
        LogoutDialog.findViewById<TextView>(R.id.no).setOnClickListener {
            LogoutDialog.dismiss()
        }
    }

    fun showDeleteMemberDialog(email: String){
        DeleteDialog = Utility.NormalDialogSetting(rootView.context, R.layout.deletemembercustomdialog, 800)
        DeleteDialog.show();

        DeleteDialog.findViewById<TextView>(R.id.yes).setOnClickListener {
            FormatchingPassword_call(email, DeleteDialog.findViewById<EditText>(R.id.passwordfordelete).getText().toString())
            DeleteDialog.dismiss()
        }
        DeleteDialog.findViewById<TextView>(R.id.no).setOnClickListener {
            DeleteDialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var act = activity as MainActivity

        if(requestCode == 601 && resultCode == 601){
            val nickname = data?.getStringExtra("nickname")
            val motto = data?.getStringExtra("motto")
            act.mainViewModel.nickname.value = nickname
            act.mainViewModel.motto.value = motto
        }
        else if(requestCode == 602 && resultCode == 602){

        }
        else if(requestCode == 603 && resultCode == 6031){
            var act = activity as MainActivity
            Log.i("tag","603호출")
            var intent = Intent(context,MainActivity::class.java)
            intent.putExtra("email",act.mainViewModel.email.value.toString())
            intent.putExtra("nickname",act.mainViewModel.nickname.value.toString())
            intent.putExtra("motto",act.mainViewModel.motto.value.toString())
            intent.putExtra("picPath",act.mainViewModel.picPath.value.toString())
            intent.putExtra("password",act.mainViewModel.password)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    fun FormatchingPassword_call(email: String, pw: String){
        val call: Call<CheckPWMatching> = retrofit2module.BaseModule().ForMatchingpassword(email,pw)

        call.enqueue(object : Callback<CheckPWMatching> {
            override fun onResponse(call: Call<CheckPWMatching>, response: Response<CheckPWMatching>) {
                val result: CheckPWMatching? = response.body()
                var returnValue = result?.returnvalue

                if(returnValue == "true"){
                    GoodbyeDialog = Utility.NormalDialogSetting(rootView.context, R.layout.goodbyecustomdialog, 800)
                    GoodbyeDialog.show();

                    //삭제호출
                    DeleteMember_call(email)


                    GoodbyeDialog.findViewById<TextView>(R.id.check).setOnClickListener {
                        GoodbyeDialog.dismiss()
                        val sp = activity!!.getSharedPreferences("AutoLogin", Activity.MODE_PRIVATE)
                        val editor = sp.edit()
                        editor.clear()
                        editor.commit()
                        var intent = Intent(context, WelcomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_SINGLE_TOP + Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                }
                else if(returnValue == "false"){
                    NotMatchDialog = Utility.NormalDialogSetting(rootView.context, R.layout.notmatchcustomdialog, 800)
                    NotMatchDialog.show();

                    NotMatchDialog.findViewById<TextView>(R.id.check).setOnClickListener {
                        NotMatchDialog.dismiss()
                    }
                }
            }

            override fun onFailure(call: Call<CheckPWMatching>, t: Throwable) {
                Log.i("??","onFailure")
            }
        })
    }

    fun DeleteMember_call(email: String){
        val call: Call<String> = retrofit2module.BaseModule().DeleteMember(email)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val result: String? = response.body()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("??","ongoing")
            }
        })
    }
}