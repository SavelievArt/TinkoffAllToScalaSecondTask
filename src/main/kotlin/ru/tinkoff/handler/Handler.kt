package ru.tinkoff.handler

import java.time.Duration

interface Handler {
    val timeout: Duration

    fun performOperation()
}
