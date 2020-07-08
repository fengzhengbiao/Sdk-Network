package com.leopard.demo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.leopard.network.NetworkService
import com.leopard.network.entity.LoadState
import com.leopard.network.ext.launch
import kotlinx.coroutines.async

class MainViewModel : ViewModel() {

    val imageData = MutableLiveData<List<String>>()
    val loadState = MutableLiveData<LoadState>()

    fun getData() {
        launch(
            {
                loadState.value = LoadState.Loading()
                val data1 = async { NetworkService.getOrCreate<ApiService>().getImage() }
                val data2 = async { NetworkService.getOrCreate<ApiService>().getImage() }
                val data3 = async { NetworkService.getOrCreate<ApiService>().getImage() }
                imageData.value = listOf(data1.await(), data2.await(), data3.await())
                loadState.value = LoadState.Success()
            },
            {
                loadState.value = LoadState.Fail(it.message ?: "加载失败")
            }
        )
    }

}