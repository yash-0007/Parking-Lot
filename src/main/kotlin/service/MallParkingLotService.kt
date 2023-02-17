package service

import config.VehicleType
import exception.SpotNotAvailableException
import exception.VehicleNotParkedException
import model.Receipt
import model.Ticket
import repository.MallParkingLot
import java.time.LocalDateTime

class MallParkingLotService(private val mallParkingLot: MallParkingLot) : ParkingLotService() {

    override fun park(vehicleType: VehicleType): Ticket {
        if (!mallParkingLot.isSpotAvailable(vehicleType))
            throw SpotNotAvailableException()

        val ticket = mallParkingLot.generateTicket(vehicleType)
        mallParkingLot.addTicket(ticket)

        return ticket
    }

    override fun unpark(ticketNumber: Int, exitDateTime: LocalDateTime): Receipt {
        if (!mallParkingLot.isVehicleParked(ticketNumber))
            throw VehicleNotParkedException()

        val receipt = mallParkingLot.generateReceipt(ticketNumber, exitDateTime)
        mallParkingLot.addReceipt(receipt)

        return receipt
    }
}