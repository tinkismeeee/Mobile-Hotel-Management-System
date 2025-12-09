import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.ext.realmListOf

class BookingRealm : RealmObject {
    var roomNumber: String = ""
    var floor: Int = 0
    var roomType: String = ""
    var maxGuests: Int = 0
    var bedCount: Int = 0
    var description: String = ""
    @PrimaryKey
    var bookingId: String = ""
    var roomId: Int = 0
    var userId: Int = 0
    var checkIn: String = ""
    var checkOut: String = ""
    var totalGuests: Int = 0
    var price : Int = 0
    var services = realmListOf<String>()
}
