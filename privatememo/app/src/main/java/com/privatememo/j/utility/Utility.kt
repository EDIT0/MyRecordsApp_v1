package com.privatememo.j.utility

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.privatememo.j.R
import com.privatememo.j.adapter.*
import com.privatememo.j.model.datamodel.CategoryInfo
import com.privatememo.j.model.datamodel.MemoInfo
import com.privatememo.j.model.datamodel.OnlyPicInfo
import com.privatememo.j.model.datamodel.SearchInfo
import com.privatememo.j.model.Repository
import java.util.*
import kotlin.collections.ArrayList


object Utility {

    lateinit var CustomDialog: Dialog

    object repositoryModule{
        val repositorymodule = Repository.getInstance()
    }

    @BindingAdapter("memo_profile_image", "error")
    @JvmStatic
    fun Profile_image (iv : ImageView, url : String?, error : Drawable){
        Glide.with(iv.context).load(url+ "? ${Date().getTime()}").apply(RequestOptions.circleCropTransform()).override(1000, 1000).error(error).into(iv)
    }

    @BindingAdapter("category_rcv")
    @JvmStatic
    fun CategoryRcv (rcv: RecyclerView, items : MutableLiveData<ArrayList<CategoryInfo.CategoryInfo2>>){
        (rcv.adapter as CategoryAdapter).items = items
        rcv.adapter?.notifyDataSetChanged()
    }

    @BindingAdapter("memo_rcv")
    @JvmStatic
    fun MemoRcv (rcv: RecyclerView, items : MutableLiveData<ArrayList<MemoInfo.MemoInfo2>>){
        (rcv.adapter as EachMemoAdapter).items = items
        rcv.adapter?.notifyDataSetChanged()
    }

    @BindingAdapter("category_rcv_image", "error")
    @JvmStatic
    fun CategoryRcvImage (iv : ImageView, url : String?, error : Drawable){
        Glide.with(iv.context).load(url+ "? ${Date().getTime()}").apply(RequestOptions.circleCropTransform()).override(1000,1000).error(error).into(iv)
    }

    @BindingAdapter("onlypic_rcv")
    @JvmStatic
    fun OnlyPicRcv (rcv: RecyclerView, items : MutableLiveData<ArrayList<OnlyPicInfo.OnlyPicInfo2>>){
        (rcv.adapter as OnlyPicAdapter).items = items
        rcv.adapter?.notifyDataSetChanged()
    }

    @BindingAdapter("onlypic_rcv_image", "error")
    @JvmStatic
    fun OnlyPicRcvImage (iv : ImageView, url : String?, error : Drawable){
        Glide.with(iv.context).load(url+ "? ${Date().getTime()}").override(1000,1000).error(error).into(iv)
    }

    @BindingAdapter("search_rcv")
    @JvmStatic
    fun SearchRcv (rcv: RecyclerView, items : ArrayList<SearchInfo.SearchInfo2>){
        (rcv.adapter as SearchAdapter).items = items
        rcv.adapter?.notifyDataSetChanged()
    }

    @BindingAdapter("calendar_rcv")
    @JvmStatic
    fun CalendarRcv (rcv: RecyclerView, items : ArrayList<MemoInfo.MemoInfo2>){
        (rcv.adapter as CalendarAdapter).items = items
        rcv.adapter?.notifyDataSetChanged()
    }

    object EachMemoLoadMore{
        var EachMemoMin = 0
        var EachMemoMid = 0
        var EachMemoMax = 10
        var DeleteMemoCount = 0
        var AddMemoCount = 0
    }

    object EachMemoSort{
        var SortState = 0
    }

    object EachMemoFloating{
        var FloatingState = 0
    }

    object SearchLoadMore{
        var SearchMin = 0
        var SearchMid = 0
        var SearchMax = 10
    }



    object NetworkState{
        var off = false //사용 중 네트워크 off 시 false로 변경
    }

    fun NormalDialogSetting(context: Context, layout: Int, width: Int): Dialog{
        var dialog:Dialog
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(layout);
        var params: WindowManager.LayoutParams = dialog?.getWindow()?.getAttributes()!!
        params.width = width
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.getWindow()?.setAttributes(params)

        return dialog
    }

    fun NetworkDialogSetting(context: Context): Dialog{
        var NetworkDialog:Dialog
        NetworkDialog = Dialog(context)
        NetworkDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        NetworkDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        NetworkDialog.setContentView(R.layout.networkcustomdialog);
        var params: WindowManager.LayoutParams = NetworkDialog?.getWindow()?.getAttributes()!!
        params.width = 800
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        NetworkDialog?.getWindow()?.setAttributes(params)
        NetworkDialog.setCancelable(false)

        return NetworkDialog
    }

    fun CheckNetworkState(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    fun NetworkUnavailable(context: Context){
        NetworkState.off = false
        var dialog = NetworkDialogSetting(context)
        var state = CheckNetworkState(context)

        if(state == true) { }
        else if(state == false){
            dialog.show();
        }

        dialog.findViewById<TextView>(R.id.retry).setOnClickListener {
            var state = CheckNetworkState(context)

            if(state == true) {
                dialog.dismiss()
            }
            else if(state == false){
                dialog.dismiss()
                dialog.show()
            }
        }
    }
}