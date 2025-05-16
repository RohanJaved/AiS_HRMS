package com.appinsnap.aishrm.ui.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.RickandmortyItemBinding
import com.appinsnap.aishrm.ui.list.model.RickAndMortyModel
import com.appinsnap.aishrm.util.ListDiffUtil
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Created by Adnan Bashir manak on 11,February,2023
 * AIS company,
 * Krachi, Pakistan.
 */
class RicMortyAdapter(
    var context: Context,
    var rickmorty: RickAndMortyModel? ,
    val adapterOnClick: (Int) -> Unit
) :
    RecyclerView.Adapter<RickMortyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RickMortyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RickandmortyItemBinding.inflate(layoutInflater, parent, false)
        return RickMortyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RickMortyViewHolder, position: Int) {
//        holder.binding?.rickandmorty = rickmorty//[position]
        holder.binding?.root?.setSafeOnClickListener {
//            adapterOnClick(rickmorty?.results?.get(position)?.id!!)
            detailDialog(position)
        }

        holder.binding?.tvSpecies?.text=rickmorty?.results?.get(position)?.species
        holder.binding?.tvStatus?.text=rickmorty?.results?.get(position)?.status
        holder.binding?.name?.text=rickmorty?.results?.get(position)?.name
        Glide
            .with(context)
            .load(rickmorty?.results?.get(position)?.image)
            .centerCrop()
            .into(holder.binding!!.imageView);
        holder.binding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
       return rickmorty?.results!!.size
    }


    fun dataSetChanged(productsArray: RickAndMortyModel) {
        val listDiffUtil = ListDiffUtil(this.rickmorty!!.results, productsArray.results)
        val diffUtilResult = DiffUtil.calculateDiff(listDiffUtil)
        this.rickmorty = productsArray
        diffUtilResult.dispatchUpdatesTo(this)
    }
    fun detailDialog(position: Int) {
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(R.layout.bottomsheet_dialog)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.behavior.isDraggable = true
        val iv_profile = bottomSheetDialog.findViewById<ImageView>(R.id.iv_profile)
        val tv_status = bottomSheetDialog.findViewById<TextView>(R.id.tv_status)
        val tv_title = bottomSheetDialog.findViewById<TextView>(R.id.textView)
        val tv_spices = bottomSheetDialog.findViewById<TextView>(R.id.tv_spices)
        val tv_noepisodes = bottomSheetDialog.findViewById<TextView>(R.id.tv_noepisodes)
        val tv_gender = bottomSheetDialog.findViewById<TextView>(R.id.tv_gender)
        val tv_episodseed = bottomSheetDialog.findViewById<TextView>(R.id.tv_episodseed)
        val tv_Originlocationname = bottomSheetDialog.findViewById<TextView>(R.id.tv_Originlocationname)
        val tv_lastknownlocation = bottomSheetDialog.findViewById<TextView>(R.id.tv_lastknownlocation)

        tv_status?.text=rickmorty?.results?.get(position)?.status
        tv_title?.text=rickmorty?.results?.get(position)?.name
        tv_spices?.text=rickmorty?.results?.get(position)?.species
        tv_noepisodes?.text=rickmorty?.results?.get(position)?.episode?.size.toString()
        tv_gender?.text=rickmorty?.results?.get(position)?.gender
        tv_Originlocationname?.text=rickmorty?.results?.get(position)?.origin?.name
        tv_lastknownlocation?.text=rickmorty?.results?.get(position)?.location?.name
        tv_episodseed?.text=""
        Glide
            .with(context)
            .load(rickmorty?.results?.get(position)?.image)
            .centerCrop()
            .into(iv_profile!!);
        bottomSheetDialog.show()
    }
}