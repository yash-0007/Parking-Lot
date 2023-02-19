package repository

import config.VehicleType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class MallParkingLotTest {
    @Test
    fun `it should return ticket with spot for parking Motorcycle and Scooter`() {
        val mallParkingLot = MallParkingLot()

        val ticket = mallParkingLot.generateTicket(VehicleType.MOTORCYCLE_SCOOTER)

        assertEquals(1, ticket.ticketNumber)
        assertEquals(1, ticket.spotNumber)
        assertEquals(VehicleType.MOTORCYCLE_SCOOTER, ticket.vehicleType)
    }

    @Test
    fun `it should return ticket with spot for parking Car and SUV`() {
        val mallParkingLot = MallParkingLot()

        val ticket = mallParkingLot.generateTicket(VehicleType.CAR_SUV)

        assertEquals(1, ticket.ticketNumber)
        assertEquals(1, ticket.spotNumber)
        assertEquals(VehicleType.CAR_SUV, ticket.vehicleType)
    }

    @Test
    fun `it should return ticket with spot for parking Bus and Truck`() {
        val mallParkingLot = MallParkingLot()

        val ticket = mallParkingLot.generateTicket(VehicleType.BUS_TRUCK)

        assertEquals(1, ticket.ticketNumber)
        assertEquals(1, ticket.spotNumber)
        assertEquals(VehicleType.BUS_TRUCK, ticket.vehicleType)
    }

    @Test
    fun `it should return receipt with fees for un-parking Motorcycle and Scooter`() {
        val mallParkingLot = MallParkingLot()
        val ticket = mallParkingLot.generateTicket(VehicleType.MOTORCYCLE_SCOOTER)
        mallParkingLot.addTicket(ticket)

        val receipt = mallParkingLot.generateReceipt(1, LocalDateTime.now().plusHours(2))

        assertEquals(1, receipt.receiptNumber)
        assertEquals(20, receipt.fees)
    }

    @Test
    fun `it should return receipt with fees for un-parking Car and SUV`() {
        val mallParkingLot = MallParkingLot()
        val ticket = mallParkingLot.generateTicket(VehicleType.CAR_SUV)
        mallParkingLot.addTicket(ticket)

        val receipt = mallParkingLot.generateReceipt(1, LocalDateTime.now().plusHours(2))

        assertEquals(1, receipt.receiptNumber)
        assertEquals(40, receipt.fees)
    }

    @Test
    fun `it should return receipt with fees for un-parking Bus and Truck`() {
        val mallParkingLot = MallParkingLot()
        val ticket = mallParkingLot.generateTicket(VehicleType.BUS_TRUCK)
        mallParkingLot.addTicket(ticket)

        val receipt = mallParkingLot.generateReceipt(1, LocalDateTime.now().plusHours(2))

        assertEquals(1, receipt.receiptNumber)
        assertEquals(100, receipt.fees)
    }

    @Test
    fun `it should start spot numbers from one for every vehicle type`() {
        val mallParkingLot = MallParkingLot()

        val ticketMotorcycleScooter = mallParkingLot.generateTicket(VehicleType.MOTORCYCLE_SCOOTER)
        val ticketCarSUV = mallParkingLot.generateTicket(VehicleType.CAR_SUV)
        val ticketBusTruck = mallParkingLot.generateTicket(VehicleType.BUS_TRUCK)

        assertEquals(1, ticketMotorcycleScooter.spotNumber)
        assertEquals(1, ticketCarSUV.spotNumber)
        assertEquals(1, ticketBusTruck.spotNumber)
    }

    @Test
    fun `it should increment spot number for every vehicle with same vehicle type`() {
        val mallParkingLot = MallParkingLot()

        val ticketCarSUVFirst = mallParkingLot.generateTicket(VehicleType.CAR_SUV)
        val ticketCarSUVSecond = mallParkingLot.generateTicket(VehicleType.CAR_SUV)

        assertEquals(1, ticketCarSUVFirst.spotNumber)
        assertEquals(2, ticketCarSUVSecond.spotNumber)
    }
}