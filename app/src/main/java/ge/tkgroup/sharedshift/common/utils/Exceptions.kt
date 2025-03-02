package ge.tkgroup.sharedshift.common.utils

class DocumentNotFoundException: Exception("Document not found")
class DocumentParseFailedException: Exception("Document couldn't be parsed")
class MappingException(message: String) : Exception(message)
class UserNotAuthenticatedException : Exception("User not authenticated")
