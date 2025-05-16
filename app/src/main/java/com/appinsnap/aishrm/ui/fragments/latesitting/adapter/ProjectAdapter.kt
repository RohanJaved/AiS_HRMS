package com.appinsnap.aishrm.ui.fragments.latesitting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.LovoptionsBinding
import com.appinsnap.aishrm.ui.fragments.latesitting.ProjectData
import okhttp3.internal.notifyAll

class ProjectAdapter(
    val context: Context,
    val list: ArrayList<ProjectData>,
    val onitemclick: onProjectItemClick,
):RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {
    class ViewHolder(val binding:LovoptionsBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LovoptionsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list.get(position)
        if (item.check) {
            holder.binding.checkboximage.setImageDrawable(context.getDrawable(R.drawable.filled_checkbox))
        } else {

            holder.binding.checkboximage.setImageDrawable(context.getDrawable(R.drawable.empty_checkbox))
        }

            holder.binding.opotionitemlayout.setOnClickListener {
                if (!item.check) {
                    holder.binding.checkboximage.setImageDrawable(context.getDrawable(R.drawable.filled_checkbox))
                } else {
                    holder.binding.checkboximage.setImageDrawable(context.getDrawable(R.drawable.empty_checkbox))
                }
//                if(list.get(position).id == 0)
//                {
//                    enableItem(list.get(position).id,true)
//                }
//                else{
//                    enableItem(list.get(position).id,false)
//                }

                onitemclick.onprojectclick(position, list.get(position).name, !item.check)
            }


            holder.binding.checkboximage.setOnClickListener {
                if (!item.check) {
                    holder.binding.checkboximage.setImageDrawable(context.getDrawable(R.drawable.filled_checkbox))
                } else {
                    holder.binding.checkboximage.setImageDrawable(context.getDrawable(R.drawable.empty_checkbox))
                }
                onitemclick.onprojectclick(position, list.get(position).name, !item.check)
            }

            holder.binding.checkboxtext.text = list.get(position).name

    }
//    fun enableItem(position: Int,removeall:Boolean) {
//        if (position == 0 && removeall) {
//            if (allprojectsPosition.isNotEmpty()) {
//                allprojectsPosition.clear()
//                for (i in list.indices) {
//                    if (list.get(i).id == 0) {
//                        allprojectsPosition.add(i)
//                    }
//                    else{
//
//                    }
//                }
//            }
//        }else {
//                if (allprojectsPosition.isNotEmpty()) {
//                    allprojectsPosition.clear()
//                    for (i in list.indices) {
//
//                        if (list.get(i).id == 0) {
//                        } else {
//                            allprojectsPosition.add(i)
//                        }
//                    }
//                }
//
//            }
//        notifyItemChanged(-1)
//    }

    interface onProjectItemClick{
        fun onprojectclick(position:Int,name:String,check:Boolean)
    }
}