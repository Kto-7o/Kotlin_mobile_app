package com.example.kotlinprogectapp.ui.screens.feed

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.pulltorefresh.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kotlinprogectapp.domain.model.ChallengeStatus
import com.example.kotlinprogectapp.ui.components.*
import com.example.kotlinprogectapp.ui.navigation.Screen
import com.example.kotlinprogectapp.ui.theme.Green
import com.example.kotlinprogectapp.ui.theme.Orange500
import com.example.kotlinprogectapp.ui.theme.Red

@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(navController: NavController) {
    val vm: FeedViewModel = hiltViewModel()
    val state by vm.uiState.collectAsStateWithLifecycle()

//    Scaffold(
////        topBar = {
////            TopAppBar(
////                title = {
////                    Text("Goal", fontWeight = FontWeight.ExtraBold, color = Orange500)
////                },
////                actions = {
////                    IconButton(onClick = { navController.navigate(Screen.Create.route) }) {
////                        Icon(Icons.Default.Add, contentDescription = "Создать")
////                    }
////                }
////            )
////        }
//    ) { padding ->
//        PullToRefreshBox(
//            isRefreshing = state.isLoading,
//            onRefresh = vm::onRefresh,
//            modifier = Modifier.padding(padding)
//        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                FeedTabRow(activeTab = state.activeTab, onSelect = vm::onTabSelected)

                when {
                    state.isLoading && state.challenges.isEmpty() ->
                        LoadingView()
                    state.error != null ->
                        ErrorState(message = state.error!!, onRetry = vm::onRefresh)
                    state.challenges.isEmpty() ->
                        EmptyState("🏆", "Нет активных челленджей\nСоздайте первый!")
                    else -> FeedList(
                        challenges = state.challenges,
                        onVerdictSubmit = vm::onVerdictSubmit,
                        onCreateClick = { navController.navigate(Screen.Create.route) }
                    )
                }
            }
        }
//    }
//}

@Composable
private fun FeedTabRow(activeTab: com.example.kotlinprogectapp.domain.model.FeedTab, onSelect: (com.example.kotlinprogectapp.domain.model.FeedTab) -> Unit) {
    val tabs = listOf(
        com.example.kotlinprogectapp.domain.model.FeedTab.ACTIVE   to "Активные",
        com.example.kotlinprogectapp.domain.model.FeedTab.INCOMING to "Входящие",
        com.example.kotlinprogectapp.domain.model.FeedTab.ARCHIVE  to "Архив"
    )
    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        tabs.forEach { (tab, label) ->
            FilterChip(
                selected = activeTab == tab,
                onClick  = { onSelect(tab) },
                label    = { Text(label) },
                colors   = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Orange500.copy(alpha = 0.15f),
                    selectedLabelColor     = Orange500
                )
            )
        }
    }
}

@Composable
private fun FeedList(
    challenges:      List<ChallengeUi>,
    onVerdictSubmit: (Long, Boolean) -> Unit,
    onCreateClick:   () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(challenges) { challenge ->
            challenge.pendingProofs.forEach { proof ->
                PendingProofCard(
                    proof     = proof,
                    onAccept  = { onVerdictSubmit(proof.id, true) },
                    onDecline = { onVerdictSubmit(proof.id, false) }
                )
                Spacer(Modifier.height(6.dp))
            }
            ChallengeCard(challenge = challenge)
        }
    }
}

@Composable
private fun ChallengeCard(challenge: ChallengeUi) {
    Card(
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                AvatarView(name = challenge.creatorName, size = 36.dp)
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(challenge.creatorName, style = MaterialTheme.typography.titleMedium)
                }
                StatusBadge(challenge.status)
            }
            Spacer(Modifier.height(10.dp))
            Text(challenge.title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("⏱ ${challenge.deadlineLabel}", style = MaterialTheme.typography.bodyMedium)
                Text("👥 ${challenge.participantCount}", style = MaterialTheme.typography.bodyMedium)
            }
            CardFooter(status = challenge.status)
        }
    }
}

@Composable
private fun CardFooter(status: ChallengeStatus) {
    if (status == ChallengeStatus.EXPIRED) return
    Spacer(Modifier.height(10.dp))
    HorizontalDivider(
        Modifier,
        DividerDefaults.Thickness,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    )
    Spacer(Modifier.height(8.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        when (status) {
            ChallengeStatus.ACTIVE ->
                TextButton(onClick = {}) { Text("Загрузить доказательство", color = Orange500) }
            ChallengeStatus.COMPLETED -> {
                TextButton(onClick = {}) { Text("Результаты") }
                TextButton(onClick = {}) { Text("Реванш") }
            }
            ChallengeStatus.INCOMING -> {
                TextButton(onClick = {}) { Text("Принять",   color = Green) }
                TextButton(onClick = {}) { Text("Отклонить", color = Red) }
            }
            else -> {}
        }
    }
}

@Composable
private fun StatusBadge(status: ChallengeStatus) {
    val (label, color) = when (status) {
        ChallengeStatus.ACTIVE    -> "Активен"  to Orange500
        ChallengeStatus.COMPLETED -> "Выполнен" to Green
        ChallengeStatus.EXPIRED   -> "Истёк"    to MaterialTheme.colorScheme.outline
        ChallengeStatus.INCOMING  -> "Входящий" to MaterialTheme.colorScheme.primary
    }
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(label, modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun PendingProofCard(proof: ProofUi, onAccept: () -> Unit, onDecline: () -> Unit) {
    Card(
        shape  = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Orange500.copy(alpha = 0.4f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Ожидает вашей оценки",
                style = MaterialTheme.typography.labelSmall, color = Orange500)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                // Превью медиафайла через Coil
                AsyncImage(
                    model             = proof.mediaUrl,
                    contentDescription = "Доказательство",
                    modifier          = Modifier.size(48.dp)
                )
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(proof.userName, style = MaterialTheme.typography.titleMedium)
                    Text(proof.challengeTitle, style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick  = onAccept,
                    modifier = Modifier.weight(1f),
                    colors   = ButtonDefaults.buttonColors(containerColor = Green.copy(alpha = 0.15f)),
                ) { Text("Засчитать", color = Green) }
                Button(
                    onClick  = onDecline,
                    modifier = Modifier.weight(1f),
                    colors   = ButtonDefaults.buttonColors(containerColor = Red.copy(alpha = 0.1f)),
                ) { Text("Отклонить", color = Red) }
            }
        }
    }
}



