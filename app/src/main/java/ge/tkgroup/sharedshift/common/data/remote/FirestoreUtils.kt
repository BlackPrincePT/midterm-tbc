package ge.tkgroup.sharedshift.common.data.remote

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import ge.tkgroup.sharedshift.common.utils.DocumentNotFoundException
import ge.tkgroup.sharedshift.common.utils.DocumentParseFailedException
import ge.tkgroup.sharedshift.common.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUtils @Inject constructor() {

    suspend inline fun <reified T> readDocument(documentRef: DocumentReference): Resource<T> {
        return try {
            val documentSnapshot = documentRef.get().await()

            if (documentSnapshot.exists()) {
                documentSnapshot.toObject(T::class.java)?.let {
                    Resource.Success(data = it)
                } ?: throw DocumentParseFailedException()
            } else {
                throw DocumentNotFoundException()
            }
        } catch (e: FirebaseFirestoreException) {
            Resource.Error(message = "Firestore error: ${e.message} (code: ${e.code}")
        } catch (e: IllegalStateException) {
            Resource.Error(message = "Illegal state: ${e.message}")
        } catch (e: DocumentParseFailedException) {
            Resource.Error(message = "Document couldn't be parsed into ${T::class.simpleName}")
        } catch (e: DocumentNotFoundException) {
            Resource.Error(message = "Such document of type: ${T::class.simpleName}, doesn't exist")
        }
    }

    suspend inline fun <reified T> queryDocuments(query: Query): Resource<List<T>> {
        return try {
            val documentSnapshot = query.get().await()

            documentSnapshot.toObjects(T::class.java).let {
                Resource.Success(data = it)
            }
        } catch (e: FirebaseFirestoreException) {
            Resource.Error(message = "Firestore error: ${e.message} (code: ${e.code}")
        } catch (e: IllegalStateException) {
            Resource.Error(message = "Illegal state: ${e.message}")
        }
    }
}