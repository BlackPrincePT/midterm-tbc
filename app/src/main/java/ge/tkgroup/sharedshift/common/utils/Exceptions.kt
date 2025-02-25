package ge.tkgroup.sharedshift.common.utils

class MappingException(message: String) : Exception(message)

/**
 * Firestore Exceptions
 */
class DocumentNotFoundException: Exception()
class DocumentParseFailedException: Exception()