package com.appinsnap.aishrm.ui.fragments.home.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.CtodepartmentitemBinding
import com.appinsnap.aishrm.ui.fragments.home.models.DepartmentGroup
import com.appinsnap.aishrm.util.Constants
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener

class DepartmentGroupAdapter(requireContext: Context, val groupname: ArrayList<DepartmentGroup>, val onGroupClick:onGroupItemCick) : RecyclerView.Adapter<DepartmentGroupAdapter.ViewHolder>() {
    private var expandedItemPosition = RecyclerView.NO_POSITION
    var selectedPosition = Constants.SELECTEDdpt
    var lastSelectedPosition = -1
   inner  class ViewHolder(val binding:CtodepartmentitemBinding  ): RecyclerView.ViewHolder(binding.root)
    {



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CtodepartmentitemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

       holder.binding.mcvDis.setSafeOnClickListener {

               lastSelectedPosition = selectedPosition
               selectedPosition = holder.getBindingAdapterPosition();
               notifyItemChanged(lastSelectedPosition);
               notifyItemChanged(selectedPosition);
               Constants.SELECTEDdpt=position
               onGroupClick.onGroupClick(Constants.SELECTEDdpt)
           };


        if (selectedPosition == holder.getBindingAdapterPosition()) {
            holder.binding.mcvDis.strokeWidth=2
            holder.binding.mcvDis.setStrokeColor(Color.parseColor("#c57611"))
            holder.binding.mcvDis.setCardBackgroundColor(Color.parseColor("#38373e"));
            holder.binding.groupname.setTextColor(Color.parseColor("#c57611"))
        } else {
            holder.binding.mcvDis.strokeWidth = 0
            holder.binding.mcvDis.setCardBackgroundColor(Color.parseColor("#474749"))
            holder.binding.groupname.setTextColor(Color.parseColor("#ffffff"))
        }
        holder.binding.groupname.text = groupname.get(position).name



    }




    var lastButtonClickTime: Long = 0

    fun checkIfValidClick(): Boolean {
        val currentTime = System.currentTimeMillis()
        val timeLapse = currentTime - lastButtonClickTime

        if (timeLapse >= 1000) {
            lastButtonClickTime = currentTime
            return true
        }
        return false
    }


    override fun getItemCount(): Int {
        return groupname.size
    }

        interface onGroupItemCick{
            fun onGroupClick(position: Int)
        }
    }

