package com.example.gigmap_frontend_sprint1.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gigmap_frontend_sprint1.model.LoginRequest
import com.example.gigmap_frontend_sprint1.model.RegisterRequest
import com.example.gigmap_frontend_sprint1.model.Users
import com.example.gigmap_frontend_sprint1.model.client.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.getValue
@SuppressLint("MutableCollectionMutableState")
class UserViewModel : ViewModel() {
    var listaUsers: ArrayList<Users> by mutableStateOf(arrayListOf())

    var listaArtists: ArrayList<Users> by mutableStateOf(arrayListOf())
    var currentUser by mutableStateOf<Users?>(null)
    var authToken by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    fun getUsers() = viewModelScope.launch(Dispatchers.IO) {
        val r = RetrofitClient.webService.getUsers()
        withContext(Dispatchers.Main) {
            r.body()?.let { listaUsers = ArrayList(it) } }
    }

    var isLoading by mutableStateOf(false)

    fun register(
        email: String,
        username: String,
        password: String,
        role: String,
        context: Context,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading = true

                val normalizedRole = when (role.uppercase()) {
                    "ARTISTA", "ARTIST" -> "ARTIST"
                    "FAN" -> "FAN"
                    else -> "FAN"
                }

                val request = RegisterRequest(email, username, password, normalizedRole)
                val response = RetrofitClient.webService.register(request)

                withContext(Dispatchers.Main) {
                    Log.d("REGISTER_DEBUG", "CODE: ${response.code()}")
                    Log.d("REGISTER_DEBUG", "BODY: ${response.body()}")
                    Log.d("REGISTER_DEBUG", "ERROR: ${response.errorBody()?.string()}")

                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        onSuccess()
                    } else {
                        Toast.makeText(
                            context,
                            "Error al registrar usuario. Código ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("REGISTER_DEBUG", "Exception: ${e.stackTraceToString()}")
                }
            } finally {
                withContext(Dispatchers.Main) { isLoading = false }
            }
        }
    }


    fun login(
        emailOrUsername: String,
        password: String,
        context: Context,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading = true
                val request = LoginRequest(emailOrUsername, password)
                val response = RetrofitClient.webService.login(request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()!!
                        authToken = loginResponse.token
                        currentUser = loginResponse.user

                        val pref = context.getSharedPreferences("pref1", Context.MODE_PRIVATE)
                        with(pref.edit()) {
                            putString("token", loginResponse.token)
                            putString("email", loginResponse.user?.email ?: "")
                            putString("username", loginResponse.user?.username ?: "")
                            putBoolean("isLogged", true)
                            apply()
                        }

                        Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                        onSuccess()
                    } else {
                        Toast.makeText(
                            context,
                            "Credenciales incorrectas. Intenta nuevamente.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("LOGIN_DEBUG", "Exception: ${e.stackTraceToString()}")
                }
            } finally {
                withContext(Dispatchers.Main) { isLoading = false }
            }
        }
    }


    fun getArtists() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = RetrofitClient.webService.getUsers()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    val allUsers = response.body()!!
                    Log.d("USER_DEBUG", "Usuarios recibidos: ${allUsers.size}")
                    Log.d("USER_DEBUG", "Roles: ${allUsers.map { it.role }}")

                    listaArtists = ArrayList(allUsers.filter { it.role.equals("ARTIST", ignoreCase = true) })
                    Log.d("USER_DEBUG", "Artistas filtrados: ${listaArtists.size}")
                } else {
                    Log.e("USER_DEBUG", "Error en getUsers: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("USER_DEBUG", "ExcepciÃ³n: ${e.message}")
        }
    }


}