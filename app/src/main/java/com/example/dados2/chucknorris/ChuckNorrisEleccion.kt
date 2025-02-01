//package com.example.dados2.chucknorris
//
//import android.R
//import android.os.Bundle
//import android.view.View
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import com.example.dados2.databinding.ActivityChuckNorrisEleccionBinding
//import kotlinx.coroutines.launch
//
//class ChuckNorrisEleccion : AppCompatActivity() {
//    private lateinit var binding: ActivityChuckNorrisEleccionBinding
//    private lateinit var api: ChuckNorrisApi
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityChuckNorrisEleccionBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        api = ChuckNorrisRetrofitClient.instance.create(ChuckNorrisApi::class.java)
//
//        gestionarSpinner()
//    }
//
//    private fun gestionarSpinner() {
//        lifecycleScope.launch {
//            try {
//                val categories = arrayOf(
//                    "Elige la categoría", "animal", "career", "celebrity", "dev", "explicit", "fashion", "food",
//                    "history", "money", "movie", "music", "political", "religion", "science",
//                    "sport", "travel"
//                )
//                val adapter = ArrayAdapter(this@ChuckNorrisEleccion, R.layout.simple_spinner_item, categories)
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                binding.spinner.adapter = adapter
//                binding.spinner.setSelection(0, false)
//                binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                        if (position > 0) {
//                            val category = parent.getItemAtPosition(position) as String
//                            buscarBroma(category)
//                        }
//                    }
//
//                    override fun onNothingSelected(parent: AdapterView<*>) {
//                        // No action needed
//                    }
//                }
//            } catch (e: Exception) {
//                binding.textView3.text = "Error: ${e.message}"
//            }
//        }
//    }
//
//    private fun buscarBroma(category: String) {
//        lifecycleScope.launch {
//            try {
//                val chiste = api.getJokeByCategory(category)
//                binding.textView3.text = chiste.value
//            } catch (e: Exception) {
//                binding.textView3.text = "Error: ${e.message}"
//            }
//        }
//    }
//}