import kotlinx.serialization.Serializable

@Serializable
data class UserKeyResponse(val userKey: String)

@Serializable
data class MapMarker(
    val key: String,
    val username: String,
    val imguser: String,
    val photomark: String,
    val street: String,
    val id: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val whatHappens: String,
    val startDate: String,
    val endDate: String,
    val startTime: String,
    val endTime: String,
    val participants: Int,
    val access: Boolean
)


data class Post_User_info_data(
    val name: String,
    val img: String
)
