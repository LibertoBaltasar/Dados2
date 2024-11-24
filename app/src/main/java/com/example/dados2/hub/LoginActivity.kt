package com.example.dados2.hub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dados2.R
import com.example.dados2.basedatos.AppDatabase
import com.example.dados2.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val sharedPref by lazy { getSharedPreferences("loginPrefs", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gestionarLogin()
        cargarCredenciales()
        setupListeners()
    }

    private fun gestionarLogin() {
        binding.buttonLogin.setOnClickListener {
            if (validarDatos()) {
                if (binding.checkBoxRecordar.isChecked) {
                    guardarCredenciales()
                } else {
                    borrarCredenciales()
                }
                // Proceed with login
            }
        }
    }

    private fun validarDatos(): Boolean {
        val nombreAValidar = binding.editTextNombre.text.toString()
        val passwordAValidar = binding.editTextTextPassword.text.toString()
        if (nombreAValidar.isEmpty() || passwordAValidar.isEmpty()) {
            binding.textViewError.text = "Error: Debe rellenar todos los campos"
            return false
        } else if (consultarDatos(nombreAValidar, passwordAValidar)) {
            return false
        } else {
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
                    val intent = Intent(this@LoginActivity, HUBActivity::class.java)
                    intent.putExtra("usuario", nombreAValidar)
                    startActivity(intent)
                }
            }
        }
        return usuarioValido
    }

    private fun cargarCredenciales() {
        val nombre = sharedPref.getString("username", "")
        val password = sharedPref.getString("password", "")
        binding.editTextNombre.setText(nombre)
        binding.editTextTextPassword.setText(password)
        binding.checkBoxRecordar.isChecked = nombre!!.isNotEmpty() && password!!.isNotEmpty()
    }

    private fun guardarCredenciales() {
        with(sharedPref.edit()) {
            putString("username", binding.editTextNombre.text.toString())
            putString("password", binding.editTextTextPassword.text.toString())
            apply()
        }
    }

    private fun borrarCredenciales() {
        with(sharedPref.edit()) {
            remove("username")
            remove("password")
            apply()
        }
    }

    private fun setupListeners() {
        binding.editTextNombre.setOnFocusChangeListener { _, _ -> binding.checkBoxRecordar.isChecked = false }
        binding.editTextTextPassword.setOnFocusChangeListener { _, _ -> binding.checkBoxRecordar.isChecked = false }
    }
}