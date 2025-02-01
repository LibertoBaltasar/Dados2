package com.example.dados2.hub

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dados2.R
import com.example.dados2.databinding.ActivityHubactivityBinding
import com.example.dados2.pig.MainActivity

class HUBActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHubactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hubactivity)
        binding = ActivityHubactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var usuario = intent.getStringExtra("usuario")
        binding.textViewUsuario.text = usuario
        lanzarPigGame()
        lanzarCamara(usuario)
        lanzarVideoPlayer(usuario)
    }
    private fun lanzarPigGame() {
        binding.imagePig.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun lanzarCamara(usuario: String?) {
        binding.CamaraImage.setOnClickListener {
            val intent = Intent(this, com.example.dados2.camara.CamaraMenu::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }
    }
    private fun lanzarVideoPlayer(usuario: String?) {
        binding.VideoPlayerImage.setOnClickListener {
            val intent = Intent(this, com.example.dados2.videoplayer.VideoPlayerSeleccionActivity::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }
    }
}