import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface PostInvite {
    @POST("/friendrequest/{uid}/{key}/{friend_key}")
    suspend fun postInvite(
        @Path("uid") uid: String,
        @Path("key") key: String,
        @Path("friend_key") friend_key: String
    ): Response<ResponseBody>
}