package com.kentlogic.philippinecities.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kentlogic.philippinecities.model.Question

@Dao
interface QuestionsDao {
    @Insert
    suspend fun insertAllQuestions(vararg question: Question)

    @Query("SELECT * FROM question ORDER BY RANDOM()")
    suspend fun getQuestions(): List<Question>

    @Query("DELETE FROM question")
    suspend fun deleteQuestions()
}