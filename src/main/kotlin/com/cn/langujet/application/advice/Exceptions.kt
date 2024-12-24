package com.cn.langujet.application.advice

import org.springframework.http.HttpStatus

open class HttpException(val httpStatus: HttpStatus, override val message: String?) : RuntimeException(message)

class LogicalException(message: String?): RuntimeException(message)

class UnprocessableException(message: String?): HttpException(HttpStatus.UNPROCESSABLE_ENTITY, message)