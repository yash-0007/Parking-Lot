package model

import config.VehicleType
import java.time.LocalDateTime

class Ticket(
    val ticketNumber: Int,
    val spotNumber: Int,
    val entryDateTime: LocalDateTime,
    val vehicleType: VehicleType
)