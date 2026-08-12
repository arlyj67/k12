package com.schoolproject.k12.exception

import com.schoolproject.k12.dto.response.ErrorResponse
import com.schoolproject.k12.model.ErrorCode
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    // 401 - Account not active
//    @ExceptionHandler(IllegalStateException::class)
//    fun handleInactiveAccount(
//        ex: IllegalStateException,
//        request: HttpServletRequest
//    ): ResponseEntity<ErrorResponse> {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
//            ErrorResponse(
//                status = HttpStatus.UNAUTHORIZED.value(),
//                error = "Unauthorized",
//                message = ex.message ?: "Unauthorized",
//                path = request.requestURI,
//                errorCode = "ACCOUNT_NOT_ACTIVE"
//            )
//        )
//    }

    // 400 — IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(
        ex: IllegalArgumentException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode = when {
            ex.message?.contains("Email already exists") == true -> ErrorCode.EMAIL_ALREADY_EXISTS
            ex.message?.contains("Student number already exists") == true -> ErrorCode.STUDENT_NUMBER_ALREADY_EXISTS
            ex.message?.contains("Employee number already exists") == true -> ErrorCode.EMPLOYEE_NUMBER_ALREADY_EXISTS
            else -> null
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Bad Request",
                message = ex.message ?: "Invalid request",
                path = request.requestURI,
                errorCode = errorCode
            )
        )
    }

    // 400 — IllegalStateException
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(
        ex: IllegalStateException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode = when {
            ex.message?.contains("School already exists") == true -> ErrorCode.SCHOOL_ALREADY_EXISTS
            ex.message?.contains("not in PENDING status") == true -> ErrorCode.STUDENT_NOT_PENDING
            else -> null
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Bad Request",
                message = ex.message ?: "Invalid state",
                path = request.requestURI,
                errorCode = errorCode
            )
        )
    }

    // 404 — NoSuchElementException
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(
        ex: NoSuchElementException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode = when {
            ex.message?.contains("Student") == true -> ErrorCode.STUDENT_NOT_FOUND
            ex.message?.contains("Employee") == true -> ErrorCode.EMPLOYEE_NOT_FOUND
            ex.message?.contains("School") == true -> ErrorCode.SCHOOL_NOT_FOUND
            ex.message?.contains("User") == true -> ErrorCode.USER_NOT_FOUND
            else -> null
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(
                status = HttpStatus.NOT_FOUND.value(),
                error = "Not Found",
                message = ex.message ?: "Resource not found",
                path = request.requestURI,
                errorCode = errorCode
            )
        )
    }

    // 400 — Validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val details = ex.bindingResult.fieldErrors
            .map { "${it.field}: ${it.defaultMessage}" }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Validation Failed",
                message = "One or more fields are invalid",
                path = request.requestURI,
                errorCode = ErrorCode.VALIDATION_FAILED,
                details = details
            )
        )
    }

    // 500 — Generic fallback
    @ExceptionHandler(Exception::class)
    fun handleGeneric(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        ex.printStackTrace() // add this temporarily
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse(
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error = "Internal Server Error",
                message = ex.message ?: "An unexpected error occurred", // show actual message
                path = request.requestURI,
                errorCode = ErrorCode.INTERNAL_SERVER_ERROR
            )
        )
    }
}