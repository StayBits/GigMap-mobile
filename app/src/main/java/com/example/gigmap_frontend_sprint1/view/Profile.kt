package com.example.gigmap_frontend_sprint1.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.gigmap_frontend_sprint1.model.Concerts
import com.example.gigmap_frontend_sprint1.model.Post
import com.example.gigmap_frontend_sprint1.viewmodel.ConcertViewModel
import com.example.gigmap_frontend_sprint1.viewmodel.PostViewModel
import com.example.gigmap_frontend_sprint1.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun Profile(
    rootNav: NavHostController,              // nav raíz (contiene "login", "signin", etc.)
    innerNav: NavHostController,             // nav interno usado para rutas del tab
    userVM: UserViewModel = viewModel(),
    concertVM: ConcertViewModel = viewModel(),
    postVM: PostViewModel = viewModel(),
    context: Context
) {

    val userId = userVM.currentUserId
    val currentUser = remember(userVM.listaUsers, userId) {
        userVM.listaUsers.find { it.id == userId }
    }

    // local state: tabs
    var selectedTab by remember { mutableStateOf(0) }
    val tabOptions = listOf("Conciertos", "Comunidades", "Likes")


    var concertsByUser by remember { mutableStateOf<List<Concerts>>(emptyList()) }
    var likedPosts by remember { mutableStateOf<List<Post>>(emptyList()) }


    val users = userVM.listaUsers
    val userById = remember(users) { users.associateBy { it.id } }


    LaunchedEffect(userId) {
        if (userId != 0) {

            concertVM.getConcertsByArtist(userId.toLong()) { result ->
                result?.let { concertsByUser = it }
            }

            postVM.getPostsLikedByUser(userId.toLong()) { result ->
                result?.let { likedPosts = it }
            }
        } else {
            concertsByUser = emptyList()
            likedPosts = emptyList()
        }
    }



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
                            innerNav.navigate("editProfile")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C0F1A)),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(text = "Editar perfil", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab])
                            .height(3.dp),
                        color = Color.DarkGray
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



            Spacer(modifier = Modifier.height(12.dp))
            when (selectedTab) {
                0 -> { // Conciertos creados por el usuario

                        if (concertsByUser.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No tienes conciertos publicados", color = Color.Gray)
                            }
                        } else {

                            //No fucniona aun :'v
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                items(concertsByUser) { c ->
                                    Card(
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 4.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            GlideImage(
                                                model = c.image ?: "",
                                                contentDescription = c.name,
                                                modifier = Modifier
                                                    .size(72.dp)
                                                    .clip(RoundedCornerShape(8.dp)),
                                                contentScale = ContentScale.Crop
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = c.name ?: "Concierto",
                                                    fontWeight = FontWeight.Bold
                                                )

                                            }
                                            IconButton(onClick = { /* navegar a detalle  */ }) {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowForward,
                                                    contentDescription = "Ver"
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                    }
                }

                1 -> { //Falta el get
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Listado de comunidades (pendiente)", color = Color.Gray)
                    }
                }

                2 -> { // Likes -> posts liked por el usuario

                        if (likedPosts.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No has dado like a ninguna publicación", color = Color.Gray)
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                items(likedPosts) { post ->


                                    val author = userById[post.userId]
                                    PostCard(
                                        post = post,
                                        authorName = author?.name,
                                        authorImage = author?.image,
                                        postVm = postVM,
                                        currentUserId = userId,
                                        onClick = { },

                                    )
                                }
                            }
                        }

                }
            }
        }

        IconButton(
            onClick = {
                // Limpiar SharedPreferences
                val pref = context.getSharedPreferences("pref1", Context.MODE_PRIVATE)
                pref.edit().clear().apply()

                // Resetear ViewModel
                userVM.currentUserId = 0

                // Navegar al login y limpiar backstack
                rootNav.navigate("login") {
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
