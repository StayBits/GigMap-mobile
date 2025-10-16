package com.example.gigmap_frontend_sprint1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.gigmap_frontend_sprint1.ui.theme.Gigmap_frontend_sprint1Theme
import com.example.gigmap_frontend_sprint1.view.nav.Navi
import com.example.gigmap_frontend_sprint1.viewmodel.ConcertViewModel
import com.example.gigmap_frontend_sprint1.viewmodel.PostViewModel
import com.example.gigmap_frontend_sprint1.viewmodel.UserViewModel
import com.google.firebase.FirebaseApp
import kotlin.getValue

class MainActivity : ComponentActivity() {
    val concertViewModel by viewModels<ConcertViewModel>()
    val postViewModel by viewModels<PostViewModel>()
    val userViewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            Gigmap_frontend_sprint1Theme {
                Navi(
                    userViewModel = userViewModel,
                    concertViewModel = concertViewModel,
                    postViewModel = postViewModel,
                    context = this
                )
            }
        }
    }
}

