package model

import java.time.LocalDateTime

class Receipt(
    val receiptNumber: Int,
    val entryDateTime: LocalDateTime,
    val exitDateTime: LocalDateTime,
    val fees: Int
)