import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

interface AddFriendsService {
    @POST("/friendrequest/{uid}/{key}/{friend_key}")
    fun addFriends(
        @Path("uid") uid: String,
        @Path("key") key: String,
        @Path("friend_key") friendKey: String
    ): Call<Void> // Возвращаем Call<Void>
}