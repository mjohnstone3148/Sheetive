package dev.matthewjohnstone.sheetive

import arrow.fx.stm.TVar
import dev.matthewjohnstone.sheetive.model.System
import io.ktor.server.application.*
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
    }
}
data class ThymeleafUser(val id: Int, val name: String)
