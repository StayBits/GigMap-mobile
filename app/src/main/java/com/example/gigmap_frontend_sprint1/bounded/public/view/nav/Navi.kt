package com.example.gigmap_frontend_sprint1.bounded.public.view.nav

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gigmap_frontend_sprint1.bounded.communities.view.Community
import com.example.gigmap_frontend_sprint1.bounded.concerts.view.ConcertDetails

import com.example.gigmap_frontend_sprint1.bounded.concerts.view.ConcertsList
import com.example.gigmap_frontend_sprint1.bounded.concerts.view.CreateConcert
import com.example.gigmap_frontend_sprint1.bounded.communities.view.CreatePost
import com.example.gigmap_frontend_sprint1.bounded.users.view.Login
import com.example.gigmap_frontend_sprint1.bounded.users.view.SignIn
import com.example.gigmap_frontend_sprint1.bounded.users.view.Welcome
import com.example.gigmap_frontend_sprint1.bounded.public.view.Home
import com.example.gigmap_frontend_sprint1.bounded.communities.viewmodel.CommunityViewModel
import com.example.gigmap_frontend_sprint1.bounded.concerts.viewmodel.ConcertViewModel
import com.example.gigmap_frontend_sprint1.bounded.communities.viewmodel.PostViewModel
import com.example.gigmap_frontend_sprint1.bounded.users.viewmodel.UserViewModel
@Composable
fun Navi(
    userViewModel: UserViewModel,
    concertViewModel: ConcertViewModel,
    postViewModel: PostViewModel,
    communityViewModel: CommunityViewModel,
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

        /*
        // importa NavType si no lo tienes: ya lo usas arriba

        composable(
            route = "community/{communityId}",
            arguments = listOf(navArgument("communityId") { type = NavType.IntType })
        ) { backStackEntry ->
            val communityId = backStackEntry.arguments?.getInt("communityId") ?: 0
            // Llamamos a CommunityScreen. Esta función puede obtener sus ViewModels internamente con viewModel()
            Community(
                navController = navController,            // o nav si prefieres
                communityId = communityId,
                communityVm = communityViewModel,              // inyecta VMs si quieres
                postVm = postViewModel
            )
        }

         */


        /*
        composable(
            route = "createPost/{communityId}",
            arguments = listOf(navArgument("communityId") { type = NavType.IntType })
        ) { backStackEntry ->
            val communityId = backStackEntry.arguments?.getInt("communityId") ?: 0
            CreatePost(
                navController = navController,
                communityId = communityId,
                postVM = postViewModel,
                userVM = userViewModel

            )
        }

        */

    }
}