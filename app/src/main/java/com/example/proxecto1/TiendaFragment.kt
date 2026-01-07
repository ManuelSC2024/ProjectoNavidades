package com.example.proxecto1

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class TiendaFragment : Fragment() {

    private lateinit var puntosText: TextView
    private lateinit var botonComprar: Button
    private lateinit var sharedPref: SharedPreferences
    private var puntosTotales = 0
    private val precioItem = 50

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_tienda, container, false)

        puntosText = view.findViewById(R.id.PuntosTienda)
        botonComprar = view.findViewById(R.id.PuntosFruta)

        sharedPref = requireActivity().getSharedPreferences("MisPuntos", Context.MODE_PRIVATE)
        puntosTotales = sharedPref.getInt("puntosTotales", 0)

        actualizarTextoPuntos()

        botonComprar.setOnClickListener {
            comprarItem()
        }

        return view
    }

    private fun actualizarTextoPuntos() {
        puntosText.text = "Puntos $puntosTotales"
    }

    private fun comprarItem() {
        if (puntosTotales >= precioItem) {
            puntosTotales -= precioItem

            sharedPref.edit()
                .putInt("puntosTotales", puntosTotales)
                .apply()

            actualizarTextoPuntos()
            mostrarPopupFelicitaciones()

        } else {
            Toast.makeText(
                requireContext(),
                "No tienes suficientes puntos 😢",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun mostrarPopupFelicitaciones() {
        AlertDialog.Builder(requireContext())
            .setTitle("🎉 ¡Felicidades!")
            .setMessage("Has conseguido tu recompensa.\n¡Sigue cuidándote y sumando puntos!")
            .setPositiveButton("Genial") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}
