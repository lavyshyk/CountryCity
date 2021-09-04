package com.lavyshyk.data.ext

import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.outcome.Transformer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

fun <InputType, OutputType> modifyFlow(
    data: Flow<InputType>,
    transformer: Transformer<InputType, OutputType>,
    dispatcher: CoroutineDispatcher
): Flow<Outcome<OutputType>> {
    return data.execute(transformer.convert, dispatcher)
}

fun <InputType, OutputType> Flow<InputType>.execute(
    convert: (InputType) -> OutputType,
    dispatcher: CoroutineDispatcher
): Flow<Outcome<OutputType>> =
    this.flowOn(dispatcher)
        .map { model -> convert(model) }
        .map { list -> Outcome.success(list) }
        .onStart { emit(Outcome.loading(true)) }
        .onCompletion { emit(Outcome.loading(false)) }
        .catch { ex -> emit(Outcome.failure(ex)) }

