package com.lavyshyk.domain.useCase.implementetion.databaseCase

import com.lavyshyk.domain.dto.CountryDto
import com.lavyshyk.domain.repository.DataBaseRepository
import com.lavyshyk.domain.useCase.UseCase
import io.reactivex.rxjava3.core.Flowable

class SaveCountiesInDatBaseUseCase(private val mDataBaseRepository: DataBaseRepository) :
UseCase<MutableList<CountryDto>, Unit>() {
    override fun buildFlowable(params: MutableList<CountryDto>?): Flowable<Unit> =
        mDataBaseRepository.saveListCountry(params?: mutableListOf())

    override val mIsParamsRequired: Boolean
        get() = true
}