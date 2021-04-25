package com.privatememo.j.ui.bottombar.memo

import android.animation.ObjectAnimator
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.privatememo.j.R
import com.privatememo.j.adapter.*
import com.privatememo.j.api.AdapterListener
import com.privatememo.j.databinding.EachmemoactivityBinding
import com.privatememo.j.datamodel.MemoInfo
import com.privatememo.j.utility.ApplyFontModule
import com.privatememo.j.utility.Utility
import com.privatememo.j.viewmodel.EachMemoViewModel
import kotlinx.android.synthetic.main.eachmemoactivity.*
import kotlinx.android.synthetic.main.memofragment.backbutton
import java.util.*
import kotlin.collections.ArrayList


class EachMemoActivity : AppCompatActivity() {

    lateinit var EachMemoBinding: EachmemoactivityBinding
    var eachMemoViewModel = EachMemoViewModel(Utility.repositoryModule.repositorymodule)
    var adapter = EachMemoAdapter()

    lateinit var EachMemoDialog: Dialog
    lateinit var progressDialog:ProgressDialog

    var font_list = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(ApplyFontModule.a.FontCall())

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        val width = size.x
        val height = size.y

        font_list.add("만든 날짜 순")
        font_list.add("수정 날짜 순")

        //eachMemoViewModel = ViewModelProvider(this).get(EachMemoViewModel::class.java)
        EachMemoBinding = DataBindingUtil.setContentView(this, R.layout.eachmemoactivity)
        EachMemoBinding.setLifecycleOwner(this)
        EachMemoBinding.eachMemoViewModel = eachMemoViewModel

        progressDialog = ProgressDialog(EachMemoBinding.root.context, android.R.style.Theme_Material_Dialog_Alert)

        EachMemoDialog = Utility.NormalDialogSetting(EachMemoBinding.root.context, R.layout.onlypiccustomdialog, 600)

        var getintent = getIntent()
        eachMemoViewModel.email = getintent.getStringExtra("email")!!
        eachMemoViewModel.cateName = getintent.getStringExtra("catename")!!
        eachMemoViewModel.cateNum = getintent.getStringExtra("catenum")!!

        var layoutmanager = LinearLayoutManager(EachMemoBinding.memoRcv.context)
        EachMemoBinding.memoRcv.layoutManager = layoutmanager
        EachMemoBinding.memoRcv.adapter = adapter

        Utility.EachMemoFloating.FloatingState = 0
        eachMemoViewModel.getMemoList_call(0, 10, Utility.EachMemoSort.SortState)

        backbutton.setOnClickListener {
            finish()
        }

        makememo.setOnClickListener {
            var intent = Intent(this, WriteMemoActivity::class.java)
            intent.putExtra("email", eachMemoViewModel.email)
            intent.putExtra("catenum", eachMemoViewModel.cateNum)
            startActivityForResult(intent,900)
        }



        var items = Observer<ArrayList<MemoInfo.MemoInfo2>> { result ->
            eachMemoViewModel.switching()
            progressDialog.dismiss()
            Log.i("live","Eachmemo Observer ")
        }
        eachMemoViewModel?.items?.observe(EachMemoBinding.lifecycleOwner!!, items)


        adapter.itemClick = object : AdapterListener {
            override fun CategoryShortClick(holder: CategoryAdapter.ViewHolder?, view: View?, position: Int) {
                TODO("Not yet implemented")
            }

            override fun EachMemoShortClick(holder: EachMemoAdapter.ViewHolder?, view: View?, position: Int) {
                var intent = Intent(EachMemoBinding.root.context, ShowAndReviseMemo::class.java)
                var bundle = Bundle()
                bundle.putInt("contentNum",eachMemoViewModel.items.value?.get(position)!!.contentnum)
                bundle.putString("title",eachMemoViewModel.items.value?.get(position)?.title)
                bundle.putString("memo",eachMemoViewModel.items.value?.get(position)?.memo)
                bundle.putString("date",eachMemoViewModel.items.value?.get(position)?.date)
                bundle.putString("revisedate",eachMemoViewModel.items.value?.get(position)?.revicedate)
                bundle.putString("time",eachMemoViewModel.items.value?.get(position)?.time)
                bundle.putString("revisetime",eachMemoViewModel.items.value?.get(position)?.revicetime)
                bundle.putString("ConBookmark",eachMemoViewModel.items.value?.get(position)?.ConBookmark)
                bundle.putString("email",eachMemoViewModel.items.value?.get(position)?.memberlist_email)
                bundle.putInt("cateNum",eachMemoViewModel.items.value?.get(position)!!.category_catenum)

                Log.i("tag", "보내는 데이터 ${eachMemoViewModel.items.value?.get(position)!!.contentnum} ${eachMemoViewModel.items.value?.get(position)?.title}")
                intent.putExtras(bundle)
                startActivityForResult(intent, 900)
            }

            override fun EachMemoLongClick(holder: EachMemoAdapter.ViewHolder?, view: View?, position: Int) {
                showCustomDialog(position)
            }

            override fun CategoryImageClick(holder: CategoryAdapter.ViewHolder?, view: View?, position: Int) {
                TODO("Not yet implemented")
            }

            override fun CategoryLongClick(holder: CategoryAdapter.ViewHolder?, view: View?, position: Int) {
                TODO("Not yet implemented")
            }

            override fun OnlyPicShortClick(holder: OnlyPicAdapter.ViewHolder?, view: View?, position: Int) {
                TODO("Not yet implemented")
            }

            override fun OnlyPicLongClick(holder: OnlyPicAdapter.ViewHolder?, view: View?, position: Int) {
                TODO("Not yet implemented")
            }

            override fun SearchShortClick(holder: SearchAdapter.ViewHolder?, view: View?, position: Int) {
                TODO("Not yet implemented")
            }

            override fun SearchLongClick(holder: SearchAdapter.ViewHolder?, view: View?, position: Int) {
                TODO("Not yet implemented")
            }

            override fun CalendarShortClick(holder: CalendarAdapter.ViewHolder?, view: View?, position: Int) {
                TODO("Not yet implemented")
            }

        }


        var sortToggle = Observer<Boolean>{ result ->
            if(result == false){
                var objectAnimator: ObjectAnimator =
                        ObjectAnimator.ofFloat(EachMemoBinding.Sortcontent, "translationY",0.toFloat())
                objectAnimator.duration = 300
                objectAnimator.start()
            }
            else if(result == true){
                var objectAnimator: ObjectAnimator =
                        ObjectAnimator.ofFloat(EachMemoBinding.Sortcontent,"translationY",-height/2.toFloat())
                objectAnimator.duration = 300
                objectAnimator.start()
            }
        }
        eachMemoViewModel.sortToggle.observe(EachMemoBinding.lifecycleOwner!!,sortToggle)

        EachMemoBinding.sortlistView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    0 -> {
                        Utility.EachMemoSort.SortState = 0
                    }
                    1 -> {
                        Utility.EachMemoSort.SortState = 1
                    }
                }
                eachMemoViewModel.controler.value = false
                eachMemoViewModel.getMemoList_call(Utility.EachMemoLoadMore.EachMemoMin,Utility.EachMemoLoadMore.EachMemoMax,Utility.EachMemoSort.SortState)
                eachMemoViewModel.sortToggle.value = false
            }
        })
        val ChangeTextSizeTitle_adapter: ArrayAdapter<String> = ArrayAdapter<String>(EachMemoBinding.root.context, android.R.layout.simple_list_item_1, font_list)
        EachMemoBinding.sortlistView.setAdapter(ChangeTextSizeTitle_adapter)




        EachMemoBinding.memoRcv.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                with(Utility.EachMemoLoadMore){
                    if (!EachMemoBinding.memoRcv.canScrollVertically(-1)) {
                        Log.i("CategoryFragment", "Top of list.")
                    } else if (!EachMemoBinding.memoRcv.canScrollVertically(1)) {
                        Log.i("CategoryFragment", "End of list.")

                        if((EachMemoMax - DeleteMemoCount > adapter.itemCount)){

                        }
                        else{
                            progressDialog.setMessage("Loading..")
                            progressDialog.show()

                            EachMemoMid = EachMemoMid + 10 - DeleteMemoCount
                            EachMemoMax = EachMemoMid + 10
                            eachMemoViewModel.whenScrolled(EachMemoMid, EachMemoMax, Utility.EachMemoSort.SortState)
                            DeleteMemoCount = 0
                        }
                    } else { }
                }

            }
        })

        EachMemoBinding.fabMain.setOnClickListener (object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(Utility.EachMemoFloating.FloatingState == 0){
                    Utility.EachMemoFloating.FloatingState = 1
                }
                else if(Utility.EachMemoFloating.FloatingState == 1){
                    Utility.EachMemoFloating.FloatingState = 0
                }
                eachMemoViewModel.MemoReverse()
            }
        })

    }

    fun showCustomDialog(position: Int){
        EachMemoDialog.show();

        EachMemoDialog.findViewById<TextView>(R.id.onlypicDelete).setOnClickListener {
            eachMemoViewModel.deleteMemo_call(position)
            EachMemoDialog.dismiss()
            Utility.EachMemoLoadMore.DeleteMemoCount += 1
        }
        EachMemoDialog.findViewById<TextView>(R.id.finish).setOnClickListener {
            EachMemoDialog.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("tag","온스타트 onStart()")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == 153){
            Log.i("tag","153호출")
            with(Utility.EachMemoLoadMore){
                eachMemoViewModel.getMemoList_call(EachMemoMin, 10, Utility.EachMemoSort.SortState)
                DeleteMemoCount = 0
                EachMemoMax = 10
                EachMemoMid = 0
            }

        }

    }
}