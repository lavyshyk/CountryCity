package com.lavyshyk.domain.useCase.implementetion.databaseCase

import com.lavyshyk.domain.dto.country.LanguageDto
import com.lavyshyk.domain.repository.DataBaseRepository
import com.lavyshyk.domain.useCase.UseCase
import io.reactivex.rxjava3.core.Flowable

class UpdateLanguagesInDataBaseUseCase(private val mDataBaseRepository: DataBaseRepository) :
    UseCase<Pair<String, MutableList<LanguageDto>>, Unit>() {

    override fun buildFlowable(params: Pair<String, MutableList<LanguageDto>>?): Flowable<Unit> =
        mDataBaseRepository.updateLanguages(params?.first ?: "", params?.second ?: mutableListOf())

    override val mIsParamsRequired: Boolean
        get() = true
}