package com.appinsnap.aishrm.ui.fragments.employeeattendance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.DepttypeitemBinding
import com.appinsnap.aishrm.databinding.LeavestypeitemBinding
import com.appinsnap.aishrm.ui.fragments.employeeattendance.EmployeeAttendance
import com.appinsnap.aishrm.ui.fragments.home.models.BodyXX
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import java.util.ArrayList

class DeptTypeAdapter(
    val context: Context,
    var departmentList: ArrayList<BodyXX>,
    val onItemClick: OnitemDeptclick?,
    var again : Boolean
    ): RecyclerView.Adapter<DeptTypeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DepttypeitemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)

    }

    fun setDepartList(departList: ArrayList<BodyXX>){
        departmentList = departList
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (again){
            holder.itemBidnig.leavetype.text = departmentList.get(position).departmentName
        }else{
            holder.bindItems(departmentList[position])
            holder.itemView.setOnClickListener {
                onItemClick?.onClickDepartment(departmentList[position], position)
            }
        }
        }


    override fun getItemCount(): Int {
        return departmentList.size
    }

    interface OnitemDeptclick{
        fun onClickDepartment(item : BodyXX, position: Int)
    }

    class ViewHolder (
        var itemBidnig : DepttypeitemBinding):RecyclerView.ViewHolder(itemBidnig.root)  {
        fun bindItems(model: BodyXX){
            itemBidnig.leavetype.text = model.departmentName
        }
    }

}
