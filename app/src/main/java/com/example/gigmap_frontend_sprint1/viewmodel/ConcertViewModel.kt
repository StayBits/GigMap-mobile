package com.example.gigmap_frontend_sprint1.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gigmap_frontend_sprint1.model.ConcertCreateRequest
import com.example.gigmap_frontend_sprint1.model.Concerts
import com.example.gigmap_frontend_sprint1.model.Users
import com.example.gigmap_frontend_sprint1.model.client.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConcertViewModel : ViewModel() {
    var listaConcerts = mutableStateListOf<Concerts>()

    fun getConcerts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.webService.getConcerts()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        listaConcerts.clear()
                        listaConcerts.addAll(response.body()!!)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
                        listaConcerts.clear()
                        listaConcerts.addAll(filtered)
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
                        listaConcerts.clear()
                        listaConcerts.addAll(allConcerts.filter { it.name.contains(query, ignoreCase = true) })
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
                println(" Exception: ${e.message}")
                e.printStackTrace()
                withContext(Dispatchers.Main) { onResult(false) }
            }
        }
    }

    fun getConfirmedUsers(concert: Concerts, allUsers: List<Users>): List<Users> {
        val attendeeIds = concert.attendees ?: emptyList()
        return allUsers.filter { it.id in attendeeIds }
    }


    //Corregir
    fun getConcertsByArtist(artistId: Long, onResult: (List<Concerts>?) -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.webService.getConcertsByArtist(artistId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        onResult(response.body())
                        // opcional: actualizar lista local si quieres
                        // listaConcerts = response.body() as ArrayList<Concerts>
                    } else {
                        onResult(null)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onResult(null) }
            }
        }
    }


}