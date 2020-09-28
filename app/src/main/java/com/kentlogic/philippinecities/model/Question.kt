package com.kentlogic.philippinecities.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Question (
    val question: String,
    val image: String,
    val optionOne: String,
    val optionTwo: String,
    val optionThree: String,
    val optionFour: String,
    val correctAnswer: Int,
    ) {
    @PrimaryKey(autoGenerate = true)
    var qid: Int? = null;
}