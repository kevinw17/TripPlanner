package com.thesis.project.tripplanner.data

data class Comment(
  val userId: String = "",
  val username: String = "",
  val profileImageUrl: String? = null,
  val commentText: String = "",
  val timestamp: Long = 0L
)
