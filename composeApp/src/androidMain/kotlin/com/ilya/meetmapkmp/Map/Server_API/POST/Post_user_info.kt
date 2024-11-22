import android.util.Log
import com.ilya.MeetingMap.Map.Interfaces.Post_user_info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


suspend fun post_user_info(key: String, uid: String, name: String, img: String) {
    try {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://meetmap.up.railway.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(Post_user_info::class.java)

        val info = Post_User_info_data(name, img)

        val call = apiService.postMarker(uid, key, info)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("PushDataJoin", "Data successfully pushed to server")
                } else {
                    Log.e("PushDataJoin", "Failed to push data to server. Error code: ${response.code()}")
                    Log.e("PushDataJoin", "Error body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("Error", "Failed to push data to server. Error message: ${t.message}")
            }
        })
    } catch (e: Exception) {
        Log.e("Error", "Exception occurred: ${e.message}")
        e.printStackTrace()
    }
}
