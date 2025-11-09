package com.example.gigmap_frontend_sprint1.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gigmap_frontend_sprint1.components.BottomBar
import com.example.gigmap_frontend_sprint1.components.TopBar
import com.example.gigmap_frontend_sprint1.viewmodel.CommunityViewModel
import com.example.gigmap_frontend_sprint1.viewmodel.ConcertViewModel
import com.example.gigmap_frontend_sprint1.viewmodel.PostViewModel
import com.example.gigmap_frontend_sprint1.viewmodel.UserViewModel

@Composable
fun Home(nav: NavHostController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val internalNav = rememberNavController()

    val concertVM: ConcertViewModel = viewModel()
    val userVM: UserViewModel = viewModel()
    val postVM: PostViewModel = viewModel()
    val communityVm: CommunityViewModel = viewModel()



    //awar
    val context = LocalContext.current

    //awar

    LaunchedEffect(Unit) {
        userVM.loadUserId(context)
    }





    Scaffold(
        topBar = {
            TopBar(
                onBackClick = {
                    if (!internalNav.popBackStack()) {

                        nav.popBackStack()
                    }
                },
                onNotificationClick = { /* TODO: notificaciones */ }
            )
        },
        bottomBar = {
            BottomBar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedItem) {
                0 -> {
                    NavHost(
                        navController = internalNav,
                        startDestination = "homecontent"
                    ) {
                        composable("homecontent") {
                            HomeContent(
                                internalNav,
                                concertVM = concertVM,
                                userVM = userVM,
                                postVM = postVM
                            )
                        }


                        composable(
                            route = "user/{userId}",
                            arguments = listOf(navArgument("userId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val userId = backStackEntry.arguments?.getInt("userId") ?: 0

                            Profile(
                                rootNav = nav,          // el nav raíz para cerrar sesión etc
                                innerNav = internalNav, // para navegación dentro de perfil
                                userVM = userVM,
                                concertVM = concertVM,
                                postVM = postVM,
                                viewedUserId = userId,// <- ESTO ES LO IMPORTANTE
                                context = LocalContext.current
                            )
                        }


                        composable("concertsList") {
                            ConcertsList(
                                internalNav,
                                concertVM = concertVM,
                                userVm = userVM

                            )
                        }
                        composable("createConcert") {
                            CreateConcert(
                                internalNav,
                                concertVM = concertVM,
                                userVM = userVM
                            )
                        }
                        composable(
                            route = "concert/{concertId}",
                            arguments = listOf(navArgument("concertId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val concertId = backStackEntry.arguments?.getInt("concertId") ?: 0
                            ConcertDetails(
                                navController = internalNav,
                                concertId = concertId,
                                concertVM = concertVM,
                                userVM = userVM
                            )
                        }



                    }
                }
                1 -> Map(nav)

                //awar
                2 -> {

                    NavHost(
                        navController = internalNav,
                        startDestination = "communitiesList"
                    ) {
                        composable("communitiesList") {
                            CommunitiesList(nav = internalNav)
                        }


                        composable(
                            route = "user/{userId}",
                            arguments = listOf(navArgument("userId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val userId = backStackEntry.arguments?.getInt("userId") ?: 0

                            Profile(
                                rootNav = nav,          // el nav raíz para cerrar sesión etc
                                innerNav = internalNav, // para navegación dentro de perfil
                                userVM = userVM,
                                concertVM = concertVM,
                                postVM = postVM,
                                viewedUserId = userId,// <- ESTO ES LO IMPORTANTE
                                context = LocalContext.current
                            )
                        }

                        composable(
                            route = "community/{communityId}",
                            arguments = listOf(navArgument("communityId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val communityId = backStackEntry.arguments?.getInt("communityId") ?: 0
                            Community(
                                navController = internalNav,
                                communityId = communityId,
                                userVm = userVM,
                                postVm = postVM,
                                communityVm = communityVm

                            )
                        }

                        // dentro del NavHost de communities (añadir junto a los otros composable)
                        composable("createCommunity") {
                            // pasar el mismo viewModel que creaste arriba en Home (communityVm)
                            CreateCommunity(
                                nav = internalNav,
                                viewModel = communityVm
                            )
                        }


                        composable(
                            route = "createPost/{communityId}",
                            arguments = listOf(navArgument("communityId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val communityId = backStackEntry.arguments?.getInt("communityId") ?: 0
                            CreatePost(
                                navController = internalNav,
                                communityId = communityId,
                                postVM = postVM,
                                userVM = userVM
                            )
                        }
                    }
                }

                //awar
                3 -> {
                    // Creamos un nav host interno para el tab "Profile"
                    NavHost(
                        navController = internalNav,
                        startDestination = "profile"
                    ) {
                        composable("profile") {

                            Profile(
                                rootNav = nav,             // nav raíz que SÍ tiene "login"
                                innerNav = internalNav,    // nav interno para "editProfile"
                                userVM = userVM,
                                concertVM = concertVM,
                                postVM = postVM,
                                context = LocalContext.current
                            )
                        }

                        composable(
                            route = "concert/{concertId}",
                            arguments = listOf(navArgument("concertId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val concertId = backStackEntry.arguments?.getInt("concertId") ?: 0
                            ConcertDetails(
                                navController = internalNav,
                                concertId = concertId,
                                concertVM = concertVM,
                                userVM = userVM
                            )
                        }


                        composable("editProfile") {

                            EditProfile(
                                nav = internalNav,
                                userVM = userVM
                            )
                        }
                    }




                }
            }
        }
    }
}