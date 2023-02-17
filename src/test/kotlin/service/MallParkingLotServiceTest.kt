package service

import config.Spots
import config.VehicleType.*
import exception.SpotNotAvailableException
import exception.VehicleNotParkedException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import repository.MallParkingLot
import java.time.LocalDateTime

class MallParkingLotServiceTest {

    @Test
    fun `it should return ticket with spot for parking Motorcycle and Scooter`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())

        val ticket = mallParkingLotService.park(MOTORCYCLE_SCOOTER)

        assertEquals(1, ticket.ticketNumber)
        assertEquals(1, ticket.spotNumber)
        assertEquals(MOTORCYCLE_SCOOTER, ticket.vehicleType)
    }

    @Test
    fun `it should return ticket with spot for parking Car and SUV`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())

        val ticket = mallParkingLotService.park(CAR_SUV)

        assertEquals(1, ticket.ticketNumber)
        assertEquals(1, ticket.spotNumber)
        assertEquals(CAR_SUV, ticket.vehicleType)
    }

    @Test
    fun `it should return ticket with spot for parking Bus and Truck`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())

        val ticket = mallParkingLotService.park(BUS_TRUCK)

        assertEquals(1, ticket.ticketNumber)
        assertEquals(1, ticket.spotNumber)
        assertEquals(BUS_TRUCK, ticket.vehicleType)
    }

    @Test
    fun `it should return receipt with fees for un-parking Motorcycle and Scooter`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())
        mallParkingLotService.park(MOTORCYCLE_SCOOTER)

        val receipt = mallParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))

        assertEquals(1, receipt.receiptNumber)
        assertEquals(20, receipt.fees)
    }

    @Test
    fun `it should return receipt with fees for un-parking Car and SUV`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())
        mallParkingLotService.park(CAR_SUV)

        val receipt = mallParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))

        assertEquals(1, receipt.receiptNumber)
        assertEquals(40, receipt.fees)
    }

    @Test
    fun `it should return receipt with fees for un-parking Bus and Truck`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())
        mallParkingLotService.park(BUS_TRUCK)

        val receipt = mallParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))

        assertEquals(1, receipt.receiptNumber)
        assertEquals(100, receipt.fees)
    }

    @Test
    fun `it should start spot numbers from one for every vehicle type`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())

        val ticketMotorcycleScooter = mallParkingLotService.park(MOTORCYCLE_SCOOTER)
        val ticketCarSUV = mallParkingLotService.park(CAR_SUV)
        val ticketBusTruck = mallParkingLotService.park(BUS_TRUCK)

        assertEquals(1, ticketMotorcycleScooter.spotNumber)
        assertEquals(1, ticketCarSUV.spotNumber)
        assertEquals(1, ticketBusTruck.spotNumber)
    }

    @Test
    fun `it should increment spot number for every vehicle with same vehicle type`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())

        val ticketCarSUVFirst = mallParkingLotService.park(CAR_SUV)
        val ticketCarSUVSecond = mallParkingLotService.park(CAR_SUV)

        assertEquals(1, ticketCarSUVFirst.spotNumber)
        assertEquals(2, ticketCarSUVSecond.spotNumber)
    }

    @Test
    fun `it should throw an exception when un-parking a vehicle which is not parked`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())

        assertThrows(VehicleNotParkedException::class.java) {
            mallParkingLotService.unpark(2, LocalDateTime.now().plusHours(2))
        }
    }

    @Test
    fun `it should throw an exception when un-parking a parked vehicle twice`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())

        mallParkingLotService.park(CAR_SUV)
        mallParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))

        assertThrows(VehicleNotParkedException::class.java) {
            mallParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))
        }
    }

    @Test
    fun `it should throw an exception when parking limit of Motorcycle and Scooter is exceeded`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())

        assertThrows(SpotNotAvailableException::class.java) {
            for (spot in 0 until Spots.motorcycleScooter + 1) {
                mallParkingLotService.park(MOTORCYCLE_SCOOTER)
            }
        }
    }

    @Test
    fun `it should throw an exception when parking limit of Car and SUV is exceeded`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())

        assertThrows(SpotNotAvailableException::class.java) {
            for (spot in 0 until Spots.carSUV + 1) {
                mallParkingLotService.park(CAR_SUV)
            }
        }
    }

    @Test
    fun `it should throw an exception when parking limit of Bus and Truck is exceeded`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())

        assertThrows(SpotNotAvailableException::class.java) {
            for (spot in 0 until Spots.busTruck + 1) {
                mallParkingLotService.park(BUS_TRUCK)
            }
        }
    }

    @Test
    fun `it should park two vehicles and un-park first vehicle and park another vehicle`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())

        mallParkingLotService.park(CAR_SUV)
        mallParkingLotService.park(CAR_SUV)
        mallParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))
        val ticket = mallParkingLotService.park(CAR_SUV)

        assertEquals(1, ticket.spotNumber)
        assertEquals(3, ticket.ticketNumber)
    }

    @Test
    fun `it should park two vehicles and un-park second vehicle and park another vehicle`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())

        mallParkingLotService.park(CAR_SUV)
        mallParkingLotService.park(CAR_SUV)
        mallParkingLotService.unpark(2, LocalDateTime.now().plusHours(2))
        val ticket = mallParkingLotService.park(CAR_SUV)

        assertEquals(2, ticket.spotNumber)
        assertEquals(3, ticket.ticketNumber)
    }

    @Test
    fun `Motorcycle parked for 3 hours and 30 mins`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())

        mallParkingLotService.park(MOTORCYCLE_SCOOTER)
        val receipt = mallParkingLotService.unpark(
            1,
            LocalDateTime.now().plusHours(3).plusMinutes(30)
        )

        assertEquals(40, receipt.fees)
    }

    @Test
    fun `Car parked for 6 hours and 1 min`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())

        mallParkingLotService.park(CAR_SUV)
        val receipt = mallParkingLotService.unpark(
            1,
            LocalDateTime.now().plusHours(6).plusMinutes(1)
        )

        assertEquals(140, receipt.fees)
    }

    @Test
    fun `Truck parked for 1 hour and 59 mins`() {
        val mallParkingLotService = MallParkingLotService(MallParkingLot())

        mallParkingLotService.park(BUS_TRUCK)
        val receipt = mallParkingLotService.unpark(
            1,
            LocalDateTime.now().plusHours(1).plusMinutes(59)
        )

        assertEquals(100, receipt.fees)
    }
}