package ge.tkgroup.sharedshift.common.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import ge.tkgroup.sharedshift.common.data.remote.model.mappers.DtoMapper
import kotlinx.coroutines.tasks.await

class FirestorePagingSource<E, D : Any>(
    private val query: Query,
    private val classInfo: Class<E>,
    private val dtoMapper: DtoMapper<E, D>
) : PagingSource<QuerySnapshot, D>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, D>): QuerySnapshot? {
        return null //TODO : Refresh
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, D> {
        return try {
            val currentPage = params.key ?: query.get().await()
            val lastVisibleObject = currentPage.documents[currentPage.size() - 1]
            val nextPage = query.startAfter(lastVisibleObject).get().await()

            val dtoData = currentPage.toObjects(classInfo)

            LoadResult.Page(
                data = dtoData.map(dtoMapper::mapToDomain),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}