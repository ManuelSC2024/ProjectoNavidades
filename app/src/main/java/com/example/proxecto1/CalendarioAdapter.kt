package com.example.proxecto1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarioAdapter(
    private val dias: List<Dia>
) : RecyclerView.Adapter<CalendarioAdapter.DiaViewHolder>() {

    inner class DiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDia: TextView = itemView.findViewById(R.id.textDia)
        val imagenEstado: ImageView = itemView.findViewById(R.id.imagenEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dia, parent, false)
        return DiaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaViewHolder, position: Int) {
        val dia = dias[position]
        holder.textDia.text = dia.numero.toString()

        if (dia.estadoResId != null) {
            holder.imagenEstado.setImageResource(dia.estadoResId)
            holder.imagenEstado.visibility = View.VISIBLE
        } else {
            holder.imagenEstado.visibility = View.GONE
        }

        // Si no es del mes actual, hacer el número y la imagen más transparentes
        if (!dia.esMesActual) {
            holder.textDia.alpha = 0.4f
            holder.imagenEstado.alpha = 0.4f
        } else {
            holder.textDia.alpha = 1f
            holder.imagenEstado.alpha = 1f
        }
    }


    override fun getItemCount() = dias.size
}
