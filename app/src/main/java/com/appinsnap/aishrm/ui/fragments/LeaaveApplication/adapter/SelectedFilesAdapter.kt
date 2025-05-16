package com.appinsnap.aishrm.ui.fragments.LeaaveApplication.adapter

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.ui.fragments.LeaaveApplication.models.ImagePickerModel
import java.io.File


class SelectedFilesAdapter(val onclick: onClickListener,private val selectedFiles: List<ImagePickerModel>, private val context: Context) :
    RecyclerView.Adapter<SelectedFilesAdapter.SelectedFileViewHolder>() {

    class SelectedFileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedFileViewHolder {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.selected_file_item, parent, false)
        return SelectedFileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SelectedFileViewHolder, position: Int) {
        val selectedFile = selectedFiles[position]
        val fileName = selectedFile.name
        if(fileName!!.length>8){
            val truncatedText = fileName.substring(0, 8) + "..."
            val spannableString = SpannableString(truncatedText)
            spannableString.setSpan(UnderlineSpan(), 0, truncatedText.length, 0)
            holder.itemView.findViewById<TextView>(R.id.fileNameTV).text = spannableString
        }else{
            val spannableString = SpannableString(fileName)
            spannableString.setSpan(UnderlineSpan(), 0, fileName.length, 0)
            holder.itemView.findViewById<TextView>(R.id.fileNameTV).text = spannableString
        }

        holder.itemView.findViewById<TextView>(R.id.fileNameTV).setOnClickListener{
            Log.e("TAG", "onBindViewHolder: The selected uri coming on the text selection is ${selectedFile}", )
            onclick.onclick(selectedFile.uri!!, position, 0)
            notifyDataSetChanged()
        }
        holder.itemView.findViewById<ImageView>(R.id.cancel_Btn).setOnClickListener {
            onclick.onclick(selectedFile.uri!!, position,1)
            notifyDataSetChanged()

        }
    }

    override fun getItemCount(): Int {
        return selectedFiles.size
    }
    interface onClickListener{
        fun onclick(data: Uri, position: Int, from: Int)
    }

}
