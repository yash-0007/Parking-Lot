package service

import config.VehicleType
import exception.SpotNotAvailableException
import exception.VehicleNotParkedException
import model.Receipt
import model.Ticket
import repository.AirportParkingLot
import java.time.LocalDateTime

class AirportParkingLotService(private val airportParkingLot: AirportParkingLot) : ParkingLotService() {
    override fun park(vehicleType: VehicleType): Ticket {
        if (!airportParkingLot.isSpotAvailable(vehicleType))
            throw SpotNotAvailableException()

        val ticket = airportParkingLot.generateTicket(vehicleType)
        airportParkingLot.addTicket(ticket)

        return ticket
    }

    override fun unpark(ticketNumber: Int, exitDateTime: LocalDateTime): Receipt {
        if (!airportParkingLot.isVehicleParked(ticketNumber))
            throw VehicleNotParkedException()

        val receipt = airportParkingLot.generateReceipt(ticketNumber, exitDateTime)
        airportParkingLot.addReceipt(receipt)

        return receipt
    }
}