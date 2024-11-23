package com.example.dados2.hub

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dados2.basedatos.AppDatabase

import com.example.dados2.databinding.ActivityRegisterBinding

import java.util.Calendar

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gestionarDatePicker()
        binding.confirmarButton.setOnClickListener {
            if(controlDatos()) {
                finalizarRegistro()
            }
        }
    }

    private fun gestionarDatePicker() {
        binding.editTextFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    binding.editTextFecha.setText(selectedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
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
        val userDao = AppDatabase.getInstance(applicationContext).userDao()
        val count = userDao.countByName(binding.nombreIntroducido.text.toString())
        if(count>0){
            Toast.makeText(this,"El usuario ya existe", Toast.LENGTH_SHORT).show()
            return false
        }else{
            return true
        }

    }

    private fun controlAceptacion(): Boolean {
        return binding.checkBoxAceptacion.isChecked

    }

    private fun controlFechaNacimiento(): Boolean {
        val fechaNacimiento = binding.editTextFecha.text.toString()
        if (fechaNacimiento.isNotEmpty()) {
            val parts = fechaNacimiento.split("/")
            if (parts.size == 3) {
                val day = parts[0].toInt()
                val month = parts[1].toInt()
                val year = parts[2].toInt()
                val birthDate = Calendar.getInstance().apply {
                    set(year, month - 1, day)
                }
                val today = Calendar.getInstance()
                var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
                if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
                    age--
                }
                return age >= 16
            }
        }
        return false
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

    private fun finalizarRegistro() {
        var user=User(0,binding.nombreIntroducido.text.toString(),binding.passwordIntroducido.text.toString(),binding.editTextFecha.text.toString())
        Toast.makeText(this,user.firstName +user.password+user.fechaNacimiento, Toast.LENGTH_SHORT)
        registrar(user)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun registrar(user: User) {
        val userDao = AppDatabase.getInstance(applicationContext).userDao()
        userDao.insertUser(user)
    }
}