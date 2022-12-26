package emesday.quickhowto2

import emesday.klate.templates.*
import emesday.klate.view.*
import emesday.quickhowto2.templates.*

class MyIndexView : IndexView() {

    override val indexTemplate = template { Index() }
}