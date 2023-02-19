package repository

import config.VehicleType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class StadiumParkingLotTest {
    @Test
    fun `it should return ticket with spot for parking Motorcycle and Scooter`() {
        val stadiumParkingLot = StadiumParkingLot()

        val ticket = stadiumParkingLot.generateTicket(VehicleType.MOTORCYCLE_SCOOTER)

        Assertions.assertEquals(1, ticket.ticketNumber)
        Assertions.assertEquals(1, ticket.spotNumber)
        Assertions.assertEquals(VehicleType.MOTORCYCLE_SCOOTER, ticket.vehicleType)
    }

    @Test
    fun `it should return ticket with spot for parking Car and SUV`() {
        val stadiumParkingLot = StadiumParkingLot()

        val ticket = stadiumParkingLot.generateTicket(VehicleType.CAR_SUV)

        Assertions.assertEquals(1, ticket.ticketNumber)
        Assertions.assertEquals(1, ticket.spotNumber)
        Assertions.assertEquals(VehicleType.CAR_SUV, ticket.vehicleType)
    }

    @Test
    fun `it should return ticket with spot for parking Bus and Truck`() {
        val stadiumParkingLot = StadiumParkingLot()

        val ticket = stadiumParkingLot.generateTicket(VehicleType.BUS_TRUCK)

        Assertions.assertEquals(1, ticket.ticketNumber)
        Assertions.assertEquals(1, ticket.spotNumber)
        Assertions.assertEquals(VehicleType.BUS_TRUCK, ticket.vehicleType)
    }

    @Test
    fun `it should return receipt with fees for un-parking Motorcycle and Scooter`() {
        val stadiumParkingLot = StadiumParkingLot()
        val ticket = stadiumParkingLot.generateTicket(VehicleType.MOTORCYCLE_SCOOTER)
        stadiumParkingLot.addTicket(ticket)

        val receipt = stadiumParkingLot.generateReceipt(1, LocalDateTime.now().plusHours(2))

        Assertions.assertEquals(1, receipt.receiptNumber)
        Assertions.assertEquals(30, receipt.fees)
    }

    @Test
    fun `it should return receipt with fees for un-parking Car and SUV`() {
        val stadiumParkingLot = StadiumParkingLot()
        val ticket = stadiumParkingLot.generateTicket(VehicleType.CAR_SUV)
        stadiumParkingLot.addTicket(ticket)

        val receipt = stadiumParkingLot.generateReceipt(1, LocalDateTime.now().plusHours(2))

        Assertions.assertEquals(1, receipt.receiptNumber)
        Assertions.assertEquals(60, receipt.fees)
    }

    @Test
    fun `it should return receipt with fees for un-parking Bus and Truck`() {
        val stadiumParkingLot = StadiumParkingLot()
        val ticket = stadiumParkingLot.generateTicket(VehicleType.BUS_TRUCK)
        stadiumParkingLot.addTicket(ticket)

        val receipt = stadiumParkingLot.generateReceipt(1, LocalDateTime.now().plusHours(2))

        Assertions.assertEquals(1, receipt.receiptNumber)
        Assertions.assertEquals(0, receipt.fees)
    }

    @Test
    fun `it should start spot numbers from one for every vehicle type`() {
        val stadiumParkingLot = StadiumParkingLot()

        val ticketMotorcycleScooter = stadiumParkingLot.generateTicket(VehicleType.MOTORCYCLE_SCOOTER)
        val ticketCarSUV = stadiumParkingLot.generateTicket(VehicleType.CAR_SUV)
        val ticketBusTruck = stadiumParkingLot.generateTicket(VehicleType.BUS_TRUCK)

        Assertions.assertEquals(1, ticketMotorcycleScooter.spotNumber)
        Assertions.assertEquals(1, ticketCarSUV.spotNumber)
        Assertions.assertEquals(1, ticketBusTruck.spotNumber)
    }

    @Test
    fun `it should increment spot number for every vehicle with same vehicle type`() {
        val stadiumParkingLot = StadiumParkingLot()

        val ticketCarSUVFirst = stadiumParkingLot.generateTicket(VehicleType.CAR_SUV)
        val ticketCarSUVSecond = stadiumParkingLot.generateTicket(VehicleType.CAR_SUV)

        Assertions.assertEquals(1, ticketCarSUVFirst.spotNumber)
        Assertions.assertEquals(2, ticketCarSUVSecond.spotNumber)
    }
}