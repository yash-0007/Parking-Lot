package repository

import config.CarSUVFees
import config.MotorcycleScooterFees
import config.Spots
import config.VehicleType
import model.Receipt
import model.Ticket
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.ceil

class AirportParkingLot {
    private var motorcycleScooterSpotsCount = Spots.motorcycleScooter
    private var carSUVSpotsCount = Spots.carSUV
    private var busTruckSpotsCount = Spots.busTruck

    private var motorcycleScooterSpots = MutableList(Spots.motorcycleScooter) { false }
    private var carSUVSpots = MutableList(Spots.carSUV) { false }
    private var busTruckSpots = MutableList(Spots.busTruck) { false }

    private var ticketNumber = 0
    private var receiptNumber = 0

    private var tickets = mutableListOf<Ticket>()
    private var receipts = mutableListOf<Receipt>()

    private fun incrementTicketNumber() {
        ticketNumber++
    }

    private fun incrementReceiptNumber() {
        receiptNumber++
    }

    private fun getFreeSpotIndex(vehicleType: VehicleType): Int {
        return when (vehicleType) {
            VehicleType.MOTORCYCLE_SCOOTER -> getFreeSpotIndexFrom(motorcycleScooterSpots)
            VehicleType.CAR_SUV -> getFreeSpotIndexFrom(carSUVSpots)
            VehicleType.BUS_TRUCK -> getFreeSpotIndexFrom(busTruckSpots)
        }
    }

    private fun getFreeSpotIndexFrom(list: MutableList<Boolean>): Int {
        for (index in list.indices) {
            if (!list[index]) {
                return index
            }
        }
        return -1
    }

    private fun updateFreeSpotToParkedSpot(freeSpotIndex: Int, vehicleType: VehicleType) {
        when (vehicleType) {
            VehicleType.MOTORCYCLE_SCOOTER -> updateFreeSpotToParkedSpotIn(freeSpotIndex, motorcycleScooterSpots)
            VehicleType.CAR_SUV -> updateFreeSpotToParkedSpotIn(freeSpotIndex, carSUVSpots)
            VehicleType.BUS_TRUCK -> updateFreeSpotToParkedSpotIn(freeSpotIndex, busTruckSpots)
        }
    }

    private fun updateFreeSpotToParkedSpotIn(freeSpotIndex: Int, list: MutableList<Boolean>) {
        list[freeSpotIndex] = true
    }

    private fun decrementFreeSpotsCount(vehicleType: VehicleType) {
        when (vehicleType) {
            VehicleType.MOTORCYCLE_SCOOTER -> motorcycleScooterSpotsCount--
            VehicleType.CAR_SUV -> carSUVSpotsCount--
            VehicleType.BUS_TRUCK -> busTruckSpotsCount--
        }
    }

    fun generateTicket(vehicleType: VehicleType): Ticket {
        incrementTicketNumber()

        val freeSpotIndex = getFreeSpotIndex(vehicleType)
        updateFreeSpotToParkedSpot(freeSpotIndex, vehicleType)
        decrementFreeSpotsCount(vehicleType)

        val spotNumber = freeSpotIndex + 1
        val entryDateTime = LocalDateTime.now()

        return Ticket(ticketNumber, spotNumber, entryDateTime, vehicleType)
    }

    fun addTicket(ticket: Ticket) {
        tickets += ticket
    }

    private fun getTicket(ticketNumber: Int): Ticket {
        for (index in tickets.indices) {
            if (ticketNumber == tickets[index].ticketNumber)
                return tickets[index]
        }
        return tickets[tickets.size - 1]
    }

    private fun updateParkedSpotToFreeSpot(spotNumber: Int, vehicleType: VehicleType) {
        when (vehicleType) {
            VehicleType.MOTORCYCLE_SCOOTER -> updateParkedSpotToFreeSpotIn(spotNumber, motorcycleScooterSpots)
            VehicleType.CAR_SUV -> updateParkedSpotToFreeSpotIn(spotNumber, carSUVSpots)
            VehicleType.BUS_TRUCK -> updateParkedSpotToFreeSpotIn(spotNumber, busTruckSpots)
        }
    }

    private fun updateParkedSpotToFreeSpotIn(spotNumber: Int, list: MutableList<Boolean>) {
        list[spotNumber - 1] = false
    }

    private fun incrementFreeSpotsCount(vehicleType: VehicleType) {
        when (vehicleType) {
            VehicleType.MOTORCYCLE_SCOOTER -> motorcycleScooterSpotsCount++
            VehicleType.CAR_SUV -> carSUVSpotsCount++
            VehicleType.BUS_TRUCK -> busTruckSpotsCount++
        }
    }

    private fun removeTicket(ticket: Ticket) {
        tickets.remove(ticket)
    }

    fun generateReceipt(ticketNumber: Int, exitDateTime: LocalDateTime): Receipt {
        incrementReceiptNumber()

        val ticket = getTicket(ticketNumber)
        val vehicleType = ticket.vehicleType

        val entryDateTime = ticket.entryDateTime

        updateParkedSpotToFreeSpot(ticket.spotNumber, vehicleType)
        incrementFreeSpotsCount(vehicleType)

        val fees = calculateFees(vehicleType, entryDateTime, exitDateTime)
        removeTicket(ticket)

        return Receipt(receiptNumber, entryDateTime, exitDateTime, fees)
    }

    fun addReceipt(receipt: Receipt) {
        receipts += receipt
    }

    fun isSpotAvailable(vehicleType: VehicleType): Boolean {
        return when (vehicleType) {
            VehicleType.MOTORCYCLE_SCOOTER -> motorcycleScooterSpotsCount > 0
            VehicleType.CAR_SUV -> carSUVSpotsCount > 0
            VehicleType.BUS_TRUCK -> busTruckSpotsCount > 0
        }
    }

    fun isVehicleParked(ticketNumber: Int): Boolean {
        for (index in tickets.indices) {
            if (ticketNumber == tickets[index].ticketNumber) return true
        }
        return false
    }

    private fun calculateFees(
        vehicleType: VehicleType,
        entryDateTime: LocalDateTime,
        exitDateTime: LocalDateTime
    ): Int {
        val durationInHours = calculateDurationInHours(entryDateTime, exitDateTime)
        return when (vehicleType) {
            VehicleType.MOTORCYCLE_SCOOTER -> calculateMotorcycleScooterFees(durationInHours)
            VehicleType.CAR_SUV -> calculateCarSUVFees(durationInHours)
            VehicleType.BUS_TRUCK -> calculateBusTruckFees(durationInHours)
        }
    }

    private fun calculateMotorcycleScooterFees(durationInHours: Int): Int {
        var fees = 0
        fees = when (durationInHours) {
            in 0..1 -> MotorcycleScooterFees.airportFlatFeesFrom0to1Hr

            in 2..8 -> MotorcycleScooterFees.airportFlatFeesFrom1to8Hrs

            in 9..24 -> MotorcycleScooterFees.airportFlatFeesFrom8to24Hrs

            else -> ceil(durationInHours.toDouble() / 24.00).toInt() * MotorcycleScooterFees.airportPerDayFlatFeesAfter24Hrs
        }
        return fees
    }

    private fun calculateCarSUVFees(durationInHours: Int): Int {
        var fees = 0
        fees = when (durationInHours) {
            in 0..12 -> CarSUVFees.airportFlatFeesFrom0to12Hrs

            in 13..24 -> CarSUVFees.airportFlatFeesFrom12to24Hrs

            else -> ceil(durationInHours.toDouble() / 24.00).toInt() * CarSUVFees.airportPerDayFlatFeesAfter24Hrs
        }
        return fees
    }

    private fun calculateBusTruckFees(durationInHours: Int): Int {
        return 0
    }

    private fun calculateDurationInHours(entryDateTime: LocalDateTime, exitDateTime: LocalDateTime): Int {
        val durationInSeconds = ChronoUnit.SECONDS.between(entryDateTime, exitDateTime).toInt()
        return ceil((durationInSeconds.toDouble() / 3600.00)).toInt()
    }
}