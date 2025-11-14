package com.example.gigmap_frontend_sprint1.view.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gigmap_frontend_sprint1.model.m1au.M1AUConcertCard
import com.example.gigmap_frontend_sprint1.viewmodel.ChatMessage
import com.example.gigmap_frontend_sprint1.viewmodel.M1AUChatViewModel
import kotlinx.coroutines.launch

@Composable
fun M1AUChatModal(
    isOpen: MutableState<Boolean>,
    viewModel: M1AUChatViewModel = viewModel(),
    onNavigateToArtist: (String?, String?) -> Unit = { _, _ -> }
) {
    val uiState = viewModel.uiState
    M1AUChatDialog(
        isOpen = isOpen.value,
        onDismiss = { isOpen.value = false },
        messages = uiState.messages,
        concerts = uiState.concerts,
        isLoading = uiState.isLoading,
        error = uiState.error,
        onSend = { message -> viewModel.sendMessage(message) },
        onErrorDismiss = { viewModel.dismissError() },
        onNavigateToArtist = onNavigateToArtist
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun M1AUChatDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    messages: List<ChatMessage>,
    concerts: List<M1AUConcertCard>,
    isLoading: Boolean,
    error: String?,
    onSend: (String) -> Unit,
    onErrorDismiss: () -> Unit,
    onNavigateToArtist: (String?, String?) -> Unit
) {
    if (!isOpen) return

    var input = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {},
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chat con M1AU",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {
                    items(messages) { message ->
                        ChatBubble(message = message)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (concerts.isNotEmpty()) {
                    Text(
                        text = "Conciertos sugeridos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 240.dp)
                    ) {
                        items(concerts) { concert ->
                            ConcertCard(concert = concert, onNavigateToArtist = onNavigateToArtist)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    value = input.value,
                    onValueChange = { input.value = it },
                    placeholder = { Text("Pregúntale algo a M1AU...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF5F5F5)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val text = input.value.trim()
                        if (text.isNotEmpty()) {
                            scope.launch {
                                onSend(text)
                                input.value = ""
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text(text = if (isLoading) "Miau pensando..." else "Enviar")
                }

                error?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val background = if (message.isUser) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
    val alignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Text(
            text = message.text,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(background)
                .padding(12.dp),
            color = Color.Black,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun ConcertCard(
    concert: M1AUConcertCard,
    onNavigateToArtist: (String?, String?) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = concert.imageUrl,
                contentDescription = concert.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = concert.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            concert.formattedDate?.let {
                Text(text = it, fontSize = 12.sp, color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (!concert.description.isNullOrBlank()) {
                Text(
                    text = concert.description,
                    fontSize = 12.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                concert.artistName?.let {
                    Text(text = it, fontWeight = FontWeight.Medium, fontSize = 12.sp)
                }
                concert.artistUsername?.let {
                    Text(text = " (@$it)", fontSize = 12.sp)
                }
            }

            concert.venueName?.let {
                Text(text = "Venue: $it", fontSize = 12.sp)
            }
            concert.venueAddress?.let {
                Text(text = it, fontSize = 11.sp, color = Color.Gray)
            }

            concert.platformName?.let {
                Text(text = "Plataforma: $it", fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { onNavigateToArtist(concert.artistId, concert.artistUsername) }) {
                Text(text = "Ver perfil del artista")
            }
        }
    }
}
