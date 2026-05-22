package com.example.kotlinprogectapp.data.local.dao



import androidx.room.*
import com.example.kotlinprogectapp.data.local.entity.ChallengeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {

    @Query("SELECT * FROM challenges WHERE tab = :tab")
    fun getChallengesByTab(tab: String): Flow<List<ChallengeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(challenges: List<ChallengeEntity>)

    @Query("DELETE FROM challenges WHERE tab = :tab")
    suspend fun deleteByTab(tab: String)
}