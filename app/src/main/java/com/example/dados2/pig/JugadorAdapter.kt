package com.example.dados2.pig

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dados2.R

class JugadorAdapter(
    private val jugadores: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<JugadorAdapter.JugadorViewHolder>() {

    inner class JugadorViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nombreTextView: TextView = view.findViewById(R.id.nombreTextView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): JugadorViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_jugador, viewGroup, false)
        return JugadorViewHolder(view)
    }

    override fun onBindViewHolder(holder: JugadorViewHolder, position: Int) {
        holder.nombreTextView.text = jugadores[position]
        holder.itemView.setOnClickListener {
            onClick(jugadores[position])
        }
    }

    override fun getItemCount(): Int = jugadores.size
}