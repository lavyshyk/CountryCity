package com.lavyshyk.countrycity.ui.capitaslListOfCountries

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.lavyshyk.countrycity.base.mvvm.BaseViewModel
import com.lavyshyk.countrycity.base.mvvm.Outcome
import com.lavyshyk.domain.dto.CapitalDto
import com.lavyshyk.domain.useCase.implementetion.netCase.GetAllCapitalsFromApiUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CapitalListViewModel(
    savedStateHandle: SavedStateHandle,
    private val mGetAllCapitalsFromApiUseCase: GetAllCapitalsFromApiUseCase
) : BaseViewModel(savedStateHandle) {

    val mCapitalList =
        savedStateHandle.getLiveData<Outcome<MutableList<CapitalDto>>>("gfgfgfg")

    fun getListOfCapitals() {
        val job = viewModelScope.launch {
            try {
                val result =
                    withContext(viewModelScope.coroutineContext + Dispatchers.IO)
                    { mGetAllCapitalsFromApiUseCase.execute() }
                mCapitalList.value = Outcome.next(result)
            } catch (e: Exception) {
                mCapitalList.value = Outcome.Failure(e)
            }
        }
    }
}
