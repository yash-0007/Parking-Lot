package repository

import config.VehicleType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class AirportParkingLotTest {
    @Test
    fun `it should return ticket with spot for parking Motorcycle and Scooter`() {
        val airportParkingLot = AirportParkingLot()

        val ticket = airportParkingLot.generateTicket(VehicleType.MOTORCYCLE_SCOOTER)

        Assertions.assertEquals(1, ticket.ticketNumber)
        Assertions.assertEquals(1, ticket.spotNumber)
        Assertions.assertEquals(VehicleType.MOTORCYCLE_SCOOTER, ticket.vehicleType)
    }

    @Test
    fun `it should return ticket with spot for parking Car and SUV`() {
        val airportParkingLot = AirportParkingLot()

        val ticket = airportParkingLot.generateTicket(VehicleType.CAR_SUV)

        Assertions.assertEquals(1, ticket.ticketNumber)
        Assertions.assertEquals(1, ticket.spotNumber)
        Assertions.assertEquals(VehicleType.CAR_SUV, ticket.vehicleType)
    }

    @Test
    fun `it should return ticket with spot for parking Bus and Truck`() {
        val airportParkingLot = AirportParkingLot()

        val ticket = airportParkingLot.generateTicket(VehicleType.BUS_TRUCK)

        Assertions.assertEquals(1, ticket.ticketNumber)
        Assertions.assertEquals(1, ticket.spotNumber)
        Assertions.assertEquals(VehicleType.BUS_TRUCK, ticket.vehicleType)
    }

    @Test
    fun `it should return receipt with fees for un-parking Motorcycle and Scooter`() {
        val airportParkingLot = AirportParkingLot()
        val ticket = airportParkingLot.generateTicket(VehicleType.MOTORCYCLE_SCOOTER)
        airportParkingLot.addTicket(ticket)

        val receipt = airportParkingLot.generateReceipt(1, LocalDateTime.now().plusHours(2))

        Assertions.assertEquals(1, receipt.receiptNumber)
        Assertions.assertEquals(40, receipt.fees)
    }

    @Test
    fun `it should return receipt with fees for un-parking Car and SUV`() {
        val airportParkingLot = AirportParkingLot()
        val ticket = airportParkingLot.generateTicket(VehicleType.CAR_SUV)
        airportParkingLot.addTicket(ticket)

        val receipt = airportParkingLot.generateReceipt(1, LocalDateTime.now().plusHours(2))

        Assertions.assertEquals(1, receipt.receiptNumber)
        Assertions.assertEquals(60, receipt.fees)
    }

    @Test
    fun `it should return receipt with fees for un-parking Bus and Truck`() {
        val airportParkingLot = AirportParkingLot()
        val ticket = airportParkingLot.generateTicket(VehicleType.BUS_TRUCK)
        airportParkingLot.addTicket(ticket)

        val receipt = airportParkingLot.generateReceipt(1, LocalDateTime.now().plusHours(2))

        Assertions.assertEquals(1, receipt.receiptNumber)
        Assertions.assertEquals(0, receipt.fees)
    }

    @Test
    fun `it should start spot numbers from one for every vehicle type`() {
        val airportParkingLot = AirportParkingLot()

        val ticketMotorcycleScooter = airportParkingLot.generateTicket(VehicleType.MOTORCYCLE_SCOOTER)
        val ticketCarSUV = airportParkingLot.generateTicket(VehicleType.CAR_SUV)
        val ticketBusTruck = airportParkingLot.generateTicket(VehicleType.BUS_TRUCK)

        Assertions.assertEquals(1, ticketMotorcycleScooter.spotNumber)
        Assertions.assertEquals(1, ticketCarSUV.spotNumber)
        Assertions.assertEquals(1, ticketBusTruck.spotNumber)
    }

    @Test
    fun `it should increment spot number for every vehicle with same vehicle type`() {
        val airportParkingLot = AirportParkingLot()

        val ticketCarSUVFirst = airportParkingLot.generateTicket(VehicleType.CAR_SUV)
        val ticketCarSUVSecond = airportParkingLot.generateTicket(VehicleType.CAR_SUV)

        Assertions.assertEquals(1, ticketCarSUVFirst.spotNumber)
        Assertions.assertEquals(2, ticketCarSUVSecond.spotNumber)
    }
}