package com.thesis.project.tripplanner.view.itinerary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.thesis.project.tripplanner.utils.Utils

@Composable
fun CommentBottomSheet(
    comments: List<Comment>,
    onSendComment: (String) -> Unit,
    onDeleteComment: (Comment) -> Unit,
    currentUserId: String,
    profileImageUrl: String? = null
) {
    var commentText by remember { mutableStateOf(Utils.EMPTY) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
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
                    val isCommentOwner = comment.userId == currentUserId
                    CommentItem(
                        comment = comment,
                        isAllowedToDeleteComment = isCommentOwner,
                        onDeleteComment = onDeleteComment
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            androidx.compose.material3.OutlinedTextField(
                value = commentText,
                onValueChange = { commentText = it },
                placeholder = { Text(text = stringResource(R.string.add_comment_here)) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFEEF3F4),
                    unfocusedContainerColor = Color(0xFFEEF3F4),
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Button(
                onClick = {
                    if (commentText.isNotEmpty()) {
                        onSendComment(commentText)
                        commentText = Utils.EMPTY
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEEF3F4),
                    contentColor = Color.Black
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.Black
                ),
                modifier = Modifier.weight(0.3f)
            ) {
                Text(text = stringResource(R.string.send))
            }
        }
    }
}

@Composable
fun CommentItem(
    comment: Comment,
    isAllowedToDeleteComment: Boolean,
    onDeleteComment: (Comment) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(comment.profileImageUrl ?: R.drawable.ic_user_profile),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
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
        if (isAllowedToDeleteComment) {
            IconButton(
                onClick = { onDeleteComment(comment) },
                modifier = Modifier
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_remove),
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
            }
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
        },
        onDeleteComment = {}
    )
}