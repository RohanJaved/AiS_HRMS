package com.appinsnap.aishrm.ui.fragments.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.FilterlovsBinding
import com.appinsnap.aishrm.ui.fragments.home.models.filtermodels.Body


class FilterAdapter(
    val context: Context,
    var filterlist: List<Body>,
    val onclick: (ArrayList<Body>) -> Unit
) :
    RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
    class ViewHolder(val binding: FilterlovsBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    var selectedItem = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FilterlovsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(
            binding
        )
    }


    override fun getItemCount(): Int {
        return filterlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.filterlayout.setOnClickListener {
            if (holder.binding.recyclerviewlayout.visibility == View.VISIBLE) {
                holder.binding.recyclerviewlayout.visibility = View.GONE
                holder.binding.childrecyclerview.visibility = View.GONE
                holder.binding.arrowimage.setImageResource(R.drawable.downarrow)
            } else {
                holder.binding.arrowimage.setImageResource(R.drawable.uparrow)
                val selectedList: ArrayList<String> = ArrayList()
                holder.binding.recyclerviewlayout.visibility = View.VISIBLE
                holder.binding.childrecyclerview.visibility = View.VISIBLE
                setAdapter(position, selectedList, holder)
            }
        }
        holder.binding.filtertext.text = filterlist.get(position).type
        holder.binding.recyclerviewlayout.visibility = View.GONE

        val mScrollTouchListener: OnItemTouchListener = object : OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val action = e.action
                when (action) {
                    MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(true)
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        }

        holder.binding.childrecyclerview.addOnItemTouchListener(mScrollTouchListener)
    }

    private fun setAdapter(
        position: Int,
        selectedList: ArrayList<String>,
        holder: ViewHolder
    ) {
        val list = filterlist.get(position).lov
        if (list.size == 2 || list.size == 1) {
            // Set height in SDP
            holder.binding.recyclerviewlayout.layoutParams.height = 390
        }
        else if(list.size==3)
        {
            holder.binding.recyclerviewlayout.layoutParams.height = 450
        }
        else {
            // Set height in SDP
            holder.binding.recyclerviewlayout.layoutParams.height = 600
        }
        val adapter = OptionRecycler(context, list) { pos, str, check ->
            filterlist.get(position).lov.get(pos).check = check
            filterlist.get(position).selectedId = filterlist.get(position).lov.get(pos).id
            selectedList.clear()
            for (i in filterlist[position].lov.indices) {
                if (filterlist[position].lov[i].check) {
                    val current = filterlist[position].lov[i].name
                    selectedList.add(current)
                }
            }
            if (selectedList.isNotEmpty()) {
                holder.binding.optionrecycler.show()
                holder.binding.optiontext.hide()
                val adapter = SelectedOptionRecycler(context, selectedList) { value ->
                    for (i in filterlist[position].lov.indices) {
                        filterlist[position].lov[i].check = false
                        for (j in value.indices)
                            if (value[j] == filterlist[position].lov[i].name)
                                filterlist[position].lov[i].check = true
                    }
                    onclick(filterlist as ArrayList<Body>)
                    setAdapter(position,selectedList,holder)
                    if(value.isEmpty()){
                        holder.binding.optionrecycler.hide()
                        holder.binding.optiontext.show()
                    }
                }
                holder.binding.optionrecycler.adapter = adapter
            }
            else {
                holder.binding.optionrecycler.hide()
                holder.binding.optiontext.show()
            }

            onclick(filterlist as ArrayList<Body>)
        }
        holder.binding.childrecyclerview.adapter = adapter
    }
}