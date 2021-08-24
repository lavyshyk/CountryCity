package com.lavyshyk.domain.useCase.implementetion.databaseCase

import com.lavyshyk.domain.repository.DataBaseRepository
import com.lavyshyk.domain.useCase.UseCase
import io.reactivex.rxjava3.core.Flowable

class GetLanguagesNameByCountryFromDataBaseUseCase (private val mDataBaseRepository: DataBaseRepository) :
    UseCase<String, MutableList<String>>(){
    override fun buildFlowable(params: String?): Flowable<MutableList<String>> {
        TODO("Not yet implemented")
    }

    override val mIsParamsRequired: Boolean
        get() = true
}