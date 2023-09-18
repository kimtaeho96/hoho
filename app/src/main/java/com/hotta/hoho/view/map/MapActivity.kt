package com.hotta.hoho.view.map

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.hotta.hoho.R
import com.hotta.hoho.databinding.ActivityMapBinding
import com.hotta.hoho.view.adapter.MapAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.lang.Exception


class MapActivity : AppCompatActivity(), PermissionListener, MapView.CurrentLocationEventListener {
    private val viewModel: MapViewModel by viewModels()

    private var x = 0.0
    private var y = 0.0


    private lateinit var binding: ActivityMapBinding
    private var gpsCheck: Boolean = true
    private val eventListener = MarkerEventListener(this)

    private val ACCESS_FINE_LOCATION = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val mapView = binding.mapView
        val sheet = binding.sheet


        mapView.setPOIItemEventListener(eventListener)

        val inputLayout = binding.inputLayout
        val autoCompleteTextView = binding.textItem


        val item: List<String> = listOf("cgv", "메가박스", "롯데시네마")
        val itemAdapter = ArrayAdapter<String>(this, R.layout.drop_down_item, item)
        autoCompleteTextView.setAdapter(itemAdapter)
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, autoCompleteTextView.text.toString(), Toast.LENGTH_SHORT).show()
            if (x != 0.0 || y != 0.0) {
                viewModel.getKakaoMapSearch(autoCompleteTextView.text.toString(), x, y, 5000)
                BottomSheetBehavior.from(sheet).state = BottomSheetBehavior.STATE_EXPANDED
                // 위치 추적 중지
                binding.mapView.currentLocationTrackingMode =
                    MapView.CurrentLocationTrackingMode.TrackingModeOff
                binding.mapView.setCurrentLocationEventListener(null)
            } else {
                Toast.makeText(this, "현재위치를 확인해주세요", Toast.LENGTH_SHORT).show()
                permissionCheck()
            }
        }


        binding.gpsimg.setOnClickListener {
            Log.d("gpscheck1", gpsCheck.toString())
            if (gpsCheck)
                if (checkLocationService()) {
                    // GPS가 켜져있을 경우
                    permissionCheck()
                    binding.gpsimg.setImageResource(R.drawable.map_gps_on)
                    gpsCheck = false
                    Log.d("gpscheck2", gpsCheck.toString())
                } else {
                    Toast.makeText(this, "GPS를 켜주세요", Toast.LENGTH_SHORT).show()
                }
            else {
                stopTracking()
                binding.gpsimg.setImageResource(R.drawable.map_gps_off)
                gpsCheck = true
                Log.d("gpscheck3", gpsCheck.toString())
            }

        }

        /*binding.btnGps.setOnClickListener {
            Log.d("MapActivity2", x.toString())
            Log.d("MapActivity2", y.toString())
            if (x != 0.0 || y != 0.0) {
                viewModel.getKakaoMapSearch("롯데시네마", x, y, 5000)
                BottomSheetBehavior.from(sheet).state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                Toast.makeText(this, "현재위치를 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }*/

        val markerArr = ArrayList<MapPOIItem>()

        viewModel.kakaoMapResult.observe(this, Observer { it ->
            Log.d("MapActivity2", it.toString())
            markerArr.clear()
            for (item in it) {
                val marker = MapPOIItem().apply {
                    itemName = item.place_name  // 마커 이름
                    mapPoint = MapPoint.mapPointWithGeoCoord(item.y.toDouble(), item.x.toDouble())
                    markerType = MapPOIItem.MarkerType.BluePin
                    selectedMarkerType = MapPOIItem.MarkerType.RedPin
                    isCustomImageAutoscale = false
                    setCustomImageAnchor(0.5f, 1.0f)
                }
                markerArr.add(marker)

            }
            mapView.addPOIItems(markerArr.toTypedArray())

            val adapter = MapAdapter(this, it, mapView, markerArr, sheet, x, y)
            binding.mapRv.adapter = adapter
            binding.mapRv.layoutManager = LinearLayoutManager(this)
        })


        // 추적중지 버튼
        /* binding.btnStop.setOnClickListener {
             stopTracking()
             BottomSheetBehavior.from(sheet).apply {
                 //peekHeight = 200
                 // this.state = BottomSheetBehavior.STATE_COLLAPSED
             }

         }*/



        BottomSheetBehavior.from(sheet).apply {
            peekHeight = 200
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }


    }


    // 위치 권한 확인
    private fun permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            TedPermission.create()
                .setPermissionListener(this)
                .setRationaleMessage("위치 정보 제공이 필요한 서비스입니다.")
                .setDeniedMessage("[설정] -> [권한]에서 권한 변경이 가능합니다.")
                .setDeniedCloseButtonText("닫기")
                .setGotoSettingButtonText("설정")
                .setRationaleTitle("HELLO")
                .setPermissions(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                .check()
        }
    }

    // GPS가 켜져있는지 확인
    private fun checkLocationService(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    // 위치추적 시작
    private fun startTracking() {
        binding.mapView.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading

        binding.mapView.setCurrentLocationEventListener(this)
    }

    // 위치추적 중지
    private fun stopTracking() {
        binding.mapView.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOff
    }

    override fun onPermissionGranted() {
        Toast.makeText(this, "위치 정보 제공이 완료되었습니다.", Toast.LENGTH_SHORT).show()
        startTracking()

    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
        Toast.makeText(this, "위치 정보 제공이 거부되었습니다.", Toast.LENGTH_SHORT).show()

    }

    override fun onCurrentLocationUpdate(p0: MapView?, p1: MapPoint?, p2: Float) {
        x = p1?.mapPointGeoCoord?.longitude!!
        y = p1?.mapPointGeoCoord?.latitude!!
        if (x != null && y != null) {
            Log.d("MapActivity1", x.toString())
            Log.d("MapActivity1", y.toString())

        }

    }

    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {
        TODO("Not yet implemented")
    }

    override fun onCurrentLocationUpdateFailed(p0: MapView?) {
        TODO("Not yet implemented")
    }

    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {
        TODO("Not yet implemented")
    }


    class MarkerEventListener(val context: Context) :
        MapView.POIItemEventListener {
        override fun onPOIItemSelected(mapView: MapView?, poiItem: MapPOIItem?) {
            Log.d("MapActivity4", "1")
        }

        override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, poiItem: MapPOIItem?) {
            // 말풍선 클릭 시 (Deprecated)
            // 이 함수도 작동하지만 그냥 아래 있는 함수에 작성하자
        }

        override fun onCalloutBalloonOfPOIItemTouched(
            mapView: MapView?,
            poiItem: MapPOIItem?,
            buttonType: MapPOIItem.CalloutBalloonButtonType?
        ) {
            Log.d("MapActivity4", "2")

            val markerLatitude = poiItem?.mapPoint?.mapPointGeoCoord?.latitude!! // 마커의 위도
            val markerLongitude = poiItem?.mapPoint?.mapPointGeoCoord?.longitude!! // 마커의 경도
            // 현재 위치 가져오기
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val currentLatitude = currentLocation?.latitude ?: 0.0 // 현재 위치의 위도
            val currentLongitude = currentLocation?.longitude ?: 0.0 // 현재 위치의 경도


            Log.d("Marker Clicked", "Marker Latitude: $markerLatitude, Longitude: $markerLongitude")
            Log.d(
                "Marker Clicked",
                "Current Latitude: $currentLatitude, Longitude: $currentLongitude"
            )
            // 말풍선 클릭 시
            val builder = AlertDialog.Builder(context)
            val itemList = arrayOf("길찾기", "닫기")
            builder.setTitle("${poiItem?.itemName}")
            builder.setItems(itemList) { dialog, which ->
                when (which) {
                    0 -> openKakaoMap(
                        markerLatitude,
                        markerLongitude,
                        currentLatitude,
                        currentLongitude
                    )

                    1 -> dialog.dismiss()
                }
            }
            builder.show()
        }

        private fun openKakaoMap(
            markerLatitude: Double,
            markerLongitude: Double,
            currentLatitude: Double,
            currentLongitude: Double
        ) {
            val kakaomapPackage = "net.daum.android.map" // Kakaomap 앱의 패키지 이름

            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("kakaomap://route?sp=${currentLatitude},${currentLongitude}&ep=${markerLatitude},${markerLongitude}&by=FOOT")
                )
                context.startActivity(intent)
            } catch (e: Exception) {
                val playStoreIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$kakaomapPackage")
                )
                context.startActivity(playStoreIntent)
            }

        }

        private fun current() {

        }

        private fun isPackageInstalled(
            packageName: String,
            packageManager: PackageManager
        ): Boolean {
            return try {
                packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }

        override fun onDraggablePOIItemMoved(
            mapView: MapView?,
            poiItem: MapPOIItem?,
            mapPoint: MapPoint?
        ) {
            // 마커의 속성 중 isDraggable = true 일 때 마커를 이동시켰을 경우
        }
    }


}