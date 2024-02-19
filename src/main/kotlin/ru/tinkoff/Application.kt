package ru.tinkoff

import ru.tinkoff.client.ClientImpl
import ru.tinkoff.handler.HandlerImpl

fun main() {
    val client = ClientImpl()
    val handler = HandlerImpl(client)
    handler.performOperation()
}