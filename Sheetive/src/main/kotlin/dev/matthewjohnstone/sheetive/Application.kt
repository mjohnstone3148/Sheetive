package dev.matthewjohnstone.sheetive

import dev.matthewjohnstone.sheetive.model.System
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val system = System(counter = 10)

    configureTemplating(system)
    configureRouting()
}
