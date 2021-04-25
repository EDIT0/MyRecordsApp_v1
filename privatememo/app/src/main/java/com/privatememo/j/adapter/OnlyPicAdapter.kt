package com.privatememo.j.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.privatememo.j.api.AdapterListener
import com.privatememo.j.databinding.OnlypicadapterBinding
import com.privatememo.j.model.datamodel.OnlyPicInfo

class OnlyPicAdapter : RecyclerView.Adapter<OnlyPicAdapter.ViewHolder>() {

    var items = MutableLiveData<ArrayList<OnlyPicInfo.OnlyPicInfo2>>()
    lateinit var itemClick: AdapterListener

    inner class ViewHolder(binding : OnlypicadapterBinding): RecyclerView.ViewHolder(binding.root){
        var binding = binding

        init {
            itemView.setOnClickListener {
                itemClick.OnlyPicShortClick(this, itemView, adapterPosition)
            }
            itemView.setOnLongClickListener {
                itemClick.OnlyPicLongClick(this, itemView, adapterPosition)
                return@setOnLongClickListener true
            }
        }

        fun bind(info : OnlyPicInfo.OnlyPicInfo2){
            binding.onlyPicInfo2Model = info
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = OnlypicadapterBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.value?.size?:0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.value?.get(position)!!)
    }
}