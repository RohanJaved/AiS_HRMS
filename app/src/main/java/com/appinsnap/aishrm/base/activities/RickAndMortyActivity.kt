package com.appinsnap.aishrm.base.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.ActivityRickAndMortyBinding
import com.appinsnap.aishrm.ui.list.adapter.RicMortyAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RickAndMortyActivity : BaseActivity() {
    private var binding: ActivityRickAndMortyBinding? = null
    private val rickMortyMortViewModel: RickAndMortViewModel by viewModels()

    private var ricMortyAdapter: RicMortyAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rick_and_morty)



        binding?.rickmortyRecyclerview?.layoutManager = GridLayoutManager(
            this,
            2,
            LinearLayoutManager.VERTICAL,
            false
        )

//        rickMortyMortViewModel.RickMortResponse.observe(this, { response ->
//            when (response) {
//                is NetworkResult.Success -> {
//
//                    response.data?.let { ricMortyAdapter?.dataSetChanged(it) }
//                    ricMortyAdapter = RicMortyAdapter(this, response.data) {
//                    }
//                    binding?.rickmortyRecyclerview?.adapter = ricMortyAdapter
//
//                    shimmer()
//
//
//                }
//                is NetworkResult.Error -> {
//                    shimmer()
//
//                    binding?.shimmerViewContainer?.visibility = View.GONE
//                    binding?.shimmerViewContainer?.stopShimmer()
//                    binding?.rickmortyRecyclerview?.visibility = View.VISIBLE
//                }
//                is NetworkResult.Loading -> {
//
//                    shimmer(show = true)
//                }
//            }
//        })

    }

    fun shimmer(show: Boolean = false) {
        if (show) {
            binding?.shimmerViewContainer?.visibility = View.VISIBLE
            binding?.shimmerViewContainer?.startShimmer()
            binding?.rickmortyRecyclerview?.visibility = View.GONE
        } else {
            binding?.shimmerViewContainer?.visibility = View.GONE
            binding?.shimmerViewContainer?.stopShimmer()
            binding?.rickmortyRecyclerview?.visibility = View.VISIBLE
        }

    }


}