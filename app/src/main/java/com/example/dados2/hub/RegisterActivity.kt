package com.example.dados2.hub

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dados2.R
import com.example.dados2.databinding.ActivityRegisterBinding
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.confirmarButton.setOnClickListener {
            if(controlDatos()) {
                registrar()
            }
        }
    }

    private fun controlDatos(): Boolean {
        var nombreValido = false
        var passwordValido = false
        var fechaNacimientoValida = false
        var aceptacionValido = false
        var noExiste =false
        nombreValido=controlNombre()
        passwordValido=controlPassword()
        fechaNacimientoValida=controlFechaNacimiento()
        aceptacionValido=controlAceptacion()
        noExiste=controlExiste()
        return if (nombreValido && passwordValido && fechaNacimientoValida && aceptacionValido&&noExiste) {
            true
        }else{
            false
        }

    }

    private fun controlExiste(): Boolean {
        //TODO: Comprobar que el usuario no existe en la base de datos
        return true
    }

    private fun controlAceptacion(): Boolean {
        return binding.checkBoxAceptacion.isChecked

    }

    private fun controlFechaNacimiento(): Boolean {
        //TODO: Comprobar que la fecha de nacimiento es válida
        return true
    }



    private fun controlPassword(): Boolean {
        val password = binding.passwordIntroducido.text.toString()
        if (password.length in 4..10 && password.any { it.isDigit() }) {
            return true
        }
        return false
    }

    private fun controlNombre(): Boolean {
        if(binding.nombreIntroducido.text.toString().length>=4&&binding.nombreIntroducido.text.toString().length<=10) {
            return true
        }
        return  false
    }


    private fun registrar() {
        var user=User(0,binding.nombreIntroducido.text.toString(),binding.passwordIntroducido.text.toString(),binding.editTextDate.text.toString())
        Toast.makeText(this,user.firstName +user.password+user.fechaNacimiento, Toast.LENGTH_SHORT)
        //TODO: Registrar el usuario en la base de datos
        //TODO:mandar a siguiente pantalla
        val intent = Intent(this, HUBActivity::class.java)
        startActivity(intent)
    }
}