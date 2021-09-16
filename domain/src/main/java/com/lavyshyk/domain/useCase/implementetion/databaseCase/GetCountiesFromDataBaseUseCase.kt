package com.lavyshyk.domain.useCase.implementetion.databaseCase

import com.lavyshyk.domain.dto.country.CountryDto
import com.lavyshyk.domain.repository.DataBaseRepository
import com.lavyshyk.domain.useCase.UseCase
import io.reactivex.rxjava3.core.Flowable

class GetCountiesFromDataBaseUseCase(private val mDataBaseRepository: DataBaseRepository) :
    UseCase<Unit, MutableList<CountryDto>>() {
    override fun buildFlowable(params: Unit?): Flowable<MutableList<CountryDto>> =
        mDataBaseRepository.getListCountry()

    override val mIsParamsRequired: Boolean
        get() = false
}