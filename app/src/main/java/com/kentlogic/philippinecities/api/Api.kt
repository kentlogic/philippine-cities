package com.kentlogic.philippinecities.api

import com.kentlogic.philippinecities.model.Question
import io.reactivex.Single
import retrofit2.http.GET

interface Api {

    @GET("")
    fun getQuestions(): Single<List<Question>>
}