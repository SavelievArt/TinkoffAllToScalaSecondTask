package ru.tinkoff.data

data class Address(val datacenter: String, val nodeId: String)
data class Event(val recipients: List<Address>, val payload: Payload)
data class Payload(val origin: String, val data: ByteArray)