package com.example.kotlinprogectapp.ui.screens.friends

import com.example.kotlinprogectapp.domain.model.Friend
import com.example.kotlinprogectapp.domain.model.FriendRequest

data class FriendsUiState(
    val activeTab:        FriendsTab           = FriendsTab.MY_FRIENDS,
    val friends:          List<Friend>         = emptyList(),
    val incomingRequests: List<FriendRequest>  = emptyList(),
    val outgoingRequests: List<FriendRequest>  = emptyList(),
    val searchQuery:      String               = "",
    val searchResults:    List<Friend>         = emptyList(),
    val isSearching:      Boolean              = false,
    val isLoading:        Boolean              = false,
    val error:            String?              = null
)

enum class FriendsTab { MY_FRIENDS, REQUESTS }

val FriendsUiState.requestsBadge: Int?
    get() = incomingRequests.size.takeIf { it > 0 }
