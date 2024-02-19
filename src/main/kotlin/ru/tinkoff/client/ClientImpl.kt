package ru.tinkoff.client

import ru.tinkoff.data.Address
import ru.tinkoff.data.Event
import ru.tinkoff.data.Payload
import java.util.*

class ClientImpl : Client {

    private var launch = 1
    private val random = Random()

    override fun readData() = Event(
        IntRange(++launch, launch * 5).map { Address("dc$it", "Node ${UUID.randomUUID()}") },
        Payload("TEST", ByteArray(10))
    )

    override fun sendData(dest: Address, payload: Payload) = if (random.nextDouble() < 0.8) Result.ACCEPTED else Result.REJECTED
}