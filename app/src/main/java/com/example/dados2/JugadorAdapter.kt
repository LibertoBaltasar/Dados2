package com.example.dados2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class JugadorAdapter(
    private val jugadores: List<String>,
) : RecyclerView.Adapter<JugadorAdapter.JugadorViewHolder>() {

    inner class JugadorViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nombreTextView: TextView = view.findViewById(R.id.nombreTextView)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): JugadorViewHolder {
        // Crea una nueva vista, que define la interfaz de usuario (UI) del elemento de la lista
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_jugador, viewGroup, false)

        return JugadorViewHolder(view)
    }

    override fun onBindViewHolder(holder: JugadorViewHolder, position: Int) {
        holder.nombreTextView.text = jugadores[position]

        // Configurar el clic en el elemento
        holder.itemView.setOnClickListener {
            //¿QUE PUEDE SER ESO DEL CONTEXT? ==> PRÓXIMOS CAPÍTULOS
            val context = holder.itemView.context
            val text = "Elemento pulsado: ${jugadores[position]}"
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = jugadores.size
}