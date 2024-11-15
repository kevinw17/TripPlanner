package com.thesis.project.tripplanner.data

data class Itinerary(
  val itineraryId: String = "",
  val userId: String = "",
  val username: String = "",
  val profileImageUrl: String? = null,
  val title: String = "",
  val description: String = "",
  val startDate: String = "",
  val endDate: String = "",
  val destinations: List<String> = listOf(),
  val likeCount: Int = 0,
  val recommendationCount: Int = 0,
  val likedBy: List<String> = emptyList(),
  val recommendedBy: List<String> = emptyList()
)

