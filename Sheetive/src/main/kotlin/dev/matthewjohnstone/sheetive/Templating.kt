package dev.matthewjohnstone.sheetive

import arrow.fx.stm.STM
import arrow.fx.stm.TVar
import arrow.fx.stm.atomically
import dev.matthewjohnstone.sheetive.model.System
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.Thymeleaf
import io.ktor.server.thymeleaf.ThymeleafContent
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

fun Application.configureTemplating(systemVar: TVar<System>) {
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/thymeleaf/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }
    routing {
        get("/html-thymeleaf") {
            call.respond(ThymeleafContent("index", mapOf("user" to ThymeleafUser(1, "user1"))))
        }

        get("/system") {
            val system = systemVar.unsafeRead()
            call.respond(ThymeleafContent("system", mapOf("system" to system)))
        }

        post("/system") {
            val formContent = call.receiveParameters()
            val amountToIncrementParameter = formContent["amountToIncrement"] ?: "1"
            val amountToIncrement = amountToIncrementParameter.toInt()
            atomically { incrementCounter(systemVar, amountToIncrement) }
            val system = systemVar.unsafeRead()
            call.respond(ThymeleafContent("system", mapOf("system" to system)))
        }
    }
}

fun STM.incrementCounter(systemVar: TVar<System>, amountToIncrement: Int) {
    val oldSystem = systemVar.read()
    val newSystem = oldSystem.copy(counter = oldSystem.counter + amountToIncrement)
    systemVar.write(newSystem)
}

data class ThymeleafUser(val id: Int, val name: String)
