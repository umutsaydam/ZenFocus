package com.umutsaydam.zenfocus.domain.model

import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.exceptions.service.CodeMismatchException
import com.amplifyframework.auth.cognito.exceptions.service.LimitExceededException
import com.amplifyframework.auth.cognito.exceptions.service.UserNotConfirmedException
import com.amplifyframework.auth.cognito.exceptions.service.UsernameExistsException
import com.amplifyframework.auth.exceptions.InvalidStateException
import com.amplifyframework.auth.exceptions.NotAuthorizedException
import com.amplifyframework.auth.exceptions.ServiceException
import com.amplifyframework.auth.exceptions.SessionExpiredException
import com.amplifyframework.auth.exceptions.ValidationException
import com.umutsaydam.zenfocus.R

sealed class AuthExceptionMessages(val messageId: Int) {
    data object NotAuthorizedError : AuthExceptionMessages(R.string.incorrect_email_or_password)
    data object UserNotConfirmedError : AuthExceptionMessages(R.string.confirm_account)
    data object ValidationError : AuthExceptionMessages(R.string.validation_exception)
    data object SessionExpiredError : AuthExceptionMessages(R.string.session_expired_exception)
    data object InvalidStateError : AuthExceptionMessages(R.string.invalid_state_exception)
    data object CodeMismatchError : AuthExceptionMessages(R.string.code_mismatch)
    data object LimitExceededError : AuthExceptionMessages(R.string.limit_exceeded)
    data object UsernameExistsError : AuthExceptionMessages(R.string.username_exists_exception)
    data object ServiceError : AuthExceptionMessages(R.string.service_exception)
    data object UnknownError : AuthExceptionMessages(R.string.unknown_error)

    companion object {
        fun exceptionToMessage(exception: AuthException): AuthExceptionMessages {
            return when (exception) {
                is NotAuthorizedException -> NotAuthorizedError
                is UserNotConfirmedException -> UserNotConfirmedError
                is ValidationException -> ValidationError
                is SessionExpiredException -> SessionExpiredError
                is InvalidStateException -> InvalidStateError
                is CodeMismatchException -> CodeMismatchError
                is LimitExceededException -> LimitExceededError
                is UsernameExistsException -> UsernameExistsError
                is ServiceException -> ServiceError
                else -> UnknownError
            }
        }
    }
}
