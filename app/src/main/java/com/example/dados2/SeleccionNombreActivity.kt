package com.example.dados2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dados2.databinding.ActivityMainBinding
import com.example.dados2.databinding.ActivitySeleccionNombreBinding

class SeleccionNombreActivity : AppCompatActivity() {
    private val listaJugadores = arrayOf("Aitor Tilla", "Ana Conda", "Armando Broncas", "Aurora Boreal", "Bartolo Mesa", "Carmen Mente", "Dolores Delirio", "Elsa Pato", "Enrique Cido", "Esteban Dido", "Elba Lazo", "Fermin Tado", "Lola Mento", "Luz Cuesta", "Margarita Flores", "Paco Tilla", "Pere Gil", "Pío Nono", "Salvador Tumbado", "Zoila Vaca")
    private lateinit var binding: ActivitySeleccionNombreBinding
    private var contador = 0
    private lateinit var intentSeleccionNombre : Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionNombreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intentSeleccionNombre = Intent(this, JuegoActivity::class.java)

        val numJugadores = intent.getIntExtra("numJugadores", 2)
        val numRondas= intent.getIntExtra("numRondas", 5)
        intentSeleccionNombre.putExtra("numJugadores", numJugadores.toString())
        intentSeleccionNombre.putExtra("numRondas", numRondas.toString())

        val jugadorAdapter = JugadorAdapter(listaJugadores.toList())

        binding.recyclerJ1.adapter = jugadorAdapter
        binding.recyclerJ1.layoutManager = LinearLayoutManager(this)

        binding.recyclerJ2.adapter = jugadorAdapter
        binding.recyclerJ2.layoutManager = LinearLayoutManager(this)

        binding.recyclerJ3.adapter = jugadorAdapter
        binding.recyclerJ3.layoutManager = LinearLayoutManager(this)

        binding.recyclerJ4.adapter = jugadorAdapter
        binding.recyclerJ4.layoutManager = LinearLayoutManager(this)
        gestionarVisibilidadRecycler(numJugadores)
//        gestionarRecycler(numJugadores,numRondas)
    }
    fun gestionarVisibilidadRecycler(numJugadores: Int) {
        when (numJugadores) {
            2 -> {
                binding.recyclerJ1.visibility = View.VISIBLE
                binding.recyclerJ2.visibility = View.VISIBLE
                binding.recyclerJ3.visibility = View.INVISIBLE
                binding.recyclerJ4.visibility = View.INVISIBLE
            }
            3 -> {
                binding.recyclerJ1.visibility = View.VISIBLE
                binding.recyclerJ2.visibility = View.VISIBLE
                binding.recyclerJ3.visibility = View.VISIBLE
                binding.recyclerJ4.visibility = View.INVISIBLE
            }
            4 -> {
                binding.recyclerJ1.visibility = View.VISIBLE
                binding.recyclerJ2.visibility = View.VISIBLE
                binding.recyclerJ3.visibility = View.VISIBLE
                binding.recyclerJ4.visibility = View.VISIBLE
            }
        }

    }
    private fun agregarJugador(nombreJugador: String, numJugadores: Int) {
        contador++
        intentSeleccionNombre.putExtra("jugador$contador", nombreJugador)

        if (contador == numJugadores) {
            startActivity(intentSeleccionNombre)
        }
    }
//    fun gestionarRecycler(numJugadores: Int, numRondas:Int) {
//
//
//        binding.recyclerJ1.setOnClickListener(View.OnClickListener {
//            intentSeleccionNombre.putExtra("jugador1", binding.recyclerJ1.toString())
//            binding.recyclerJ1.visibility = View.INVISIBLE
//            contador++
//            if (contador==numJugadores) {
//                startActivity(intentSeleccionNombre)
//            }
//        })
//        binding.recyclerJ2.setOnClickListener(View.OnClickListener {
//            intentSeleccionNombre.putExtra("jugador2", binding.recyclerJ1.toString())
//            binding.recyclerJ2.visibility = View.INVISIBLE
//            contador++
//            if (contador==numJugadores) {
//                startActivity(intentSeleccionNombre)
//            }
//        })
//        binding.recyclerJ3.setOnClickListener(View.OnClickListener {
//            intentSeleccionNombre.putExtra("jugador3", binding.recyclerJ1.toString())
//            binding.recyclerJ3.visibility = View.INVISIBLE
//            contador++
//            if (contador==numJugadores) {
//                startActivity(intentSeleccionNombre)
//            }
//        })
//        binding.recyclerJ4.setOnClickListener(View.OnClickListener {
//            intentSeleccionNombre.putExtra("jugador4", binding.recyclerJ1.toString())
//            binding.recyclerJ4.visibility = View.INVISIBLE
//            contador++
//            if (contador==numJugadores) {
//                startActivity(intentSeleccionNombre)
//            }
//        })
}