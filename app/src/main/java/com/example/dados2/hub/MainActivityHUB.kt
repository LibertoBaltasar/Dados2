package com.example.dados2.hub

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dados2.R
import com.example.dados2.databinding.ActivityFinalBinding
import com.example.dados2.databinding.ActivityMainHubBinding

class MainActivityHUB : AppCompatActivity() {
    private lateinit var binding: ActivityMainHubBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_hub)
        binding = ActivityMainHubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gestionarbotones()
    }
    private fun gestionarbotones() {
        binding.loginButton.setOnClickListener {
            lanzarLogin()
        }
        binding.registerButton.setOnClickListener {
            lanzarRegister()
        }
    }
    private fun lanzarLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    private fun lanzarRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}