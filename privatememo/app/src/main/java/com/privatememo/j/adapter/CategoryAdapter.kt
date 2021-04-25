package com.privatememo.j.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.privatememo.j.api.AdapterListener
import com.privatememo.j.databinding.CategoryadapterBinding
import com.privatememo.j.model.datamodel.CategoryInfo
import com.privatememo.j.utility.MemberSettingModule
import kotlinx.android.synthetic.main.categoryadapter.view.*

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    var items = MutableLiveData<ArrayList<CategoryInfo.CategoryInfo2>>()
    lateinit var itemClick: AdapterListener

    inner class ViewHolder(binding : CategoryadapterBinding): RecyclerView.ViewHolder(binding.root){
        var binding = binding

        init {
            itemView.setOnClickListener {
                itemClick.CategoryShortClick(this, itemView, adapterPosition)
            }
            itemView.cateimage.setOnClickListener {
                itemClick.CategoryImageClick(this, itemView, adapterPosition)
            }
            itemView.setOnLongClickListener {
                itemClick.CategoryLongClick(this, itemView, adapterPosition)
                return@setOnLongClickListener true
            }
            itemView.cateTitle.setTextSize(MemberSettingModule.TitleSize.toFloat())
            itemView.cateContent.setTextSize(MemberSettingModule.ContentSize.toFloat())
        }

        fun bind(info : CategoryInfo.CategoryInfo2){
            binding.categoryInfo2Model = info
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        var binding = CategoryadapterBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.value?.size?:0
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        holder.bind(items.value?.get(position)!!)
    }
}