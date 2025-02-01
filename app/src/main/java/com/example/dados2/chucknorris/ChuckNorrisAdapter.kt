package com.example.dados2.chucknorris

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.dados2.R

class ChuckNorrisAdapter(context: Context, private val categories: Array<String>) :
    ArrayAdapter<String>(context, 0, categories) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent, R.layout.item_chuck_eleccion)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = createViewFromResource(position, convertView, parent, R.layout.item_chuck_eleccion)
        if (position == 0) {
            view.isEnabled = false
        }
        return view
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup, resource: Int): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        val textView = view.findViewById<TextView>(R.id.categoryTextView)
        textView.text = categories[position]
        return view
    }
}