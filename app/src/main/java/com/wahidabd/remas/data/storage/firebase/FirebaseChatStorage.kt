package com.wahidabd.remas.data.storage.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.request.chat.ChatRoomRequest
import com.wahidabd.remas.data.response.chat.ChatRoomResponse
import com.wahidabd.remas.data.response.chat.UserChatGroupResponse
import com.wahidabd.remas.data.storage.ChatStorage
import com.wahidabd.remas.utils.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception

class FirebaseChatStorage : ChatStorage {

    override suspend fun getGroupChat(id: String): Flow<Response<List<UserChatGroupResponse>>> = callbackFlow {

        trySend(Response.Loading())

        db.child(id).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<UserChatGroupResponse>()
                for (item in snapshot.children){
                    val data = item.getValue(UserChatGroupResponse::class.java)
                    if (data != null) list.add(data)
                }

                trySend(Response.Success(list.sortedByDescending { it.date }))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Response.Error(e = Exception(error.message)))
            }

        })

        awaitClose { this.close() }
    }

    override suspend fun sendMessage(request: ChatRoomRequest): Flow<Response<Boolean>> = callbackFlow {

        trySend(Response.Loading())

        request.id = db.push().key.toString()
        db.child(request.sender_id).child(request.receiver_id).child(Constants.TABLE.CHAT_ROOM)
            .child(request.id.toString()).setValue(request.toMessage())
            .addOnCompleteListener {
                db.child(request.receiver_id).child(request.sender_id).updateChildren(request.toSender())

                db.child(request.receiver_id).child(request.sender_id).child(Constants.TABLE.CHAT_ROOM)
                    .child(request.id.toString()).setValue(request.toMessage())

                // update unread count on receiver user
                val query = db.child(request.sender_id).child(request.receiver_id).child(Constants.TABLE.CHAT_ROOM)
                    .orderByChild("read").equalTo(false)

                query.get().addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val count = task.result.childrenCount.toInt()
                        request.unread = count
                        db.child(request.sender_id).child(request.receiver_id).updateChildren(request.toReceiver())
                    }
                }

                trySend(Response.Success(true))
            }

            .addOnFailureListener { trySend(Response.Error( e = Exception(it.printStackTrace().toString()))) }

        awaitClose { this.close() }
    }

    override suspend fun readMessage(
        sender: String,
        receiver: String
    ): Flow<Response<List<ChatRoomResponse>>> = callbackFlow {

        trySend(Response.Loading())

        db.child(sender).child(receiver).child(Constants.TABLE.CHAT_ROOM).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<ChatRoomResponse>()
                for (item in snapshot.children){
                    val data = item.getValue(ChatRoomResponse::class.java)
                    if (data != null) list.add(data)
                }

                trySend(Response.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Response.Error( e = Exception(error.message)))
            }
        })

        awaitClose { this.close() }
    }

    companion object {
        val db = FirebaseDatabase.getInstance().getReference(Constants.TABLE.CHAT)
    }
}