package ge.tkgroup.sharedshift.settings.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import javax.inject.Inject

class SignUserOut @Inject constructor(private val authRepository: AuthRepository) {

    operator fun invoke() {
        authRepository.signOut()
    }
}