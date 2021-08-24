package com.lavyshyk.data.repository

import com.lavyshyk.data.network.CountryServiceCoroutine
import com.lavyshyk.data.transformToListCapitalDto
import com.lavyshyk.domain.dto.CapitalDto
import com.lavyshyk.domain.repository.NetworkRepositoryOnCoroutine

class NetworkRepositoryOnCoroutineImpl(private val mService: CountryServiceCoroutine) :
    NetworkRepositoryOnCoroutine {
    override suspend fun getAllCapitals(): MutableList<CapitalDto> =
        mService.getAllCapitals().transformToListCapitalDto()

}