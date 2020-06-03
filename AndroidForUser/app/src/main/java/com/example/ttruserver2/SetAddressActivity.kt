package com.example.ttruserver2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_set_address.*
import java.io.IOException
import java.util.*

class SetAddressActivity : AppCompatActivity() {

    var locationManager : LocationManager? = null
    private val REQUEST_CODE_LOCATION : Int = 2
    var address : String = ""
    var latitude : Double? = null
    var longitude : Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_address)

        setLatLng.setOnClickListener {
            getCurrentLoc()
            UserData.setLng(longitude as Double)
            UserData.setLat(latitude as Double)
            UserData.setAddress(address as String)
            finish()
        }
    }
    private fun getCurrentLoc(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        val userLocation: Location = getLatLng()
        if(userLocation != null){
            latitude = userLocation.latitude
            longitude = userLocation.longitude
            Log.d("check current location", "현재 내 위치값 : $latitude, $longitude")
            //Locationtxt.text = "Your Current Coordinates are : \nLat:" + latitude + " ; Long:" + longitude

            val mGeocoder = Geocoder(applicationContext, Locale.KOREAN)
            var mResultList : List<Address>? = null

            try {
                mResultList = mGeocoder.getFromLocation(
                    latitude!!, longitude!!, 1
                )
            }catch (e: IOException){
                e.printStackTrace()
            }
            if(mResultList != null){
                Log.d("check current location", mResultList[0].getAddressLine(0))
                address = mResultList[0].getAddressLine(0)
                address = address.substring(5)
                //Addersstxt.text = "Your Current Address is : \n" + currentLocation
            }
        }
    }
    private fun getLatLng() : Location {
        var currentLatLng: Location? = null
        if(ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), this.REQUEST_CODE_LOCATION)
            getLatLng()
        }else{
            val locationProvider = LocationManager.GPS_PROVIDER
            currentLatLng = locationManager?.getLastKnownLocation(locationProvider)
        }
        return currentLatLng!!
    }
}