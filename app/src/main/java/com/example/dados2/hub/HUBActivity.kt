package com.example.dados2.hub

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        lanzarPigGame()
    }
    private fun lanzarPigGame() {
        binding.imagePig.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}