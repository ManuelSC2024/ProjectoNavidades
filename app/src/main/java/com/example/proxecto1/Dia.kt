package com.example.proxecto1

data class Dia(
    val fecha: String,       // yyyy-MM-dd
    val numero: Int,         // 1, 2, 3...
    val estadoResId: Int?,   // drawable del estado de ánimo, null si no hay
    val esMesActual: Boolean // true si pertenece al mes mostrado, false si es del anterior/siguiente
)
