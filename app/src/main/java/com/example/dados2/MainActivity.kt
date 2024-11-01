package com.example.dados2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dados2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gestorSpinner()
    }

    private fun gestorSpinner() {
        val listaJugadores = arrayOf(
            "Selecciona la cantidad de jugadores", "2 Jugadores", "3 Jugadores", "4 Jugadores")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaJugadores)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.selectorJugadores.adapter = adapter
        binding.selectorJugadores.setSelection(0, false)
        binding.selectorJugadores.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String
                val numRondas=binding.seleccionRondas.text.toString().toIntOrNull() ?: 5
                if (position > 0) {
                    Toast.makeText(this@MainActivity, "Has seleccionado $numRondas rondas y ${position+1} jugadores", Toast.LENGTH_SHORT).show()
                    val intentMain = Intent(this@MainActivity, SeleccionNombreActivity::class.java)
                    intentMain.putExtra("numJugadores", position+1)
                    intentMain.putExtra("numRondas", numRondas)
                    startActivity(intentMain)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(
                    this@MainActivity, "Selecciona la cantidad de jugadores", Toast.LENGTH_SHORT).show()

            }
        }
    }
}