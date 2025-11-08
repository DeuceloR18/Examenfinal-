package com.example.pruebafinal

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch
import java.util.*

// Data classes
data class Usuario(
    val uid: String = "",
    val nombre: String = "",
    val carnet: String = "",
    val carrera: String = "",
    val fotoUrl: String = "",
    val rol: String = "estudiante"
)

data class Equipo(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val imagenUrl: String = "",
    val disponible: Boolean = true,
    val contadorPrestamos: Long = 0L
)

data class Prestamo(
    val id: String = "",
    val idUsuario: String = "",
    val idEquipo: String = "",
    val fechaPrestamo: Date? = null,
    val fechaDevolucion: Date? = null,
    val estado: String = "Pendiente"
)
