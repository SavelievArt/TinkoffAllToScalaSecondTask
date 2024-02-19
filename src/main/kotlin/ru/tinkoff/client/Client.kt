package ru.tinkoff.client

import ru.tinkoff.data.Address
import ru.tinkoff.data.Event
import ru.tinkoff.data.Payload

interface Client {
    //блокирующий метод для чтения данных
    fun readData(): Event

    //блокирующий метод отправки данных
    fun sendData(dest: Address, payload: Payload): Result
}