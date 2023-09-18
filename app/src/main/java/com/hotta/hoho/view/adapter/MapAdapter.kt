package com.hotta.hoho.view.adapter

import android.content.Context
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hotta.hoho.R
import com.hotta.hoho.view.map.Document
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class MapAdapter(
    val content: Context,
    val item: List<Document>,
    val mapView: MapView,
    val markerArr: ArrayList<MapPOIItem>,
    val sheet: LinearLayout,
    val x: Double,
    val y: Double
) :
    RecyclerView.Adapter<MapAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun binItems(item: Document) {
            val locationA = Location("pointA")
            locationA.latitude = y.toDouble()
            locationA.longitude = x.toDouble()
            val locationB = Location("pointB")
            locationB.latitude = item.y.toDouble()
            locationB.longitude = item.x.toDouble()

            val distance1 = (locationA.distanceTo(locationB)) / 1000
            val formattedDistance = String.format("%.1f", distance1)
            Log.d("distance", formattedDistance.toString())

            val text = itemView.findViewById<TextView>(R.id.place_name)
            val distance = itemView.findViewById<TextView>(R.id.distance)
            text.text = item.place_name
            distance.text = formattedDistance + " Km "


            text.setOnClickListener {


                val mapPoint = MapPoint.mapPointWithGeoCoord(item.y.toDouble(), item.x.toDouble())

                for (marker in markerArr) {
                    if (marker.itemName == item.place_name) {
                        marker.markerType = MapPOIItem.MarkerType.RedPin
                        mapView.selectPOIItem(marker, true)
                        mapView.setMapCenterPointAndZoomLevel(mapPoint, 2, true)

                        BottomSheetBehavior.from(sheet).state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }

            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.map_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MapAdapter.ViewHolder, position: Int) {
        holder.binItems(item[position])
    }

    override fun getItemCount(): Int {
        return item.size
    }

}