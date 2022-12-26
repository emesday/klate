package emesday.klate

import io.ktor.http.*
import io.ktor.server.application.*

val orderColumnRegex = "_oc_(.*)".toRegex()

val pageRegex = "page_(.*)".toRegex()

val pageSizeRegex = "psize_(.*)".toRegex()

class Stack(val data: MutableList<String>, val size: Int) {

    constructor(): this(mutableListOf<String>(), 5)
    constructor(data: MutableList<String>): this(data, 5)

    fun push(item: String) {
        if (data.lastOrNull() != item) {
            data.add(item)
        }
        if (data.size > size) {
            data.removeFirst()
        }
    }

    fun pop(): String? = data.removeLastOrNull()

    fun toJson(): List<String> = data
}

fun argsToMap(params: Parameters, regex: Regex): Map<String, String> {
    return params.entries().mapNotNull { entry ->
        entry.value.firstOrNull()?.let { value ->
            regex.matchEntire(entry.key)
                ?.destructured
                ?.let { (key) ->
                    key to value
                }
        }
    }.toMap()
}

/**
 * Get page arguments for group by
 */
val ApplicationCall.groupByArgs: String
    get() = request.queryParameters["group_by"] ?: ""

/**
 * Get page arguments, returns a dictionary
 * { <VIEW_NAME>: PAGE_NUMBER }
 *
 * Arguments are passed: page_<VIEW_NAME>=<PAGE_NUMBER>
 */
val ApplicationCall.pageArgs: Map<String, Int>
    get() = argsToMap(request.queryParameters, pageRegex).mapValues { it.value.toInt() }

/**
 * Get page size arguments, returns an int
 * { <VIEW_NAME>: PAGE_NUMBER }
 * Arguments are passed: psize_<VIEW_NAME>=<PAGE_SIZE>
 */
val ApplicationCall.pageSizeArgs: Map<String, Int>
    get() = argsToMap(request.queryParameters, pageSizeRegex).mapValues { it.value.toInt() }

/**
 *
 * Get order arguments, return a dictionary
 * { <VIEW_NAME>: (ORDER_COL, ORDER_DIRECTION) }
 *
 * Arguments are passed like: _oc_<VIEW_NAME>=<COL_NAME>&_od_<VIEW_NAME>='asc'|'desc'
 */
val ApplicationCall.orderArgs: Map<String, Pair<String, String>>
    get() {
        val params = request.queryParameters
        val columns = argsToMap(params, orderColumnRegex)
        return columns
            .mapNotNull { (viewName, column) ->
                params["_od_$viewName"]?.let { direction ->
                    if (direction in setOf("asc", "desc")) {
                        Pair(viewName, Pair(column, direction))
                    } else {
                        null
                    }
                }
            }
            .toMap()
    }

class Filters {
    fun clearFilters() {}
}
/**
 * Sets filters with the given current request args
 *
 * Request arg filters are of the form "_flt_<DECIMAL>_<VIEW_NAME>_<COL_NAME>"
 *
 * @param filters Filter instance to apply the request filters on
 * @param disallowIfNotInSearch If True, disallow filters that are not in the search
 * :return:
 */
fun ApplicationCall.getFilerArgs(filters: Filters, disallowIfNotInSearch: Boolean = true) {
    // TODO
}

