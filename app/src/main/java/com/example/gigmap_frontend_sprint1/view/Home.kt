package com.example.gigmap_frontend_sprint1.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gigmap_frontend_sprint1.components.BottomBar
import com.example.gigmap_frontend_sprint1.components.TopBar
import com.example.gigmap_frontend_sprint1.viewmodel.ConcertViewModel
import com.example.gigmap_frontend_sprint1.viewmodel.PostViewModel
import com.example.gigmap_frontend_sprint1.viewmodel.UserViewModel

@Composable
fun Home(nav: NavHostController) {
    var selectedItem by remember { mutableStateOf(0) }
    val internalNav = rememberNavController()

    val concertVM: ConcertViewModel = viewModel()
    val userVM: UserViewModel = viewModel()
    val postVM: PostViewModel = viewModel()

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
                2 -> Communities(nav)
                3 -> Profile(nav)
            }
        }
    }
}