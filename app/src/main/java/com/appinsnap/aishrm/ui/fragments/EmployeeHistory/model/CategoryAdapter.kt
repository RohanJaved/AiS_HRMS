package com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.LeavestypeitemBinding
import com.appinsnap.aishrm.ui.fragments.home.models.categories


class CategoryAdapter(val categorylist: ArrayList<categories>,val oncategoryselect:onCategorySelect) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    inner  class ViewHolder(val binding: LeavestypeitemBinding): RecyclerView.ViewHolder(binding.root)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LeavestypeitemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categorylist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.leavetype.text = categorylist[position].name
        holder.binding.categorylayout.setOnClickListener {
            oncategoryselect.onCategoryItem(position)
        }
    }
    interface onCategorySelect{
        fun onCategoryItem(position:Int){
        }
    }


}

