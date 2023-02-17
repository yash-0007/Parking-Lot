package service

import config.Spots
import config.VehicleType
import exception.SpotNotAvailableException
import exception.VehicleNotParkedException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import repository.StadiumParkingLot
import java.time.LocalDateTime

class StadiumParkingLotServiceTest {
    @Test
    fun `it should return ticket with spot for parking Motorcycle and Scooter`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())

        val ticket = stadiumParkingLotService.park(VehicleType.MOTORCYCLE_SCOOTER)

        Assertions.assertEquals(1, ticket.ticketNumber)
        Assertions.assertEquals(1, ticket.spotNumber)
        Assertions.assertEquals(VehicleType.MOTORCYCLE_SCOOTER, ticket.vehicleType)
    }

    @Test
    fun `it should return ticket with spot for parking Car and SUV`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())

        val ticket = stadiumParkingLotService.park(VehicleType.CAR_SUV)

        Assertions.assertEquals(1, ticket.ticketNumber)
        Assertions.assertEquals(1, ticket.spotNumber)
        Assertions.assertEquals(VehicleType.CAR_SUV, ticket.vehicleType)
    }

    @Test
    fun `it should return ticket with spot for parking Bus and Truck`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())

        val ticket = stadiumParkingLotService.park(VehicleType.BUS_TRUCK)

        Assertions.assertEquals(1, ticket.ticketNumber)
        Assertions.assertEquals(1, ticket.spotNumber)
        Assertions.assertEquals(VehicleType.BUS_TRUCK, ticket.vehicleType)
    }

    @Test
    fun `it should return receipt with fees for un-parking Motorcycle and Scooter`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())
        stadiumParkingLotService.park(VehicleType.MOTORCYCLE_SCOOTER)

        val receipt = stadiumParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))

        Assertions.assertEquals(1, receipt.receiptNumber)
        Assertions.assertEquals(30, receipt.fees)
    }

    @Test
    fun `it should return receipt with fees for un-parking Car and SUV`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())
        stadiumParkingLotService.park(VehicleType.CAR_SUV)

        val receipt = stadiumParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))

        Assertions.assertEquals(1, receipt.receiptNumber)
        Assertions.assertEquals(60, receipt.fees)
    }

    @Test
    fun `it should return receipt with fees for un-parking Bus and Truck`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())
        stadiumParkingLotService.park(VehicleType.BUS_TRUCK)

        val receipt = stadiumParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))

        Assertions.assertEquals(1, receipt.receiptNumber)
        Assertions.assertEquals(0, receipt.fees)
    }

    @Test
    fun `it should start spot numbers from one for every vehicle type`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())

        val ticketMotorcycleScooter = stadiumParkingLotService.park(VehicleType.MOTORCYCLE_SCOOTER)
        val ticketCarSUV = stadiumParkingLotService.park(VehicleType.CAR_SUV)
        val ticketBusTruck = stadiumParkingLotService.park(VehicleType.BUS_TRUCK)

        Assertions.assertEquals(1, ticketMotorcycleScooter.spotNumber)
        Assertions.assertEquals(1, ticketCarSUV.spotNumber)
        Assertions.assertEquals(1, ticketBusTruck.spotNumber)
    }

    @Test
    fun `it should increment spot number for every vehicle with same vehicle type`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())

        val ticketCarSUVFirst = stadiumParkingLotService.park(VehicleType.CAR_SUV)
        val ticketCarSUVSecond = stadiumParkingLotService.park(VehicleType.CAR_SUV)

        Assertions.assertEquals(1, ticketCarSUVFirst.spotNumber)
        Assertions.assertEquals(2, ticketCarSUVSecond.spotNumber)
    }

    @Test
    fun `it should throw an exception when un-parking a vehicle which is not parked`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())

        Assertions.assertThrows(VehicleNotParkedException::class.java) {
            stadiumParkingLotService.unpark(2, LocalDateTime.now().plusHours(2))
        }
    }

    @Test
    fun `it should throw an exception when un-parking a parked vehicle twice`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())

        stadiumParkingLotService.park(VehicleType.CAR_SUV)
        stadiumParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))

        Assertions.assertThrows(VehicleNotParkedException::class.java) {
            stadiumParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))
        }
    }

    @Test
    fun `it should throw an exception when parking limit of Motorcycle and Scooter is exceeded`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())

        Assertions.assertThrows(SpotNotAvailableException::class.java) {
            for (spot in 0 until Spots.motorcycleScooter + 1) {
                stadiumParkingLotService.park(VehicleType.MOTORCYCLE_SCOOTER)
            }
        }
    }

    @Test
    fun `it should throw an exception when parking limit of Car and SUV is exceeded`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())

        Assertions.assertThrows(SpotNotAvailableException::class.java) {
            for (spot in 0 until Spots.carSUV + 1) {
                stadiumParkingLotService.park(VehicleType.CAR_SUV)
            }
        }
    }

    @Test
    fun `it should throw an exception when parking limit of Bus and Truck is exceeded`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())

        Assertions.assertThrows(SpotNotAvailableException::class.java) {
            for (spot in 0 until Spots.busTruck + 1) {
                stadiumParkingLotService.park(VehicleType.BUS_TRUCK)
            }
        }
    }

    @Test
    fun `it should park two vehicles and un-park first vehicle and park another vehicle`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())

        stadiumParkingLotService.park(VehicleType.CAR_SUV)
        stadiumParkingLotService.park(VehicleType.CAR_SUV)
        stadiumParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))
        val ticket = stadiumParkingLotService.park(VehicleType.CAR_SUV)

        Assertions.assertEquals(1, ticket.spotNumber)
        Assertions.assertEquals(3, ticket.ticketNumber)
    }

    @Test
    fun `it should park two vehicles and un-park second vehicle and park another vehicle`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())

        stadiumParkingLotService.park(VehicleType.CAR_SUV)
        stadiumParkingLotService.park(VehicleType.CAR_SUV)
        stadiumParkingLotService.unpark(2, LocalDateTime.now().plusHours(2))
        val ticket = stadiumParkingLotService.park(VehicleType.CAR_SUV)

        Assertions.assertEquals(2, ticket.spotNumber)
        Assertions.assertEquals(3, ticket.ticketNumber)
    }

    @Test
    fun `Motorcycle parked for 3 hours and 40 mins`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())

        stadiumParkingLotService.park(VehicleType.MOTORCYCLE_SCOOTER)
        val receipt = stadiumParkingLotService.unpark(
            1,
            LocalDateTime.now().plusHours(3).plusMinutes(40)
        )

        Assertions.assertEquals(30, receipt.fees)
    }

    @Test
    fun `Motorcycle parked for 14 hours and 59 mins`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())

        stadiumParkingLotService.park(VehicleType.MOTORCYCLE_SCOOTER)
        val receipt = stadiumParkingLotService.unpark(
            1,
            LocalDateTime.now().plusHours(14).plusMinutes(59)
        )

        Assertions.assertEquals(390, receipt.fees)
    }

    @Test
    fun `Electric SUV parked for 11 hours and 30 mins`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())

        stadiumParkingLotService.park(VehicleType.CAR_SUV)
        val receipt = stadiumParkingLotService.unpark(
            1,
            LocalDateTime.now().plusHours(11).plusMinutes(30)
        )

        Assertions.assertEquals(180, receipt.fees)
    }

    @Test
    fun `SUV parked for 13 hours and 5 mins`() {
        val stadiumParkingLotService = StadiumParkingLotService(StadiumParkingLot())

        stadiumParkingLotService.park(VehicleType.CAR_SUV)
        val receipt = stadiumParkingLotService.unpark(
            1,
            LocalDateTime.now().plusHours(13).plusMinutes(5)
        )

        Assertions.assertEquals(580, receipt.fees)
    }
}