package com.privatememo.j.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.privatememo.j.api.AdapterListener
import com.privatememo.j.databinding.SearchadapterBinding
import com.privatememo.j.model.datamodel.SearchInfo
import com.privatememo.j.utility.MemberSettingModule
import kotlinx.android.synthetic.main.searchadapter.view.*


class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    var items = ArrayList<SearchInfo.SearchInfo2>()
    lateinit var itemClick: AdapterListener

    inner class ViewHolder(binding : SearchadapterBinding): RecyclerView.ViewHolder(binding.root){
        var binding = binding

        init {
            itemView.setOnClickListener {
                itemClick.SearchShortClick(this, itemView, adapterPosition)
            }
            itemView.setOnLongClickListener {
                itemClick.SearchLongClick(this, itemView, adapterPosition)
                return@setOnLongClickListener true
            }
            itemView.cateTitle.setTextSize(MemberSettingModule.TitleSize.toFloat())
            itemView.title.setTextSize(MemberSettingModule.TitleSize.toFloat())
            itemView.content.setTextSize(MemberSettingModule.TitleSize.toFloat())
            itemView.cateTitle.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
        }

        fun bind(info : SearchInfo.SearchInfo2){
            binding.searchInfo2ViewModel = info
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = SearchadapterBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}