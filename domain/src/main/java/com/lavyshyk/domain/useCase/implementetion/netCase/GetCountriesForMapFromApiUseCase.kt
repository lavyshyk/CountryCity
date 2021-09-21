package com.lavyshyk.domain.useCase.implementetion.netCase

import com.lavyshyk.domain.dto.country.CountryInfoMapDto
import com.lavyshyk.domain.repository.NetworkRepository
import com.lavyshyk.domain.useCase.UseCase
import io.reactivex.rxjava3.core.Flowable

class GetCountriesForMapFromApiUseCase(private val mNetworkRepository: NetworkRepository) :
    UseCase<Unit, MutableList<CountryInfoMapDto>>() {

    override fun buildFlowable(params: Unit?): Flowable<MutableList<CountryInfoMapDto>> =
        mNetworkRepository.getInfoAboutAllCountryForMap()

    override val mIsParamsRequired: Boolean
        get() = false
}