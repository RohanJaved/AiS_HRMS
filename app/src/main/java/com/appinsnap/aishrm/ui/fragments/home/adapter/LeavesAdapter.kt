package com.appinsnap.aishrm.ui.fragments.home.adapter

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.LeavesitemBinding
import com.appinsnap.aishrm.ui.fragments.home.models.SettingApiResponse
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.bumptech.glide.Glide
import com.jakewharton.retrofit2.adapter.rxjava2.Result.error


class LeavesAdapter( val context: Context, val list: List<SettingApiResponse.DashboardTile?>, private val onItemClick: (data: Int) -> Unit): RecyclerView.Adapter<LeavesAdapter.ViewHolder>() {
    class ViewHolder(val binding:LeavesitemBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LeavesitemBinding.inflate(layoutInflater, parent, false)

       /* screensizewidth=screensizewidth-30;
        var width=screensizewidth/3*/

      /*  val layoutParams = binding.mcvattendancestatus.layoutParams
        layoutParams.width = pixelsToSdp(screensizewidth,context).toInt()
        binding.mcvattendancestatus.layoutParams = layoutParams
*/

        return ViewHolder(binding)

    }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.txtmenu.text = item?.name ?: ""

        holder.binding.mcvattendancestatus.setSafeOnClickListener {

            item?.id?.let { it1 -> onItemClick(it1) }


        }





        if (item?.icon!=null)
        {

            if (item?.icon.toString().lowercase().contains("jpg") || item?.icon.toString().lowercase().contains("jpeg")||item?.icon.toString().lowercase().contains("png"))
            {
                Glide.with(context)
                    .load(item?.icon)
                    .placeholder(R.drawable.progressbar_custom)
                    .into(holder.binding.imgmenu)

                holder.itemView.setPadding(0, 0, 20, 0)
            }else if (item?.icon.toString().lowercase().contains("gif"))
            {
                Glide.with(context)
                    .asGif()
                    .load(item?.icon)
                    .placeholder(R.drawable.progressbar_custom)
                    .into(holder.binding.imgmenu)

                holder.itemView.setPadding(0, 0, 20, 0)
            }else if (item?.icon.toString().lowercase().contains("gif"))
            {
                //show svg from url
            }else
            {
                Glide.with(context)
                    .asGif()
                    .load("")
                    .placeholder(R.drawable.progressbar_custom)
                    .into(holder.binding.imgmenu)

                holder.itemView.setPadding(0, 0, 20, 0)
            }


        }

    }

    override fun getItemCount(): Int {
        return list.size
    }









}