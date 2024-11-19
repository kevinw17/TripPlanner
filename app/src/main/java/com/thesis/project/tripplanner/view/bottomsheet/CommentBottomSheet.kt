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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thesis.project.tripplanner.R

data class Comment(
    val username: String,
    val commentText: String,
    val timeAgo: String
)

@Composable
fun CommentBottomSheet(
    comments: List<Comment> = listOf(
        Comment("Fred", "Jalan-jalan ke Bandung sangat seru!! pokoknyaaa seru deh", "1h ago"),
        Comment("Fred", "Jalan-jalan ke Bandung sangat seru!! pokoknyaaa seru deh", "2h ago"),
        Comment("Fred", "Jalan-jalan ke Bandung sangat seru!! pokoknyaaa seru deh", "3h ago")
    ),
    onSendComment: (String) -> Unit
) {
    var commentText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 72.dp, start = 16.dp, end = 16.dp)
            .heightIn(max = 400.dp)
    ) {
        Text(text = "Comments", fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(comments) { comment ->
                CommentItem(comment)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

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
            painter = painterResource(R.drawable.ic_user_profile),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "${comment.username} • ${comment.timeAgo}",
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = comment.commentText,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }

    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun CommentBottomSheetPreview() {
    CommentBottomSheet(
        comments = listOf(
            Comment("Fred", "Jalan-jalan ke Bandung sangat seru!! pokoknyaaa seru deh", "1h ago"),
            Comment("Fred", "Jalan-jalan ke Bandung sangat seru!! pokoknyaaa seru deh", "2h ago"),
            Comment("Fred", "Jalan-jalan ke Bandung sangat seru!! pokoknyaaa seru deh", "3h ago")
        )
    ) { comment ->
        // Handle the comment sent (for example, add it to a list or send it to a server)
    }
}
