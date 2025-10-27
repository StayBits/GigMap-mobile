package com.example.gigmap_frontend_sprint1.shared.nav

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gigmap_frontend_sprint1.communities.view.Community
import com.example.gigmap_frontend_sprint1.concerts.view.ConcertDetails

import com.example.gigmap_frontend_sprint1.concerts.view.ConcertsList
import com.example.gigmap_frontend_sprint1.concerts.view.CreateConcert
import com.example.gigmap_frontend_sprint1.communities.view.CreatePost
import com.example.gigmap_frontend_sprint1.users.view.Login
import com.example.gigmap_frontend_sprint1.users.view.SignIn
import com.example.gigmap_frontend_sprint1.public.Welcome
import com.example.gigmap_frontend_sprint1.public.Home
import com.example.gigmap_frontend_sprint1.communities.viewmodel.CommunityViewModel
import com.example.gigmap_frontend_sprint1.concerts.viewmodel.ConcertViewModel
import com.example.gigmap_frontend_sprint1.communities.viewmodel.PostViewModel
import com.example.gigmap_frontend_sprint1.users.viewmodel.UserViewModel
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