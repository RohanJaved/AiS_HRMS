package com.appinsnap.aishrm.ui.fragments.leaves.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.LeavesdashboarditemBinding
import com.appinsnap.aishrm.ui.fragments.leaves.models.GetLeaveCount
import java.util.Random


class LeavesDashboardAdapter(val context: Context?, val leavescount: List<GetLeaveCount>?): RecyclerView.Adapter<LeavesDashboardAdapter.ViewHolder>() {

    class ViewHolder(val binding: LeavesdashboarditemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LeavesdashboarditemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var itemposition = leavescount?.get(position)
        var leavename = itemposition?.name?.split("L")?.get(0)
     holder.binding.txtleavetitle.text = leavename
        var totalRemLeaves:Int=0
        var remleaves = itemposition?.totalLeave?.minus(itemposition.availLeave)
        if (remleaves != null) {
            if(remleaves< 0)
            {
                totalRemLeaves = 0
            }
            else{
                totalRemLeaves = remleaves
            }

                holder.binding.txtleavecount.text = totalRemLeaves.toString()
        }
        var availleave = itemposition?.availLeave
        var totalleaves = itemposition?.totalLeave
        var totlaleavepercentage = (availleave!!.toDouble() / totalleaves!!.toDouble())*100.0f
        var totalleavesreamining = 100 - totlaleavepercentage
        if(itemposition?.name!=null){
            if(itemposition?.name?.lowercase()!!.contains("annual"))
            {
                holder.binding.progressbar.apply {
                    if (totalleavesreamining != null) {
                        val color = Color.parseColor("#01b9bc")
                        backgroundProgressBarColor = Color.parseColor("#ffffff")
                        backgroundProgressBarWidth = 3f
                        progressBarWidth = 3f
                        progressBarColor = color
                    }

                }
            }
           else if(itemposition?.name?.lowercase()!!.contains("casual"))
            {
                holder.binding.progressbar.apply {
                    if (totalleavesreamining != null) {
                        val color = Color.parseColor("#b5cd4e")
                        backgroundProgressBarColor = Color.parseColor("#ffffff")
                        backgroundProgressBarWidth = 3f
                        progressBarWidth = 3f
                        progressBarColor = color
                    }

                }
            }
           else if(itemposition?.name?.lowercase()!!.contains("medical"))
            {
                holder.binding.progressbar.apply {
                    if (totalleavesreamining != null) {
                        val color = Color.parseColor("#ff860d")
                        backgroundProgressBarColor = Color.parseColor("#ffffff")
                        backgroundProgressBarWidth = 3f
                        progressBarWidth = 3f
                        progressBarColor = color
                    }
                }
            }
        }


     holder.binding.progressbar.apply {
         if (totalleavesreamining != null) {
                 progress = totalleavesreamining.toFloat()
                 progressMax = 100f
                 backgroundProgressBarWidth = 3f
                 progressBarWidth = 3f
             }
             }
           //
         }



    override fun getItemCount(): Int {
        return leavescount!!.size
    }



}