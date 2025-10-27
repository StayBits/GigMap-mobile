package com.example.gigmap_frontend_sprint1.bounded.users.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.gigmap_frontend_sprint1.bounded.users.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    nav: NavHostController,
    userVM: UserViewModel = viewModel(),
    context: Context
) {
    // Cargar user si no está cargado (opcional)
    LaunchedEffect(Unit) {
        // Si tienes loadUserId en ViewModel, podrías llamarlo desde el Home
        // Aquí lo dejamos opcional (descomenta si lo necesitas)
        // userVM.loadUserId(context)
    }

    val userId = userVM.currentUserId
    val currentUser = remember(userVM.listaUsers, userId) {
        userVM.listaUsers.find { it.id == userId }
    }

    var selectedTab by remember { mutableStateOf(0) }
    val tabOptions = listOf("Conciertos", "Comunidades", "Likes")

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Avatar centrado
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Círculo de imagen
                    val imageUrl = currentUser?.image ?: "" // ajusta según tu modelo
                    AsyncImage(
                        model = if (imageUrl.isNotBlank()) imageUrl else null,
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEDEDED), CircleShape)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nombre (grande)
                    Text(
                        text = currentUser?.name ?: "Nombre de usuario",
                        fontSize = 20.sp,
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium
                    )

                    // Username (más pequeño)
                    Text(
                        text = currentUser?.username?.let { "@$it" } ?: "@username",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Botón Editar perfil
                    Button(
                        onClick = {
                            // Navegar a editar perfil si existe la ruta
                            nav.navigate("editProfile")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C0F1A)),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(text = "Editar perfil", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tabs con 3 secciones: Conciertos | Comunidades | Likes
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab])
                            .height(3.dp), // grosor de la barrita
                        color = Color.DarkGray// 🔴 barrita roja
                    )
                }
            ) {
                tabOptions.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                color = if (selectedTab == index) Color(0xFF5C0F1A) else Color.Gray
                            )
                        }
                    )
                }
            }


            // Contenido de cada pestaña (placeholders que puedes reemplazar con listas reales)
            Spacer(modifier = Modifier.height(12.dp))
            when (selectedTab) {
                0 -> {
                    // Conciertos
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Aquí van los conciertos del usuario", color = Color.DarkGray)
                        // TODO: reemplaza con lista real de conciertos
                    }
                }
                1 -> {
                    // Comunidades
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Aquí van las comunidades del usuario", color = Color.DarkGray)
                        // TODO: lista de comunidades
                    }
                }
                2 -> {
                    // Likes
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Aquí van los posts liked por el usuario", color = Color.DarkGray)
                        // TODO: lista de likes
                    }
                }
            }
        }

        // Botón cerrar sesión arriba a la derecha
        IconButton(
            onClick = {
                // Limpiar SharedPreferences
                val pref = context.getSharedPreferences("pref1", Context.MODE_PRIVATE)
                pref.edit().clear().apply()

                // Resetear ViewModel
                userVM.currentUserId = 0

                // Navegar al login y limpiar backstack
                nav.navigate("login") {
                    popUpTo(0) { inclusive = true } // o popUpTo("home") según tu grafo
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cerrar sesión",
                tint = Color(0xFF5C0F1A)
            )
        }
    }
}
