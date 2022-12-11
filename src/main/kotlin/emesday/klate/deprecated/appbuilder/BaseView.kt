package emesday.klate.deprecated.appbuilder

class SomeRoute

//abstract class BaseView : emesday.klate.deprecated.appbuilder.AbstractViewApi {
//
//    // var blueprint
//
////    var routeBase: String
//
//    var templateFolder: String = "templates"
//
//    var staticFolder: String? = "static"
//
//    var previousClassPermissionName: String? = null
//
//    var routeBase: String? = null
//
//    val thisClassName = this.javaClass.canonicalName
//
//    init {
//        if (previousClassPermissionName == null && classPermissionName != null) {
//            previousClassPermissionName = thisClassName
//        }
//        classPermissionName = classPermissionName ?: this.javaClass.canonicalName
//    }
//
//    fun register(appbuilder: emesday.klate.deprecated.appbuilder.AppBuilder, endpoint: String? = null, staticFolder: String? = null): emesday.klate.deprecated.appbuilder.SomeRoute {
//        this.appbuilder = appbuilder
//        this.endpoint = endpoint ?: thisClassName
//        routeBase = routeBase ?: "/${thisClassName.lowercase()}"
//        this.staticFolder = staticFolder
//        val someRoute = if (this.staticFolder == null) {
//            // add route
//            // do something
//            emesday.klate.deprecated.appbuilder.SomeRoute()
//        } else {
//            // add route
//            // do something
//            emesday.klate.deprecated.appbuilder.SomeRoute()
//        }
//        registerUrls()
//        return someRoute
//    }
//
//    private fun registerUrls() {
//    }
//
//    fun renderTemplate(template: String, kwargs: Map<String, String>): FreeMarkerContent {
//        return FreeMarkerContent(template, kwargs)
//    }
//}
//
//abstract class BaseModelView : emesday.klate.deprecated.appbuilder.BaseView()
//
//abstract class BaseCRUDView : emesday.klate.deprecated.appbuilder.BaseModelView()
//
//abstract class RestCRUDView : emesday.klate.deprecated.appbuilder.BaseCRUDView()
//
//abstract class ModelView : emesday.klate.deprecated.appbuilder.RestCRUDView()
