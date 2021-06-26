package com.ocak.firebaseordu.view

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.ocak.firebaseordu.GoogleMapDTO
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.databinding.ActivityMaps5Binding
import okhttp3.OkHttpClient
import okhttp3.Request

class MapsActivity5 : AppCompatActivity(){

    lateinit var mapFragment : SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var binding: ActivityMaps5Binding

    var userLatitude =""
    var userLongitude =""
    var latitude = ""
    var longitude = ""
    var varisAdi = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaps5Binding.inflate(layoutInflater)
        setContentView(binding.root)




        latitude = intent.getStringExtra("latitude").toString()
        longitude = intent.getStringExtra("longitude").toString()
        varisAdi = intent.getStringExtra("ad").toString()
        userLatitude = intent.getStringExtra("userLatitude").toString()
        userLongitude = intent.getStringExtra("userLongitude").toString()




        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {


            googleMap = it


            val userLatitudeDouble = userLatitude.toDouble()
            val userLongitudeDouble = userLongitude.toDouble()



            val location1 = LatLng(userLatitudeDouble,userLongitudeDouble)
            googleMap.addMarker(MarkerOptions().position(location1).title("Konumunuz")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location1,10f))

            val latitudeDouble = latitude.toDouble()
            val longitudeDouble = longitude.toDouble()

            val location3 = LatLng(latitudeDouble,longitudeDouble)
            googleMap.addMarker(MarkerOptions().position(location3).title(varisAdi)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

            val URL = getDirectionURL(location1,location3)
            GetDirection(URL).execute()

        })
    }

    fun getDirectionURL(origin:LatLng,dest:LatLng) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${dest.latitude},${dest.longitude}&sensor=false&mode=driving"+"&key="+"AIzaSyA8rW8MzzGepdbh_IUzK6tqOOUSGpszeEM"
    }

    private inner class GetDirection(val url : String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body()!!.string()
            val result =  ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data, GoogleMapDTO::class.java)

                val path =  ArrayList<LatLng>()

                for (i in 0..(respObj.routes[0].legs[0].steps.size-1)){
                    val startLatLng = LatLng(respObj.routes[0].legs[0].steps[i].start_location.lat.toDouble()
                        ,respObj.routes[0].legs[0].steps[i].start_location.lng.toDouble())
                    path.add(startLatLng)
                    val endLatLng = LatLng(respObj.routes[0].legs[0].steps[i].end_location.lat.toDouble()
                        ,respObj.routes[0].legs[0].steps[i].end_location.lng.toDouble())
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e:Exception){
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.BLUE)
                lineoption.geodesic(true)
            }
            googleMap.addPolyline(lineoption)
        }
    }

    public fun decodePolyline(encoded: String): List<LatLng> {

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }

        return poly
    }


}