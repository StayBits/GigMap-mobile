package com.example.gigmap_frontend_sprint1.bounded.public.view

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
import com.example.gigmap_frontend_sprint1.bounded.shared.components.BottomBar
import com.example.gigmap_frontend_sprint1.bounded.shared.components.TopBar
import com.example.gigmap_frontend_sprint1.bounded.communities.viewmodel.CommunityViewModel
import com.example.gigmap_frontend_sprint1.bounded.concerts.viewmodel.ConcertViewModel
import com.example.gigmap_frontend_sprint1.bounded.communities.viewmodel.PostViewModel
import com.example.gigmap_frontend_sprint1.bounded.users.viewmodel.UserViewModel
import com.example.gigmap_frontend_sprint1.bounded.concerts.view.ConcertsList
import com.example.gigmap_frontend_sprint1.bounded.concerts.view.CreateConcert
import com.example.gigmap_frontend_sprint1.bounded.concerts.view.ConcertDetails
import com.example.gigmap_frontend_sprint1.bounded.communities.view.CommunitiesList
import com.example.gigmap_frontend_sprint1.bounded.communities.view.Community
import com.example.gigmap_frontend_sprint1.bounded.communities.view.CreatePost
import com.example.gigmap_frontend_sprint1.bounded.users.view.Profile

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
                        composable("concertsList") {
                            ConcertsList(
                                internalNav,
                                concertVM = concertVM
                            )
                        }
                        composable("createConcert") {
                            CreateConcert(
                                internalNav,
                                concertVM = concertVM
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
                3 -> Profile(
                    nav = nav,
                    userVM = userVM,
                    context = LocalContext.current
                )

                // dentro de Home(...) en el when(selectedItem)



            }
        }
    }
}