package com.example.dados2.basedatos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface JugadorDao {
    @Query("SELECT * FROM jugadores WHERE nombre LIKE :firstName")
    fun findByName(firstName: String): Jugador
    @Insert
    fun insertJugador(jugador: Jugador)
    @Update
    fun updateJugador(jugador: Jugador):Int
}