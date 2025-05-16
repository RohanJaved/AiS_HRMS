package com.appinsnap.aishrm.ui.fragments.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.DepartmentattendanceitemBinding
import com.appinsnap.aishrm.ui.fragments.home.models.BodyXX
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener

class DepartmentEmployeeAdapter(
    requireContext: Context,
    val departmentattendancelist: List<BodyXX>?,
    val ondeptclick: onDeptMenuClick,
    val groupname: String?,
    val from: String

) : RecyclerView.Adapter<DepartmentEmployeeAdapter.ViewHolder>() {

    class ViewHolder(val binding: DepartmentattendanceitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DepartmentattendanceitemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.mcvDt0.setSafeOnClickListener {

            ondeptclick.ondeptitemclick(position,from)

        }
        when (groupname) {
            "DIS" -> {
                if (departmentattendancelist?.get(position)?.group?.lowercase()
                        ?.contains("dis") == true
                ) {
                    var presentcount = departmentattendancelist?.get(position)?.presentCount.toString()
                    var absentcount = departmentattendancelist?.get(position)?.absentCount.toString()
                    var leaveCount =  departmentattendancelist?.get(position)?.leaveCount.toString()
                    val totalmembers =  presentcount.toInt()+absentcount.toInt() + leaveCount.toInt()
                    holder.binding.txtteammembers.text ="Total Members"+" "+"-" +" " +totalmembers
                    holder.binding.txtdepartment.text =
                        departmentattendancelist.get(position).departmentName
                    holder.binding.txtpresentcount.text =
                        departmentattendancelist.get(position).presentCount.toString()
                    holder.binding.txtabsentcount.text =
                        departmentattendancelist.get(position).absentCount.toString()
                    holder.binding.txtlatearrivalcount.text =
                        departmentattendancelist.get(position).lateCount.toString()
                    holder.binding.txtleavecount.text = departmentattendancelist.get(position).leaveCount.toString()
                }
            }
            "DTS" -> {
                if (departmentattendancelist?.get(position)?.group?.lowercase()
                        ?.contains("dts") == true
                ) {
                    var presentcount = departmentattendancelist?.get(position)?.presentCount.toString()
                    var absentcount = departmentattendancelist?.get(position)?.absentCount.toString()
                    var leaveCount = departmentattendancelist?.get(position)?.leaveCount.toString()
                    val totalmembers =  presentcount.toInt()+absentcount.toInt() + leaveCount.toInt()
                    holder.binding.txtteammembers.text = "Total Members"+" "+"-" + " "+totalmembers
                    holder.binding.txtdepartment.text =
                        departmentattendancelist.get(position).departmentName
                    holder.binding.txtpresentcount.text =
                        departmentattendancelist.get(position).presentCount.toString()
                    holder.binding.txtabsentcount.text =
                        departmentattendancelist.get(position).absentCount.toString()
                    holder.binding.txtlatearrivalcount.text =
                        departmentattendancelist.get(position).lateCount.toString()
                    holder.binding.txtleavecount.text = departmentattendancelist.get(position).leaveCount.toString()
                }

            }
            "Shared" -> {
                if (departmentattendancelist?.get(position)?.group?.lowercase()
                        ?.contains("shared") == true) {
                    var presentcount = departmentattendancelist?.get(position)?.presentCount.toString()
                    var absentcount = departmentattendancelist?.get(position)?.absentCount.toString()
                    var leaveCount = departmentattendancelist.get(position).leaveCount.toString()
                    val totalmembers = presentcount.toInt() + absentcount.toInt()+leaveCount.toInt()
                    holder.binding.txtteammembers.text = "Total Members" + " " + "-" + " " + totalmembers
                    holder.binding.txtdepartment.text = departmentattendancelist.get(position).departmentName
                    holder.binding.txtpresentcount.text = departmentattendancelist.get(position).presentCount.toString()
                    holder.binding.txtabsentcount.text = departmentattendancelist.get(position).absentCount.toString()
                    holder.binding.txtlatearrivalcount.text = departmentattendancelist.get(position).lateCount.toString()
                    holder.binding.txtleavecount.text = departmentattendancelist.get(position).leaveCount.toString()
                }
            }

            else->{
                var presentcount = departmentattendancelist?.get(position)?.presentCount.toString()
                var absentcount = departmentattendancelist?.get(position)?.absentCount.toString()
                var leaveCount = departmentattendancelist?.get(position)?.leaveCount.toString()
                val totalmembers =  presentcount.toInt()+absentcount.toInt()+leaveCount.toInt()
                holder.binding.txtteammembers.text = "Total Members"+" "+"-"+ " "+ totalmembers
                holder.binding.txtdepartment.text = departmentattendancelist?.get(position)?.departmentName
                holder.binding.txtpresentcount.text = departmentattendancelist?.get(position)?.presentCount.toString()
                holder.binding.txtabsentcount.text =
                    departmentattendancelist?.get(position)?.absentCount.toString()
                holder.binding.txtlatearrivalcount.text = departmentattendancelist?.get(position)?.lateCount.toString()
                holder.binding.txtleavecount.text = departmentattendancelist?.get(position)?.leaveCount.toString()
            }

        }

    }

    override fun getItemCount(): Int {
        return departmentattendancelist!!.size
    }

    interface onDeptMenuClick {
        fun ondeptitemclick(position: Int, from: String)
    }

}