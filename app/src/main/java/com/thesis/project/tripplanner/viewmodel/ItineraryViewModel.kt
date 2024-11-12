package com.thesis.project.tripplanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.thesis.project.tripplanner.data.Itinerary
import com.thesis.project.tripplanner.data.OtherUserItinerary
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
    val itinerary = Itinerary(userId, username, profileImageUrl, title, description, startDate, endDate, destinations)
    firestore.collection("itineraries").add(itinerary)
      .addOnSuccessListener {
        //Will add implementation later
      }
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
}
