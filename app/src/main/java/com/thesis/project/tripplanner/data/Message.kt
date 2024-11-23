package com.thesis.project.tripplanner.data

data class Message(
  val userId: String,
  val username: String,
  val lastMessage: String,
  val lastMessageDate: String,
  val isUnread: Boolean
)
