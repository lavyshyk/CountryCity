package com.lavyshyk.domain.repository

import com.lavyshyk.domain.dto.CapitalDto

interface NetworkRepositoryOnCoroutine {

    suspend fun getAllCapitals(): MutableList<CapitalDto>
}