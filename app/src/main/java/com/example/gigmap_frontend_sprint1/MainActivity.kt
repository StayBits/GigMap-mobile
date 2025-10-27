package com.example.gigmap_frontend_sprint1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.gigmap_frontend_sprint1.bounded.shared.ui.theme.Gigmap_frontend_sprint1Theme
import com.example.gigmap_frontend_sprint1.bounded.public.view.nav.Navi
import com.example.gigmap_frontend_sprint1.bounded.communities.viewmodel.CommunityViewModel
import com.example.gigmap_frontend_sprint1.bounded.concerts.viewmodel.ConcertViewModel
import com.example.gigmap_frontend_sprint1.bounded.communities.viewmodel.PostViewModel
import com.example.gigmap_frontend_sprint1.bounded.users.viewmodel.UserViewModel
import com.google.firebase.FirebaseApp
import kotlin.getValue

class MainActivity : ComponentActivity() {
    val concertViewModel by viewModels<ConcertViewModel>()
    val postViewModel by viewModels<PostViewModel>()
    val userViewModel by viewModels<UserViewModel>()

    val communityViewModel by viewModels<CommunityViewModel>()

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
                    communityViewModel = communityViewModel,
                    context = this
                )
            }
        }
    }
}

