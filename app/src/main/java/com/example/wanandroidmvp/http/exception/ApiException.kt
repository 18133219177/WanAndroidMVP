package com.example.wanandroidmvp.http.exception

import kotlin.reflect.jvm.internal.impl.serialization.deserialization.FlexibleTypeDeserializer.ThrowException

class ApiException : RuntimeException {
    protected var code: Int? = null

    constructor(throwable: Throwable, code: Int) : super(throwable) {
        this.code = code
    }

    constructor(message: String) : super(Throwable(message))
}