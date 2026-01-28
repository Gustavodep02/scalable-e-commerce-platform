package com.gustavo.payment_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class Payment {

    private String sessionId;
    @Column(columnDefinition = "TEXT")
    private String checkoutUrl;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID orderId;
    private Long amount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
