package ru.tinkoff.handler

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.tinkoff.client.Client
import ru.tinkoff.client.Result
import ru.tinkoff.data.Event
import java.time.Duration
import java.util.concurrent.Executors

class HandlerImpl(private val client: Client) : Handler {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(HandlerImpl::class.java)
        private val executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    }

    override val timeout: Duration = Duration.ofSeconds(1)

    override fun performOperation() {
        logger.info("Начинаем приём данных")
        while (true) {
            sendData(client.readData())
        }
    }

    private fun sendData(event: Event) {
        CoroutineScope(Dispatchers.IO).launch {
            val queue = mutableListOf<Deferred<Unit>>()
            event.recipients.forEach {
                val sendJob = CoroutineScope(executorService.asCoroutineDispatcher()).async {
                    var result: Result
                    do {
                        result = client.sendData(it, event.payload)
                        logger.info("Answer from ${it.datacenter}:${it.nodeId} with status: $result")
                        if (result == Result.REJECTED) {
                            delay(timeout.toMillis())
                        }
                    } while (result == Result.REJECTED)
                }
                queue.add(sendJob)
            }
            queue.awaitAll()
        }
    }
}