import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ilya.MeetingMap.Map.DataModel.Friends_type
import com.ilya.MeetingMap.Map.Server_API.POST.addFriends

import com.ilya.meetmapkmp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import nl.dionsegijn.konfetti.core.models.Size
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

 fun show_friends_third(uid: String,key: String,context: Context, data: List<Friends_type>) {
    // Inflate the custom layout for the dialog
    val dialogView = LayoutInflater.from(context).inflate(R.layout.friends_list_three, null)
    val konfettiView = dialogView.findViewById<KonfettiView>(R.id.konfettiView)

    // Find views inside the custom layout
    val icon_first = dialogView.findViewById<ImageView>(R.id.icon_first)
    val icon_second = dialogView.findViewById<ImageView>(R.id.icon_second)
    val icon_third = dialogView.findViewById<ImageView>(R.id.icon_third)

    val name_first = dialogView.findViewById<TextView>(R.id.name_first)
    val name_second = dialogView.findViewById<TextView>(R.id.name_second)
    val name_third = dialogView.findViewById<TextView>(R.id.name_three)

    val person_add_first = dialogView.findViewById<Button>(R.id.person_add_first)
    val person_add_second = dialogView.findViewById<Button>(R.id.person_add_second)
    val person_add_third = dialogView.findViewById<Button>(R.id.person_add_three)

    // Set up the button click listeners
    person_add_first.setOnClickListener {
        if (data.isNotEmpty() && uid != null && key != null) {
            CoroutineScope(Dispatchers.IO).launch{
                addFriends(uid, key, data[0].key)
            }
        } else {
            Toast.makeText(context, "Error: No data or invalid data", Toast.LENGTH_SHORT).show()
        }
    }
    person_add_second.setOnClickListener {
        if (data.isNotEmpty() && uid != null && key != null) {
            CoroutineScope(Dispatchers.IO).launch{
                addFriends(uid, key, data[1].key)
            }
        } else {
            Toast.makeText(context, "Error: No data or invalid data", Toast.LENGTH_SHORT).show()
        }
    }
    person_add_third.setOnClickListener {
        if (data.isNotEmpty() && uid != null && key != null) {
            CoroutineScope(Dispatchers.IO).launch{
                addFriends(uid, key, data[2].key)
            }
        } else {
            Toast.makeText(context, "Error: No data or invalid data", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle data population
    when (data.size) {
        0 -> {
            // If no data, hide the dialog or handle as needed
            Toast.makeText(context, "No friends data available.", Toast.LENGTH_SHORT).show()
            return
        }
        1 -> {
            // If only one friend, populate the first set of views and hide the other sets
            val friend = data[0]
            name_first.text = friend.name
            // Use a library like Glide or Picasso to load the image
            Glide.with(context).load(friend.img).into(icon_first)

            person_add_second.visibility = View.GONE
            icon_second.visibility = View.GONE
            name_second.visibility = View.GONE

            person_add_third.visibility = View.GONE
            icon_third.visibility = View.GONE
            name_third.visibility = View.GONE
        }
        2 -> {
            // If two friends, populate two sets of views and hide the third set
            val friend1 = data[0]
            val friend2 = data[1]

            name_first.text = friend1.name
            Glide.with(context).load(friend1.img).into(icon_first)

            name_second.text = friend2.name
            Glide.with(context).load(friend2.img).into(icon_second)

            person_add_third.visibility = View.GONE
            icon_third.visibility = View.GONE
            name_third.visibility = View.GONE
        }
        3 -> {
            // Если три пользователя, заполняем все три набора данных
            val friend1 = data[0]
            val friend2 = data[1]
            val friend3 = data[2]

            name_first.text = friend1.name
            Glide.with(context).load(friend1.img).into(icon_first)

            name_second.text = friend2.name
            Glide.with(context).load(friend2.img).into(icon_second)

            name_third.text = friend3.name
            Glide.with(context).load(friend3.img).into(icon_third)

            person_add_first.visibility = View.VISIBLE
            person_add_second.visibility = View.VISIBLE
            person_add_third.visibility = View.VISIBLE

            icon_first.visibility = View.VISIBLE
            icon_second.visibility = View.VISIBLE
            icon_third.visibility = View.VISIBLE

            name_first.visibility = View.VISIBLE
            name_second.visibility = View.VISIBLE
            name_third.visibility = View.VISIBLE
        }
        else -> {
            // Handle the case where there are more than three friends, if needed
            Toast.makeText(context, "Too many friends to display.", Toast.LENGTH_SHORT).show()
            return
        }
    }

    // Build and show the dialog
    val alertDialog = AlertDialog.Builder(context, R.style.CustomDialog)
        .setView(dialogView)
        .create()

    // Remove default background
    alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    // Set dialog to center of the screen
    alertDialog.window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )

    alertDialog.show()

    // Center the dialog
    val layoutParams = WindowManager.LayoutParams()
    layoutParams.copyFrom(alertDialog.window?.attributes)
    layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
    layoutParams.gravity = Gravity.CENTER
    alertDialog.window?.attributes = layoutParams

    // Set up the konfetti
    val emitterConfig = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)

    konfettiView.start(
        Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(Color.YELLOW, Color.GREEN, Color.MAGENTA),
            position = Position.Relative(0.5, 0.0),
            size = listOf(Size.SMALL, Size.LARGE),
            timeToLive = 3000L,
            shapes = listOf(Shape.Square),
            emitter = emitterConfig
        )
    )
}
