package com.lavyshyk.domain.useCase.implementetion.netCase

import com.lavyshyk.domain.dto.CountryDto
import com.lavyshyk.domain.repository.NetworkRepository
import com.lavyshyk.domain.useCase.UseCase
import io.reactivex.rxjava3.core.Flowable

class GetCountriesByNameFromApiUseCase(private val mNetworkRepository: NetworkRepository) :
    UseCase<String, MutableList<CountryDto>>() {
    override fun buildFlowable(params: String?): Flowable<MutableList<CountryDto>> =
        mNetworkRepository.geCountryListByName(params ?: "")

    override val mIsParamsRequired: Boolean = true

}