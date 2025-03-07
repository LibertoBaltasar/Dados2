package com.example.dados2.videoplayer

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dados2.camara.obtenerArchivos
import com.example.dados2.videoplayer.ui.theme.Dados2Theme

class VideoPlayerSeleccionActivity : ComponentActivity() {
    private val usuario by lazy { intent.getStringExtra("usuario") ?: "usuario" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Dados2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VideoPlayerSeleccion(modifier = Modifier.padding(innerPadding), this, usuario)
                }
            }
        }
    }
}

@Composable
fun VideoPlayerSeleccion(modifier: Modifier = Modifier, contexto: Context, usuario: String) {
    val archivos = remember { mutableStateListOf<Uri>().apply { addAll(obtenerVideosGrabados(contexto, usuario)) } }
    var videos = remember { obtenerVideosGrabados(contexto, usuario) }
    var mostrarDialogo by rememberSaveable { mutableStateOf(false) }
    var archivoAEliminar by rememberSaveable { mutableStateOf<Uri?>(null) }
    var videoTrabajo by remember { mutableStateOf(Uri.EMPTY) }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        items(videos) { video ->
            Text(
                text = video.toString(),
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        archivoAEliminar=video
                         videoTrabajo= video
                        mostrarDialogo=true
                    }
            )
        }
    }
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text(text = "Eliminar o Reproducir Archivo") },
            text = { Text(text = "¿Qué quieres hacer con ${archivoAEliminar?.lastPathSegment}?") },
            confirmButton = {
                Button(onClick = {
                    archivoAEliminar?.let {
                        contexto.contentResolver.delete(it, null, null)
                        archivos.remove(it)
                    }
                    mostrarDialogo = false
                    archivoAEliminar = null
                    videos= obtenerVideosGrabados(contexto,usuario)
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = {
                    val intent = Intent(contexto, VideoPlayerActivity::class.java).apply {
                        putExtra("videoUri", videoTrabajo.toString())
                    }
                    contexto.startActivity(intent)
                    mostrarDialogo = false
                    archivoAEliminar = null}) {
                    Text("Reproducir")
                }
            }
        )
    }
}

fun obtenerVideosGrabados(contexto: Context, usuario: String): List<Uri> {
    val coleccionVideos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }

    val proyeccion = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME
    )

    val seleccion = "${MediaStore.Video.Media.RELATIVE_PATH} LIKE ?"
    val argumentosSeleccion = arrayOf("Pictures/CameraX/$usuario%")

    val orden = "${MediaStore.Video.Media.DATE_ADDED} DESC"

    val consulta = contexto.contentResolver.query(
        coleccionVideos,
        proyeccion,
        seleccion,
        argumentosSeleccion,
        orden
    )

    val uris = mutableListOf<Uri>()
    consulta?.use { cursor ->
        val columnaId = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(columnaId)
            val uriContenido = ContentUris.withAppendedId(coleccionVideos, id)
            uris.add(uriContenido)
            Log.d("VideoPlayerActivity", "Video encontrado: $uriContenido")
        }
    }

    if (uris.isEmpty()) {
        Log.d("VideoPlayerActivity", "No hay video de: $usuario")
    }

    return uris
}