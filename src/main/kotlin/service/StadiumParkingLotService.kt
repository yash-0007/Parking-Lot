package service

import config.VehicleType
import exception.SpotNotAvailableException
import exception.VehicleNotParkedException
import model.Receipt
import model.Ticket
import repository.StadiumParkingLot
import java.time.LocalDateTime

class StadiumParkingLotService(private val stadiumParkingLot: StadiumParkingLot) : ParkingLotService() {
    override fun park(vehicleType: VehicleType): Ticket {
        if (!stadiumParkingLot.isSpotAvailable(vehicleType))
            throw SpotNotAvailableException()

        val ticket = stadiumParkingLot.generateTicket(vehicleType)
        stadiumParkingLot.addTicket(ticket)

        return ticket
    }

    override fun unpark(ticketNumber: Int, exitDateTime: LocalDateTime): Receipt {
        if (!stadiumParkingLot.isVehicleParked(ticketNumber))
            throw VehicleNotParkedException()

        val receipt = stadiumParkingLot.generateReceipt(ticketNumber, exitDateTime)
        stadiumParkingLot.addReceipt(receipt)

        return receipt
    }
}