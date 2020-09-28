package com.kentlogic.philippinecities.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.kentlogic.philippinecities.R
import com.kentlogic.philippinecities.Util.Constants
import com.kentlogic.philippinecities.viewmodel.QuestionViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: QuestionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        viewModel = ViewModelProviders.of(this).get(QuestionViewModel::class.java)
        viewModel.updateQuestions()
        observeViewModel()

        btn_download.setOnClickListener {
            viewModel.updateQuestions()
            viewModel.getQuestions()
        }

        btn_start.setOnClickListener {
            if (tv_name.text.toString().isNullOrEmpty()) {
                Snackbar.make(cv_container, "Please enter your name.", Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                val intent = Intent(this, QuizQuestionsActivity::class.java)
                intent.putExtra(Constants.USER_NAME, tv_name.text.toString())
                startActivity(intent)
                finish()
            }
        }
    }

    private fun observeViewModel() {

        viewModel.questions.observe(this, Observer {
            it?.let {
                cv_container.visibility = View.VISIBLE
                btn_start.visibility = if(!it.isNullOrEmpty()) View.VISIBLE else View.GONE
                btn_download.visibility = if(it.isNullOrEmpty()) View.VISIBLE else View.GONE

            }
        })

        viewModel.isLoading.observe(this, Observer {
            it?.let{
                ll_download_questions.visibility = if(it) View.VISIBLE else View.GONE
                if(it) {

                }
            }
        })

        viewModel.errorMsg.observe(this, Observer {
            it?.let{
                if(!it.isNullOrEmpty()) {
                    Snackbar.make(cv_container, "Check your network and try again.", Snackbar.LENGTH_LONG).show()
                    btn_download.visibility =View.VISIBLE
                    btn_start.visibility =  View.GONE
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishActivity(0)
    }
}