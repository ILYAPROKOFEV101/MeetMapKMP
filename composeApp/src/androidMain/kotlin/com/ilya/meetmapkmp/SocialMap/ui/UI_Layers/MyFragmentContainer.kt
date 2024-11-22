package com.ilya.MeetingMap.SocialMap.ui.UI_Layers

import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import com.example.yourapp.ui.Chat_with_Friends_fragment
import com.example.yourapp.ui.Find_friends_fragment


// aункция для вызова , новых
@Composable
fun MyFragmentContainer() {
    AndroidView(
        factory = { context ->
            FragmentContainerView(context).apply {
                id = View.generateViewId()
            }
        },
        update = { view ->
            val fragmentManager =
                (view.context as FragmentActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val findFriendsFragment = Find_friends_fragment()
            fragmentTransaction.replace(view.id, findFriendsFragment)
            fragmentTransaction.commit()
        }
    )
}

@Composable
fun Chat_fragment() {
    AndroidView(
        factory = { context ->
            FragmentContainerView(context).apply {
                id = View.generateViewId()
            }
        },
        update = { view ->
            val fragmentManager =
                (view.context as FragmentActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val findFriendsFragment = Chat_with_Friends_fragment()
            fragmentTransaction.replace(view.id, findFriendsFragment)
            fragmentTransaction.commit()
        }
    )
}

