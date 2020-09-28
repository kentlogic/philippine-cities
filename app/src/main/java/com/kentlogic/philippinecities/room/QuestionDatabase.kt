package com.kentlogic.philippinecities.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kentlogic.philippinecities.model.Question

//Using arrayof for scaling in the future
@Database(entities = arrayOf(Question::class), version = 1)
abstract class QuestionDatabase: RoomDatabase() {
    abstract fun questionDao(): QuestionsDao

    companion object {
        @Volatile private var instance: QuestionDatabase? = null
        private val LOCK = Any()

                operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
                    instance ?: buildDatabase(context).also {
                        instance = it
                    }
                }
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            QuestionDatabase::class.java,
            "question_db"
        ).build()
    }
}