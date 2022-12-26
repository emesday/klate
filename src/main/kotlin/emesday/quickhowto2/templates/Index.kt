package emesday.quickhowto2.templates

import emesday.klate.templates.*
import emesday.klate.templates.ab.*
import kotlinx.html.*

class Index : Base() {

    override fun HTML.apply(ctx: KlateContext) {
        content {
            h1 {
                +"Alternative 2 INDEX"
            }
        }
    }
}