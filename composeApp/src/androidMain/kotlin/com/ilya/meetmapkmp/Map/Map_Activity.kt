package com.ilya.meetmapkmp.Mine_menu


import com.ilya.meetmapkmp.Map.Server_API.POST.Became_Participant_fun
import MapMarker

import MarkerAdapter
import MarkerData
import SpaceItemDecoration
import WebSocketManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bitmapDescriptorFromVector
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.Gson
import com.ilya.MeetingMap.Map.Server_API.GET.getPublicMarker
import com.ilya.MeetingMap.Map.ViewModel.show_friends.show_friends_one
import com.ilya.meetmapkmp.Map.DataModel.Friends_type
import com.ilya.meetmapkmp.Map.Interfaces.WebSocketCallback

import com.ilya.meetmapkmp.Map.Server_API.WebSocketClient.WebSocketClient

import com.ilya.meetmapkmp.Map.shake_logik.ShakeDetector





import com.ilya.codewithfriends.presentation.profile.ID
import com.ilya.codewithfriends.presentation.profile.IMG
import com.ilya.codewithfriends.presentation.profile.UID
import com.ilya.codewithfriends.presentation.sign_in.GoogleAuthUiClient
import com.ilya.meetmapkmp.Map.DB.convertMapMarkerToMarkerData
import com.ilya.meetmapkmp.Map.DB.convertMarkerDataListToMarkerList
import com.ilya.meetmapkmp.Map.DB.convertMarkerListToMarkerDataList
import com.ilya.meetmapkmp.Map.Interfaces.MarkerManager

import com.ilya.meetmapkmp.Map.Server_API.GET.getAddressFromCoordinates
import com.ilya.meetmapkmp.Map.Server_API.POST.postInvite
import com.ilya.meetmapkmp.Map.ViewModel.MapViewModel
import com.ilya.meetmapkmp.Map.ViewModel.PersonalizedMarkersViewModel
import com.ilya.meetmapkmp.Map.ViewModel.PersonalizedMarkersViewModelFactory
import com.ilya.meetmapkmp.R
import com.ilya.meetmapkmp.SocialMap.SocialMapActivity
import com.ilya.reaction.logik.PreferenceHelper.getUserKey
import decodePoly
import generateUID
import getMapRoute
import getParticipant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import markerDataToMapMarker
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import nl.dionsegijn.konfetti.core.models.Size
import nl.dionsegijn.konfetti.xml.KonfettiView
import okhttp3.OkHttpClient
import post_user_info
import sendGetRequest
import show_friends_fourth
import show_friends_more
import show_friends_third
import show_friends_two
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit




@OptIn(ExperimentalPermissionsApi::class)
class Map_Activity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnMapClickListener,
     WebSocketCallback, MarkerManager {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var polylineOptions: PolylineOptions
    private var speedTextView: TextView? = null
    private var distanceTextView: TextView? = null
    private var totalDistance: Double = 0.0
    private var lastLocation: Location? = null
    private val markers: MutableList<Marker> = mutableListOf()
    private var speedUnit = "KM/H"
    private val updateSpeedHandler = Handler()
    private var destinationMarker: Marker? = null
    private lateinit var polyline: Polyline
    private lateinit var webSocketClient: WebSocketClient
    private var currentDialog: AlertDialog? = null
    private val collectedFriends = mutableListOf<Friends_type>()
    var currentLatLngGlobal by mutableStateOf<LatLng>(LatLng(0.0, 0.0))
    var routePoints by mutableStateOf<LatLng>(LatLng(0.0, 0.0))
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    private val client = OkHttpClient()
    private lateinit var shakeDetector: ShakeDetector
    private var isItemDecorationAdded = false // Флаг
    private val webSocketManager = WebSocketManager(client, this)
    val markerDataMap: MutableMap<Marker, MarkerData> = ConcurrentHashMap()

    private companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 1
    }

    // Список для хранения данных маркеров (MarkerData)
    // Создайте map для хранения связи между маркерами карты и данными
 //   private val markerDataMap: MutableMap<Marker, MarkerData> = mutableMapOf()





    var uid_main = ""

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)


       var PersonalizedMarkersViewModel = ViewModelProvider(
            this,
            PersonalizedMarkersViewModelFactory(applicationContext)
        ).get(PersonalizedMarkersViewModel::class.java)


        val url = "wss://meetmap.up.railway.app/map/${ID(userData = googleAuthUiClient.getSignedInUser())}/${getUserKey(this)}"
        webSocketClient = WebSocketClient(url)

        val name = UID(userData = googleAuthUiClient.getSignedInUser())
        val img = IMG(userData = googleAuthUiClient.getSignedInUser())
        val uid = ID(userData = googleAuthUiClient.getSignedInUser())

        // Запуск корутины в соответствующем месте
        CoroutineScope(Dispatchers.IO).launch {
            if(getUserKey(this@Map_Activity) == "")
            {
                sendGetRequest("$uid", client, this@Map_Activity)
            }
            getUserKey(this@Map_Activity)?.let { post_user_info(it, uid.toString(), name.toString(), img.toString()) }
            }

        val bottomSheet: View = findViewById(R.id.bottomSheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        // Устанавливаем начальное состояние
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        // Устанавливаем начальное состояние свернутого листа
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        // Можно установить максимальную высоту или другие параметры
        bottomSheetBehavior.peekHeight = 200 // Высота в свернутом состоянии
        // Добавляем слушатель, чтобы отслеживать изменения состояния
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        val textView: TextView = findViewById(R.id.infoTextView)
                        textView.text = getString(R.string.my_tags)
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        Log.d("URL_GET_MAKER", "${currentLatLngGlobal.latitude} and ${currentLatLngGlobal.longitude}")

        uid_main = uid.toString()

        Log.d("UserKey", getUserKey(this).toString())
        supportActionBar?.hide()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) { initializeMap() } else { ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION) }

        CoroutineScope(Dispatchers.IO).launch {
            lifecycleScope.launch {
                var previousMarkers: List<MarkerData>? = null // Храним предыдущие данные
                // тут я отрисовываю маркеры на карте, сохраненные в базе данных
                val initialMarkers = convertMarkerListToMarkerDataList(PersonalizedMarkersViewModel.getAllMarkers())
                handleReceivedMarkers("$uid", initialMarkers.toMutableList())

                // Первый цикл — запросы к базе данных раз в 1 секунду
                launch {
                    while (true) {
                        try {
                            // Получаем новые данные из базы данных
                            val currentMarkers = convertMarkerListToMarkerDataList(PersonalizedMarkersViewModel.getAllMarkers())

                            // Сравниваем текущие данные с предыдущими
                            if (currentMarkers != previousMarkers) {
                                // Данные изменились, обрабатываем их
                                handleReceivedMarkers("$uid", currentMarkers.toMutableList())

                                withContext(Dispatchers.Main) {
                                    // Обновление UI или выполнение других действий с данными
                                    currentMarkers.forEach { markerData ->
                                        Log.d("MYmarkesrs", "New Marker: $markerData")

                                        // Преобразование MarkerData в MapMarker
                                        val mapMarker = markerDataToMapMarker(markerData)

                                        // Добавление маркера на карту
                                        addMarker(LatLng(mapMarker.lat, mapMarker.lon), markerData)

                                    }
                                }
                            } else {
                                Log.d("MYmarkesrs", "Markers unchanged, skipping handleReceivedMarkers")
                            }
                            // Обновляем предыдущие данные
                            previousMarkers = currentMarkers
                        } catch (e: Exception) {
                            Log.e("MYmarkesrs", "Error processing markers", e)
                        }

                        // Пауза перед следующим запросом к базе данных
                        delay(1000) // 1 секунда
                    }
                }

                // Второй цикл — запросы к серверу раз в 30 секунд
                launch {
                    while (true) {
                        try {
                            // Получаем данные с сервера
                            val server = getParticipant(uid.toString(), getUserKey(this@Map_Activity).toString())

                            // Сохраняем данные в базу данных
                            PersonalizedMarkersViewModel.addMarkers(convertMarkerDataListToMarkerList(server))
                        } catch (e: Exception) {
                            Log.e("MYmarkesrs", "Error fetching data from server", e)
                        }

                        // Пауза перед следующим запросом к серверу
                        delay(30000) // 30 секунд
                    }
                }
            }
        }

    }

        // нужно чтобы отоброжать данные по меткам в нижнем баре
    private suspend fun handleReceivedMarkers(uid: String, markerList: MutableList<MarkerData>) {

        var DBViewModel = ViewModelProvider(
            this,
            PersonalizedMarkersViewModelFactory(applicationContext)
        ).get(PersonalizedMarkersViewModel::class.java)

        withContext(Dispatchers.Main) {

                Log.d("WebSocket", "Markers updated: $markerList")

                val recyclerView: RecyclerView = findViewById(R.id.markerRecyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this@Map_Activity)

                // Добавляем ItemDecoration только один раз
                if (!isItemDecorationAdded) {
                    val space = resources.getDimensionPixelSize(R.dimen.space_between_items)
                    recyclerView.addItemDecoration(SpaceItemDecoration(space))
                    isItemDecorationAdded = true
                }


            // Устанавливаем адаптер с нужными параметрами
            recyclerView.adapter = MarkerAdapter(
                markerList,
                this@Map_Activity, // Передаем Map_Activity для обработки кликов
                this@Map_Activity,  // Передаем MapMarkerManager для управления маркерами
                uid,
                DBViewModel
            )

            }
    }

        // Вызов алерт диалог , для тогочтобы показать друга
    // Реализация метода интерфейса WebSocketCallback
    override fun onMessageReceived(dataList: List<WebSocketManager.ReceivedData>) {
            runOnUiThread {
                if (dataList.isEmpty()) return@runOnUiThread

                // Преобразуем список ReceivedData в список Friends_type
                val newFriends = dataList.map { data ->
                    Friends_type(
                        name = data.user_name,
                        img = data.img,
                        key = data.key
                    )
                }

                Log.d("DATAIMHAVE","$newFriends")

                // Добавляем новых друзей в список собранных данных
                collectedFriends.addAll(newFriends)

                // Ждем 1-2 секунды перед обработкой собранных данных
                Handler(Looper.getMainLooper()).postDelayed({
                    // Проверяем, сколько друзей собрано, и показываем соответствующий диалог
                    when (collectedFriends.size) {
                            1 -> {
                            currentDialog?.dismiss()  // Закрываем текущий диалог, если он есть

                                    show_friends_one(uid_main, getUserKey(this).toString(), this, newFriends)

                        }

                        2 -> {
                            currentDialog?.dismiss()  // Закрываем текущий диалог, если он есть

                                show_friends_two(uid_main, getUserKey(this).toString(),this, newFriends)

                        }
                        3 -> {
                            currentDialog?.dismiss()  // Закрываем текущий диалог, если он есть

                                show_friends_third(uid_main, getUserKey(this).toString(),this, newFriends)


                        }
                        4 -> {
                            currentDialog?.dismiss()  // Закрываем текущий диалог, если он есть

                                show_friends_fourth(uid_main, getUserKey(this).toString(),this, newFriends)


                        }
                        5 -> {
                               show_friends_more(uid_main, getUserKey(this).toString(),this, newFriends)

                        }

                    }

                    // Очищаем список после отображения
                    collectedFriends.clear()
                   // webSocketManager.shutdown()

                    // Показать конфетти
                    val konfettiView = findViewById<KonfettiView>(R.id.konfettiView_map)
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

                    // Показываем Toast с информацией о первой записи
                    val firstData = collectedFriends.firstOrNull()
                    firstData?.let {
                        Toast.makeText(this, "Получены данные: ${it.name}, ${it.img}, ${it.key}", Toast.LENGTH_LONG).show()
                    }
                }, 200) // Ждем 1 секунду перед обработкой
            }
        }


    fun onFindLocation(lat: Double, lon: Double) {
        findLocation_mark(lat, lon) // Вызов функции перемещения камеры
        routePoints = LatLng(lat, lon)
    }

    private var currentPolyline: Polyline? = null

    fun findLocation_route() {
        CoroutineScope(Dispatchers.Main).launch {
            val routeGeometry = getMapRoute(currentLatLngGlobal.latitude, currentLatLngGlobal.longitude, routePoints.latitude, routePoints.longitude)
            routeGeometry?.let {
                val routePoints = decodePoly(it)

                // Удаляем предыдущий маршрут, если он существует
                currentPolyline?.remove()

                // Добавляем новый маршрут
                currentPolyline = addRouteToMap(routePoints, 5)
            }
            Log.d("MapRoute", "Route geometry: $routeGeometry")
        }
    }

    private fun initializeMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        polylineOptions = PolylineOptions()
    }


    fun onStandardButtonClick(view: View) {
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

    // Метод для обработки нажатия на кнопку "Satellite"
    fun onSatelliteButtonClick(view: View) {
        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMapReady(googleMap: GoogleMap) {
        var DBViewModel = ViewModelProvider(
        this,
        PersonalizedMarkersViewModelFactory(applicationContext)
    ).get(PersonalizedMarkersViewModel::class.java)


        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.isTrafficEnabled = false
        mMap.isBuildingsEnabled = true
        mMap.isIndoorEnabled = true

        val styleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
        mMap.setMapStyle(styleOptions)

        val locationAutoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.locationAutoCompleteTextView)
        val findButton = findViewById<ImageView>(R.id.findButton)
        val routeButton = findViewById<ImageView>(R.id.routeButton)
        val socialbutton = findViewById<ImageView>(R.id.social)


        socialbutton.setOnClickListener{
            val intent = Intent(this, SocialMapActivity::class.java)
            startActivity(intent)
        }

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line)
        locationAutoCompleteTextView.setAdapter(adapter)

        locationAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position).toString()
            findLocation(selectedItem)
        }

        findButton.setOnClickListener {
            val locationText = locationAutoCompleteTextView.text.toString()
            if (locationText.isNotEmpty()) {
                findLocation(locationText)
            } else {
                Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show()
            }
        }

        val mapViewModel: MapViewModel by viewModels {
            ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        }


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        val currentLatLng = LatLng(it.latitude, it.longitude)
                        currentLatLngGlobal = currentLatLng
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                        mapViewModel.fetchMarkers(uid_main, LatLng(it.latitude, it.longitude))
                        lifecycleScope.launch {
                                try {
                                    mapViewModel.markers.collect { markers ->
                                        markers.forEach { mapMarker ->
                                            val markerLatLng = LatLng(mapMarker.lat, mapMarker.lon)
                                             addMarker(markerLatLng, convertMapMarkerToMarkerData(mapMarker))

                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e("MarkerData", "Error fetching markers", e)
                                }
                        }

                        shakeDetector = ShakeDetector(this, object : ShakeDetector.OnShakeListener {
                            private val shakeInterval: Long = 10 * 1000 // 10 секунд
                            private var lastShakeTime: Long = 0

                            override fun onShake() {
                                val currentTime = System.currentTimeMillis()
                                if (currentTime - lastShakeTime >= shakeInterval) {
                                    lastShakeTime = currentTime
                                    val key = getUserKey(this@Map_Activity)
                                    val lat = currentLatLng.latitude
                                    val lon = currentLatLng.longitude
                                    val url = "wss://meetmap.up.railway.app/shake/$key/$lat/$lon"
                                    webSocketManager.setupWebSocket(url)
                                    Toast.makeText(this@Map_Activity, "Телефон трясут! Подключение...", Toast.LENGTH_SHORT).show()
                                } else {
                                    Log.d("ShakeDetector", "Слишком рано для нового вызова.")
                                }
                            }
                        })

                        updateSpeed(it.speed)
                        updateDistance(it)


                        // В вашей активности или фрагменте, где находится GoogleMap
                        mMap.setOnMarkerClickListener { marker ->
                            // Получаем данные маркера из map, используя сам объект marker
                            val mapMarker = markerDataMap[marker]

                            mapMarker?.let {
                                // Показываем информацию о маркере в диалоговом окне
                                showMarkerDialog(it)
                                Log.d("MarkerData_new2", it.toString())
                            }

                            true  // Возвращаем true, чтобы предотвратить стандартное поведение (открытие InfoWindow)
                        }


                        // Установка обработчика кликов по карте
                        mMap.setOnMapClickListener { latLng ->
                            showAddMarkerDialog(latLng, this, uid_main, this, DBViewModel)
                        }

                        // Инициализация объекта Polyline
                        polyline = mMap.addPolyline(PolylineOptions().width(5f).color(android.graphics.Color.BLUE))

                        var isRouteDrawn = false
                        routeButton.setOnClickListener {
                            if (isRouteDrawn) {
                                currentPolyline?.remove()
                                removeMarkers()
                                isRouteDrawn = false
                            } else {
                                findLocation_route()
                                isRouteDrawn = true
                            }
                        }
                    }
                }
            }, 200) // Задержка в 0.2 секунду перед выполнением кода
        }
    }
    override fun removeSpecificMarker(markerData: MarkerData) {
        Handler(Looper.getMainLooper()).post {
            Log.d("RemoveMarker", "Попытка удалить маркер с id=${markerData.id}")
            Log.d(
                "RemoveMarker",
                "Состояние markerDataMap до удаления: ${markerDataMap.entries.map { "key=${it.key.title}, id=${it.value.id}" }}"
            )

            val markerToRemove = markerDataMap.entries.find { it.value.id == markerData.id }

            markerToRemove?.let { entry ->
                // Удаление маркера с карты
                entry.key.remove()

                // Удаление маркера из коллекции
                markerDataMap.remove(entry.key)

                Log.d("RemoveMarker", "Метка с id=${markerData.id} успешно удалена")
            } ?: run {
                Log.e(
                    "RemoveMarker",
                    "Маркер с id=${markerData.id} не найден. Текущее состояние: ${markerDataMap.entries.map { "key=${it.key.title}, id=${it.value.id}" }}"
                )
            }
        }
    }


    private val markerList = mutableListOf<Marker>()  // Список для сохранения маркеров

    private fun addRouteToMap(routePoints: List<LatLng>, circleSpacing: Int): Polyline {
        // Удаляем предыдущую полилинию, если она существует
        currentPolyline?.remove()

        // Удаляем все маркеры с карты
        removeMarkers()

        // Создаем полилинию для маршрута
        val polylineOptions = PolylineOptions()
            .addAll(routePoints)
            .width(12f)
            .color(Color.parseColor("#4285F4"))  // Основной цвет линии
            .geodesic(true)               // Сглаживание углов
            .startCap(RoundCap())         // Закругление начала линии
            .endCap(RoundCap())           // Закругление конца линии
            .jointType(JointType.ROUND)   // Закругление соединений между линиями

        // Добавляем полилинию на карту и сохраняем её в currentPolyline
        currentPolyline = mMap.addPolyline(polylineOptions)

        // Получаем уменьшенный Bitmap для кружков
        val customCircleBitmap = getResizedBitmap(R.drawable.custom_circle, 20, 20)  // Уменьшаем до 20x20 пикселей

        // Добавляем кастомные кружки с регулируемым расстоянием
        for (i in routePoints.indices step circleSpacing) {
            val point = routePoints[i]
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(point)
                    .icon(customCircleBitmap)  // Используем уменьшенное изображение кружка
                    .anchor(0.5f, 0.5f)  // Центр маркера совпадает с точкой маршрута
            )
            // Добавляем маркер в список для дальнейшего удаления
            markerList.add(marker!!)
        }

        // Возвращаем новую полилинию
        return currentPolyline!!
    }

    // Удаление всех маркеров с карты
    private fun removeMarkers() {
        for (marker in markerList) {
            marker.remove()
        }
        // Очищаем список маркеров
        markerList.clear()
    }

    // Функция для изменения размера Bitmap
    private fun getResizedBitmap(drawableId: Int, width: Int, height: Int): BitmapDescriptor {
        val drawable = ContextCompat.getDrawable(this, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        // Создаем уменьшенный bitmap
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap)
    }



    private fun showMarkerDialog(marker: MarkerData) {
        var DBViewModel = ViewModelProvider(
            this,
            PersonalizedMarkersViewModelFactory(applicationContext)
        ).get(PersonalizedMarkersViewModel::class.java)


        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_view_marker, null)
        val marker_image  = dialogView.findViewById<ImageView>(R.id.marker_image)
        val marker_name = dialogView.findViewById<TextView>(R.id.marker_name)
        val marker_about_marker = dialogView.findViewById<TextView>(R.id.marker_about_marker)
        val marker_street = dialogView.findViewById<TextView>(R.id.marker_street)
        val marker_start_Date = dialogView.findViewById<TextView>(R.id.marker_start_Date)
        val marker_end_Date = dialogView.findViewById<TextView>(R.id.marker_end_Date)
        val marker_button_not = dialogView.findViewById<Button>(R.id.marker_button_not)
        val marker_button_ready = dialogView.findViewById<Button>(R.id.marker_button_ready)

        val key = getUserKey(this@Map_Activity)

        // Установка данных маркера в элементы диалога
        marker_name.text = marker.name
        marker_about_marker.text = marker.whatHappens
        marker_street.text = ""
        marker_start_Date.text = "${marker.startDate} Time:${marker.startTime}"
        marker_end_Date.text = "${marker.endDate} Time:${marker.endTime}"

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        // Создание и показ диалога
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_background)

        // Обработка нажатия кнопки "Нет"
        marker_button_not.setOnClickListener {
            dialog.dismiss()
                removeSpecificMarker(marker)
        }

        // Обработка нажатия кнопки "Готово"
        marker_button_ready.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                DBViewModel.addMarkers(convertMarkerDataListToMarkerList(listOf(marker)))
                Became_Participant_fun(uid_main, key.toString(), marker.id)
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMapClick(latLng: LatLng) {
        var DBViewModel = ViewModelProvider(
            this,
            PersonalizedMarkersViewModelFactory(applicationContext)
        ).get(PersonalizedMarkersViewModel::class.java)

        showAddMarkerDialog(latLng, this, uid_main, this, DBViewModel)
    }



    private fun addMarker(latLng: LatLng, markerData: MarkerData): Marker? {
        if (!::mMap.isInitialized) {
            Log.e("MapError", "mMap не инициализирован")
            return null
        }

        // Проверяем, есть ли метка с таким же id
        if (markerDataMap.values.any { it.id == markerData.id }) {
            Log.w("AddMarker", "Метка с id=${markerData.id} уже существует. Добавление отменено.")
            return null
        }

        // Добавляем новую метку на карту
        val marker = mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(markerData.name)
                .icon(
                    bitmapDescriptorFromVector(
                        this@Map_Activity,
                        R.drawable.location_on_,
                        "FF005B",
                        140,
                        140
                    )
                )
        )

        // Сохраняем маркер, если он успешно добавлен
        marker?.let {
            markerDataMap[it] = markerData
            Log.d("AddMarker", "Метка добавлена: id=${markerData.id}, name=${markerData.name}")
        }

        return marker
    }




    // Добавьте метод для поиска местоположения по адресу
    private fun findLocation(address: String) {
        val geocoder = Geocoder(this)
        try {
            val results = geocoder.getFromLocationName(address, 1)
            if (results != null && results.isNotEmpty()) {
                val location = results[0]
                val latLng = LatLng(location.latitude, location.longitude)

                // Добавление метки на карту
                mMap.addMarker(MarkerOptions().position(latLng).title(address))

                // Перемещение камеры к метке
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

                routePoints = LatLng(location.latitude, location.longitude)
            } else {
                // Обработка случая, когда результаты геокодирования пусты
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun findLocation_mark(lat: Double, lon: Double) {
        // Создание объекта LatLng с переданными координатами
        val latLng = LatLng(lat, lon)

        // Проверка, что карта доступна
        mMap?.let { map ->
            // Добавление метки на карту
            // Перемещение камеры к метке
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        } ?: run {
            // Обработка случая, когда карта не доступна
            Toast.makeText(this, "Map is not available", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initializeMap()
                } else {
                    // Handle the case where the user denies location permission
                }
            }
        }
    }

    private fun addPointToPolyline(latLng: LatLng) {
        polylineOptions.add(latLng)
        mMap.addPolyline(polylineOptions)
    }

    private fun clearPolyline() {
        polylineOptions.points.clear()
        mMap.clear()
    }

    override fun onPolylineClick(polyline: Polyline) {
        // Handle the click event on the polyline if needed
    }

    private fun updateSpeed(speed: Float) {
        speedUnit = "km/h" // Установка единиц измерения в километры в час
        // Отображение скорости
        speedTextView?.text = String.format("Speed: %.2f $speedUnit", speed)
    }

    private fun updateDistance(location: Location) {
        if (lastLocation != null) {
            val distance = location.distanceTo(lastLocation!!)
            totalDistance += distance.toDouble()
            distanceTextView?.text = String.format("Distance: %.2f meters", totalDistance)
        }
        lastLocation = location
    }


    override fun onResume() {
        super.onResume()
        if (!::mMap.isInitialized) {
            initializeMap() // Инициализировать карту, если она еще не инициализирована
        }
        webSocketClient.start()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showAddMarkerDialog(
        latLng: LatLng,
        context: Context,
        uid_main: String,
        activity: FragmentActivity, // добавлен параметр
        DB_ViewModel: PersonalizedMarkersViewModel
    ) {

        // val addmarker = Map_Activity()

        // Раздуйте макет диалога
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_marker, null)
        var access = false // Переменная для хранения состояния Switch
        // Найдите элементы внутри макета диалога
        val selectDateButton_start = dialogView.findViewById<Button>(R.id.selectDateButtonstart)
        val selectDateButton_end = dialogView.findViewById<Button>(R.id.selectDateButtonend)
        val selectTimeButtonstart = dialogView.findViewById<Button>(R.id.selectTimeButton_start)
        val selectTimeButtonend = dialogView.findViewById<Button>(R.id.selectTimeButton_end)
        val editName = dialogView.findViewById<EditText>(R.id.editname)
        val editobout = dialogView.findViewById<EditText>(R.id.editobout)
        val seekBar = dialogView.findViewById<SeekBar>(R.id.seekBar)
        val textView = dialogView.findViewById<TextView>(R.id.textView)
        val publicSwitch = dialogView.findViewById<Switch>(R.id.switch2) // Найдите ваш Switch
        var selectedDate_start: String? = null
        var selectedDate_end: String? = null
        var selectedTime: Pair<Int, Int>? = null
        var startTime: String? = null
        var endTime: String? = null
        publicSwitch.text = if (!access) "Публичная метка" else "Приватная метка"
        publicSwitch.setOnCheckedChangeListener { _, isChecked ->
            access = isChecked
            publicSwitch.text = if (!access) "Публичная метка" else "Приватная метка"
        }

        selectDateButton_start.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .build()

            datePicker.show(activity.supportFragmentManager, "DATE_PICKER")

            datePicker.addOnPositiveButtonClickListener {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                selectedDate_start = dateFormat.format(Date(it))
                selectDateButton_start.text = selectedDate_start
            }
        }

        selectDateButton_end.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .build()

            datePicker.show(activity.supportFragmentManager, "DATE_PICKER")

            datePicker.addOnPositiveButtonClickListener {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                selectedDate_end = dateFormat.format(Date(it))
                selectDateButton_end.text = selectedDate_end
            }
        }

        selectTimeButtonstart.setOnClickListener {
            val is24HourFormat = true
            val timeFormat = if (is24HourFormat) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(timeFormat)
                .setTitleText("Выберите время")
                .build()

            timePicker.show(activity.supportFragmentManager, "TIME_PICKER")

            timePicker.addOnPositiveButtonClickListener {
                val hour = timePicker.hour
                val minute = timePicker.minute
                val formattedTime = String.format("%02d:%02d", hour, minute)
                selectedTime = Pair(hour, minute)
                selectTimeButtonstart.text = formattedTime
                startTime = formattedTime // Сохраняем выбранное время в переменной
            }
        }

        selectTimeButtonend.setOnClickListener {
            val is24HourFormat = true
            val timeFormat = if (is24HourFormat) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(timeFormat)
                .setTitleText("Выберите время")
                .build()

            timePicker.show(activity.supportFragmentManager, "TIME_PICKER")

            timePicker.addOnPositiveButtonClickListener {
                val hour = timePicker.hour
                val minute = timePicker.minute
                val formattedTime = String.format("%02d:%02d", hour, minute)
                selectedTime = Pair(hour, minute)
                selectTimeButtonend.text = formattedTime
                endTime = formattedTime // Сохраняем выбранное время в переменной
            }
        }

        // Создаем диалоговое окно с инфлейтированным макетом
        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)
            .setPositiveButton("Да") { dialog, _ ->
                // Получаем текст из EditText
                val editText = dialogView.findViewById<EditText>(R.id.editname)
                val markerDescription = editobout.text.toString() // Получаем текст из поля editAbout
                val markerTitle = editText.text.toString()

                if (markerTitle.isNotEmpty()) {
                    val participants = seekBar.progress + 1

                    // Запуск корутины для получения улицы
                    CoroutineScope(Dispatchers.IO).launch {
                        val street = getAddressFromCoordinates(latLng.latitude, latLng.longitude) ?: "Unknown Street"

                        // Теперь создаем MarkerData после получения улицы
                        val markerData = MarkerData(
                            key = getUserKey(context).toString(),
                            username = "Ilya",
                            imguser = "Photo",
                            photomark = "photo",
                            street = street,
                            id = generateUID(),
                            lat = latLng.latitude,
                            lon = latLng.longitude,
                            name = markerTitle,
                            whatHappens = markerDescription,
                            startDate = selectedDate_start?.let { LocalDate.parse(it) }.toString(),
                            endDate = selectedDate_end?.let { LocalDate.parse(it) }.toString(),
                            startTime = startTime.toString(),
                            endTime = endTime.toString(),
                            participants = participants,
                            access = access
                        )

                        // Запуск на главном потоке для обновления UI
                        withContext(Dispatchers.Main) {

                            addMarker(latLng, markerData)

                            val gson = Gson()
                            val markerDataJson = gson.toJson(markerData)
                            Log.d("PushDataJoin", "MarkerData JSON: $markerDataJson")

                            // Запуск корутины для отправки данных на сервер и
                            CoroutineScope(Dispatchers.IO).launch {
                                postInvite(getUserKey(context).toString(), uid_main, markerData)
                                DB_ViewModel.addMarkers(convertMarkerDataListToMarkerList(listOf(markerData)))
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "Название метки не может быть пустым", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_background)
        dialog.show()
    }




    override fun onDestroy() {
        super.onDestroy()
        webSocketManager.closeWebSocket()
        webSocketClient.close()
        shakeDetector.stop()  // Останавливаем детектор, когда activity уничтожается
    }

    private fun generateUniqueRequestId(): String {
        // Генерация уникального идентификатора для запроса
        return System.currentTimeMillis().toString()
    }



}

