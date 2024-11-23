package com.thesis.project.tripplanner.data

import com.thesis.project.tripplanner.utils.Utils

data class Comment(
  val userId: String = Utils.EMPTY,
  val username: String = Utils.EMPTY,
  val profileImageUrl: String? = null,
  val commentText: String = Utils.EMPTY,
  val timestamp: Long = 0L
)