package com.example.pruebafinal

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Date

class MainViewModel : ViewModel() {
    val usuarioState: MutableState<Usuario?> = mutableStateOf(null)
    val equiposState: MutableState<List<Equipo>> = mutableStateOf(emptyList())
    val prestamosState: MutableState<List<Prestamo>> = mutableStateOf(emptyList())
    val errorState: MutableState<String?> = mutableStateOf(null)

    fun login(email: String, password: String, onSuccess: (Usuario) -> Unit) {
        viewModelScope.launch {
            when (val r = FirebaseRepository.loginWithEmail(email, password)) {
                is Result.Success -> {
                    usuarioState.value = r.getOrNull()
                    onSuccess(r.getOrNull()!!)
                }
                is Result.Failure -> errorState.value = r.exceptionOrNull()?.localizedMessage
            }
        }
    }

    fun register(email: String, password: String, usuario: Usuario, onSuccess: (Usuario) -> Unit) {
        viewModelScope.launch {
            when (val r = FirebaseRepository.registerWithEmail(email, password, usuario)) {
                is Result.Success -> {
                    usuarioState.value = r.getOrNull()
                    onSuccess(r.getOrNull()!!)
                }
                is Result.Failure -> errorState.value = r.exceptionOrNull()?.localizedMessage
            }
        }
    }

    fun cargarEquipos() {
        viewModelScope.launch {
            when (val r = FirebaseRepository.getAllEquipos()) {
                is Result.Success -> equiposState.value = r.getOrNull()!!
                is Result.Failure -> errorState.value = r.exceptionOrNull()?.localizedMessage
            }
        }
    }

    fun solicitarPrestamo(uid: String, equipo: Equipo) {
        viewModelScope.launch {
            val ahora = Date()
            val tresDias = Date(ahora.time + 3L * 24 * 60 * 60 * 1000)
            val prestamo = Prestamo(
                id = "",
                idUsuario = uid,
                idEquipo = equipo.id,
                fechaPrestamo = ahora,
                fechaDevolucion = tresDias,
                estado = "Pendiente"
            )
            when (val r = FirebaseRepository.solicitarPrestamo(prestamo)) {
                is Result.Success -> {
                }
                is Result.Failure -> errorState.value = r.exceptionOrNull()?.localizedMessage
            }
        }
    }

    fun cargarPrestamosUsuario(uid: String) {
        viewModelScope.launch {
            when (val r = FirebaseRepository.getPrestamosByUsuario(uid)) {
                is Result.Success -> prestamosState.value = r.getOrNull()!!
                is Result.Failure -> errorState.value = r.exceptionOrNull()?.localizedMessage
            }
        }
    }

    fun cargarPrestamosPendientes() {
        viewModelScope.launch {
            when (val r = FirebaseRepository.getPrestamosPendientes()) {
                is Result.Success -> prestamosState.value = r.getOrNull()!!
                is Result.Failure -> errorState.value = r.exceptionOrNull()?.localizedMessage
            }
        }
    }

    fun aprobarPrestamo(prestamo: Prestamo) {
        viewModelScope.launch {
            when (val r = FirebaseRepository.actualizarEstadoPrestamo(prestamo.id, "Aprobado")) {
                is Result.Success -> {
                    FirebaseRepository.marcarEquipoNoDisponible(prestamo.idEquipo)
                }
                is Result.Failure -> errorState.value = r.exceptionOrNull()?.localizedMessage
            }
        }
    }

    fun rechazarPrestamo(prestamo: Prestamo) {
        viewModelScope.launch {
            when (val r = FirebaseRepository.actualizarEstadoPrestamo(prestamo.id, "Rechazado")) {
                is Result.Failure -> errorState.value = r.exceptionOrNull()?.localizedMessage
            }
        }
    }

    fun marcarDevuelto(prestamo: Prestamo) {
        viewModelScope.launch {
            when (val r = FirebaseRepository.actualizarEstadoPrestamo(prestamo.id, "Devuelto")) {
                is Result.Success -> FirebaseRepository.marcarEquipoDisponible(prestamo.idEquipo)
                is Result.Failure -> errorState.value = r.exceptionOrNull()?.localizedMessage
            }
        }
    }
}