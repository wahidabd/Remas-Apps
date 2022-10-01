package com.wahidabd.remas.data.storage.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.models.User
import com.wahidabd.remas.data.request.auth.LoginRequest
import com.wahidabd.remas.data.request.auth.RegisterRequest
import com.wahidabd.remas.data.response.GenericResponse
import com.wahidabd.remas.data.storage.UserStorage
import com.wahidabd.remas.utils.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception

class FirebaseUserStorage : UserStorage {

    override suspend fun login(request: LoginRequest): Flow<Response<User>> = callbackFlow {
        trySend(Response.Loading())

        auth.signInWithEmailAndPassword(request.email, request.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser?.isEmailVerified == true) {
                        user.child(task.result?.user?.uid.toString())
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        val data = snapshot.getValue(User::class.java)
                                        if (data != null) trySend(Response.Success(data))
                                    } else {
                                        trySend(Response.Error(e = Exception("User not found!")))
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    trySend(Response.Error(error.toException()))
                                }
                            })
                    } else {
                        trySend(Response.Error(e = Exception("Email anda belum diverifikasi!")))
                    }
                } else {
                    trySend(Response.Error(e = Exception("Gagal login, silahkan coba lagi!")))
                }
            }

            .addOnFailureListener { e -> trySend(Response.Error(e = Exception(e.message))) }

        awaitClose { this.close() }
    }

    override suspend fun register(request: RegisterRequest): Flow<Response<GenericResponse>> =
        callbackFlow {
            trySend(Response.Loading())

            auth.createUserWithEmailAndPassword(request.email, request.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val id = auth.currentUser?.uid

                        user.child(id.toString()).setValue(request).addOnSuccessListener {
                            val response = GenericResponse(
                                status = true,
                                message = "Berhasil mendaftar, silahkan cek email atau folder spam pada email ${request.email}"
                            )
                            trySend(Response.Success(response))
                        }
                    }else{
                        trySend(Response.Error(e = Exception("Gagal mendaftar, coba beberapa saat lagi")))
                    }
                }

                .addOnFailureListener { trySend(Response.Error(it)) }
            awaitClose { this.close() }
        }

    override suspend fun userDetail(id: String): Flow<Response<User>> = callbackFlow {

        trySend(Response.Loading())

        user.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) trySend(Response.Success(user))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Response.Error(error.toException()))
            }
        })
        awaitClose { this.close() }
    }

    companion object {
        private val auth = FirebaseAuth.getInstance()
        private val user = FirebaseDatabase.getInstance().getReference(Constants.TABLE.USER)
    }
}