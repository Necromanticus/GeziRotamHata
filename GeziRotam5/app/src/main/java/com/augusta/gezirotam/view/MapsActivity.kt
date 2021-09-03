package com.augusta.gezirotam.view

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.augusta.gezirotam.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.*

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import com.google.android.gms.maps.model.PolylineOptions

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


import com.google.maps.android.PolyUtil


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

import com.augusta.gezirotam.Model.DirectionResponses
import com.augusta.gezirotam.R
import java.lang.Exception


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener : LocationListener
    private var currentMarker : Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        auth = Firebase.auth

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true
        mMap.setOnMapClickListener(selectingPlace)

     //"https://maps.googleapis.com/maps/api/directions/json?origin=10.3181466,123.9029382&destination=10.311795,123.915864&key=<AIzaSyDxsPg7-OCYmLXd4nL5usNJLFOFRgXP7ZE>"
        var osuruk = null
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object  : LocationListener {
            open override fun onLocationChanged(location: Location) {


                if (location != null){
                    //mMap.clear()

                    val newUserLocation = LatLng(location.latitude,location.longitude)
                    val guncelLat = newUserLocation.latitude.toString()
                    val guncelLong = newUserLocation.longitude.toString()



                    val markerOption = MarkerOptions().position(LatLng(newUserLocation.latitude,newUserLocation.longitude))
                        .title("Güncel Konum").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .snippet(auth.currentUser?.email)

                    currentMarker?.remove()
                    currentMarker=mMap.addMarker(markerOption)
                    currentMarker?.tag=703

                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newUserLocation,15f))

                    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newUserLocation,10f))

                }
            }


        }

        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2,2f,locationListener)

            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation!=null){ap
                val lastKnownLatLng = LatLng(lastKnownLocation.latitude,lastKnownLocation.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLatLng,15f))
            }
        }

    }
    val selectingPlace = object : GoogleMap.OnMapClickListener{
         override fun onMapClick(p0: LatLng)  {

            val hedef = p0.latitude.toString() + "," + p0.longitude.toString()
            val baslangic = latitudeFromDatabase.toString() + "," + longitudeFromDatabase.toString()


            val mark = mMap.addMarker(MarkerOptions().position(p0).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .title("Seçilen Konum"))

            val apiServices = RetrofitClient.apiServices(this@MapsActivity)
            apiServices.getDirection(baslangic, hedef, getString(R.string.api_key))
                .enqueue(object : Callback<DirectionResponses> {
                    override fun onResponse(call: Call<DirectionResponses>, response: Response<DirectionResponses>) {
                        println(response)
                        drawPolyline(response)
                        Log.d("AAAAAAAAAAAAAA", response.message())
                    }

                    override fun onFailure(call: Call<DirectionResponses>, t: Throwable) {
                        Log.e("BBBBBBBBBBBB", t.localizedMessage)
                    }
                })

            /*val builder = AlertDialog.Builder(this@MapsActivity)
            builder.setTitle("İsteğe Dayalı Rota Oluşturma")
            builder.setMessage("İhtiyaçlarınıza Uygun Bir Şekilde Rota Oluşturmamızı İster misiniz?")
            builder.setPositiveButton("Evet") {dialog, which->

                /**Burada Sorular sorulacak**/

            }.setNegativeButton("Hayır"){dialog, which ->
                Toast.makeText(this@MapsActivity,"Zort",Toast.LENGTH_SHORT).show()
                /** 2 nokta arası rota oluştur**/


            }*/


        }

    }
    private fun drawPolyline(response: Response<DirectionResponses>) {
        val shape = response.body()?.routes?.get(0)?.overviewPolyline?.points
        val polyline = PolylineOptions()
            .addAll(PolyUtil.decode(shape))
            .width(15f)
            .color(Color.RED)
        mMap.addPolyline(polyline)
    }
    private interface ApiServices {
        @GET("maps/api/directions/json")
        fun getDirection(@Query("origin") origin: String,
                         @Query("destination") destination: String,
                         @Query("key") apiKey: String): Call<DirectionResponses>
    }
    private object RetrofitClient {
        fun apiServices(context: Context): ApiServices {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(context.resources.getString(R.string.base_url))
                .build()

            return retrofit.create<ApiServices>(ApiServices::class.java)
        }
    }






    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            if (grantResults.size > 0){
                if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2,2f,locationListener)

                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}