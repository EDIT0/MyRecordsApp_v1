package com.privatememo.j.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.privatememo.j.api.AdapterListener
import com.privatememo.j.databinding.CalendaradapterBinding
import com.privatememo.j.model.datamodel.MemoInfo
import com.privatememo.j.utility.MemberSettingModule
import kotlinx.android.synthetic.main.calendaradapter.view.*

class CalendarAdapter : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    var items = ArrayList<MemoInfo.MemoInfo2>()
    lateinit var itemClick: AdapterListener

    inner class ViewHolder(binding : CalendaradapterBinding): RecyclerView.ViewHolder(binding.root){
        var binding = binding

        init {
            itemView.setOnClickListener {
                itemClick.CalendarShortClick(this, itemView, adapterPosition)
            }
            itemView.title.setTextSize(MemberSettingModule.TitleSize.toFloat())
            itemView.content.setTextSize(MemberSettingModule.ContentSize.toFloat())
        }

        fun bind(info : MemoInfo.MemoInfo2){
            binding.memoInfo2ViewModel = info
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = CalendaradapterBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}