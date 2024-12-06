package com.thesis.project.tripplanner.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.thesis.project.tripplanner.data.Comment
import com.thesis.project.tripplanner.data.Destination
import com.thesis.project.tripplanner.data.Friend
import com.thesis.project.tripplanner.data.Itinerary
import com.thesis.project.tripplanner.data.OtherUserItinerary
import com.thesis.project.tripplanner.view.account.FriendshipStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItineraryViewModel : ViewModel() {
  private val firestore = FirebaseFirestore.getInstance()

  private val _itineraries = MutableStateFlow<List<Itinerary>>(emptyList())
  val itineraries = _itineraries.asStateFlow()

  private val _itineraryCount = MutableStateFlow(0)
  val itineraryCount = _itineraryCount.asStateFlow()

  private val _otherUserItinerary = MutableStateFlow<List<OtherUserItinerary>>(emptyList())
  val otherUserItinerary = _otherUserItinerary.asStateFlow()

  private val _destinations = MutableStateFlow<List<Destination>>(emptyList())
  val destinations = _destinations.asStateFlow()

  private val _filteredItineraries = MutableStateFlow<List<Itinerary>>(emptyList())
  val filteredItineraries = _filteredItineraries.asStateFlow()

  private val _selectedItinerary = MutableStateFlow<Itinerary?>(null)
  val selectedItinerary = _selectedItinerary.asStateFlow()

  private val _friends = MutableStateFlow<List<Friend>>(emptyList())
  val friends = _friends.asStateFlow()

  private val _friendshipStatus = MutableStateFlow(FriendshipStatus.NOT_FRIEND)
  val friendshipStatus = _friendshipStatus.asStateFlow()

  private val _friendsCount = MutableStateFlow(0)
  val friendsCount = _friendsCount.asStateFlow()

  private val _comments = MutableStateFlow<List<Comment>>(emptyList())
  val comments = _comments.asStateFlow()

  fun loadItineraries(userId: String) {
    viewModelScope.launch {
      firestore.collection("itineraries")
        .whereEqualTo("userId", userId)
        .get()
        .addOnSuccessListener { result ->
          val itineraryList = result.documents.mapNotNull { it.toObject(Itinerary::class.java) }
          _itineraries.value = itineraryList
          _itineraryCount.value = itineraryList.size
        }
        .addOnFailureListener { exception ->
          exception.printStackTrace()
        }
    }
  }

  fun saveItinerary(
    userId: String,
    username: String,
    profileImageUrl: String?,
    title: String,
    description: String,
    startDate: String,
    endDate: String,
    destinations: List<String>
  ) {
    val itineraryRef = firestore.collection("itineraries").document()
    val itineraryId = itineraryRef.id
    Log.d("SaveItinerary", "Generated itineraryId: $itineraryId")

    val itinerary = Itinerary(
      itineraryId = itineraryId,
      userId =userId,
      username = username,
      profileImageUrl = profileImageUrl,
      title = title,
      description = description,
      startDate = startDate,
      endDate = endDate,
      destinations = destinations,
      likeCount = 0,
      recommendationCount = 0)
    firestore.collection("itineraries").add(itinerary)
      .addOnSuccessListener {}
      .addOnFailureListener { exception ->
        exception.printStackTrace()
      }
  }

  fun clearItineraries() {
    _itineraries.value = emptyList()
    _itineraryCount.value = 0
  }

  fun loadOtherUsersItineraries(currentUserId: String) {
    viewModelScope.launch {
      firestore.collection("itineraries")
        .whereNotEqualTo("userId", currentUserId)
        .get()
        .addOnSuccessListener { result ->
          val itineraryWithUserList = result.documents.mapNotNull { doc ->
            val itinerary = doc.toObject(Itinerary::class.java)
            itinerary?.let {
              OtherUserItinerary(
                itinerary = it,
                username = it.username,
                profileImageUrl = it.profileImageUrl
              )
            }
          }
          _otherUserItinerary.value = itineraryWithUserList
        }
        .addOnFailureListener { exception ->
          exception.printStackTrace()
        }
    }
  }

  fun loadDestinations() {
    firestore.collection("destinations").document("h836meLJxOaVKOD1uHwk")
      .get()
      .addOnSuccessListener { document ->
        val rawList = document.get("list") as? List<*>
        val destinations = rawList?.mapNotNull { item ->
          (item as? Map<*, *>)?.let { map ->
            val name = map["name"] as? String
            val description = map["description"] as? String
            if (name != null && description != null) {
              Destination(name, description)
            } else {
              null
            }
          }
        } ?: emptyList()

        _destinations.value = destinations
      }
      .addOnFailureListener { exception ->
        exception.printStackTrace()
      }
  }

  fun loadItinerariesByDestination(selectedDestination: String, currentUserId: String) {
    firestore.collection("itineraries")
      .whereArrayContains("destinations", selectedDestination)
      .get()
      .addOnSuccessListener { result ->
        val itineraryList = result.documents.mapNotNull { document ->
          val itinerary = document.toObject(Itinerary::class.java)
          if (itinerary?.userId != currentUserId) itinerary else null
        }
        _filteredItineraries.value = itineraryList
      }
      .addOnFailureListener { exception ->
        exception.printStackTrace()
      }
  }

  fun loadItineraryById(userId: String, itineraryId: String) {
    firestore.collection("itineraries")
      .whereEqualTo("userId", userId)
      .addSnapshotListener { querySnapshot, exception ->
        if (exception != null) {
          return@addSnapshotListener
        }

        if (querySnapshot != null && !querySnapshot.isEmpty) {
          val itineraryList = querySnapshot.documents.mapNotNull { it.toObject(Itinerary::class.java) }
          _itineraries.value = itineraryList

          val selected = itineraryList.find { it.itineraryId == itineraryId }
          _selectedItinerary.value = selected
        }
      }
  }

  fun updateLikeCount(itineraryId: String, userId: String, isLiked: Boolean) {
    firestore.collection("itineraries")
      .whereEqualTo("userId", userId)
      .whereEqualTo("itineraryId", itineraryId)
      .get()
      .addOnSuccessListener { querySnapshot ->
        if (!querySnapshot.isEmpty) {
          val documentId = querySnapshot.documents[0].id
          val itineraryRef = firestore.collection("itineraries").document(documentId)

          firestore.runTransaction { transaction ->
            val snapshot = transaction.get(itineraryRef)
            val currentLikeCount = snapshot.getLong("likeCount") ?: 0
            val likedBy = (snapshot.get("likedBy") as? List<*>)?.filterIsInstance<String>() ?: emptyList()

            if (isLiked) {
              transaction.update(itineraryRef, mapOf(
                "likeCount" to currentLikeCount + 1,
                "likedBy" to likedBy + userId
              ))
            } else {
              transaction.update(itineraryRef, mapOf(
                "likeCount" to currentLikeCount,
                "likedBy" to likedBy.filter { it != userId }
              ))
            }
          }.addOnSuccessListener {}
            .addOnFailureListener { exception ->
            exception.printStackTrace()
          }
        }
      }
      .addOnFailureListener { exception ->
        exception.printStackTrace()
      }
  }

  fun updateRecommendationCount(itineraryId: String, userId: String, isRecommended: Boolean) {
    firestore.collection("itineraries")
      .whereEqualTo("itineraryId", itineraryId)
      .get()
      .addOnSuccessListener { querySnapshot ->
        if (!querySnapshot.isEmpty) {
          val documentId = querySnapshot.documents[0].id
          val itineraryRef = firestore.collection("itineraries").document(documentId)

          firestore.runTransaction { transaction ->
            val snapshot = transaction.get(itineraryRef)
            val currentRecommendationCount = snapshot.getLong("recommendationCount") ?: 0
            val recommendedBy = (snapshot.get("recommendedBy") as? List<*>)?.filterIsInstance<String>() ?: emptyList()

            if (!recommendedBy.contains(userId)) {
              transaction.update(itineraryRef, mapOf(
                "recommendationCount" to currentRecommendationCount + 1,
                "recommendedBy" to recommendedBy + userId
              ))
            } else {
              transaction.update(itineraryRef, mapOf(
                "recommendationCount" to maxOf(0, currentRecommendationCount - 1),
                "recommendedBy" to recommendedBy.filter { it != userId }
              ))
            }
          }.addOnSuccessListener {}
            .addOnFailureListener { exception ->
            exception.printStackTrace()
          }
        }
      }
      .addOnFailureListener { exception ->
        exception.printStackTrace()
      }
  }

  fun sendFriendRequest(currentUserId: String, targetUserId: String) {
    firestore.collection("friendships").add(
      mapOf(
        "userId" to currentUserId,
        "friendId" to targetUserId,
        "status" to FriendshipStatus.REQUEST_SENT.name
      )
    ).addOnSuccessListener {
      _friendshipStatus.value = FriendshipStatus.REQUEST_SENT
    }.addOnFailureListener { exception ->
      exception.printStackTrace()
    }
  }

  fun acceptFriendRequest(currentUserId: String, targetUserId: String) {
    firestore.collection("friendships")
      .whereEqualTo("userId", targetUserId)
      .whereEqualTo("friendId", currentUserId)
      .get()
      .addOnSuccessListener { querySnapshot ->
        val document = querySnapshot.documents.firstOrNull()
        document?.reference?.update("status", FriendshipStatus.FRIEND.name)

        firestore.collection("friendships").add(
          mapOf(
            "userId" to currentUserId,
            "friendId" to targetUserId,
            "status" to FriendshipStatus.FRIEND.name
          )
        ).addOnSuccessListener {
          _friendshipStatus.value = FriendshipStatus.FRIEND
        }
      }.addOnFailureListener { exception ->
        exception.printStackTrace()
      }
  }

  fun cancelFriendRequest(currentUserId: String, targetUserId: String) {
    firestore.collection("friendships")
      .whereEqualTo("userId", currentUserId)
      .whereEqualTo("friendId", targetUserId)
      .get()
      .addOnSuccessListener { querySnapshot ->
        querySnapshot.documents.firstOrNull()?.reference?.delete()
        _friendshipStatus.value = FriendshipStatus.NOT_FRIEND
      }.addOnFailureListener { exception ->
        exception.printStackTrace()
      }
  }

  fun checkFriendshipStatus(currentUserId: String, targetUserId: String) {
    firestore.collection("friendships")
      .whereEqualTo("userId", currentUserId)
      .whereEqualTo("friendId", targetUserId)
      .get()
      .addOnSuccessListener { querySnapshot ->
        val status = querySnapshot.documents.firstOrNull()?.getString("status")
        if (status == FriendshipStatus.REQUEST_SENT.name) {
          _friendshipStatus.value = FriendshipStatus.REQUEST_SENT
        } else if (status == FriendshipStatus.FRIEND.name) {
          _friendshipStatus.value = FriendshipStatus.FRIEND
        } else {
          firestore.collection("friendships")
            .whereEqualTo("userId", targetUserId)
            .whereEqualTo("friendId", currentUserId)
            .get()
            .addOnSuccessListener { reverseSnapshot ->
              val reverseStatus = reverseSnapshot.documents.firstOrNull()?.getString("status")
              if (reverseStatus == FriendshipStatus.REQUEST_SENT.name) {
                _friendshipStatus.value = FriendshipStatus.ACCEPT_REQUEST
              } else if (reverseStatus == FriendshipStatus.FRIEND.name) {
                _friendshipStatus.value = FriendshipStatus.FRIEND
              } else {
                _friendshipStatus.value = FriendshipStatus.NOT_FRIEND
              }
            }
            .addOnFailureListener {
              _friendshipStatus.value = FriendshipStatus.NOT_FRIEND
            }
        }
      }
      .addOnFailureListener {
        _friendshipStatus.value = FriendshipStatus.NOT_FRIEND
      }
  }

  fun loadFriends(userId: String) {
    _friends.value = emptyList()
    firestore.collection("friendships")
      .whereEqualTo("userId", userId)
      .whereEqualTo("status", FriendshipStatus.FRIEND.name)
      .get()
      .addOnSuccessListener { result ->
        val friendList = mutableListOf<Friend>()
        result.documents.forEach { document ->
          val friendId = document.getString("friendId")
          if (friendId != null) {
            firestore.collection("users")
              .document(friendId)
              .get()
              .addOnSuccessListener { userDocument ->
                val friendName = userDocument.getString("name") ?: "Unknown"
                friendList.add(Friend(id = friendId, name = friendName))
                _friends.value = friendList
                _friendsCount.value = friendList.size
              }
              .addOnFailureListener { exception ->
                exception.printStackTrace()
              }
          }
        }
      }
      .addOnFailureListener { exception ->
        exception.printStackTrace()
      }
  }

  fun deleteItinerary(itineraryId: String, currentUserId: String, onSuccess: () -> Unit) {
    firestore.collection("itineraries")
      .whereEqualTo("itineraryId", itineraryId)
      .get()
      .addOnSuccessListener { querySnapshot ->
        if (!querySnapshot.isEmpty) {
          val document = querySnapshot.documents.first()
          val itineraryOwnerId = document.getString("userId")

          if (itineraryOwnerId == currentUserId) {
            firestore.collection("itineraries").document(document.id)
              .delete()
              .addOnSuccessListener {
                onSuccess()
              }
              .addOnFailureListener { exception ->
                exception.printStackTrace()
              }
          }
        }
      }
      .addOnFailureListener { exception ->
        exception.printStackTrace()
      }
  }

  fun loadComments(itineraryId: String) {
    firestore.collection("comments").document(itineraryId)
      .addSnapshotListener { documentSnapshot, exception ->
        if (exception != null) {
          Log.e("ItineraryViewModel", "Error listening for comments: ${exception.message}")
          return@addSnapshotListener
        }

        if (documentSnapshot != null && documentSnapshot.exists()) {
          val commentsList = documentSnapshot.get("comments") as? List<Map<String, Any>>
          val parsedComments = commentsList?.map { commentMap ->
            Comment(
              userId = commentMap["userId"] as String,
              username = commentMap["username"] as String,
              profileImageUrl = commentMap["profileImageUrl"] as? String,
              commentText = commentMap["commentText"] as String,
              timestamp = (commentMap["timestamp"] as Long)
            )
          } ?: emptyList()

          _comments.value = parsedComments
        } else {
          _comments.value = emptyList()
        }
      }
  }

  fun addComment(
    itineraryId: String,
    username: String,
    commentText: String,
    profileImageUrl: String?,
    userId: String
  ) {
    val comment = mapOf(
      "userId" to userId,
      "username" to username,
      "profileImageUrl" to profileImageUrl,
      "commentText" to commentText,
      "timestamp" to System.currentTimeMillis()
    )

    firestore.collection("comments").document(itineraryId)
      .update("comments", FieldValue.arrayUnion(comment))
      .addOnSuccessListener {}
      .addOnFailureListener { exception ->
        firestore.collection("comments").document(itineraryId)
          .set(mapOf("comments" to listOf(comment)))
          .addOnSuccessListener {}
          .addOnFailureListener { innerException ->
            exception.printStackTrace()
          }
      }
  }

  fun deleteComment(itineraryId: String, comment: Comment) {
    firestore.collection("comments").document(itineraryId)
      .update("comments", FieldValue.arrayRemove(comment))
      .addOnSuccessListener {}
      .addOnFailureListener { exception ->
        exception.printStackTrace()
      }
  }

  fun deleteFriend(currentUserId: String, targetUserId: String) {
    firestore.collection("friendships")
      .whereEqualTo("userId", currentUserId)
      .whereEqualTo("friendId", targetUserId)
      .get()
      .addOnSuccessListener { querySnapshot ->
        querySnapshot.documents.firstOrNull()?.reference?.delete()
        firestore.collection("friendships")
          .whereEqualTo("userId", targetUserId)
          .whereEqualTo("friendId", currentUserId)
          .get()
          .addOnSuccessListener { reverseSnapshot ->
            reverseSnapshot.documents.firstOrNull()?.reference?.delete()
            _friendshipStatus.value = FriendshipStatus.NOT_FRIEND
          }
      }
      .addOnFailureListener { exception ->
        exception.printStackTrace()
      }
  }
}