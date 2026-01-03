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
        puntosText.text = "Puntos  $puntosTotales"

        // Restaurar estado de los checkboxes
        checkBoxFruta.isChecked = sharedPref.getBoolean("checkBoxFruta", false)
        checkBoxPaseo.isChecked = sharedPref.getBoolean("checkBoxPaseo", false)
        checkBoxSiesta.isChecked = sharedPref.getBoolean("checkBoxSiesta", false)

        // Restaurar estado de ánimo del día actual
        val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        when (sharedPref.getString("estado_$fechaHoy", "")) {
            "feliz" -> mostrarEstado(R.drawable.feliz, "¡¡Qué bien verte feliz! Sigue haciendo lo que te hace sonreír!")
            "triste" -> mostrarEstado(R.drawable.triste, "Está bien sentirse triste a veces. Respira hondo y date un momento para ti")
            "enfadado" -> mostrarEstado(R.drawable.enfadado, "Calmarse ayuda. Prueba dar un paseo o escuchar música para relajarte")
            "normal" -> mostrarEstado(R.drawable.normal, "Todo en calma hoy. Aprovecha para hacer algo que te guste o te motive")
        }

        // Click listeners para CardViews de estado de ánimo
        felizCard.setOnClickListener { seleccionarEstado("feliz", R.drawable.feliz, "¡¡Qué bien verte feliz! Sigue haciendo lo que te hace sonreír!") }
        tristeCard.setOnClickListener { seleccionarEstado("triste", R.drawable.triste, "Está bien sentirse triste a veces. Respira hondo y date un momento para ti") }
        enfadadoCard.setOnClickListener { seleccionarEstado("enfadado", R.drawable.enfadado, "Calmarse ayuda. Prueba dar un paseo o escuchar música para relajarte") }
        normalCard.setOnClickListener { seleccionarEstado("normal", R.drawable.normal, "Todo en calma hoy. Aprovecha para hacer algo que te guste o te motive") }

        // Click listeners para CheckBoxes
        setupCheckBox(checkBoxFruta, 20, "checkBoxFruta")
        setupCheckBox(checkBoxPaseo, 10, "checkBoxPaseo")
        setupCheckBox(checkBoxSiesta, 30, "checkBoxSiesta")

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

    // Función para manejar puntos y persistencia
    private fun setupCheckBox(checkBox: CheckBox, puntos: Int, key: String) {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Sumar puntos
                puntosTotales += puntos
                puntosText.text = "Puntos  $puntosTotales"

                // Guardar en SharedPreferences
                with(sharedPref.edit()) {
                    putInt("puntosTotales", puntosTotales)
                    putBoolean(key, true)
                    apply()
                }
            } else {
                checkBox.isChecked = true
            }
        }
    }
}
