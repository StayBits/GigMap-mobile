package com.example.gigmap_frontend_sprint1.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gigmap_frontend_sprint1.model.ConcertCreateRequest
import com.example.gigmap_frontend_sprint1.model.Concerts
import com.example.gigmap_frontend_sprint1.model.Users
import com.example.gigmap_frontend_sprint1.model.client.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
@SuppressLint("MutableCollectionMutableState")
class ConcertViewModel : ViewModel() {
    var listaConcerts: ArrayList<Concerts> = arrayListOf()

    fun getConcerts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.webService.getConcerts()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        listaConcerts = ArrayList(response.body()!!)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }
    fun filterByMultipleGenres(genres: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.webService.getConcerts()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val filtered = response.body()!!.filter { concert ->
                            genres.contains(concert.genre.uppercase())
                        }
                        listaConcerts = ArrayList(filtered)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun searchByName(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.webService.getConcerts()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val allConcerts = response.body()!!
                        listaConcerts = ArrayList(
                            allConcerts.filter {
                                it.name.contains(query, ignoreCase = true)
                            }
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createConcert(concertRequest: ConcertCreateRequest, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                println(" Enviando request al backend:")
                println(" Request: $concertRequest")

                val response = RetrofitClient.webService.createConcert(concertRequest)

                println(" Response code: ${response.code()}")
                println(" Response body: ${response.body()}")
                println(" Response error: ${response.errorBody()?.string()}")

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        // Agregar el nuevo concierto al listado
                        val newConcert = response.body()!!
                        listaConcerts.add(newConcert)
                        println(" Concierto creado exitosamente")
                        onResult(true)
                    } else {
                        println(" Error del servidor: ${response.code()}")
                        onResult(false)
                    }
                }
            } catch (e: Exception) {
                println(" Excepci n: ${e.message}")
                e.printStackTrace()
                withContext(Dispatchers.Main) { onResult(false) }
            }
        }
    }

    fun getConfirmedUsers(concert: Concerts, allUsers: List<Users>): List<Users> {
        val attendeeIds = concert.attendees ?: emptyList()
        return allUsers.filter { it.id in attendeeIds }
    }
}