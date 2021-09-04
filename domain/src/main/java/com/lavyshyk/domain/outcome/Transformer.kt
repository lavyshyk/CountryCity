package com.lavyshyk.domain.outcome


interface Transformer<InputType, OutputType> {
    var convert: (InputType) -> OutputType
}

