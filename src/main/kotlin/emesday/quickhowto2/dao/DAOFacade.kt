package emesday.quickhowto2.dao

interface DAOFacade<T : Comparable<T>, M> {
    suspend fun getAll(): List<M>
    suspend fun get(id: T): M?
    suspend fun create(item: M): M?
    suspend fun update(id: T, item: M): Boolean
    suspend fun delete(id: T): Boolean
}
