package com.example.kotlinprogectapp.ui.screens.friends

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.kotlinprogectapp.domain.model.Friend
import com.example.kotlinprogectapp.domain.model.FriendRequest
import com.example.kotlinprogectapp.domain.model.UserRelation
import com.example.kotlinprogectapp.ui.components.AvatarView
import com.example.kotlinprogectapp.ui.navigation.Screen
import com.example.kotlinprogectapp.ui.theme.Green
import com.example.kotlinprogectapp.ui.theme.Orange500
import com.example.kotlinprogectapp.ui.theme.Red

@Composable
fun FriendsScreen(navController: NavController) {
    val vm: FriendsViewModel = hiltViewModel()
    val state by vm.uiState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf<Friend?>(null) }

    showDeleteDialog?.let { friend ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title   = { Text("Удалить друга?") },
            text    = { Text("${friend.username} будет удалён из списка друзей") },
            confirmButton = {
                TextButton(onClick = {
                    vm.onDeleteFriend(friend.id)
                    showDeleteDialog = null
                }) { Text("Удалить", color = Red) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) { Text("Отмена") }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Поиск
        SearchBar(
            query         = state.searchQuery,
            onQueryChange = vm::onSearchQueryChanged,
            modifier      = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (state.searchQuery.isNotBlank()) {
            // Режим поиска
            SearchResultsList(
                results         = state.searchResults,
                isSearching     = state.isSearching,
                onSendRequest   = vm::onSendRequest,
                onProfileClick  = { navController.navigate(Screen.FriendProfile.createRoute(it)) }
            )
        } else {

            TabRow(
                selectedTabIndex = state.activeTab.ordinal,
                containerColor   = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = state.activeTab == FriendsTab.MY_FRIENDS,
                    onClick  = { vm.onTabSelected(FriendsTab.MY_FRIENDS) },
                    text     = { Text("Мои друзья") }
                )
                Tab(
                    selected = state.activeTab == FriendsTab.REQUESTS,
                    onClick  = { vm.onTabSelected(FriendsTab.REQUESTS) },
                    text     = {
                        BadgedBox(badge = {
                            state.requestsBadge?.let { Badge { Text("$it") } }
                        }) { Text("Запросы") }
                    }
                )
            }

            when (state.activeTab) {
                FriendsTab.MY_FRIENDS -> FriendsList(
                    friends        = state.friends,
                    onProfileClick = { navController.navigate(Screen.FriendProfile.createRoute(it)) },
                    onLongPress    = { showDeleteDialog = it }
                )
                FriendsTab.REQUESTS  -> RequestsList(
                    incoming  = state.incomingRequests,
                    outgoing  = state.outgoingRequests,
                    onRespond = vm::onRequestRespond
                )
            }
        }
    }
}

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit, modifier: Modifier) {
    OutlinedTextField(
        value         = query,
        onValueChange = onQueryChange,
        modifier      = modifier.fillMaxWidth(),
        placeholder   = { Text("Поиск по имени или @тегу") },
        leadingIcon   = { Icon(Icons.Default.Search, contentDescription = null) },
        singleLine    = true,
        shape         = RoundedCornerShape(12.dp)
    )
}

@Composable
private fun SearchResultsList(
    results:        List<Friend>,
    isSearching:    Boolean,
    onSendRequest:  (Long) -> Unit,
    onProfileClick: (Long) -> Unit
) {
    if (isSearching) {
        Box(Modifier.fillMaxWidth().padding(16.dp), Alignment.Center) {
            CircularProgressIndicator(color = Orange500)
        }
        return
    }
    LazyColumn {
        items(results) { friend ->
            FriendRow(
                friend = friend,
                onClick = { onProfileClick(friend.id) },
                trailing = {
                    when (friend.relation) {
                        UserRelation.NONE    -> TextButton(onClick = { onSendRequest(friend.id) }) {
                            Text("Добавить", color = Orange500)
                        }
                        UserRelation.PENDING -> TextButton(onClick = {}, enabled = false) {
                            Text("Отправлен")
                        }
                        UserRelation.FRIEND  -> TextButton(onClick = {}, enabled = false) {
                            Text("Друзья")
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FriendsList(
    friends:       List<Friend>,
    onProfileClick: (Long) -> Unit,
    onLongPress:   (Friend) -> Unit
) {
    LazyColumn {
        items(friends) { friend ->
            FriendRow(
                friend    = friend,
                modifier  = Modifier.combinedClickable(
                    onClick     = { onProfileClick(friend.id) },
                    onLongClick = { onLongPress(friend) }
                ),
                trailing  = {
                    Text(
                        "${friend.activeChallenges} активных",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        }
    }
}

@Composable
private fun RequestsList(
    incoming: List<FriendRequest>,
    outgoing: List<FriendRequest>,
    onRespond: (Long, Boolean) -> Unit
) {
    LazyColumn {
        if (incoming.isNotEmpty()) {
            item {
                Text("Входящие", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
            items(incoming) { request ->
                FriendRow(
                    friend   = request.fromUser,
                    trailing = {
                        Row {
                            IconButton(onClick = { onRespond(request.id, true) }) {
                                Text("✓", color = Green)
                            }
                            IconButton(onClick = { onRespond(request.id, false) }) {
                                Text("✕", color = Red)
                            }
                        }
                    }
                )
            }
        }
        if (outgoing.isNotEmpty()) {
            item {
                Text("Отправленные", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
            items(outgoing) { request ->
                FriendRow(
                    friend   = request.fromUser,
                    trailing = {
                        TextButton(onClick = {}, enabled = false) { Text("Отправлен") }
                    }
                )
            }
        }
    }
}

@Composable
private fun FriendRow(
    friend:   Friend,
    modifier: Modifier = Modifier,
    onClick:  (() -> Unit)? = null,
    trailing: @Composable () -> Unit
) {
    ListItem(
        modifier     = modifier,
        headlineContent   = { Text(friend.username, fontWeight = FontWeight.SemiBold) },
        supportingContent = { Text("@${friend.tag}", style = MaterialTheme.typography.bodyMedium) },
        leadingContent    = { AvatarView(name = friend.username, size = 42.dp) },
        trailingContent   = trailing
    )
    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
}
