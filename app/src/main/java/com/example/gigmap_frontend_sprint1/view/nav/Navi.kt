package com.example.gigmap_frontend_sprint1.view.nav

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gigmap_frontend_sprint1.view.ConcertDetails

import com.example.gigmap_frontend_sprint1.view.ConcertsList
import com.example.gigmap_frontend_sprint1.view.CreateConcert
import com.example.gigmap_frontend_sprint1.view.Login
import com.example.gigmap_frontend_sprint1.view.SignIn
import com.example.gigmap_frontend_sprint1.view.Welcome
import com.example.gigmap_frontend_sprint1.view.Home
import com.example.gigmap_frontend_sprint1.viewmodel.ConcertViewModel
import com.example.gigmap_frontend_sprint1.viewmodel.PostViewModel
import com.example.gigmap_frontend_sprint1.viewmodel.UserViewModel
@Composable
fun Navi(
    userViewModel: UserViewModel,
    concertViewModel: ConcertViewModel,
    postViewModel: PostViewModel,
    context: Context
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {

        composable("welcome") {
            Welcome(navController)
        }


        composable("login") {
            Login(userViewModel, navController, context)
        }

        composable("signin") {
            SignIn(navController, userViewModel)
        }


        composable("homecontent") {
            Home(
                nav = navController
            )
        }

        composable("concertsList") {
            ConcertsList(navController, concertViewModel)
        }

        composable("createConcert") {
            CreateConcert(navController = navController, concertVM = concertViewModel)
        }


        composable(
            route = "concert/{concertId}",
            arguments = listOf(navArgument("concertId") { type = NavType.IntType })
        ) { backStackEntry ->
            val concertId = backStackEntry.arguments?.getInt("concertId") ?: 0
            ConcertDetails(
                navController = navController,
                concertId = concertId,
                concertVM = concertViewModel,
                userVM = userViewModel
            )
        }

    }
}