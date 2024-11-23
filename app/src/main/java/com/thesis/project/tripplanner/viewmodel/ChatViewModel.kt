package com.thesis.project.tripplanner.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.thesis.project.tripplanner.data.Chat
import com.thesis.project.tripplanner.data.Message
import com.thesis.project.tripplanner.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel : ViewModel() {
  private val firestore = FirebaseFirestore.getInstance()

  private val m_chatMessages = MutableStateFlow<List<Chat>>(emptyList())
  val chatMessages = m_chatMessages.asStateFlow()

  private val m_userChatPreviews = MutableStateFlow<List<Message>>(emptyList())
  val userChatPreviews = m_userChatPreviews.asStateFlow()

  fun loadUserChatPreviews(currentUserId: String) {
    firestore.collection("userChats")
      .document(currentUserId)
      .addSnapshotListener { snapshot, error ->
        if (error != null) {
          error.printStackTrace()
          return@addSnapshotListener
        }

        if (snapshot != null && snapshot.exists()) {
          val chatPreviews = snapshot.data?.entries?.mapNotNull { entry ->
            val userId = entry.key
            val data = entry.value as? Map<*, *>
            val username = data?.get("username") as? String
            val lastMessage = data?.get("lastMessage") as? String ?: Utils.EMPTY
            val lastMessageDate = data?.get("lastMessageDate") as? String ?: Utils.EMPTY
            val isUnread = data?.get("isUnread") as? Boolean ?: false

            if (username.isNullOrEmpty()) {
              fetchUserName(userId) { fetchedName ->
                if (!fetchedName.isNullOrEmpty()) {
                  updateUserChatUsername(currentUserId, userId, fetchedName)
                }
              }
            }

            Message(
              userId = userId,
              username = username ?: userId,
              lastMessage = lastMessage,
              lastMessageDate = lastMessageDate,
              isUnread = isUnread
            )
          } ?: emptyList()

          m_userChatPreviews.value = chatPreviews
        }
      }
  }

  fun loadChatMessages(currentUserId: String, otherUserId: String) {
    firestore.collection("chats")
      .whereArrayContains("participants", currentUserId)
      .addSnapshotListener { querySnapshot, exception ->
        if (exception != null) {
          exception.printStackTrace()
          return@addSnapshotListener
        }

        if (querySnapshot != null && !querySnapshot.isEmpty) {
          val messages = querySnapshot.documents.flatMap { document ->
            val chatMessages = document.get("messages") as? List<Map<String, Any>>
            chatMessages?.map { messageMap ->
              Chat(
                text = messageMap["text"] as String,
                timestamp = messageMap["timestamp"] as Long,
                isSentByUser = messageMap["senderId"] == currentUserId
              )
            } ?: emptyList()
          }
          m_chatMessages.value = messages.sortedBy { it.timestamp }
        }
      }
  }

  fun sendMessage(currentUserId: String, otherUserId: String, message: String) {
    val chatRef = firestore.collection("chats")
      .whereArrayContains("participants", currentUserId)
      .limit(1)

    chatRef.get().addOnSuccessListener { querySnapshot ->
      val chatId = if (!querySnapshot.isEmpty) {
        querySnapshot.documents.first().id
      } else {
        firestore.collection("chats").document().id
      }

      val chatMessage = mapOf(
        "text" to message,
        "timestamp" to System.currentTimeMillis(),
        "senderId" to currentUserId
      )

      firestore.collection("chats")
        .document(chatId)
        .set(
          mapOf(
            "participants" to listOf(currentUserId, otherUserId),
            "messages" to FieldValue.arrayUnion(chatMessage)
          ),
          SetOptions.merge()
        )
        .addOnFailureListener { exception ->
          exception.printStackTrace()
        }

      val userChatUpdates = mapOf(
        "lastMessage" to message,
        "lastMessageDate" to System.currentTimeMillis().toString(),
        "isUnread" to true
      )

      firestore.collection("userChats")
        .document(currentUserId)
        .set(mapOf(otherUserId to userChatUpdates), SetOptions.merge())

      firestore.collection("userChats")
        .document(otherUserId)
        .set(mapOf(currentUserId to userChatUpdates), SetOptions.merge())
    }
  }

  fun fetchUserName(userId: String, onResult: (String?) -> Unit) {
    firestore.collection("users")
      .document(userId)
      .get()
      .addOnSuccessListener { document ->
        val userName = document.getString("name")
        onResult(userName)
      }
      .addOnFailureListener { exception ->
        exception.printStackTrace()
        onResult(null)
      }
  }

  private fun updateUserChatUsername(currentUserId: String, otherUserId: String, username: String) {
    firestore.collection("userChats")
      .document(currentUserId)
      .update("$otherUserId.username", username)
      .addOnFailureListener { it.printStackTrace() }
  }

  fun markMessagesAsRead(currentUserId: String, otherUserId: String) {
    firestore.collection("userChats")
      .document(currentUserId)
      .update("$otherUserId.isUnread", false)
      .addOnFailureListener { exception ->
        exception.printStackTrace()
      }
  }
}