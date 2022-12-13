package com.wahidabd.remas.data.storage.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.storage.ReportStorage
import com.wahidabd.remas.domain.models.User
import com.wahidabd.remas.utils.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception

class FirebaseReportStorage : ReportStorage {

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

    companion object {
        val db = FirebaseDatabase.getInstance().getReference(Constants.TABLE.REPORT)
        val user = FirebaseDatabase.getInstance().getReference(Constants.TABLE.USER)
    }
}