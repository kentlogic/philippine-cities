package com.kentlogic.philippinecities.view

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.kentlogic.philippinecities.R
import com.kentlogic.philippinecities.Util.Constants
import kotlinx.android.synthetic.main.activity_quiz_result.*

class QuizResult : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val username = intent.getStringExtra(Constants.USER_NAME)
        val totalPoints = intent.getStringExtra(Constants.CORRECT_ANSWERS)
        val totalQuestions = intent.getStringExtra(Constants.TOTAL_QUESTIONS)

        //High score if user has 3 or less wrong answers
        if (totalQuestions.toInt() - 3 <= totalPoints.toInt()) {
            tv_username.text = "Congratulations $username!"
            tv_score.text = "Your score is $totalPoints out of $totalQuestions"
            Glide.with(this)
                .load(getDrawable(R.drawable.confettie))
                .into(img_animation)
        } else {
            tv_username.text = "Nice try $username!"
            tv_score.text = "Your score is $totalPoints out of $totalQuestions"
            img_trophy.setImageResource(R.drawable.img_trophy_fail)

        }

        btn_quiz_result.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}

