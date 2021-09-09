package com.lavyshyk.data.ext

import com.lavyshyk.data.model.Capital
import com.lavyshyk.domain.dto.CapitalDto
import com.lavyshyk.domain.outcome.Transformer

class ListCapitalTransformer : Transformer<MutableList<Capital>, MutableList<CapitalDto>> {
    override var convert: (MutableList<Capital>) -> MutableList<CapitalDto> =
        { data ->
            data.map {
                CapitalDto(capital = it.capital ?: "")
            }.toMutableList()
        }
}