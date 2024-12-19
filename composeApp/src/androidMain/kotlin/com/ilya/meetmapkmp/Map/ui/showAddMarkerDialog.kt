import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.Gson
import com.ilya.meetmapkmp.Map.Server_API.GET.getAddressFromCoordinates
import com.ilya.meetmapkmp.Map.Server_API.POST.postInvite
import com.ilya.meetmapkmp.R
import com.ilya.reaction.logik.PreferenceHelper.getUserKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale




