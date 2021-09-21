package com.lavyshyk.domain.useCase.implementetion.netCase

import com.lavyshyk.domain.dto.country.CountryDto
import com.lavyshyk.domain.repository.NetworkRepository
import com.lavyshyk.domain.useCase.UseCase
import io.reactivex.rxjava3.core.Flowable

class GetAllCountiesFromApiUseCase(private val mNetworkRepository: NetworkRepository) :
    UseCase<Unit, MutableList<CountryDto>>() {

    override fun buildFlowable(params: Unit?): Flowable<MutableList<CountryDto>> =
        mNetworkRepository.getCountriesInfo()


    override val mIsParamsRequired: Boolean = false
}