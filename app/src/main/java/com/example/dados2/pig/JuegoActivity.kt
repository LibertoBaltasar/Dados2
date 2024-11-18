package com.example.dados2.pig

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.dados2.R
import com.example.dados2.databinding.ActivityJuegoBinding
import kotlin.random.Random

class JuegoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJuegoBinding
    private var numJugadores: Int =2
    private var numRondas: Int =5
    private var rondaActual: Int =1
    private var jugadorActual: Int =1
    private var puntosActuales: Int =0
    private var listaJugadoresFinal: MutableList<Jugador> = mutableListOf()
    val listaJugadores: MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)
        binding = ActivityJuegoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        numJugadores=intent.getIntExtra("numJugadores",2)
        numRondas=intent.getIntExtra("numRondas",5)
        binding.rondaFinal.text = numRondas.toString()
        binding.pasar.visibility= View.INVISIBLE
        gestionarJugadores()
        gestionarPartida()
    }

    private fun ocultarJugadores() {
        binding.casilleroJ4.visibility = View.INVISIBLE
        binding.casilleroJ3.visibility = View.INVISIBLE
    }

    private fun gestionarJugadores(){
        ocultarJugadores()
        listaJugadores.add(intent.getStringExtra("nombreJ1") ?:"J1")
        listaJugadores.add(intent.getStringExtra("nombreJ2") ?:"J2")
        if(numJugadores==3) {
            listaJugadores.add(intent.getStringExtra("nombreJ3") ?: "J3")
            binding.casilleroJ3.visibility = View.VISIBLE
        }else if(numJugadores==4) {
            listaJugadores.add(intent.getStringExtra("nombreJ3") ?: "J3")
            binding.casilleroJ3.visibility = View.VISIBLE
            listaJugadores.add(intent.getStringExtra("nombreJ4") ?:"J4")
            binding.casilleroJ4.visibility = View.VISIBLE
        }
        randomizarOrdenJugadores()
    }

    private fun randomizarOrdenJugadores(){
        var contador:Int=1
        do {
            var num=generarNumAleatorio(0,listaJugadores.size)
            val cellId = resources.getIdentifier("nomJ${contador}", "id", packageName)
            val cellView = binding.casillero.findViewById<TextView>(cellId)
            cellView?.text = listaJugadores.get(num)

            listaJugadores.removeAt(num)
            contador++
        }while(listaJugadores.isNotEmpty())
    }

    private fun gestionarPartida(){
        binding.jugadorActual.text = jugadorActual.toString()
        binding.rondaActual.text = rondaActual.toString()
        binding.Play.setOnClickListener {
            val numero:Int = tirarDado()
            seleccionarDado(numero)
            if (numero==1){
                Toast.makeText(this@JuegoActivity,"Lo siento, turno del siguiente", Toast.LENGTH_SHORT).show()
                pasarTurno(true)
            }else{
                puntosActuales+=numero
                Toast.makeText(this@JuegoActivity,"Felicidades, has acumulado ${puntosActuales}", Toast.LENGTH_SHORT).show()
                binding.puntuacionActual.text = puntosActuales.toString()
                binding.pasar.visibility= View.VISIBLE
            }
        }
        binding.pasar.setOnClickListener {
            pasarTurno(false)
        }
    }

    private fun pasarTurno(fallo:Boolean){
        if (fallo){
            puntosActuales=0
        }
        actualizarPuntuacionFinal()
        binding.puntuacionActual.text = "0"
        binding.pasar.visibility= View.INVISIBLE
        if (jugadorActual<numJugadores){
            jugadorActual++
        }else{
            jugadorActual=1
            rondaActual++
            if (rondaActual>numRondas){
                finalizarPartida()
                guardarJugadores()
                irAActivityFinal()
            }
        }

        binding.jugadorActual.text = jugadorActual.toString()
        binding.rondaActual.text = rondaActual.toString()
        puntosActuales=0

    }

    private fun actualizarPuntuacionFinal(){
        val cellId = resources.getIdentifier("puntuacionFinalJ${jugadorActual}", "id", packageName)
        val cellView = binding.casillero.findViewById<TextView>(cellId)
        cellView?.text = ((cellView?.text.toString().toIntOrNull()?:0)+puntosActuales).toString()
    }

    private fun finalizarPartida() {
        Toast.makeText(this@JuegoActivity, "Has terminado la partida", Toast.LENGTH_SHORT).show()
        binding.imagenDado.visibility= View.INVISIBLE
        binding.pasar.visibility= View.INVISIBLE
        binding.Play.visibility = View.INVISIBLE
        binding.indicadorEstado.visibility = View.INVISIBLE
        var ganador=seleccionarGanador()
        if (ganador.length==1) {
            binding.mensajeVictoria.text = "Has ganado la partida jugador: ${seleccionarGanador()}"
        }else{
            //En caso de empate considero que han ganado todos los jugadores que han empatado con la máxima puntuación
            binding.mensajeVictoria.text = "Han ganado la partida los jugadores: ${seleccionarGanador()}"
        }
        binding.mensajeVictoria.visibility= View.VISIBLE
    }

    private fun seleccionarGanador(): String {
        val puntuaciones = mutableListOf<Int>(
            binding.puntuacionFinalJ1.text.toString().toIntOrNull() ?: 0,
            binding.puntuacionFinalJ2.text.toString().toIntOrNull() ?: 0,
            binding.puntuacionFinalJ3.text.toString().toIntOrNull() ?: -1,
            binding.puntuacionFinalJ4.text.toString().toIntOrNull() ?: -1
        )
        var ganador=verJugadorMaxPuntuacion(puntuaciones)
        var ganadores = verJugadorMaxPuntuacion(puntuaciones).toString()
        var maxPuntuacion = puntuaciones[verJugadorMaxPuntuacion(puntuaciones)-1]
        for (i in 1..<puntuaciones.size) {
            if(puntuaciones[i] == maxPuntuacion&& i!=ganador-1){
                ganadores += ", ${(i + 1)}"
            }
        }
        return ganadores
    }

    private fun verJugadorMaxPuntuacion(puntuaciones: List<Int>): Int {
        var ganador = 0
        var maxPuntuacion = puntuaciones[0]

        for (i in 1..3) {
            if (puntuaciones[i] > maxPuntuacion) {
                maxPuntuacion = puntuaciones[i]
                ganador = i
            }
        }

        return ganador + 1
    }

    private fun seleccionarDado(opcion: Int) {
        when (opcion) {
            1 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.dado1
            ))
            2 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.dado2
            ))
            3 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.dado3
            ))
            4 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.dado4
            ))
            5 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.dado5
            ))
            6 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.dado6
            ))
        }
    }

    private fun tirarDado(): Int {
        return generarNumAleatorio(1,7)
    }

    private fun generarNumAleatorio(inicio: Int,fin:Int): Int {
        return Random.nextInt(inicio, fin)
    }

    private fun guardarJugadores(){
        for (i in 1..numJugadores){
            val cellId = resources.getIdentifier("nomJ${i}", "id", packageName)
            val cellView = binding.casillero.findViewById<TextView>(cellId)
            val cellIdPun = resources.getIdentifier("puntuacionFinalJ${i}", "id", packageName)
            val cellViewPun = binding.casillero.findViewById<TextView>(cellIdPun)
            val jugador = Jugador(cellView?.text.toString(),cellViewPun?.text.toString().toIntOrNull()?:0)
            listaJugadoresFinal.add(jugador)
        }
    }

    private fun irAActivityFinal() {
        val intent = Intent(this, ActivityFinal::class.java)
        intent.putExtra("listaJugadoresFinal", ArrayList(listaJugadoresFinal))
        startActivity(intent)
    }

}