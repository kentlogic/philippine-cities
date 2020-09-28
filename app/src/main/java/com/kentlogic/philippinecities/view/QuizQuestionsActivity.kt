package com.kentlogic.philippinecities.view

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kentlogic.philippinecities.R
import com.kentlogic.philippinecities.Util.Constants
import com.kentlogic.philippinecities.model.Question
import com.kentlogic.philippinecities.viewmodel.QuestionViewModel
import kotlinx.android.synthetic.main.activity_quiz_questions.*
import java.lang.Exception

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {
    private var mCurrentPosition: Int = 1

    //private var mQuestionList: ArrayList<Question>? = null
    private lateinit var mQuestionList: List<Question>
    private var mSelectedPosition: Int = 0
    private var totalPoints: Int = 0
    private var mUserName: String? = null
    private lateinit var viewModel: QuestionViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        viewModel = ViewModelProviders.of(this).get(QuestionViewModel::class.java)
        viewModel.getQuestions()
        mUserName = intent.getStringExtra(Constants.USER_NAME)

        observeViewModel()

        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
    }


    private fun setQuestion() {
        progressBar.max = mQuestionList!!.size
        val question = mQuestionList!![mCurrentPosition - 1]

        progressBar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition" + "/" + progressBar.max

        tv_question.text = question!!.question

        try {
            Glide.with(this)
                .load(question.image)
                .fitCenter()
                .into(iv_image)
        } catch (e: Exception) {
            Snackbar.make(ll_queston_container, "Failed to load the image.", Snackbar.LENGTH_LONG)
                .show()
        }
        tv_option_one.text = question.optionOne
        tv_option_two.text = question.optionTwo
        tv_option_three.text = question.optionThree
        tv_option_four.text = question.optionFour
        defaultOptionsView()

        if (mCurrentPosition == mQuestionList!!.size) {
            btn_submit.text = "Finish"
        } else {
            btn_submit.text = "Submit"
        }
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, tv_option_one)
        options.add(1, tv_option_two)
        options.add(2, tv_option_three)
        options.add(3, tv_option_four)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_option_one -> {
                selectedOption(tv_option_one, 1)
            }
            R.id.tv_option_two -> {
                selectedOption(tv_option_two, 2)
            }
            R.id.tv_option_three -> {
                selectedOption(tv_option_three, 3)
            }
            R.id.tv_option_four -> {
                selectedOption(tv_option_four, 4)
            }
            R.id.btn_submit -> {
                //Proceed to next question if no answer is selected
                if (mSelectedPosition == 0) {
                    mCurrentPosition++

                    when {
                        //Check the question position
                        mCurrentPosition <= mQuestionList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            val result = Intent(this, QuizResult::class.java)
                            result.putExtra(Constants.USER_NAME, mUserName)
                            result.putExtra(Constants.CORRECT_ANSWERS, totalPoints.toString())
                            result.putExtra(
                                Constants.TOTAL_QUESTIONS,
                                mQuestionList!!.size.toString()
                            )
                            result.putExtra(Constants.USER_NAME, mUserName)
                            startActivity(result)
                        }
                    }
                } else {
                    val question = mQuestionList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer != mSelectedPosition) {
                        answerView(mSelectedPosition, R.drawable.wrong_option_border_bg)
                    } else {
                        totalPoints++
                    }

                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    //Update button if on the last question
                    if (mCurrentPosition == mQuestionList!!.size) {
                        btn_submit.text = "Finish"
                    } else {
                        btn_submit.text = "Next Question"
                    }
                    //Reset and proceed to next question
                    mSelectedPosition = 0
                }

            }
        }
    }

    //Check the answer and set the background drawable
    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                tv_option_one.background = ContextCompat.getDrawable(this, drawableView)
            }
            2 -> {
                tv_option_two.background = ContextCompat.getDrawable(this, drawableView)
            }
            3 -> {
                tv_option_three.background = ContextCompat.getDrawable(this, drawableView)
            }
            4 -> {
                tv_option_four.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }

    private fun selectedOption(tv: TextView, selectedOptionNum: Int) {
        defaultOptionsView()
        tv.setTextColor(Color.parseColor("#363A43"))
        mSelectedPosition = selectedOptionNum
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.setTextColor(Color.WHITE)
        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
    }

    private fun observeViewModel() {

        viewModel.questions.observe(this, Observer {
            it?.let {
                mQuestionList = it
                if (!it.isNullOrEmpty()) {
                    setQuestion()
                    ll_queston_container.visibility = View.VISIBLE
                } else {
                    ll_queston_container.visibility = View.GONE
                }

            }
        })

        viewModel.isLoading.observe(this, Observer {
            it?.let {
                pb_loading_questions.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    ll_queston_container.visibility = View.GONE
                }
            }
        })

        viewModel.errorMsg.observe(this, Observer {
            it?.let {
                if (!it.isEmpty()) startActivity(Intent(this, MainActivity::class.java))
            }
        })
    }
}