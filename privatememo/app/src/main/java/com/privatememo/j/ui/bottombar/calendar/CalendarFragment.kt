package com.privatememo.j.ui.bottombar.calendar

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.privatememo.j.adapter.CalendarAdapter
import com.privatememo.j.R
import com.privatememo.j.adapter.CategoryAdapter
import com.privatememo.j.adapter.EachMemoAdapter
import com.privatememo.j.adapter.OnlyPicAdapter
import com.privatememo.j.adapter.SearchAdapter
import com.privatememo.j.listener.AdapterListener
import com.privatememo.j.databinding.CalendarfragmentBinding
import com.privatememo.j.model.datamodel.CategoryInfo
import com.privatememo.j.model.datamodel.MemoInfo
import com.privatememo.j.ui.bottombar.MainActivity
import com.privatememo.j.ui.bottombar.memo.ShowAndReviseMemo
import com.privatememo.j.ui.bottombar.memo.WriteMemoActivity
import com.privatememo.j.utility.ApplyFontModule
import com.privatememo.j.utility.Utility
import com.privatememo.j.viewmodel.CalendarViewModel
import kotlinx.android.synthetic.main.calendarfragment.*
import sun.bob.mcalendarview.MarkStyle
import sun.bob.mcalendarview.listeners.OnDateClickListener
import sun.bob.mcalendarview.vo.DateData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CalendarFragment : Fragment() {

    lateinit var CalendarBinding: CalendarfragmentBinding
    var calendarViewModel = CalendarViewModel(Utility.repositoryModule.repositorymodule)
    var adapter = CalendarAdapter()

    var dt = Date()
    var t_year = SimpleDateFormat("yyyy")
    var t_month = SimpleDateFormat("MM")
    var t_day = SimpleDateFormat("dd")
    var Year = t_year.format(dt).toInt()
    var Month = t_month.format(dt).toInt()
    var Day = t_day.format(dt).toInt()

    var Save_ClickedYear = 0
    var Save_ClickedMonth = 0
    var Save_ClickedDay = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getContext()?.getTheme()?.applyStyle(ApplyFontModule.a.FontCall(), true)

        CalendarBinding = DataBindingUtil.inflate(inflater, R.layout.calendarfragment, calendarfrag,false)
        //calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        CalendarBinding.setLifecycleOwner(this)
        CalendarBinding.calendarViewModel = calendarViewModel

        CalendarBinding.calendarView.markedDates.removeAdd()


        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        val width = size.x
        val height = size.y

        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                height/2
        )
        layoutParams.leftMargin = 50
        layoutParams.rightMargin = 50
        CalendarBinding.Categorycontent.layoutParams = layoutParams

        CalendarBinding.boxdown.setOnClickListener {
            var objectAnimator: ObjectAnimator =
                    ObjectAnimator.ofFloat(CalendarBinding.Categorycontent, "translationY",0.toFloat())
            objectAnimator.duration = 300
            objectAnimator.start()
            calendarViewModel.categoryToggle.value = false
        }

        var fontToggle = Observer<Boolean>{ result ->
            if(result == false){
                var objectAnimator: ObjectAnimator =
                        ObjectAnimator.ofFloat(CalendarBinding.Categorycontent, "translationY",0.toFloat())
                objectAnimator.duration = 300
                objectAnimator.start()
            }
            else if(result == true){
                var objectAnimator: ObjectAnimator =
                        ObjectAnimator.ofFloat(CalendarBinding.Categorycontent,"translationY",-height/2.toFloat())
                objectAnimator.duration = 300
                objectAnimator.start()
            }
        }
        calendarViewModel.categoryToggle.observe(CalendarBinding.lifecycleOwner!!,fontToggle)

        CalendarBinding.categorylistView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var intent = Intent(context, WriteMemoActivity::class.java)
                intent.putExtra("email", calendarViewModel.email.get().toString())
                intent.putExtra("catenum", calendarViewModel.CategoryList_catenum.get(position).toString())
                intent.putExtra("calendarDate", "${calendarViewModel.ClickedYear.get()}_${calendarViewModel.ClickedMonth.get()}_${calendarViewModel.ClickedDay.get()}")
                startActivityForResult(intent, 0)

                calendarViewModel.categoryToggle.value = false
            }
        })
        val ChangeTextSizeTitle_adapter: ArrayAdapter<String> = ArrayAdapter<String>(CalendarBinding.root.context, android.R.layout.simple_list_item_1, calendarViewModel.list)
        CalendarBinding.categorylistView.setAdapter(ChangeTextSizeTitle_adapter)

        var act = activity as MainActivity
        Log.i("tag","이것은 캘린더의 이메일입니다. ${act.mainViewModel.email.value}")
        calendarViewModel.email.set(act.mainViewModel.email.value)

        var layoutmanager = LinearLayoutManager(CalendarBinding.calendarRcv.context)
        CalendarBinding.calendarRcv.layoutManager = layoutmanager
        CalendarBinding.calendarRcv.adapter = adapter

        calendarViewModel.insertCategoryintoList() //메뉴에 카테고리 넣기

        CalendarBinding.fabMain.setOnClickListener( object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(CalendarBinding.datetext.getText() != "선택된 날짜"){
                    if(calendarViewModel.list.isEmpty()){
                        Toast.makeText(context,"카테고리를 만들어주세요.",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        calendarViewModel.fontButton()
                    }
                }
                else{
                    Toast.makeText(context,"날짜를 선택해주세요.",Toast.LENGTH_SHORT).show()
                }
            }
        })

        calendarViewModel.getCalendarMemo_call() //모든 메모 불러오기


        var total_items = Observer<ArrayList<MemoInfo.MemoInfo2>> { result ->

            CalendarBinding.calendarView.markedDates.removeAdd()
            calendarViewModel.items.clear()
            calendarViewModel.splitDateArray.clear()
            calendarViewModel.switching()

            try {
                calendarViewModel.setSplitDate()
                Log.i("live", "포문 시작 ${calendarViewModel.total_items.value?.size!!}")
                for (i in 0 until calendarViewModel.total_items.value?.size!!) {
                    var year = Integer.parseInt(calendarViewModel.splitDateArray.get(i)[0])
                    var month = Integer.parseInt(calendarViewModel.splitDateArray.get(i)[1])
                    var day = Integer.parseInt(calendarViewModel.splitDateArray.get(i)[2])

                    CalendarBinding.calendarView.markDate(DateData(year, month, day).setMarkStyle(MarkStyle(MarkStyle.DOT, Color.MAGENTA)))
                    Log.i("live", "여기 찍음, ${year} ${month} ${day} ${calendarViewModel.total_items.value?.size}")
                }
            }catch (e:Exception){}
            Log.i("live","캘린더 토탈 아이템 옵저버")
        }
        calendarViewModel?.total_items?.observe(CalendarBinding.lifecycleOwner!!, total_items)


        CalendarBinding.ToDay.setOnClickListener {
            CalendarBinding.calendarView.travelTo(DateData(Year,Month,Day))
        }

        adapter.itemClick = object : AdapterListener {
            override fun CategoryShortClick(holder: CategoryAdapter.ViewHolder?, view: View?, position: Int) {
                TODO("Not yet implemented")
            }

            override fun EachMemoShortClick(holder: EachMemoAdapter.ViewHolder?, view: View?, position: Int) {

            }

            override fun EachMemoLongClick(holder: EachMemoAdapter.ViewHolder?, view: View?, position: Int) {

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
                var intent = Intent(CalendarBinding.root.context, ShowAndReviseMemo::class.java)
                var bundle = Bundle()
                bundle.putInt("contentNum",calendarViewModel.items.get(position)!!.contentnum)
                bundle.putString("title",calendarViewModel.items.get(position)?.title)
                bundle.putString("memo",calendarViewModel.items.get(position)?.memo)
                bundle.putString("date",calendarViewModel.items.get(position)?.date)
                bundle.putString("revisedate",calendarViewModel.items.get(position)?.revicedate)
                bundle.putString("time",calendarViewModel.items.get(position)?.time)
                bundle.putString("revisetime",calendarViewModel.items.get(position)?.revicetime)
                bundle.putString("ConBookmark",calendarViewModel.items.get(position)?.ConBookmark)
                bundle.putString("email",calendarViewModel.items.get(position)?.memberlist_email)
                bundle.putInt("cateNum",calendarViewModel.items.get(position)!!.category_catenum)

                intent.putExtras(bundle)
                startActivityForResult(intent, 153)
            }

        }

        CalendarBinding.calendarView.setOnDateClickListener( object : OnDateClickListener(){
            override fun onDateClick(view: View?, date: DateData?) {
                calendarViewModel.ClickedYear.set(date?.year)
                calendarViewModel.ClickedMonth.set(date?.month)
                calendarViewModel.ClickedDay.set(date?.day)

                CalendarBinding.datetext.text = "" + calendarViewModel.ClickedYear.get() + "." + calendarViewModel.ClickedMonth.get() + "." + calendarViewModel.ClickedDay.get()

                if(CalendarBinding.calendarView.markedDates.remove(date) == true){
                    Log.i("tag","true 호출")
                    CalendarBinding.calendarView.markDate(date?.setMarkStyle(MarkStyle(MarkStyle.DOT, Color.MAGENTA)))
                    //Log.i("tag","${CalendarBinding.calendarView.markedDates.check(date)}")

                    calendarViewModel.items.clear()
                    for(i in 0 until calendarViewModel.total_items.value?.size!!){
                        if(calendarViewModel.ClickedYear.get() == Integer.parseInt(calendarViewModel.splitDateArray.get(i)[0])
                            && calendarViewModel.ClickedMonth.get() == Integer.parseInt(calendarViewModel.splitDateArray.get(i)[1])
                            && calendarViewModel.ClickedDay.get() == Integer.parseInt(calendarViewModel.splitDateArray.get(i)[2]))

                            calendarViewModel.items.add(calendarViewModel.total_items.value!!.get(i))
                        Log.i("tag","이야 호출  ${calendarViewModel.total_items.value!!.get(i).title}")
                    }
                    calendarViewModel.switching()
                    adapter.notifyDataSetChanged()
                }
                else if(CalendarBinding.calendarView.markedDates.remove(date) == false){
                    calendarViewModel.items.clear()
                    Log.i("tag","false 호출")
                    calendarViewModel.switching()
                }

                Save_ClickedYear = calendarViewModel.ClickedYear.get()?:Year
                Save_ClickedMonth = calendarViewModel.ClickedMonth.get()?:Month
                Save_ClickedDay = calendarViewModel.ClickedDay.get()?:Day

            }
        })

        return CalendarBinding.root
    }

    override fun onStart() {
        super.onStart()
        Log.i("tag","캘린더프래그먼트 온 스타트")

    }

    override fun onResume() {
        super.onResume()
        Log.i("tag","캘린더프래그먼트 온 리숨")
        if(Utility.NetworkState.off == true){
            Utility.NetworkUnavailable(CalendarBinding.root.context)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == 153) {

            Log.i("tag","153호출")
            calendarViewModel.getCalendarMemo_call()
        }
        else if(resultCode == 154){
            Log.i("tag","154호출")
        }
    }
}