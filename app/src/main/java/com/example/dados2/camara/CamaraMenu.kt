package com.example.dados2.camara

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dados2.camara.ui.theme.Dados2Theme

class CamaraMenu : ComponentActivity() {
    private val REQUEST_CODE_PERMISSIONS = 10
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    private val OPTIONAL_PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val usuario = intent.getStringExtra("usuario") ?: "usuario"
        setContent {
            Dados2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CamaraMenu(usuario, modifier = Modifier.padding(innerPadding))
                }
            }
        }
        if (!allPermissionsGranted()) {
            requestPermissionsIfNeeded()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(baseContext, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionsIfNeeded() {
        val permissionsToRequest = REQUIRED_PERMISSIONS.filter { permission ->
            ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                finish()
            }
        }
    }
}

@Composable
fun CamaraMenu(usuario: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var mostrarDialogo by rememberSaveable { mutableStateOf(false) }
    var mostrarDialogoGaleria by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = {
            mostrarDialogo=true
        }) {
            Text("Cámara")
        }
        Button(onClick = {
            mostrarDialogoGaleria=true
        }) {
            Text("Galería")
        }
    }
    if(mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text(text = "Guardar Archivos") },
            text = { Text(text = "Donde quieres guardar los archivos") },
            confirmButton = {
                Button(onClick = {
                    val intent = Intent(context, CamaraActivity::class.java).apply {
                        putExtra("usuario", usuario)
                    }
                    context.startActivity(intent)
                    mostrarDialogo=false
                }) {
                    Text("Guardar en carpeta personal")
                }
            },
            dismissButton = {
                Button(onClick = {
                    val intent = Intent(context, CamaraActivity::class.java).apply {
                        putExtra("usuario", "comunes")
                    }
                    context.startActivity(intent)
                    mostrarDialogo=false
                }) {
                    Text("Guardar en carpeta pública")
                }
            }
        )
    }
    if(mostrarDialogoGaleria) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoGaleria = false },
            title = { Text(text = "Ver Archivos") },
            text = { Text(text = "Donde quieres ver los archivos") },
            confirmButton = {
                Button(onClick = {
                    val intent = Intent(context, GalleryActivity::class.java).apply {
                        putExtra("usuario", usuario)
                    }
                    context.startActivity(intent)
                    mostrarDialogoGaleria=false
                }) {
                    Text("Carpeta personal")
                }
            },
            dismissButton = {
                Button(onClick = {
                    val intent = Intent(context, GalleryActivity::class.java).apply {
                        putExtra("usuario", "comunes")
                    }
                    context.startActivity(intent)
                    mostrarDialogoGaleria=false
                }) {
                    Text("Carpeta pública")
                }
            }
        )
    }
}