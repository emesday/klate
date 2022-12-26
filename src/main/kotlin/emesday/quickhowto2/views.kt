package emesday.quickhowto2

import emesday.klate.*


class GroupModelView : BaseModelView(ContactGroup) {
}

fun KlatePluginInstance.createGroupModelView(): BaseView {
    return GroupModelView()
}
