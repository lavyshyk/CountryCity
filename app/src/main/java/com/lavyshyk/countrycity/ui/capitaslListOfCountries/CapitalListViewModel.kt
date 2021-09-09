package com.lavyshyk.countrycity.ui.capitaslListOfCountries

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.lavyshyk.countrycity.CAPITAL_DTO
import com.lavyshyk.countrycity.base.mvvm.BaseViewModel
import com.lavyshyk.countrycity.base.mvvm.OutcomeCoroutine
import com.lavyshyk.countrycity.base.mvvm.coroutineJobIO
import com.lavyshyk.data.iterator.IteratorFilterByNameCapital
import com.lavyshyk.data.iterator.IteratorFilterCountyByCapitalName
import com.lavyshyk.domain.dto.CapitalDto
import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.useCase.implementetion.netCase.GetAllCapitalsFromApiUseCase
import com.lavyshyk.domain.useCase.implementetion.netCase.flow.GetAllCapitalsFromApiFlowUseCase
import kotlinx.coroutines.flow.Flow


class CapitalListViewModel(
    private val handle: SavedStateHandle,
    private val mGetAllCapitalsFromApiUseCase: GetAllCapitalsFromApiUseCase,
    private val mGetAllCapitalsFromApiFlowUseCase: GetAllCapitalsFromApiFlowUseCase,
    private val mIteratorFilterByNameCapital: IteratorFilterByNameCapital,
    private val mIteratorFilterCountyByCapitalName: IteratorFilterCountyByCapitalName
) : BaseViewModel(handle) {

    val mCapitalList =
        handle.getLiveData<OutcomeCoroutine<MutableList<CapitalDto>>>(CAPITAL_DTO)

    val mFilteredStream = mIteratorFilterByNameCapital.getFiltratedDataCapital()

    val mFilteredCountry = mIteratorFilterCountyByCapitalName.getFiltratedDataCountry()

    fun getListOfCapital() {
       mJob = coroutineJobIO(viewModelScope, { mGetAllCapitalsFromApiUseCase.execute() }, mCapitalList)
    }

    fun getListOfCapitalsByFlow(): Flow<Outcome<MutableList<CapitalDto>>> =
        mGetAllCapitalsFromApiFlowUseCase.execute()

    fun setFilter(name: String){
        mIteratorFilterByNameCapital.setNewQuery(name.lowercase())
    }
    fun setFilter(capitalDto: CapitalDto){
        mIteratorFilterCountyByCapitalName.setCapitalQuery(capitalDto.capital.lowercase())
    }

}
