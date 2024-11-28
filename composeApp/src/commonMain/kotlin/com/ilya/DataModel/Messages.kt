
import kotlinx.serialization.Serializable


@Serializable
data class Messages_Chat(
    val content: String? = null,
    val profilerIMG: String? = null,
    val messageTime: Long? = null,
    val key: String? = null,
    val senderUsername: String? = null,
    val gifUrls: List<String> = emptyList(),
    val imageUrls: List<String> = emptyList(),
    val videoUrls: List<String> = emptyList(),
    val fileUrls: List<String> = emptyList()

)
