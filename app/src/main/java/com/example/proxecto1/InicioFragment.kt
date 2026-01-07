package com.example.proxecto1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class InicioFragment : Fragment() {

    private lateinit var imagenFondo: ImageView
    private lateinit var textoFondo: TextView
    private lateinit var felizCard: CardView
    private lateinit var tristeCard: CardView
    private lateinit var enfadadoCard: CardView
    private lateinit var normalCard: CardView

    private lateinit var checkBoxFruta: CheckBox
    private lateinit var checkBoxPaseo: CheckBox
    private lateinit var checkBoxSiesta: CheckBox
    private lateinit var puntosText: TextView

    private var puntosTotales: Int = 0
    private lateinit var sharedPref: android.content.SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inicio, container, false)

        // Imagen y texto de estado de ánimo
        imagenFondo = view.findViewById(R.id.ImagenEmogiFondo)
        textoFondo = view.findViewById(R.id.TextoFondo)
        felizCard = view.findViewById(R.id.d10CardView)
        tristeCard = view.findViewById(R.id.TristeCardView)
        enfadadoCard = view.findViewById(R.id.EnfadadoCardView)
        normalCard = view.findViewById(R.id.NormalCardView)

        // CheckBoxes
        checkBoxFruta = view.findViewById(R.id.checkBoxFruta)
        checkBoxPaseo = view.findViewById(R.id.checkBoxPaseo)
        checkBoxSiesta = view.findViewById(R.id.checkBoxSiesta)

        // Texto de puntos
        puntosText = view.findViewById(R.id.PuntosTotales)

        // SharedPreferences
        sharedPref = requireActivity().getSharedPreferences("MisPuntos", Context.MODE_PRIVATE)
        puntosTotales = sharedPref.getInt("puntosTotales", 0)
        // Usamos string resource con formato
        puntosText.text = getString(R.string.puntos, puntosTotales)

        // Restaurar estado de los checkboxes
        val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        checkBoxFruta.isChecked = sharedPref.getBoolean("checkBoxFruta_$fechaHoy", false)
        checkBoxPaseo.isChecked = sharedPref.getBoolean("checkBoxPaseo_$fechaHoy", false)
        checkBoxSiesta.isChecked = sharedPref.getBoolean("checkBoxSiesta_$fechaHoy", false)

        // Restaurar estado de ánimo del día actual usando strings.xml
        when (sharedPref.getString("estado_$fechaHoy", "")) {
            "feliz" -> mostrarEstado(R.drawable.feliz, getString(R.string.feliz_bien))
            "triste" -> mostrarEstado(R.drawable.triste, getString(R.string.triste_texto))
            "enfadado" -> mostrarEstado(R.drawable.enfadado, getString(R.string.enfadado_texto))
            "normal" -> mostrarEstado(R.drawable.normal, getString(R.string.normal_texto))
        }

        // Click listeners para CardViews de estado de ánimo
        felizCard.setOnClickListener {
            seleccionarEstado("feliz", R.drawable.feliz, getString(R.string.feliz_bien))
        }
        tristeCard.setOnClickListener {
            seleccionarEstado("triste", R.drawable.triste, getString(R.string.triste_texto))
        }
        enfadadoCard.setOnClickListener {
            seleccionarEstado("enfadado", R.drawable.enfadado, getString(R.string.enfadado_texto))
        }
        normalCard.setOnClickListener {
            seleccionarEstado("normal", R.drawable.normal, getString(R.string.normal_texto))
        }

        // Click listeners para CheckBoxes
        marcado(checkBoxFruta, 20, "checkBoxFruta")
        marcado(checkBoxPaseo, 10, "checkBoxPaseo")
        marcado(checkBoxSiesta, 30, "checkBoxSiesta")

        return view
    }

    private fun mostrarEstado(drawableRes: Int, texto: String) {
        imagenFondo.setImageResource(drawableRes)
        textoFondo.text = texto
    }

    private fun seleccionarEstado(clave: String, drawableRes: Int, texto: String) {
        mostrarEstado(drawableRes, texto)
        val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        with(sharedPref.edit()) {
            putString("estado_$fechaHoy", clave)
            apply()
        }
    }

    private fun marcado(checkBox: CheckBox, puntos: Int, key: String) {
        val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val marcado = sharedPref.getBoolean("${key}_$fechaHoy", false)
        checkBox.setOnCheckedChangeListener(null)
        checkBox.isChecked = marcado
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (!marcado) {
                    puntosTotales += puntos
                    puntosText.text = getString(R.string.puntos, puntosTotales)
                    with(sharedPref.edit()) {
                        putInt("puntosTotales", puntosTotales)
                        putBoolean("${key}_$fechaHoy", true)
                        apply()
                    }
                }
            } else {
                checkBox.isChecked = true
            }
        }
    }
}
