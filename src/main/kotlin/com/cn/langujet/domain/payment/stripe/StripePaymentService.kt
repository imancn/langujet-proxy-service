package com.cn.langujet.domain.payment.stripe

import com.stripe.Stripe
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class StripePaymentService {
    @Value("\${stripe.api.secret.key}")
    private lateinit var apiSecretKey: String
    
    @Value("\${payment.redirect.url}")
    private lateinit var paymentRedirectUrl: String
    
    @PostConstruct
    fun setup() {
        Stripe.apiKey = apiSecretKey
    }
    
    fun createPaymentSession(price: Double, orderId: String): Session {
        val sessionParams = SessionCreateParams.builder()
            .addAllPaymentMethodType(
                listOf(
                    SessionCreateParams.PaymentMethodType.CARD,
                    SessionCreateParams.PaymentMethodType.PAYPAL,
                )
            )
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("eur")
                            .setUnitAmountDecimal(BigDecimal(price * 100.0))
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Order price")
//                                    .setDescription("")
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("$paymentRedirectUrl?id=$orderId")
            .setCancelUrl("$paymentRedirectUrl?id=$orderId")
            .build()
        
        return Session.create(sessionParams)
    }
}





