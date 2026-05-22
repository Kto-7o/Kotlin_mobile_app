package com.example.kotlinprogectapp.ui.screens.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.kotlinprogectapp.domain.model.ProofType
import com.example.kotlinprogectapp.ui.components.AvatarView
import com.example.kotlinprogectapp.ui.components.ChipSelector
import com.example.kotlinprogectapp.ui.theme.Orange500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateChallengeScreen(navController: NavController) {
    val vm: CreateViewModel = hiltViewModel()
    val state   by vm.uiState.collectAsStateWithLifecycle()
    val friends by vm.friends.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Новый челлендж") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            // Название
            OutlinedTextField(
                value         = state.title,
                onValueChange = vm::onTitleChanged,
                label         = { Text("Название задания") },
                singleLine    = true,
                modifier      = Modifier.fillMaxWidth()
            )

            // Описание
            OutlinedTextField(
                value         = state.description,
                onValueChange = vm::onDescriptionChanged,
                label         = { Text("Описание и правила") },
                minLines      = 3,
                modifier      = Modifier.fillMaxWidth()
            )

            // Дедлайн
            Column {
                Text("Дедлайн", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(6.dp))
                ChipSelector(
                    options  = listOf(1, 3, 7, 30),
                    selected = state.deadlineDays,
                    label    = { "${it}д" },
                    onSelect = vm::onDeadlineChanged
                )
            }


            Column {
                Text("Доказательство", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(6.dp))
                ChipSelector(
                    options  = ProofType.values().toList(),
                    selected = state.proofType,
                    label    = { when (it) {
                        ProofType.PHOTO      -> "Фото"
                        ProofType.VIDEO      -> "Видео"
                        ProofType.SCREENSHOT -> "Скриншот"
                    }},
                    onSelect = vm::onProofTypeChanged
                )
            }


            if (friends.isNotEmpty()) {
                Column {
                    Text("Пригласить друзей", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(friends) { friend ->
                            val selected = friend.id in state.invitedFriends
                            FilterChip(
                                selected = selected,
                                onClick  = { vm.onFriendToggled(friend.id) },
                                label    = { Text(friend.username) },
                                leadingIcon = { AvatarView(name = friend.username, size = 22.dp) },
                                colors   = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Orange500.copy(alpha = 0.15f),
                                    selectedLabelColor     = Orange500
                                )
                            )
                        }
                    }
                }
            }

            if (state.error != null) {
                Text(state.error!!, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium)
            }

            Button(
                onClick  = vm::onSubmit,
                enabled  = state.isValid && !state.isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Orange500)
            ) {
                if (state.isLoading) CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                else Text("Создать челлендж", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
