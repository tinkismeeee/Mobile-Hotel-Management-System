import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.ext.realmListOf

class BookingRealm : RealmObject {
    @PrimaryKey
    var bookingId: String = ""
    var roomId: Int = 0
    var userId: Int = 0
    var checkIn: String = ""
    var checkOut: String = ""
    var totalGuests: Int = 0
    var services = realmListOf<String>()
}
