package emesday.klate.templates

import kotlinx.html.*

class CustomIndex : KlateTemplate {

    override fun HTML.apply(ctx: KlateContext) {
        body {
            +"This is a custom index view."
        }
    }
}