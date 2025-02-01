package com.example.dados2.camara

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.video.VideoCapture
import androidx.core.content.ContextCompat
import com.example.dados2.databinding.ActivityCamaraBinding
import java.text.SimpleDateFormat
import java.util.*

class CamaraActivity : ComponentActivity() {
    private var capturaImagen: ImageCapture? = null
    private var capturaVideo: VideoCapture<Recorder>? = null
    private lateinit var binding: ActivityCamaraBinding
    private var estaGrabando = false
    private var grabacion: Recording? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCamaraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val futuroProveedorCamara = ProcessCameraProvider.getInstance(this)
        futuroProveedorCamara.addListener({
            val proveedorCamara = futuroProveedorCamara.get()
            val vistaPrevia = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }
            capturaImagen = ImageCapture.Builder().build()
            val grabador = Recorder.Builder().build()
            capturaVideo = VideoCapture.withOutput(grabador)
            val selectorCamara = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                proveedorCamara.unbindAll()
                proveedorCamara.bindToLifecycle(this, selectorCamara, vistaPrevia, capturaImagen, capturaVideo)
            } catch (exc: Exception) {
                Log.e(TAG, "Fallo en la vinculación del caso de uso", exc)
            }
        }, ContextCompat.getMainExecutor(this))

        binding.imageCaptureButton.setOnClickListener { tomarFoto() }
        binding.videoCaptureButton.setOnClickListener { capturarVideo() }
    }

    private fun tomarFoto() {
        val capturaImagen = capturaImagen ?: return

        val nombre = SimpleDateFormat(FORMATO_NOMBRE_ARCHIVO, Locale.US).format(System.currentTimeMillis())
        val valoresContenido = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, nombre)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX/${intent.getStringExtra("usuario")}")
            }
        }

        val opcionesSalida = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            valoresContenido
        ).build()

        capturaImagen.takePicture(
            opcionesSalida,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Error con la foto: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Foto guardada: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            }
        )
    }

    private fun capturarVideo() {
        val capturaVideo = capturaVideo ?: return

        if (estaGrabando) {
            grabacion?.stop()
            grabacion = null
            estaGrabando = false
            binding.videoCaptureButton.text = "Grabar"
            Toast.makeText(this, "Detenida la grabación", Toast.LENGTH_SHORT).show()
        } else {
            val nombre = SimpleDateFormat(FORMATO_NOMBRE_ARCHIVO, Locale.US).format(System.currentTimeMillis())
            val valoresContenido = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, nombre)
                put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    put(MediaStore.Video.Media.RELATIVE_PATH, "Pictures/CameraX/${intent.getStringExtra("usuario")}")
                }
            }

            val opcionesSalidaMediaStore = MediaStoreOutputOptions.Builder(
                contentResolver,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            ).setContentValues(valoresContenido).build()

            grabacion = capturaVideo.output
                .prepareRecording(this, opcionesSalidaMediaStore)
                .start(ContextCompat.getMainExecutor(this)) { eventoGrabacion ->
                    when (eventoGrabacion) {
                        is VideoRecordEvent.Start -> {
                            estaGrabando = true
                            binding.videoCaptureButton.text = "Grabando"
                            Toast.makeText(this, "Grabación iniciada", Toast.LENGTH_SHORT).show()
                        }
                        is VideoRecordEvent.Finalize -> {
                            if (!eventoGrabacion.hasError()) {
                                val msg = "Vídeo grabado: ${eventoGrabacion.outputResults.outputUri}"
                                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                                Log.d(TAG, msg)
                            } else {
                                Log.e(TAG, "Error en el vídeo: ${eventoGrabacion.error}")
                            }
                            estaGrabando = false
                            binding.videoCaptureButton.text = "Grabar"
                        }
                    }
                }
        }
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FORMATO_NOMBRE_ARCHIVO = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}