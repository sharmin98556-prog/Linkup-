package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PostEntity
import com.example.data.model.StoryEntity
import com.example.data.model.UserProfile
import com.example.ui.components.PostCard
import com.example.ui.components.PostComposerCard
import com.example.ui.components.StoriesCarousel

@Composable
fun FeedScreen(
    posts: List<PostEntity>,
    stories: List<StoryEntity>,
    userProfile: UserProfile,
    searchQuery: String,
    onCreateStoryClick: () -> Unit,
    onStoryClick: (StoryEntity) -> Unit,
    onComposerClick: () -> Unit,
    onReactionSelect: (postId: Long, reaction: String) -> Unit,
    onCommentsClick: (postId: Long) -> Unit,
    onShareClick: (postId: Long) -> Unit,
    onSaveClick: (postId: Long) -> Unit,
    onDeleteClick: (postId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Only show Stories & Post Composer when not filtering by search
        if (searchQuery.isBlank()) {
            item {
                StoriesCarousel(
                    stories = stories,
                    userProfile = userProfile,
                    onCreateStoryClick = onCreateStoryClick,
                    onStoryClick = onStoryClick
                )
            }

            item {
                PostComposerCard(
                    userProfile = userProfile,
                    onComposerClick = onComposerClick,
                    onPhotoClick = onComposerClick,
                    onFeelingClick = onComposerClick
                )
            }
        } else {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Search results for \"$searchQuery\" (${posts.size} found)",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (posts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isNotBlank()) "No posts found matching your search." else "No posts yet. Be the first to share something!",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            items(posts, key = { it.id }) { post ->
                PostCard(
                    post = post,
                    onReactionSelect = { reaction -> onReactionSelect(post.id, reaction) },
                    onCommentsClick = { onCommentsClick(post.id) },
                    onShareClick = { onShareClick(post.id) },
                    onSaveClick = { onSaveClick(post.id) },
                    onDeleteClick = { onDeleteClick(post.id) },
                    isCurrentUserPost = post.authorName == userProfile.name
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
