package com.thesis.project.tripplanner.view.itinerary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.thesis.project.tripplanner.R
import com.thesis.project.tripplanner.data.Comment

@Composable
fun CommentBottomSheet(
    comments: List<Comment>,
    onSendComment: (String) -> Unit,
    currentUserId: String,
    profileImageUrl: String? = null
) {
    var commentText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 48.dp)
            .navigationBarsPadding()
            .heightIn(max = 400.dp)
    ) {
        Text(text = "Comments", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        if (comments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_comment),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(comments) { comment ->
                    CommentItem(comment = comment)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = commentText,
                onValueChange = { commentText = it },
                placeholder = { Text(text = "Add comment here") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(24.dp)
            )
            IconButton(
                onClick = {
                    if (commentText.isNotEmpty()) {
                        onSendComment(commentText)
                        commentText = ""
                    }
                },
                modifier = Modifier
                    .background(Color.Gray, CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(comment.profileImageUrl ?: R.drawable.ic_user_profile),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "${comment.username} • ${getTimeAgo(comment.timestamp)}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = comment.commentText,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

private fun getTimeAgo(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> "$seconds seconds ago"
        minutes < 60 -> "$minutes minutes ago"
        hours < 24 -> "$hours hours ago"
        else -> "$days days ago"
    }
}

@Preview(showBackground = true)
@Composable
fun CommentBottomSheetPreview() {
    CommentBottomSheet(
        comments = listOf(
            Comment(
                username = "Fred",
                commentText = "Jalan-jalan ke Bandung sangat seru!! pokoknyaaa seru deh",
                timestamp = System.currentTimeMillis()
            ),
            Comment(
                username = "Fred",
                commentText = "Jalan-jalan ke Bandung sangat seru!! pokoknyaaa seru deh",
                timestamp = System.currentTimeMillis()
            ),
            Comment(
                username = "Fred",
                commentText = "Jalan-jalan ke Bandung sangat seru!! pokoknyaaa seru deh",
                timestamp = System.currentTimeMillis()
            )
        ),
        currentUserId = "",
        profileImageUrl = null,
        onSendComment = { _ ->
        }
    )
}
