package com.example.gigmap_frontend_sprint1.view
// imports
import android.annotation.SuppressLint
import android.webkit.WebSettings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.gigmap_frontend_sprint1.viewmodel.ConcertViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ConcertsList(
    navController: NavHostController,
    concertVM: ConcertViewModel
) {
    val concerts = concertVM.listaConcerts
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedGenres = remember { mutableStateListOf<String>() }

    LaunchedEffect(concerts.isEmpty()) {
        if (concerts.isEmpty()) concertVM.getConcerts()
    }

    // Lista de géneros válidos del backend
    val allGenres = listOf(
        "ROCK", "POP", "ELECTRONICA", "URBANO", "JAZZ", "INDIE",
        "CLASICO", "METAL", "FOLK", "COUNTRY", "REGGAE", "BLUES",
        "ALTERNATIVE", "PUNK", "SOUL", "FUNK", "R_AND_B", "LATIN",
        "WORLD", "HIP_HOP", "CLASSICAL", "ELECTRONIC", "OTHER"
    )

    // Drawer que sale desde la derecha
    RightSideDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(260.dp)
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                // 🔹 Header con título y botón cerrar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FILTRAR POR GÉNERO",
                        color = Color(0xFF5C0F1A),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar filtros",
                        tint = Color(0xFF5C0F1A),
                        modifier = Modifier
                            .size(26.dp)
                            .clickable { scope.launch { drawerState.close() } }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val allGenres = listOf(
                    "ROCK", "POP", "ELECTRONICA", "URBANO", "JAZZ", "INDIE",
                    "CLASICO", "METAL", "FOLK", "COUNTRY", "REGGAE", "BLUES",
                    "ALTERNATIVE", "PUNK", "SOUL", "FUNK", "R_AND_B", "LATIN",
                    "WORLD", "HIP_HOP", "CLASSICAL", "ELECTRONIC", "OTHER"
                )

                // 🔸 Scroll con géneros (toma todo el espacio disponible)
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(allGenres) { genre ->
                        val isSelected = selectedGenres.contains(genre)
                        Button(
                            onClick = {
                                if (isSelected) selectedGenres.remove(genre)
                                else selectedGenres.add(genre)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) Color(0xFF5C0F1A) else Color.White,
                                contentColor = if (isSelected) Color.White else Color(0xFF5C0F1A)
                            ),
                            border = BorderStroke(1.dp, Color(0xFF5C0F1A)),
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(genre)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 Botón aplicar filtro (fijo al final)
                Button(
                    onClick = {
                        if (selectedGenres.isEmpty()) {
                            concertVM.getConcerts()
                        } else {
                            concertVM.filterByMultipleGenres(selectedGenres)
                        }
                        scope.launch { drawerState.close() }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C0F1A)),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("APLICAR FILTROS")
                }

                // 🔹 Botón limpiar (fijo abajo)
                Text(
                    text = "LIMPIAR FILTROS",
                    color = Color(0xFF5C0F1A),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedGenres.clear()
                            concertVM.getConcerts()
                            scope.launch { drawerState.close() }
                        }
                        .padding(top = 12.dp)
                )
            }
        }
    ) {
        // 🎶 Contenido principal
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("createConcert") },
                    containerColor = Color(0xFF5C0F1A),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Crear concierto")
                }
            },
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(top = 10.dp, bottom = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color(0xFF5C0F1A))
                        Text(
                            text = "Filtrar por género",
                            color = Color(0xFF5C0F1A),
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            Icons.Default.FilterAlt,
                            contentDescription = "Abrir filtro",
                            tint = Color(0xFF5C0F1A),
                            modifier = Modifier.clickable {
                                scope.launch { drawerState.open() }
                            }
                        )
                    }
                }
            },
            containerColor = Color.White
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
            ) {
                if (concerts.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF5C0F1A))
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(concerts) { concert ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate("concert/${concert.id}")
                                    },
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column {
                                    GlideImage(
                                        model = concert.image,
                                        contentDescription = concert.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(160.dp),
                                        contentScale = ContentScale.Crop
                                    )

                                    Column(Modifier.padding(12.dp)) {
                                        Text(
                                            text = concert.name,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = InterFontFamily,
                                            color = Color(0xFF5C0F1A),
                                            fontSize = 16.sp
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = concert.venue.name,
                                            color = Color(0xFF736D6D),
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = InterFontFamily,
                                            fontSize = 14.sp
                                        )
                                        Spacer(Modifier.height(2.dp))
                                        Text(
                                            text = concert.date.take(10).replace("-", "/"),
                                            color = Color.Gray,
                                            fontSize = 13.sp
                                        )
                                    }
                                }

                            }
                        }
                    }
                }

            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RightSideDrawer(
    drawerState: DrawerState,
    drawerContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit
) {
    BoxWithConstraints {
        Row(modifier = Modifier.fillMaxSize()) {

            Box(Modifier.weight(1f)) { content() }

            // Drawer animado desde la derecha
            AnimatedVisibility(
                visible = drawerState.isOpen,
                enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
                exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth })
            ) {
                Surface(
                    tonalElevation = 8.dp,
                    shadowElevation = 12.dp,
                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
                    color = Color.White,
                    modifier = Modifier
                        .width(260.dp)
                        .fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = drawerContent
                    )
                }
            }
        }
    }
}
