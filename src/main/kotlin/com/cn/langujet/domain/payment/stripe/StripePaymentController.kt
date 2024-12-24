package com.cn.langujet.domain.payment.stripe

import com.cn.langujet.domain.payment.stripe.models.StripeSessionInfo
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class StripePaymentController(private val stripePaymentService: StripePaymentService) {
    @PostMapping("api/v1/payments/stripe/create-session")
    fun createPaymentSession(
        @RequestParam price: Double, @RequestParam orderId: String
    ): StripeSessionInfo {
        return stripePaymentService.createPaymentSession(price, orderId).let {
            StripeSessionInfo(it.url, it.id)
        }
    }
}





