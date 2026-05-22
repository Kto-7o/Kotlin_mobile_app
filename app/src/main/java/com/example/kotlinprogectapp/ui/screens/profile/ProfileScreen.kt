package com.example.kotlinprogectapp.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.kotlinprogectapp.domain.model.ChallengeResult
import com.example.kotlinprogectapp.domain.model.ChallengeHistoryItem
import com.example.kotlinprogectapp.domain.model.User
import com.example.kotlinprogectapp.domain.model.UserRelation
import com.example.kotlinprogectapp.domain.model.UserStats
import com.example.kotlinprogectapp.ui.components.AvatarView
import com.example.kotlinprogectapp.ui.components.LoadingView
import com.example.kotlinprogectapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, userId: Long? = null) {
    val vm: ProfileViewModel = hiltViewModel()
    val state by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.events.collect { route ->
            navController.navigate(route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (state.isOwnProfile) Text("Профиль")
                    else Text(state.user?.username ?: "")
                },
                navigationIcon = {
                    if (!state.isOwnProfile) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                        }
                    }
                }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> LoadingView(Modifier.padding(padding))
            state.user == null -> Box(
                Modifier.fillMaxSize().padding(padding),
                Alignment.Center
            ) { Text("Не удалось загрузить профиль") }
            else -> LazyColumn(
                modifier       = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                item { ProfileHero(user = state.user!!) }

                // Кнопка Добавить — только на чужом профиле
                if (!state.isOwnProfile) {
                    item { AddFriendButton(relation = state.relation, onAdd = vm::onAddFriend) }
                }

                state.stats?.let { stats ->
                    item { StatsRow(stats = stats) }
                }

                if (state.history.isNotEmpty()) {
                    item {
                        Text(
                            "История",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style    = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(state.history) { item -> HistoryItem(item) }
                }

                if (state.isOwnProfile) {
                    item {
                        Spacer(Modifier.height(16.dp))
                        TextButton(
                            onClick  = vm::onLogout,
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Выйти", color = Red) }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileHero(user: User) {
    Column(
        modifier            = Modifier.fillMaxWidth().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AvatarView(name = user.username, size = 72.dp)
        Spacer(Modifier.height(12.dp))
        Text(user.username, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("@${user.tag}", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun AddFriendButton(relation: UserRelation, onAdd: () -> Unit) {
    val (label, enabled) = when (relation) {
        UserRelation.NONE    -> "Добавить в друзья"  to true
        UserRelation.PENDING -> "Запрос отправлен"    to false
        UserRelation.FRIEND  -> "Уже друзья"          to false
    }
    Button(
        onClick  = onAdd,
        enabled  = enabled,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        colors   = ButtonDefaults.buttonColors(containerColor = Orange500)
    ) { Text(label, fontWeight = FontWeight.SemiBold) }
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun StatsRow(stats: UserStats) {
    Row(
        modifier            = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(
            "Создано"   to stats.created.toString(),
            "Выполнено" to stats.completed.toString(),
            "Успех"     to "${stats.successRate}%"
        ).forEach { (label, value) ->
            Card(
                modifier = Modifier.weight(1f),
                colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape    = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier            = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(value, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text(label, style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(item: ChallengeHistoryItem) {
    val (icon, color) = when (item.result) {
        ChallengeResult.SUCCESS -> "✓" to Green
        ChallengeResult.FAIL    -> "✕" to Red
        ChallengeResult.EXPIRED -> "⏱" to MaterialTheme.colorScheme.outline
    }
    ListItem(
        headlineContent   = { Text(item.title, style = MaterialTheme.typography.bodyLarge) },
        supportingContent = { Text(item.date, style = MaterialTheme.typography.bodyMedium) },
        leadingContent    = {
            Surface(
                shape  = RoundedCornerShape(8.dp),
                color  = color.copy(alpha = 0.12f),
                modifier = Modifier.size(32.dp)
            ) {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text(icon, color = color, fontWeight = FontWeight.Bold)
                }
            }
        }
    )
    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
}