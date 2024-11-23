package com.example.dados2.hub

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dados2.R
import com.example.dados2.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gestionarLogin()
    }

    private fun gestionarLogin() {
        binding.buttonLogin.setOnClickListener {
            if(validarDatos()){
                val intent = Intent(this, HUBActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun validarDatos(): Boolean {
        //TODO: Implementar validación de datos
        return true
    }
}