package emesday.klate.deprecated.appbuilder.ktor

data class TreeNode<T : Any>(
    val item: T? = null,
    val children: MutableList<TreeNode<T>> = mutableListOf(),
)

private fun <T : Any, P : TreeNode<T>> P.add(childNode: P) {
    children.add(childNode)
}

fun <T : Any, P : TreeNode<T>> P.add(
    item: T,
    childrenBody: (TreeNode<T>.() -> Unit)? = null,
) {
    val root = TreeNode(item)
    if (childrenBody != null) {
        childrenBody(root)
    }
    add(root)
}

fun <T : Any, P : TreeNode<T>> P.print(depth: Int = 0) {
    val item = item ?: "root"
    println(" ".repeat(depth * 4) + "|- $item")
    for (child in children) {
        child.print(depth + 1)
    }
}

fun <T : Any, P : TreeNode<T>> P.iterator(): Iterator<T> {
    return iterator {
        if (item != null) {
            yield(item)
        }
        for (childNode in children) {
            for (child in childNode.iterator()) {
                yield(child)
            }
        }
    }
}
