package com.example.dados2.basedatos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jugadores")
data class Jugador (
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    @ColumnInfo(name = "nombre")var nombre: String?,
    @ColumnInfo(name = "puntajeMax")var puntajeMax: Int,
    @ColumnInfo(name = "puntajeAcumulado")var puntajeAcumulado: Int,
    @ColumnInfo(name = "destructor")var destructor: Boolean,
    @ColumnInfo(name = "imparable")var imparable: Boolean,
)