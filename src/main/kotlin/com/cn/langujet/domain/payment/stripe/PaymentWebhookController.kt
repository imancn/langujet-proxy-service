package com.cn.langujet.domain.payment.stripe

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class PaymentWebhookController(
    private val stripeWebHookClient: StripeWebHookClient,
    
    @Value("\${langujet.client.secret}") private val langujetClientSecret: String,
    
    ) {
    @PostMapping("/stripe/checkout/session/webhook")
    fun handleStripeWebhook(
        @RequestBody payload: String, @RequestHeader("Stripe-Signature") signatureHeader: String
    ): ResponseEntity<String> {
        return stripeWebHookClient.handleStripeWebhook(
            payload,
            signatureHeader,
            langujetClientSecret
        )
    }
}