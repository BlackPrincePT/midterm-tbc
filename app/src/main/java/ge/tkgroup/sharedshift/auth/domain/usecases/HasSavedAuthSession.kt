package ge.tkgroup.sharedshift.auth.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import javax.inject.Inject

class HasSavedAuthSession @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke() = authRepository.hasSavedAuthSession()
}