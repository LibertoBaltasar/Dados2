package com.example.dados2.camara

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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dados2.camara.ui.theme.Dados2Theme

class GalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cargarArchivos()
    }

    private fun cargarArchivos() {
        setContent {
            Dados2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Galeria(modifier = Modifier.padding(innerPadding), this, intent.getStringExtra("usuario") ?: "")
                }
            }
        }
    }

    private fun navegarAtras() {
        val intent = Intent()
        setResult(RESULT_CANCELED, intent)
        finish()
    }
}

@Composable
fun Galeria(modifier: Modifier = Modifier, contexto: Context, usuario: String) {
    val archivos = remember { mutableStateListOf<Uri>().apply { addAll(obtenerArchivos(contexto, usuario)) } }
    var mostrarDialogo by rememberSaveable { mutableStateOf(false) }
    var archivoAEliminar by rememberSaveable { mutableStateOf<Uri?>(null) }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(archivos) { archivo ->
            Text(
                text = archivo.toString(),
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        archivoAEliminar = archivo
                        mostrarDialogo = true
                    }
            )
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text(text = "Eliminar Archivo") },
            text = { Text(text = "¿Seguro que quieres eliminar el archivo ${archivoAEliminar?.lastPathSegment}?") },
            confirmButton = {
                Button(onClick = {
                    archivoAEliminar?.let {
                        contexto.contentResolver.delete(it, null, null)
                        archivos.remove(it)
                    }
                    mostrarDialogo = false
                    archivoAEliminar = null
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = { mostrarDialogo = false
                    archivoAEliminar = null}) {
                    Text("Cancelar")
                }
            }
        )
    }
}

fun obtenerArchivos(contexto: Context, usuario: String): List<Uri> {
    val coleccionImagenes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val coleccionVideos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }

    val proyeccion = arrayOf(
        MediaStore.MediaColumns._ID,
        MediaStore.MediaColumns.DISPLAY_NAME
    )

    val seleccion = "${MediaStore.MediaColumns.RELATIVE_PATH} LIKE ?"
    val argumentosSeleccion = arrayOf("Pictures/CameraX/$usuario%")

    val orden = "${MediaStore.MediaColumns.DATE_ADDED} DESC"

    val urisImagenes = mutableListOf<Uri>()
    val urisVideos = mutableListOf<Uri>()

    val consultaImagenes = contexto.contentResolver.query(
        coleccionImagenes,
        proyeccion,
        seleccion,
        argumentosSeleccion,
        orden
    )

    consultaImagenes?.use { cursor ->
        val columnaId = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(columnaId)
            val uriContenido = ContentUris.withAppendedId(coleccionImagenes, id)
            urisImagenes.add(uriContenido)
            Log.d("GalleryActivity", "Imagen Encontrada: $uriContenido")
        }
    }

    val consultaVideos = contexto.contentResolver.query(
        coleccionVideos,
        proyeccion,
        seleccion,
        argumentosSeleccion,
        orden
    )

    consultaVideos?.use { cursor ->
        val columnaId = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(columnaId)
            val uriContenido = ContentUris.withAppendedId(coleccionVideos, id)
            urisVideos.add(uriContenido)
            Log.d("GalleryActivity", "Video Encontrado: $uriContenido")
        }
    }

    val uris = urisImagenes + urisVideos

    if (uris.isEmpty()) {
        Log.d("GalleryActivity", "No hay archivos de $usuario")
    }

    return uris
}