package com.thesis.project.tripplanner.data

data class Itinerary(
  val userId: String = "",
  val username: String = "",
  val profileImageUrl: String? = null,
  val title: String = "",
  val description: String = "",
  val startDate: String = "",
  val endDate: String = "",
  val destinations: List<String> = listOf()
)

