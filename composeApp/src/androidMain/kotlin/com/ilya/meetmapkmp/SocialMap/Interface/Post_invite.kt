import com.ilya.meetmapkmp.SocialMap.DataModel.TokenResponse
import kotlinx.serialization.json.JsonObject
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface PostInvite {
    @POST("friendrequest/{uid}/{key}/{friendKey}")
    suspend fun postInvite(
        @Path("uid") uid: String,
        @Path("key") key: String,
        @Path("friendKey") friendKey: String
    ): Response<TokenResponse>
}