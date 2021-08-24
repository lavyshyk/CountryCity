package com.lavyshyk.domain.useCase.implementetion.netCase

import com.lavyshyk.domain.dto.CountryDataDetailDto
import com.lavyshyk.domain.repository.NetworkRepository
import com.lavyshyk.domain.useCase.UseCase
import io.reactivex.rxjava3.core.Flowable

class GetCountyDetailInfoFromApiUseCase(private val mNetworkRepository: NetworkRepository) :
    UseCase<String, MutableList<CountryDataDetailDto>>() {
    override fun buildFlowable(params: String?): Flowable<MutableList<CountryDataDetailDto>> =
        mNetworkRepository.getInfoAboutCountry(params ?: "")

    override val mIsParamsRequired: Boolean
        get() = true
}