package com.example.gigmap_frontend_sprint1.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gigmap_frontend_sprint1.model.Post
import com.example.gigmap_frontend_sprint1.model.client.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
@SuppressLint("MutableCollectionMutableState")
class PostViewModel : ViewModel() {

    var listaPosts: ArrayList<Post> by mutableStateOf(arrayListOf())

    fun getPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.webService.getPosts()
            withContext(Dispatchers.Main) {
                if (response.body() != null) {
                    listaPosts = response.body() as ArrayList<Post>
                }
            }
        }
    }
}