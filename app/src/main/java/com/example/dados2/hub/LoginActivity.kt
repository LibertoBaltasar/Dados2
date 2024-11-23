package com.example.dados2.hub

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dados2.R
import com.example.dados2.basedatos.AppDatabase
import com.example.dados2.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        val nombreAValidar=binding.editTextNombre.text.toString()
        val passwordAValidar=binding.editTextTextPassword.text.toString()
        if(nombreAValidar.isEmpty() || passwordAValidar.isEmpty()){
            binding.textViewError.text="Error: Debe rellenar todos los campos"
            return false
        }else if(consultarDatos(nombreAValidar,passwordAValidar)){
            return false
        }else{
            return true
        }
    }

    private fun consultarDatos(nombreAValidar: String, passwordAValidar: String): Boolean {
        val userDao = AppDatabase.getInstance(applicationContext).userDao()
        var usuarioValido = false
        CoroutineScope(Dispatchers.IO).launch {
            val user = userDao.findByName(nombreAValidar)
            runOnUiThread {
                if (user == null) {
                    binding.textViewError.text = "Error: Usuario no encontrado"
                } else if (user.password != passwordAValidar) {
                    binding.textViewError.text = "Error: Contraseña incorrecta"
                } else {
                    usuarioValido = true
                }
            }
        }
        return usuarioValido
    }
}