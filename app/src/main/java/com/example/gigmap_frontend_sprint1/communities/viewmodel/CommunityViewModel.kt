package com.example.gigmap_frontend_sprint1.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gigmap_frontend_sprint1.model.Community
import com.example.gigmap_frontend_sprint1.model.client.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("MutableCollectionMutableState")

class CommunityViewModel : ViewModel() {

    var listaCommunities: ArrayList<Community> by mutableStateOf(arrayListOf())

    fun getCommunities() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.webService.getCommunities()
            withContext(Dispatchers.Main) {
                if (response.body() != null) {
                    listaCommunities = response.body() as ArrayList<Community>
                }
            }
        }
    }
}