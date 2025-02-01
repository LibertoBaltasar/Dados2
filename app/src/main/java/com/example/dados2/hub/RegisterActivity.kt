package com.example.dados2.hub
import android.app.DatePickerDialog
import android.content.Intent

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dados2.basedatos.AppDatabase
import com.example.dados2.databinding.ActivityRegisterBinding
import kotlinx.coroutines.*
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gestionarDatePicker()
        binding.confirmarButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if (controlDatos()) {
                    finalizarRegistro()
                }
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

    private suspend fun controlDatos(): Boolean {
        val nombreValido = controlNombre()
        val passwordValido = controlPassword()
        val fechaNacimientoValida = controlFechaNacimiento()
        val aceptacionValido = controlAceptacion()
        val noExiste = controlExiste()

        return nombreValido && passwordValido && fechaNacimientoValida && aceptacionValido && noExiste
    }

    private suspend fun controlExiste(): Boolean {
        val userDao = AppDatabase.getInstance(applicationContext).userDao()
        val count = withContext(Dispatchers.IO) {
            userDao.countByName(binding.nombreIntroducido.text.toString())
        }
        return if (count > 0) {
            Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show()
            binding.textNombre.text = "Error: El usuario ya existe"
            false
        } else {
            true
        }
    }

    private fun controlAceptacion(): Boolean {
        if (binding.checkBoxAceptacion.isChecked) {
            binding.textViewAceptacion.text = ""
            return true
        } else {
            binding.textViewAceptacion.text = "Error: Debe aceptar las condiciones"
        }
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
                if (age >= 16) {
                    return true
                } else {
                    binding.textFecha.text = "Error: Debes tener más de 16 años"
                    return false
                }
            }
        } else {
            binding.textFecha.text = "Error: El campo de fecha no puede estar vacío"
        }
        return false
    }

    private fun controlPassword(): Boolean {
        val password = binding.passwordIntroducido.text.toString()
        if (password.length in 4..10 && password.any { it.isDigit() }) {
            binding.textPassword.text = "Contraseña"
            return true
        }
        binding.textPassword.text = "Error: La contraseña debe tener entre 4 y 10 caracteres y al menos un número"
        return false
    }

    private fun controlNombre(): Boolean {
        if (binding.nombreIntroducido.text.toString().length in 4..10) {
            binding.textNombre.text = "Nombre"
            return true
        }
        binding.textNombre.text = "Error: El nombre debe tener entre 4 y 10 caracteres"
        return false
    }

    private fun finalizarRegistro() {
        val user = User(0, binding.nombreIntroducido.text.toString(), binding.passwordIntroducido.text.toString(), binding.editTextFecha.text.toString())
        Toast.makeText(this, "${user.firstName} ${user.password} ${user.fechaNacimiento}", Toast.LENGTH_SHORT).show()
        registrar(user)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun registrar(user: User) {
        val userDao = AppDatabase.getInstance(applicationContext).userDao()
        CoroutineScope(Dispatchers.IO).launch {
            userDao.insertUser(user)
        }
    }

}