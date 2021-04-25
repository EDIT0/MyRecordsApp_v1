package com.privatememo.j.ui.bottombar.calendar

import android.animation.ObjectAnimator
import android.app.Dialog
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.privatememo.j.adapter.CalendarAdapter
import com.privatememo.j.R
import com.privatememo.j.adapter.CategoryAdapter
import com.privatememo.j.adapter.EachMemoAdapter
import com.privatememo.j.adapter.OnlyPicAdapter
import com.privatememo.j.adapter.SearchAdapter
import com.privatememo.j.api.AdapterListener
import com.privatememo.j.databinding.CalendarfragmentBinding
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


class CalendarFragment : Fragment() {

    lateinit var CalendarBinding: CalendarfragmentBinding
    var calendarViewModel = CalendarViewModel()
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

    lateinit var CalendarDialog: Dialog
    var list = ArrayList<String>() //카테고리 리스트

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getContext()?.getTheme()?.applyStyle(ApplyFontModule.a.FontCall(), true)

        CalendarBinding = DataBindingUtil.inflate(inflater, R.layout.calendarfragment, calendarfrag,false)
        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
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
                //CalendarDialog.dismiss()//구역

                calendarViewModel.categoryToggle.value = false
            }
        })
        val ChangeTextSizeTitle_adapter: ArrayAdapter<String> = ArrayAdapter<String>(CalendarBinding.root.context, android.R.layout.simple_list_item_1, list)
        CalendarBinding.categorylistView.setAdapter(ChangeTextSizeTitle_adapter)

        //구역
        /*CalendarDialog = Dialog(CalendarBinding.root.context)
        CalendarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        CalendarDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        CalendarDialog.setContentView(R.layout.calendarfloatingcustomdialog);
        var params: WindowManager.LayoutParams = CalendarDialog?.getWindow()?.getAttributes()!!
        params.width = 800
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        CalendarDialog?.getWindow()?.setAttributes(params)*/

        //구역
        /*var listview = CalendarDialog.findViewById<ListView>(R.id.CateListView) //커스텀 다이얼로그 리스트뷰
        //구역
        listview.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var intent = Intent(context, WriteMemoActivity::class.java)
                intent.putExtra("email", calendarViewModel.email.get().toString())
                intent.putExtra("catenum", calendarViewModel.CategoryList_catenum.get(position).toString())
                intent.putExtra("calendarDate", "${calendarViewModel.ClickedYear.get()}_${calendarViewModel.ClickedMonth.get()}_${calendarViewModel.ClickedDay.get()}")
                startActivityForResult(intent, 0)
                CalendarDialog.dismiss()
            }
        })*/

        var act = activity as MainActivity
        Log.i("tag","이것은 캘린더의 이메일입니다. ${act.mainViewModel.email.value}")
        calendarViewModel.email.set(act.mainViewModel.email.value)

        var layoutmanager = LinearLayoutManager(CalendarBinding.calendarRcv.context)
        CalendarBinding.calendarRcv.layoutManager = layoutmanager
        CalendarBinding.calendarRcv.adapter = adapter


        for (i in 0 until act.mainViewModel.items.size) {
            calendarViewModel.CategoryList_catenum.add(act.mainViewModel.items.get(i).catenum)
            calendarViewModel.CategoryList_catename.add(act.mainViewModel.items.get(i).catename)

            list.add(calendarViewModel.CategoryList_catename.get(i))
        }
        //구역
        /*Log.i("tag", "사이즈: ${calendarViewModel.CategoryList_catename.size} / ${calendarViewModel.CategoryList_catenum.size}")
        val category_adapter: ArrayAdapter<String> = ArrayAdapter<String>(CalendarBinding.root.context, android.R.layout.simple_list_item_1, list)
        listview.setAdapter(category_adapter)*/





        CalendarBinding.fabMain.setOnClickListener( object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(CalendarBinding.datetext.getText() != "선택된 날짜"){
                    if(list.isEmpty()){
                        Toast.makeText(context,"카테고리를 만들어주세요.",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        calendarViewModel.fontButton()
                    }
                }
                else{
                    Toast.makeText(context,"날짜를 선택해주세요.",Toast.LENGTH_SHORT).show()
                }
                //구역
                /*if(CalendarBinding.datetext.getText() != "선택된 날짜") {
                    Log.i("tag","${calendarViewModel.ClickedYear.get().toString().length}")
                    if(list.isEmpty()){
                        Toast.makeText(context,"카테고리를 만들어주세요.",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        showCustomDialog()
                    }

                }
                else{
                    Toast.makeText(context,"날짜를 선택해주세요.",Toast.LENGTH_SHORT).show()
                }*/
            }
        })

        calendarViewModel.getCalendarMemo_call() //모든 메모 불러오기


        var controler = Observer<Boolean> { result ->
            if(calendarViewModel.items.size == 0){
                CalendarBinding.layout.visibility = View.VISIBLE
            }
            else{
                CalendarBinding.layout.visibility = View.INVISIBLE
            }
        }
        calendarViewModel?.controler?.observe(CalendarBinding.lifecycleOwner!!, controler)

        var CompleteGettingData = Observer<Boolean> { result ->
            if(calendarViewModel.CompleteGettingData.value == true) {
                for (i in 0 until calendarViewModel.total_items.size) {
                    var year = Integer.parseInt(calendarViewModel.splitDateArray.get(i)[0])
                    var month = Integer.parseInt(calendarViewModel.splitDateArray.get(i)[1])
                    var day = Integer.parseInt(calendarViewModel.splitDateArray.get(i)[2])

                    CalendarBinding.calendarView.markDate(DateData(year, month, day).setMarkStyle(MarkStyle(MarkStyle.DOT, Color.MAGENTA)))
                    Log.i("tag", "여기 찍음, ${year} ${month} ${day} ${calendarViewModel.total_items.size}")
                }
            }
            else{

            }
        }
        calendarViewModel?.CompleteGettingData?.observe(CalendarBinding.lifecycleOwner!!, CompleteGettingData)

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
                bundle.putInt("contentNum",calendarViewModel.items.get(position).contentnum)
                bundle.putString("title",calendarViewModel.items.get(position).title)
                bundle.putString("memo",calendarViewModel.items.get(position).memo)
                bundle.putString("date",calendarViewModel.items.get(position).date)
                bundle.putString("revisedate",calendarViewModel.items.get(position).revicedate)
                bundle.putString("time",calendarViewModel.items.get(position).time)
                bundle.putString("revisetime",calendarViewModel.items.get(position).revicetime)
                bundle.putString("ConBookmark",calendarViewModel.items.get(position).ConBookmark)
                bundle.putString("email",calendarViewModel.items.get(position).memberlist_email)
                bundle.putInt("cateNum",calendarViewModel.items.get(position).category_catenum)

                intent.putExtras(bundle)
                startActivityForResult(intent, 153)
            }

        }

        var calendar = Calendar.getInstance();

        //CalendarBinding.calendarView.setDate(dt,true,true)
        //set(Calendar.YEAR,3,1)
        /*calendar.set(Calendar.MONTH,4)
        calendar.set(Calendar.DATE, 1)*/

        //calendar.set(Year,Month-1,Day-1)



        //CalendarBinding.calendarView.travelTo(DateData(Year,Month,Day)) //현재 날짜로 지정

        /*CalendarBinding.calendarView.markDate(Year, Month, 27)*/
        //CalendarBinding.calendarView.markDate(DateData(Year, Month, 28).setMarkStyle(MarkStyle(MarkStyle.DOT, Color.MAGENTA)))
        //CalendarBinding.calendarView.markDate(DateData(Year, Month, 3).setMarkStyle(MarkStyle(MarkStyle.DOT, Color.MAGENTA)))
        //CalendarBinding.calendarView.markDate(DateData(Year, Month, 25).setMarkStyle(MarkStyle(MarkStyle.DOT, Color.MAGENTA)))



        //CalendarBinding.calendarView.unMarkDate(Year, Month, 27)

        CalendarBinding.calendarView.setOnDateClickListener( object : OnDateClickListener(){
            override fun onDateClick(view: View?, date: DateData?) {
                calendarViewModel.ClickedYear.set(date?.year)
                calendarViewModel.ClickedMonth.set(date?.month)
                calendarViewModel.ClickedDay.set(date?.day)

                CalendarBinding.datetext.text = "" + calendarViewModel.ClickedYear.get() + "." + calendarViewModel.ClickedMonth.get() + "." + calendarViewModel.ClickedDay.get()

                //if(CalendarBinding.calendarView.markedDates.check(DateData(Year, Month, 28))){

                //}
                //Log.i("tag","클릭 ${CalendarBinding.calendarView.markedDates.remove(date)}")

                //CalendarBinding.calendarView.unMarkDate(Save_ClickedYear, Save_ClickedMonth, Save_ClickedDay)



                if(CalendarBinding.calendarView.markedDates.remove(date) == true){
                    Log.i("tag","true 호출")
                    CalendarBinding.calendarView.markDate(date?.setMarkStyle(MarkStyle(MarkStyle.DOT, Color.MAGENTA)))
                    //Log.i("tag","${CalendarBinding.calendarView.markedDates.check(date)}")

                    calendarViewModel.items.clear()
                    for(i in 0 until calendarViewModel.total_items.size){
                        if(calendarViewModel.ClickedYear.get() == Integer.parseInt(calendarViewModel.splitDateArray.get(i)[0])
                            && calendarViewModel.ClickedMonth.get() == Integer.parseInt(calendarViewModel.splitDateArray.get(i)[1])
                            && calendarViewModel.ClickedDay.get() == Integer.parseInt(calendarViewModel.splitDateArray.get(i)[2]))
                        calendarViewModel.items.add(calendarViewModel.total_items.get(i))
                        Log.i("tag","이야 호출  ${calendarViewModel.total_items.get(i).title}")
                    }
                    calendarViewModel.switching()
                    adapter.notifyDataSetChanged()
                }
                else if(CalendarBinding.calendarView.markedDates.remove(date) == false){
                    calendarViewModel.items.clear()
                    Log.i("tag","false 호출")
                    calendarViewModel.switching()
                }
                //Log.i("tag","${Save_ClickedYear} ${Save_ClickedMonth} ${Save_ClickedDay}")


                Save_ClickedYear = calendarViewModel.ClickedYear.get()?:Year
                Save_ClickedMonth = calendarViewModel.ClickedMonth.get()?:Month
                Save_ClickedDay = calendarViewModel.ClickedDay.get()?:Day



                //CalendarBinding.calendarView.markDate(Save_ClickedYear?:Year, ClickedMonth?:Month, ClickedDay?:Day)

            }

        })




        return CalendarBinding.root
    }

    //구역
    /*fun showCustomDialog(){
        CalendarDialog.show();

        CalendarDialog.findViewById<TextView>(R.id.finish).setOnClickListener {
            CalendarDialog.dismiss()
        }
    }*/

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
            CalendarBinding.calendarView.markedDates.removeAdd()
            //.calendarView.unMarkDate(calendarViewModel.ClickedYear.get()!!,calendarViewModel.ClickedMonth.get()!!,calendarViewModel.ClickedDay.get()!!)
            //Log.i("tag",""+calendarViewModel.ClickedYear.get()!! + "/" + calendarViewModel.ClickedMonth.get()!!+"/"+calendarViewModel.ClickedDay.get()!!)
            calendarViewModel.total_items.clear()
            calendarViewModel.items.clear()
            calendarViewModel.splitDateArray.clear()
            calendarViewModel.switching()
            calendarViewModel.CompleteGettingData.value = false

            calendarViewModel.getCalendarMemo_call()
        }
        else if(resultCode == 154){
            Log.i("tag","154호출")
        }


    }
}