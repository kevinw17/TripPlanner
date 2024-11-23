package com.thesis.project.tripplanner.data

import com.thesis.project.tripplanner.utils.Utils

data class Itinerary(
  val itineraryId: String = Utils.EMPTY,
  val userId: String = Utils.EMPTY,
  val username: String = Utils.EMPTY,
  val profileImageUrl: String? = null,
  val title: String = Utils.EMPTY,
  val description: String = Utils.EMPTY,
  val startDate: String = Utils.EMPTY,
  val endDate: String = Utils.EMPTY,
  val destinations: List<String> = listOf(),
  val likeCount: Int = 0,
  val recommendationCount: Int = 0,
  val likedBy: List<String> = emptyList(),
  val recommendedBy: List<String> = emptyList()
)