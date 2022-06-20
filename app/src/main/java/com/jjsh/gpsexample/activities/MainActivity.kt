package com.jjsh.gpsexample.activities

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.jjsh.gpsexample.R
import com.jjsh.gpsexample.databinding.ActivityMainBinding
import com.jjsh.gpsexample.utils.GPSTracker
import com.jjsh.gpsexample.viewmodels.MainViewModel

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!checkLocationServicesStatus())
            showDialogForLocationServiceSetting()
        else
            checkRunTimePermission()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
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
        val requiredPermission = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}