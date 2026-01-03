package com.example.proxecto1

import android.content.Context
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class CalendarioFragment : Fragment() {

    private lateinit var recyclerCalendario: RecyclerView
    private lateinit var mesActual: TextView

    private lateinit var botonAnterior: ImageButton
    private lateinit var botonSiguiente: ImageButton

    private val calendar = Calendar.getInstance()
    private lateinit var diasDelMes: List<Dia>
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_calendario, container, false)

        recyclerCalendario = view.findViewById(R.id.recyclerCalendario)
        mesActual = view.findViewById(R.id.mesActual)
        sharedPref = requireActivity().getSharedPreferences("MisPuntos", Context.MODE_PRIVATE)

        recyclerCalendario.layoutManager = GridLayoutManager(context, 7)

        // Inicializar botones
        botonAnterior = view.findViewById(R.id.botonAnterior)
        botonSiguiente = view.findViewById(R.id.botonSiguiente)

        // Configurar listeners de navegación
        botonAnterior.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            cargarMes(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
        }

        botonSiguiente.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            cargarMes(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
        }

        // Cargar mes actual
        cargarMes(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
        return view
    }


    private fun cargarMes(año: Int, mes: Int) {
        val cal = Calendar.getInstance()
        cal.set(año, mes, 1)
        val dias = mutableListOf<Dia>()

        val diasEnMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val primerDiaSemana = cal.get(Calendar.DAY_OF_WEEK) - 1 // domingo=0

        // --- Días del mes anterior ---
        val calAnterior = Calendar.getInstance()
        calAnterior.set(año, mes, 1)
        calAnterior.add(Calendar.MONTH, -1)
        val diasMesAnterior = calAnterior.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 0 until primerDiaSemana) {
            val diaNum = diasMesAnterior - primerDiaSemana + 1 + i
            val fecha = String.format("%04d-%02d-%02d", calAnterior.get(Calendar.YEAR),
                calAnterior.get(Calendar.MONTH) + 1, diaNum)

            val estadoResId = when(sharedPref.getString("estado_$fecha", "")) {
                "feliz" -> R.drawable.feliz
                "triste" -> R.drawable.triste
                "enfadado" -> R.drawable.enfadado
                "normal" -> R.drawable.normal
                else -> null
            }

            dias.add(Dia(fecha, diaNum, estadoResId, false))
        }

        // --- Días del mes actual ---
        for (d in 1..diasEnMes) {
            val fecha = String.format("%04d-%02d-%02d", año, mes + 1, d)
            val estadoResId = when(sharedPref.getString("estado_$fecha", "")) {
                "feliz" -> R.drawable.feliz
                "triste" -> R.drawable.triste
                "enfadado" -> R.drawable.enfadado
                "normal" -> R.drawable.normal
                else -> null
            }
            dias.add(Dia(fecha, d, estadoResId, true))
        }

        // --- Días del mes siguiente para completar la última semana ---
        val totalCeldas = ((dias.size + 6) / 7) * 7 // múltiplo de 7
        val calSiguiente = Calendar.getInstance()
        calSiguiente.set(año, mes, 1)
        calSiguiente.add(Calendar.MONTH, 1)

        var nextDay = 1
        while (dias.size < totalCeldas) {
            val fecha = String.format("%04d-%02d-%02d", calSiguiente.get(Calendar.YEAR),
                calSiguiente.get(Calendar.MONTH) + 1, nextDay)
            val estadoResId = when(sharedPref.getString("estado_$fecha", "")) {
                "feliz" -> R.drawable.feliz
                "triste" -> R.drawable.triste
                "enfadado" -> R.drawable.enfadado
                "normal" -> R.drawable.normal
                else -> null
            }

            dias.add(Dia(fecha, nextDay, estadoResId, false))
            nextDay++
        }

        diasDelMes = dias
        recyclerCalendario.adapter = CalendarioAdapter(diasDelMes)

        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        mesActual.text = sdf.format(cal.time)
    }

}
