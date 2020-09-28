package com.kentlogic.philippinecities.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.kentlogic.philippinecities.api.ApiService
import com.kentlogic.philippinecities.model.Question
import com.kentlogic.philippinecities.room.QuestionDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class QuestionViewModel(application: Application): BaseViewModel(application) {

    val questions = MutableLiveData<List<Question>>()
    val isLoading = MutableLiveData<Boolean>()
    val errorMsg = MutableLiveData<String>()

    val apiService = ApiService()

    val disposable = CompositeDisposable()

    fun updateQuestions() {
        isLoading.value = true
       loadFromApi()
    }


    fun loadFromApi(){
        disposable.add(
            apiService.getQuestions()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Question>>() {
                    override fun onSuccess(t: List<Question>) {
                        saveToLocal(t)
                    }

                    override fun onError(e: Throwable) {
                        getQuestions()
                        print("Error $e.stackTrace")
                    }
                })
        )
    }

    private fun saveToLocal(questionList: List<Question>){
        launch {
            val dao = QuestionDatabase(getApplication()).questionDao()
            dao.deleteQuestions()
            dao.insertAllQuestions(*questionList.toTypedArray())
            isLoading.value = false
        }

    }

     fun getQuestions() {
        launch {
            val dao= QuestionDatabase(getApplication()).questionDao().getQuestions()
            questionsRetreived(dao)
        }
    }

    fun questionsRetreived(questionList: List<Question>) {
        if(questionList.isEmpty()) {
            errorMsg.value = "Check your network and try again."
        }
        isLoading.value = false
        questions.value = questionList
    }
}