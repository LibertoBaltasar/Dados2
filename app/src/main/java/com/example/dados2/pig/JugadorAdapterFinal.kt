package com.example.dados2.pig

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.dados2.R

class JugadorAdapterFinal(private val context: Context, private val jugadores: List<Jugador>) : BaseAdapter() {

    override fun getCount(): Int {
        return jugadores.size
    }

    override fun getItem(position: Int): Any {
        return jugadores[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_jugador_final, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val jugador = jugadores[position]
        viewHolder.nombreTextView.text = jugador.nombre
        viewHolder.puntosTextView.text = jugador.puntos.toString()

        return view
    }

    private class ViewHolder(view: View) {
        val nombreTextView: TextView = view.findViewById(R.id.nombreJugador)
        val puntosTextView: TextView = view.findViewById(R.id.puntosJugador)
    }
}