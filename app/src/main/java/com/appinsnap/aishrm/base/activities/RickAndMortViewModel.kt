package com.appinsnap.aishrm.base.activities

import com.appinsnap.aishrm.base.BaseViewModel
import com.appinsnap.aishrm.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RickAndMortViewModel  @Inject constructor(private val repository: Repository) : BaseViewModel(){
//    private var _RickMortResponse: MutableLiveData<NetworkResult<RickAndMortyModel>> = MutableLiveData()
//    val RickMortResponse: LiveData<NetworkResult<RickAndMortyModel>> = _RickMortResponse

//    init {
//        getProducts()
//    }

//    private fun getProducts() = coroutinesScope.launch {
//        getRickMortySafeCall()
//    }
//    private suspend fun getRickMortySafeCall() {
//        _RickMortResponse.postValue(NetworkResult.Loading())
//        if (Utils.hasInternetConnection()) {
//            try {
//                val response = repository.remote.getRickAndMorty()
//                _RickMortResponse.postValue(NetworkResult.Success(response))
//
//            } catch (e: Exception) {
//                _RickMortResponse.postValue(NetworkResult.Error("Products not found."))
//            }
//        } else {
//            _RickMortResponse.value = NetworkResult.Error("No Internet Connection.")
//        }
//    }
}