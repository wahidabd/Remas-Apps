package com.wahidabd.remas.data.storage.firebase

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.request.report.ReportRequest
import com.wahidabd.remas.data.response.ReportDocumentResponse
import com.wahidabd.remas.data.storage.ReportStorage
import com.wahidabd.remas.domain.models.User
import com.wahidabd.remas.utils.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseReportStorage : ReportStorage {

    override suspend fun createReport(request: ReportRequest): Flow<Response<Boolean>> = callbackFlow {

        trySend(Response.Loading())

        request.id = db.push().key.toString()
        if (request.file != null){
            val file = Uri.fromFile(request.file)
            storage.child(request.id.toString()).putFile(file).addOnSuccessListener {
                storage.child(request.id.toString()).downloadUrl.addOnSuccessListener { url ->
                    request.file_url = url.toString()

                    // set to database
                    db.child(request.user_id.toString()).updateChildren(request.toUserMap())
                    db.child(request.user_id.toString()).child(Constants.TABLE.REPORT_ROOM).child(request.id.toString()).setValue(request.toMap())
                        .addOnSuccessListener {
                            trySend(Response.Success(true))
                        }
                        .addOnFailureListener { trySend(Response.Error(e = Exception(it.message.toString()))) }
                }
            }
        }else{
            request.id = db.push().key.toString()
            db.child(request.user_id.toString()).updateChildren(request.toUserMap())
            db.child(request.user_id.toString()).child(Constants.TABLE.REPORT_ROOM).child(request.id.toString()).setValue(request.toMap())
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }
                .addOnFailureListener { trySend(Response.Error(e = Exception(it.message.toString()))) }
        }

        awaitClose { this.close() }
    }

    override suspend fun getUser(): Flow<Response<List<User>>> = callbackFlow {
        trySend(Response.Loading())

        user.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<User>()
                for (item in snapshot.children){
                    val data = item.getValue(User::class.java)
                    if (data != null && data.role == 1) list.add(data)
                }
                trySend(Response.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Response.Error(e = Exception(error.message)))
            }
        })

        awaitClose { this.close() }
    }

    override suspend fun getUserDocument(): Flow<Response<List<User>>> = callbackFlow {

        trySend(Response.Loading())
        db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<User>()

                for (item in snapshot.children){
                    val data = item.getValue(User::class.java)
                    if (data != null) list.add(data)
                }

                trySend(Response.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Response.Error(e = Exception(error.message)))
            }

        })

        awaitClose { this.close() }
    }

    override suspend fun getReportByUser(id: String): Flow<Response<List<ReportDocumentResponse>>> = callbackFlow {
        db.child(id).child(Constants.TABLE.REPORT_ROOM)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<ReportDocumentResponse>()

                    for (item in snapshot.children){
                        val data = item.getValue(ReportDocumentResponse::class.java)
                        if (data != null) list.add(data)
                    }

                    trySend(Response.Success(list))
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(Response.Error(e = Exception(error.message)))
                }

            })

        awaitClose { this.close() }
    }

    companion object {
        val db = FirebaseDatabase.getInstance().getReference(Constants.TABLE.REPORT)
        val user = FirebaseDatabase.getInstance().getReference(Constants.TABLE.USER)
        val storage = FirebaseStorage.getInstance().getReference(Constants.TABLE.REPORT_STORAGE)
    }
}