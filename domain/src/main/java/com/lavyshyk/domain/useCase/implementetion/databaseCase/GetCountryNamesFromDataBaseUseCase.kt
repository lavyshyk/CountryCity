package com.lavyshyk.domain.useCase.implementetion.databaseCase

import com.lavyshyk.domain.repository.DataBaseRepository
import com.lavyshyk.domain.useCase.UseCase
import io.reactivex.rxjava3.core.Flowable

class GetCountryNamesFromDataBaseUseCase(private val mDataBaseRepository: DataBaseRepository) :
    UseCase<Unit, MutableList<String>>() {
    override fun buildFlowable(params: Unit?): Flowable<MutableList<String>> =
        mDataBaseRepository.getListCountryName()


    override val mIsParamsRequired: Boolean
        get() = false
}