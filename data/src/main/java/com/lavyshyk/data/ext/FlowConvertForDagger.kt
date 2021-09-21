package com.lavyshyk.data.ext

import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.outcome.Transformer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

fun <InputType, OutputType> modifyFlowForDagger(
    data: Flow<InputType>,
    transformer: Transformer<InputType, OutputType>,
): Flow<Outcome<OutputType>> {
    return data.execute(transformer.convert)
}

fun <InputType, OutputType> Flow<InputType>.execute(
    convert: (InputType) -> OutputType,
): Flow<Outcome<OutputType>> =
    this.flowOn(Dispatchers.IO)
        .map { model -> convert(model) }
        .map { list -> Outcome.success(list) }
        .onStart { emit(Outcome.loading(true)) }
        .onCompletion { emit(Outcome.loading(false)) }
        .catch { ex -> emit(Outcome.failure(ex)) }