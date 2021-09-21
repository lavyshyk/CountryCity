package com.lavyshyk.countrycity.base.mvi

//fun <T : ViewModel> RootBaseActivity.viewModelProvider(
//    factory: ViewModelProvider.Factory,
//    model: KClass<T>
//): T {
//    return ViewModelProvider(this, factory).get(model.java)
//}

fun Boolean.runIfTrue(block: () -> Unit) {
    if (this) {
        block()
    }
}

//fun CallErrors.getMessage(context: Context): String {
//    return when (this) {
//        is CallErrors.ErrorEmptyData -> context.getString(R.string.error_empty_data)
//        is CallErrors.ErrorServer -> context.getString(R.string.error_server_error)
//        is CallErrors.ErrorException ->  context.getString(
//            R.string.error_exception
//        )
//    }
//
//}