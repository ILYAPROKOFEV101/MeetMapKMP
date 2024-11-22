import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.ilya.MeetingMap.Map.DataModel.Friends_type


fun show_friends_more(uid: String,key: String,context: Context, data: List<Friends_type>) {
    // Преобразуем данные в список строк

    Log.d("show_friends_more", "Received data: $data")
    val names = data.map { it.name }

    // Создаем простой ArrayAdapter
    val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_2, android.R.id.text1, names)

    // Создаем ListView
    val listView = ListView(context)
    listView.adapter = adapter

    // Отображаем диалоговое окно с ListView
    AlertDialog.Builder(context)
        .setView(listView)
        .setTitle("Список друзей")
        .setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        .create()
        .show()
}