import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ilya.meetmapkmp.Map.DB.convertMarkerDataListToMarkerList
import com.ilya.meetmapkmp.Map.DB.convertMarkerDataToMarker
import com.ilya.meetmapkmp.Map.Interfaces.OnMarkerClickListener
import com.ilya.meetmapkmp.Map.Server_API.DELETE.deleteParticipantMarker
import com.ilya.meetmapkmp.Map.ViewModel.PersonalizedMarkersViewModel
import com.ilya.meetmapkmp.Mine_menu.Map_Activity


import com.ilya.meetmapkmp.R

import com.ilya.reaction.logik.PreferenceHelper.getUserKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MarkerAdapter(
    private var markerList: MutableList<MarkerData>, // Изменяемый список
    private val onMarkerClickListener: Map_Activity, // Интерфейс в конструкторе
    private val removemareker: OnMarkerClickListener, // Интерфейс в конструкторе
    private val uid: String,
    private val viewmodel: PersonalizedMarkersViewModel
) : RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder>() {



    private val key = getUserKey(onMarkerClickListener).toString()

    inner class MarkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val markerName: TextView = itemView.findViewById(R.id.markerName)
        val markerDescription: TextView = itemView.findViewById(R.id.markerDescription)
        val stree: TextView = itemView.findViewById(R.id.marker_street)
        val starData: TextView = itemView.findViewById(R.id.marker_start_Date)
        val endData: TextView = itemView.findViewById(R.id.marker_end_Date)
        val find_marker_button: Button = itemView.findViewById(R.id.marker_button_find_tag)
        val delte_marker_button: Button = itemView.findViewById(R.id.marker_button_delete_tag)

        fun bind(marker: MarkerData) {
            // Привязка данных маркера к элементам UI
            markerName.text = marker.name
            markerDescription.text = marker.whatHappens
            stree.text = "" // Это поле не используется в вашем коде, возможно, нужно добавить данные
            starData.text = "${marker.startDate} Time:${marker.startTime}"
            endData.text = "${marker.endDate} Time:${marker.endTime}"

            // Обработка нажатия на кнопку для поиска маркера
            find_marker_button.setOnClickListener {
                onMarkerClickListener.onFindLocation(marker.lat, marker.lon)
            }

            // Обработка нажатия на кнопку для удаления маркера
            delte_marker_button.setOnClickListener {

                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val removedMarker = markerList[position]
                    markerList.removeAt(position)
                    notifyItemRemoved(position)

                    // Обновление данных в базе данных или других источниках
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            deleteParticipantMarker(uid, key, removedMarker.id)
                            viewmodel.deleteMarkerById(removedMarker.id)
                            removemareker.removeSpecificMarker(removedMarker)
                        } catch (e: Exception) {
                            Log.e("RemoveMarker", "Ошибка при удалении маркера из базы данных: ${e.message}")
                        }
                    }
                } else {
                    Log.e("RemoveMarker", "Невозможно удалить маркер: позиция недействительна")
                }

            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_marker, parent, false)
        return MarkerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        val marker = markerList[position]
        holder.bind(marker)
    }

    override fun getItemCount(): Int {
        return markerList.size
    }




    // Метод для обновления списка маркеров
    fun updateMarkers(newMarkers: List<MarkerData>) {
        markerList.clear()
        markerList.addAll(newMarkers)
        notifyDataSetChanged() // Полностью обновляем адаптер
    }
}


    // Класс для добавления отступов между элементами списка в RecyclerView
class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            // Добавляем отступы, кроме последнего элемента
            if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1)) {
                outRect.bottom = space
            }
        }
    }


