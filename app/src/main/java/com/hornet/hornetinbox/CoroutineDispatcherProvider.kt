package com.hornet.hornetinbox

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CoroutineDispatcherProvider @Inject constructor() {
    val io = Dispatchers.IO
    val default = Dispatchers.Default
}