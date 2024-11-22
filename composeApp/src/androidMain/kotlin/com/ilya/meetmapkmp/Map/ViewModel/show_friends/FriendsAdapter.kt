import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilya.meetmapkmp.Map.DataModel.Friends_type
import com.ilya.meetmapkmp.R


class FriendsAdapter(
    private val friendsList: List<Friends_type>
) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendImage: ImageView = itemView.findViewById(R.id.friendImage)
        val friendName: TextView = itemView.findViewById(R.id.friendName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friendsList[position]
        holder.friendName.text = friend.name
        Glide.with(holder.itemView.context)
            .load(friend.img)
            .placeholder(R.drawable.ic_launcher_background) // Замените на свой плейсхолдер
            .into(holder.friendImage)
    }

    override fun getItemCount(): Int {
        return friendsList.size
    }
}
