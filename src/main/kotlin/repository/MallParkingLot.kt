package repository

import config.*
import config.VehicleType.*
import model.Receipt
import model.Ticket
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.ceil

class MallParkingLot {
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
            MOTORCYCLE_SCOOTER -> getFreeSpotIndexFrom(motorcycleScooterSpots)
            CAR_SUV -> getFreeSpotIndexFrom(carSUVSpots)
            BUS_TRUCK -> getFreeSpotIndexFrom(busTruckSpots)
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
            MOTORCYCLE_SCOOTER -> updateFreeSpotToParkedSpotIn(freeSpotIndex, motorcycleScooterSpots)
            CAR_SUV -> updateFreeSpotToParkedSpotIn(freeSpotIndex, carSUVSpots)
            BUS_TRUCK -> updateFreeSpotToParkedSpotIn(freeSpotIndex, busTruckSpots)
        }
    }

    private fun updateFreeSpotToParkedSpotIn(freeSpotIndex: Int, list: MutableList<Boolean>) {
        list[freeSpotIndex] = true
    }

    private fun decrementFreeSpotsCount(vehicleType: VehicleType) {
        when (vehicleType) {
            MOTORCYCLE_SCOOTER -> motorcycleScooterSpotsCount--
            CAR_SUV -> carSUVSpotsCount--
            BUS_TRUCK -> busTruckSpotsCount--
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
            MOTORCYCLE_SCOOTER -> updateParkedSpotToFreeSpotIn(spotNumber, motorcycleScooterSpots)
            CAR_SUV -> updateParkedSpotToFreeSpotIn(spotNumber, carSUVSpots)
            BUS_TRUCK -> updateParkedSpotToFreeSpotIn(spotNumber, busTruckSpots)
        }
    }

    private fun updateParkedSpotToFreeSpotIn(spotNumber: Int, list: MutableList<Boolean>) {
        list[spotNumber - 1] = false
    }

    private fun incrementFreeSpotsCount(vehicleType: VehicleType) {
        when (vehicleType) {
            MOTORCYCLE_SCOOTER -> motorcycleScooterSpotsCount++
            CAR_SUV -> carSUVSpotsCount++
            BUS_TRUCK -> busTruckSpotsCount++
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
            MOTORCYCLE_SCOOTER -> motorcycleScooterSpotsCount > 0
            CAR_SUV -> carSUVSpotsCount > 0
            BUS_TRUCK -> busTruckSpotsCount > 0
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
            MOTORCYCLE_SCOOTER -> calculateMotorcycleScooterFees(durationInHours)
            CAR_SUV -> calculateCarSUVFees(durationInHours)
            BUS_TRUCK -> calculateBusTruckFees(durationInHours)
        }
    }

    private fun calculateMotorcycleScooterFees(durationInHours: Int): Int {
        return durationInHours * MotorcycleScooterFees.mallPerHourFlatFees
    }

    private fun calculateCarSUVFees(durationInHours: Int): Int {
        return durationInHours * CarSUVFees.mallPerHourFlatFees
    }

    private fun calculateBusTruckFees(durationInHours: Int): Int {
        return durationInHours * BusTruckFees.mallPerHourFlatFees
    }

    private fun calculateDurationInHours(entryDateTime: LocalDateTime, exitDateTime: LocalDateTime): Int {
        val durationInSeconds = ChronoUnit.SECONDS.between(entryDateTime, exitDateTime).toInt()
        return ceil((durationInSeconds.toDouble() / 3600.00)).toInt()
    }
}