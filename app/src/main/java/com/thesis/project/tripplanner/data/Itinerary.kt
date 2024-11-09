package com.thesis.project.tripplanner.data

data class Itinerary(
  val userId: String = "",
  val title: String = "",
  val description: String = "",
  val startDate: String = "",
  val endDate: String = "",
  val destinations: List<String> = listOf()
)

