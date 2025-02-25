package ge.tkgroup.sharedshift.common.data.remote.model.mappers

import ge.tkgroup.sharedshift.common.data.remote.model.UserDto
import ge.tkgroup.sharedshift.common.domain.model.User
import ge.tkgroup.sharedshift.common.utils.MappingException
import javax.inject.Inject

class UserDtoMapper @Inject constructor() : DtoMapper<UserDto, User> {

    override fun mapToDomain(data: UserDto): User {
        return User(
            id = data.id ?: throw MappingException("User ID cannot be null"),
            username = data.username
        )
    }

    override fun mapFromDomain(data: User): UserDto {
        return UserDto(
            id = data.id,
            username = data.username,
        )
    }
}