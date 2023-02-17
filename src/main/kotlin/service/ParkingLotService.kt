package service

import config.VehicleType
import model.Receipt
import model.Ticket
import java.time.LocalDateTime

abstract class ParkingLotService {
    abstract fun park(vehicleType: VehicleType): Ticket
    abstract fun unpark(ticketNumber: Int, exitDateTime: LocalDateTime): Receipt
}