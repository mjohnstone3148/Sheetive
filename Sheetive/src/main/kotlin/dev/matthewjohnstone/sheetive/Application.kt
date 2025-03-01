package dev.matthewjohnstone.sheetive

import arrow.fx.stm.TVar
import dev.matthewjohnstone.sheetive.model.System
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    var systemVar: TVar<System>?
    runBlocking { systemVar = TVar.new(System(counter = 5)) }


    configureTemplating(systemVar!!)
    configureRouting()
}
