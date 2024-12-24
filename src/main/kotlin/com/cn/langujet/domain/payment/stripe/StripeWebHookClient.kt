package com.cn.langujet.domain.payment.stripe

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "StripeWebHookClient", url = "https://api.langujet.com/")
interface StripeWebHookClient {
    @PostMapping("api/v1/stripe/checkout/session/webhook")
    fun handleStripeWebhook(
        @RequestBody payload: String,
        @RequestHeader("Stripe-Signature") signatureHeader: String,
        @RequestHeader("Authorization") authorizationHeader: String,
    ): ResponseEntity<String>
}