package com.example.kotlinprogectapp.ui.screens.feed


import android.os.Build
import androidx.annotation.RequiresApi
import com.example.kotlinprogectapp.domain.model.ChallengeStatus
import com.example.kotlinprogectapp.domain.model.FeedTab
import com.example.kotlinprogectapp.domain.model.ProofType
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class FeedUiState(
    val activeTab:  FeedTab          = FeedTab.ACTIVE,
    val challenges: List<ChallengeUi> = emptyList(),
    val isLoading:  Boolean          = false,
    val error:      String?          = null
)

data class ChallengeUi(
    val id:               Long,
    val title:            String,
    val creatorName:      String,
    val deadlineLabel:    String,
    val participantCount: Int,
    val status:           ChallengeStatus,
    val proofType:        ProofType,
    val pendingProofs:    List<ProofUi> = emptyList()
)

data class ProofUi(
    val id:             Long,
    val userId:         Long,
    val userName:       String,
    val mediaUrl:       String,
    val challengeTitle: String
)


@RequiresApi(Build.VERSION_CODES.O)
fun formatDeadline(deadline: LocalDate): String {
    val daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), deadline)
    return when {
        daysLeft < 0  -> "Истёк"
        daysLeft == 0L -> "Последний день"
        daysLeft == 1L -> "Остался 1 день"
        else           -> "Осталось $daysLeft дн."
    }
}