import config.VehicleType
import repository.MallParkingLot
import service.MallParkingLotService
import java.time.LocalDateTime

fun main() {
    val mallParkingLotService = MallParkingLotService(MallParkingLot())

    mallParkingLotService.park(VehicleType.MOTORCYCLE_SCOOTER)

    val receipt = mallParkingLotService.unpark(
        1,
        LocalDateTime.now().plusHours(4)
    )

    println(receipt.fees)
    println(receipt.receiptNumber)
    println(receipt.entryDateTime)
    println(receipt.exitDateTime)
}