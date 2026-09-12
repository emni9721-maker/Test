package app.zero.vault.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.zero.vault.model.*
import coil.compose.AsyncImage

/**
 * Vault PIN Verification Dialog to unlock AES-256 encrypted folders
 */
@Composable
fun VaultPinDialog(
    albumName: String,
    onDismiss: () -> Unit,
    onPinEntered: (String) -> Boolean
) {
    var enteredPin by remember { mutableStateOf("") }
    var hasError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("vault_pin_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Encrypted Vault",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Text(
                    text = "Encrypted Folder Access",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Enter 4-digit security PIN to unlock \"$albumName\" (Default: 1234)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = enteredPin,
                    onValueChange = {
                        if (it.length <= 4) {
                            enteredPin = it
                            hasError = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("vault_pin_input"),
                    singleLine = true,
                    isError = hasError,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    label = { Text("4-Digit PIN") },
                    supportingText = {
                        if (hasError) {
                            Text("Incorrect PIN code. Try '1234'", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val success = onPinEntered(enteredPin)
                            if (!success) {
                                hasError = true
                            }
                        },
                        enabled = enteredPin.length == 4,
                        modifier = Modifier.testTag("vault_unlock_button")
                    ) {
                        Text("Unlock")
                    }
                }
            }
        }
    }
}

/**
 * Media Upload Dialog (Adds new item to local vault and simulates encryption)
 */
@Composable
fun UploadMediaDialog(
    albums: List<Album>,
    onDismiss: () -> Unit,
    onUploadConfirmed: (url: String, filename: String, caption: String, type: MediaType, albumId: String?) -> Unit
) {
    var caption by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(MediaType.IMAGE) }
    var selectedAlbumId by remember { mutableStateOf<String?>(albums.firstOrNull()?.id) }

    // Pre-curated sample cloud media URLs for instant mock preview
    val sampleUrls = listOf(
        "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=1200&q=80",
        "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=1200&q=80",
        "https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=1200&q=80",
        "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=1200&q=80"
    )
    var chosenUrl by remember { mutableStateOf(sampleUrls[0]) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("upload_media_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Import to ZERO Vault",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Media Preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    AsyncImage(
                        model = chosenUrl,
                        contentDescription = "Upload Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "AES-256 E2EE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Media Type Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedType == MediaType.IMAGE,
                        onClick = { selectedType = MediaType.IMAGE },
                        label = { Text("Photo (RAW)") }
                    )
                    FilterChip(
                        selected = selectedType == MediaType.VIDEO,
                        onClick = { selectedType = MediaType.VIDEO },
                        label = { Text("4K Video") }
                    )
                }

                OutlinedTextField(
                    value = caption,
                    onValueChange = { caption = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("upload_caption_input"),
                    label = { Text("Caption or description") },
                    placeholder = { Text("Captured in raw format...") }
                )

                Button(
                    onClick = {
                        val filename = if (selectedType == MediaType.VIDEO) "ZERO_VID_Captured.mp4" else "ZERO_IMG_Captured.jpg"
                        onUploadConfirmed(chosenUrl, filename, caption, selectedType, selectedAlbumId)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("upload_confirm_button")
                ) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Encrypt & Store to Cloud")
                }
            }
        }
    }
}

/**
 * Create Post Dialog (Feed Composer)
 */
@Composable
fun CreatePostDialog(
    onDismiss: () -> Unit,
    onPostCreated: (content: String, mediaUrl: String?, privacy: PostPrivacy) -> Unit
) {
    var contentText by remember { mutableStateOf("") }
    var privacy by remember { mutableStateOf(PostPrivacy.PUBLIC) }
    var attachImage by remember { mutableStateOf(true) }
    val defaultMediaUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=1200&q=80"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("create_post_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "New Feed Post",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                OutlinedTextField(
                    value = contentText,
                    onValueChange = { contentText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("post_content_input"),
                    placeholder = { Text("Share an encrypted vault update or caption...") }
                )

                // Privacy selector chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = privacy == PostPrivacy.PUBLIC,
                        onClick = { privacy = PostPrivacy.PUBLIC },
                        label = { Text("Public") },
                        leadingIcon = { Icon(Icons.Default.Public, contentDescription = null, modifier = Modifier.size(14.dp)) }
                    )
                    FilterChip(
                        selected = privacy == PostPrivacy.FRIENDS,
                        onClick = { privacy = PostPrivacy.FRIENDS },
                        label = { Text("Friends") },
                        leadingIcon = { Icon(Icons.Default.Group, contentDescription = null, modifier = Modifier.size(14.dp)) }
                    )
                    FilterChip(
                        selected = privacy == PostPrivacy.PRIVATE,
                        onClick = { privacy = PostPrivacy.PRIVATE },
                        label = { Text("Private") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp)) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = attachImage,
                            onCheckedChange = { attachImage = it }
                        )
                        Text("Attach Vault Photo", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Button(
                    onClick = {
                        if (contentText.isNotBlank()) {
                            onPostCreated(contentText, if (attachImage) defaultMediaUrl else null, privacy)
                        }
                    },
                    enabled = contentText.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("publish_post_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Publish Post")
                }
            }
        }
    }
}

/**
 * Messenger Dialog (Encrypted Direct Chats)
 */
@Composable
fun MessengerDialog(
    conversations: List<Conversation>,
    onDismiss: () -> Unit,
    onSendMessage: (receiverId: String, text: String) -> Unit
) {
    var activeConversation by remember { mutableStateOf(conversations.firstOrNull()) }
    var draftMessage by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f)
                .padding(8.dp)
                .testTag("messenger_modal"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Chat Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AsyncImage(
                            model = activeConversation?.otherUser?.avatarUrl,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                        )
                        Column {
                            Text(
                                text = activeConversation?.otherUser?.displayName ?: "Direct Chat",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "End-to-End Encrypted (Zero Vault)",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close Messenger")
                    }
                }

                // Chat Messages Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Seed chat messages
                    Box(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = activeConversation?.lastMessage?.text ?: "Hello on ZERO!",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.End)
                            .clip(RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Glad to connect! All vault transfers are encrypted with AES-GCM.",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 13.sp
                        )
                    }
                }

                // Chat Input Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = draftMessage,
                        onValueChange = { draftMessage = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("messenger_input"),
                        placeholder = { Text("Write encrypted message...") },
                        singleLine = true
                    )

                    IconButton(
                        onClick = {
                            if (draftMessage.isNotBlank() && activeConversation != null) {
                                onSendMessage(activeConversation!!.otherUser.id, draftMessage)
                                draftMessage = ""
                            }
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .testTag("messenger_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

/**
 * Story Viewer Modal
 */
@Composable
fun StoryViewerModal(
    story: Story,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .testTag("story_viewer_modal")
        ) {
            AsyncImage(
                model = story.mediaUrl,
                contentDescription = "Story Media",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Story top progress and author bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp)
            ) {
                LinearProgressIndicator(
                    progress = { 0.7f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AsyncImage(
                            model = story.user.avatarUrl,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                        )
                        Text(
                            text = story.user.displayName,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close Story", tint = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }

            // Story bottom caption
            story.caption?.let { cap ->
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                        .padding(20.dp)
                ) {
                    Text(
                        text = cap,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

/**
 * Full-screen Photo Detail Viewer
 */
@Composable
fun PhotoDetailModal(
    photo: PhotoItem,
    onDismiss: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    onDeletePhoto: (String) -> Unit,
    onRestorePhoto: (String) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .testTag("photo_detail_modal")
        ) {
            // Main Photo / Media
            AsyncImage(
                model = photo.url,
                contentDescription = photo.caption,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            // Top control bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 12.dp, end = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                }

                Row {
                    IconButton(onClick = { onToggleFavorite(photo.id) }) {
                        Icon(
                            imageVector = if (photo.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (photo.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                        )
                    }

                    if (photo.isDeleted) {
                        IconButton(onClick = { onRestorePhoto(photo.id) }) {
                            Icon(Icons.Default.RestoreFromTrash, contentDescription = "Restore", tint = MaterialTheme.colorScheme.primary)
                        }
                    } else {
                        IconButton(onClick = {
                            onDeletePhoto(photo.id)
                            onDismiss()
                        }) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Move to Trash", tint = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                }
            }

            // Bottom metadata info card
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = photo.filename,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (photo.caption.isNotBlank()) {
                        Text(
                            text = photo.caption,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Size: ${photo.sizeBytes / (1024 * 1024)} MB",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        photo.location?.let { loc ->
                            Text(
                                text = "📍 $loc",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
