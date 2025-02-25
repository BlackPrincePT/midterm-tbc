package ge.tkgroup.sharedshift.common.data.remote.model.mappers

interface DtoMapper<E, D> {

    fun mapToDomain(data: E): D

    fun mapFromDomain(data: D): E
}