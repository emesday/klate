package emesday.klate.config

import io.ktor.server.config.*

class KlateApplicationConfigDB(
    override val ac: ApplicationConfig,
    override val path: String,
) : KlateApplicationConfigChild {

    var driver: String?
        get() = ac.string(combine("driver"))
        set(value) {
            ac.put(combine("driver"), value)
        }

    var url: String?
        get() = ac.string(combine("url"))
        set(value) {
            ac.put(combine("url"), value)
        }
}
