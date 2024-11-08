package com.thesis.project.tripplanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.thesis.project.tripplanner.data.Itinerary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItineraryViewModel : ViewModel() {
  private val firestore = FirebaseFirestore.getInstance()

  private val _itineraries = MutableStateFlow<List<Itinerary>>(emptyList())
  val itineraries = _itineraries.asStateFlow()

  private val _itineraryCount = MutableStateFlow(0)
  val itineraryCount = _itineraryCount.asStateFlow()

  fun loadItineraries() {
    viewModelScope.launch {
      firestore.collection("itineraries")
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

  fun saveItinerary(username: String, title: String, description: String, startDate: String, endDate: String, destinations: List<String>) {
    val itinerary = Itinerary(username, title, description, startDate, endDate, destinations)
    firestore.collection("itineraries").add(itinerary)
      .addOnSuccessListener {
        //Will add implementation later
      }
      .addOnFailureListener { exception ->
        exception.printStackTrace()
      }
  }
}
