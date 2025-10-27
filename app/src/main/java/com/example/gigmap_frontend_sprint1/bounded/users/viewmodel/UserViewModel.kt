package com.example.gigmap_frontend_sprint1.bounded.users.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gigmap_frontend_sprint1.bounded.users.model.CreateDeviceTokenRequest
import com.example.gigmap_frontend_sprint1.bounded.users.model.LoginRequest
import com.example.gigmap_frontend_sprint1.bounded.users.model.RegisterRequest
import com.example.gigmap_frontend_sprint1.bounded.users.model.Users
import com.example.gigmap_frontend_sprint1.bounded.shared.client.RetrofitClient
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("MutableCollectionMutableState")
class UserViewModel : ViewModel() {
    var listaUsers: ArrayList<Users> by mutableStateOf(arrayListOf())
    var listaArtists: ArrayList<Users> by mutableStateOf(arrayListOf())
    var currentUserId by mutableIntStateOf(0)
    var authToken by mutableStateOf<String?>(null)


    //awar
    fun loadUserId(context: Context) {
        val sharedPref = context.getSharedPreferences("pref1", Context.MODE_PRIVATE)
        currentUserId = sharedPref.getInt("userId", 0)
    }


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
                        currentUserId = loginResponse.id

                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val token = task.result ?: ""

                                viewModelScope.launch(Dispatchers.IO) {
                                    sendDeviceTokenToServer(token, currentUserId)
                                }
                            } else {
                                Log.e("TOKEN", "Failed to get FCM Token", task.exception)
                            }
                        }


                        //renato
                        // Guardar el userId en SharedPreferences
                        val sharedPref = context.getSharedPreferences("pref1", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putInt("userId", loginResponse.id)
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

    private fun sendDeviceTokenToServer(fcmToken: String, userId: Int) {
        if (fcmToken.isEmpty() || userId == 0) {
            Log.w("DeviceToken", "Token FCM or UserId unavailable")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = CreateDeviceTokenRequest(userId, fcmToken)
                val response = RetrofitClient.webService.createDeviceToken(request)

                if (response.isSuccessful) {
                    Log.d("DeviceToken", "Token saved successfully")
                } else {
                    Log.e("DeviceToken", "Failed to save token: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("DeviceToken", "Exception: ${e.message}")
            }
        }
    }
}