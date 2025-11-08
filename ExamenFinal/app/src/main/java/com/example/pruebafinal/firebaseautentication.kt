package com.example.pruebafinal

import androidx.room.util.copy

object FirebaseRepository {
    private val auth: FirebaseAuth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()


    private const val USERS = "usuarios"
    private const val EQUIPOS = "equipos"
    private const val PRESTAMOS = "prestamos"


    suspend fun registerWithEmail(
        email: String,
        password: String,
        usuario: Usuario
    ): Result<Usuario> {
        return try {
            val res = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = res.user?.uid ?: throw Exception("UID nulo")
            val userWithUid = usuario.copy(uid = uid)
            db.collection(USERS).document(uid).set(userWithUid).await()
            Result.success(userWithUid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginWithEmail(email: String, password: String): Result<Usuario> {
        return try {
            val res = auth.signInWithEmailAndPassword(email, password).await()
            val uid = res.user?.uid ?: throw Exception("UID nulo")
            val snap = db.collection(USERS).document(uid).get().await()
            val u = snap.toObject(Usuario::class.java) ?: throw Exception("Usuario no encontrado")
            Result.success(u)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() { auth.signOut() }

    //funcion de equipos
    suspend fun getAllEquipos(): Result<List<Equipo>> {
        return try {
            val snap = db.collection(EQUIPOS).get().await()
            val list = snap.documents.map { doc ->
                doc.toObject(Equipo::class.java)!!.copy(id = doc.id)
            }
            Result.success(list)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getEquipoById(id: String): Result<Equipo> {
        return try {
            val snap = db.collection(EQUIPOS).document(id).get().await()
            val eq = snap.toObject(Equipo::class.java)!!.copy(id = snap.id)
            Result.success(eq)
        } catch (e: Exception) { Result.failure(e) }
    }

    // PRESTAMOS
    suspend fun solicitarPrestamo(prestamo: Prestamo): Result<String> {
        return try {
            val ref = db.collection(PRESTAMOS).add(prestamo).await()
            Result.success(ref.id)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getPrestamosByUsuario(uid: String): Result<List<Prestamo>> {
        return try {
            val snap = db.collection(PRESTAMOS).whereEqualTo("idUsuario", uid).get().await()
            val list = snap.documents.map { d -> d.toObject(Prestamo::class.java)!!.copy(id = d.id) }
            Result.success(list)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getPrestamosPendientes(): Result<List<Prestamo>> {
        return try {
            val snap = db.collection(PRESTAMOS).whereEqualTo("estado", "Pendiente").get().await()
            val list = snap.documents.map { d -> d.toObject(Prestamo::class.java)!!.copy(id = d.id) }
            Result.success(list)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun actualizarEstadoPrestamo(idPrestamo: String, nuevoEstado: String): Result<Unit> {
        return try {
            db.collection(PRESTAMOS).document(idPrestamo).update("estado", nuevoEstado).await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun marcarEquipoNoDisponible(idEquipo: String): Result<Unit> {
        return try {
            db.collection(EQUIPOS).document(idEquipo).update("disponible", false).await()
            db.collection(EQUIPOS).document(idEquipo).update("contadorPrestamos", com.google.firebase.firestore.FieldValue.increment(1)).await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun marcarEquipoDisponible(idEquipo: String): Result<Unit> {
        return try {
            db.collection(EQUIPOS).document(idEquipo).update("disponible", true).await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getEquiposMasSolicitados(limit: Long = 10): Result<List<Equipo>> {
        return try {
            val snap = db.collection(EQUIPOS).orderBy("contadorPrestamos", com.google.firebase.firestore.Query.Direction.DESCENDING).limit(limit).get().await()
            val list = snap.documents.map { d -> d.toObject(Equipo::class.java)!!.copy(id = d.id) }
            Result.success(list)
        } catch (e: Exception) { Result.failure(e) }
    }
}
