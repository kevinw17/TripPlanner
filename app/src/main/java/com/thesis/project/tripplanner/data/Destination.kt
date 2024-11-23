package com.thesis.project.tripplanner.data

import com.thesis.project.tripplanner.utils.Utils

data class Destination(
  val name: String = Utils.EMPTY,
  val description: String = Utils.EMPTY
)