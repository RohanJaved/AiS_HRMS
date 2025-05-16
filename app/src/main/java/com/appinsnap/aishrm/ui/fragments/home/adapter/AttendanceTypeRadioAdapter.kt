package com.appinsnap.aishrm.ui.fragments.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.AttandceTypeRadioItemBinding
import com.appinsnap.aishrm.ui.fragments.home.models.SettingApiResponse
import com.appinsnap.aishrm.util.LoggerGenratter

class AttendanceTypeRadioAdapter(val officesLocationResponse: ArrayList<SettingApiResponse.AttendanceType?>,val onClickedItem: OnClickedItem) : RecyclerView.Adapter<AttendanceTypeRadioAdapter.ViewHolder>() {
    class ViewHolder(val binding: AttandceTypeRadioItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AttandceTypeRadioItemBinding.inflate(layoutInflater, parent, false)
       return ViewHolder(
           binding
       )
    }

    override fun getItemCount(): Int {
      return officesLocationResponse.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = officesLocationResponse[position]
        with(holder.binding) {
            item?.let {
                title.text = item.title
                radioButton.isChecked = item.isCheck
            }
            radioButton.setOnCheckedChangeListener { button, ischeck ->
                if (button.isPressed) {
                    LoggerGenratter.getInstance().printLog("ATTENDANCE RADIO", "onBindViewHolder:checkItem  position $position check $ischeck")
                    onClickedItem.onClick(position, ischeck)
                }
            }
        }

    }
    interface OnClickedItem{
        fun onClick(position: Int, ischeck: Boolean)
    }
}