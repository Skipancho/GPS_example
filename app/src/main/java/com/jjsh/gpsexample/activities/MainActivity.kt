package com.jjsh.gpsexample.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jjsh.gpsexample.App
import com.jjsh.gpsexample.R
import com.jjsh.gpsexample.adapters.SubwayStationAdapter
import com.jjsh.gpsexample.databinding.ActivityMainBinding
import com.jjsh.gpsexample.datas.GeoData
import com.jjsh.gpsexample.datas.SubwayStation
import com.jjsh.gpsexample.utils.Prefs
import com.jjsh.gpsexample.viewmodels.MainViewModel
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import splitties.activities.start

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java].apply {
            this.context = this@MainActivity
        }
    }

    private val subwayStationAdapter = SubwayStationAdapter().apply {
        onClick = {
            start<MapActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!checkLocationServicesStatus())
            showDialogForLocationServiceSetting()
        else
            checkRunTimePermission()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        App.progressOn.observe(this){
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.resultBtn.visibility = View.GONE
            }else {
                binding.progressBar.visibility = View.GONE
                binding.resultBtn.visibility = View.VISIBLE
            }
        }

        initTestData()
        initView()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView(){
        subwayStationAdapter.updateList(App.stationData)
        binding.stationRecyclerview.adapter = subwayStationAdapter
        binding.stationRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.resultBtn.setOnClickListener {
            viewModel.testOnClick()
            subwayStationAdapter.updateList(App.stationData)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initTestData() = GlobalScope.launch{
        App.progressOn.postValue(true)
        val assetManager = resources.assets
        val inputStream = assetManager.open(FILE_NAME)
        /*val dataList : List<List<String>> = inputStream.bufferedReader().use { br ->
            br.readLines().map { it.split(",") }.toList()
        }
        val geocoder = Geocoder(this@MainActivity)
        dataList.forEach { it ->
            val name = it[2]
            val line = it[1]
            val address = it[3]
            geocoder.getFromLocationName(address,5)[0].let { adrs ->
                App.stationData.add(SubwayStation(name,line,address,adrs.latitude,adrs.longitude))
            }
            //App.stationData.add(SubwayStation(name, line, address))
        }*/
        val jsonArray = JSONArray(inputStream.reader().readText())
        val size = jsonArray.length()
        val geoMap = Prefs.geoMap
        val geocoder = Geocoder(this@MainActivity)
        for( i in 0 until size){
            val obj : JSONObject = jsonArray.getJSONObject(i)
            val (name, line, address) = arrayOf(
                obj.getString("name"),obj.getString("line"),obj.getString("address")
            )
            if (geoMap.containsKey(address)){
                val geo = geoMap[address]!!
                App.stationData.add(SubwayStation(name,line,address,geo.latitude,geo.longitude))
            }else{
                geocoder.getFromLocationName(address,5)[0].let { adrs ->
                    geoMap[address] = GeoData(address,adrs.latitude,adrs.longitude)
                    App.stationData.add(SubwayStation(name,line,address,adrs.latitude,adrs.longitude))
                }
            }
        }
        Prefs.geoMap = geoMap
        App.progressOn.postValue(false)
    }

    /**
     * GPS on/off 체크
     */
    private fun checkLocationServicesStatus() : Boolean{
        val locationManager : LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    /**
     * GPS on/off 설정 요청
     */
    private fun showDialogForLocationServiceSetting(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("GPS 비활성화")
            .setMessage("앱 사용을 위해 GPS가 필요합니다.\n GPS를 활성화 하시겠습니까?")
            .setCancelable(true)
            .setPositiveButton("확인") { _, _ ->
                val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
            }
            .setNegativeButton("취소"){ d, _ ->
                d.cancel()
            }
            .create().show()
    }

    /**
     * 퍼미션 체크
     */
    private fun checkRunTimePermission(){
        for (permission in requiredPermission){
            val hasPermission = ContextCompat.checkSelfPermission(this, permission)
            if (hasPermission != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,requiredPermission[0])){
                    Toast.makeText(this,"위치 접근 권한 필요",Toast.LENGTH_LONG).show()
                    ActivityCompat.requestPermissions(this,requiredPermission,
                        PERMISSIONS_REQUEST_CODE)
                }else{
                    ActivityCompat.requestPermissions(this,requiredPermission,
                        PERMISSIONS_REQUEST_CODE)
                }
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            GPS_ENABLE_REQUEST_CODE -> {
                if (checkLocationServicesStatus()){
                    Log.d("MainActivity","onActivityResult : GPS 활성화 확인")
                    checkRunTimePermission()
                    return
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.size == requiredPermission.size){
            var checkResult = true
            for (result in grantResults){
                if (result != PackageManager.PERMISSION_GRANTED){
                    checkResult = false
                    break
                }
            }
            if (!checkResult){
                Toast.makeText(this,"사용 권한이 거부되었습니다. 사용 권한을 허용해주세요.",Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }


    companion object{
        const val GPS_ENABLE_REQUEST_CODE = 2001
        const val PERMISSIONS_REQUEST_CODE = 100
        const val FILE_NAME = "data.json"
        //const val FILE_NAME = "subway_daegu.csv"
        val requiredPermission = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}