package com.example.premissions

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.premissions.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Установите обработчик нажатия на кнопку
        binding.permissionRequestFrame.setOnClickListener {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        val cameraPermission = Manifest.permission.CAMERA
        val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

        val permissionsToRequest = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, cameraPermission) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(cameraPermission)
        }

        if (ContextCompat.checkSelfPermission(this, locationPermission) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(locationPermission)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), CAMERA_REQUEST_CODE)
        } else {
            // Разрешения уже предоставлены
            binding.permissionRequestFrame.visibility = View.GONE
            binding.permissionGranted.visibility = View.VISIBLE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != CAMERA_REQUEST_CODE) {
            Log.e("PermissionActivity", "Пришел результат не с тем requestCode, который ожидался")
            return
        }

        val cameraPermissionIndex = permissions.indexOf(Manifest.permission.CAMERA)

        val permissionProvided = grantResults[cameraPermissionIndex]
        if (permissionProvided == PackageManager.PERMISSION_GRANTED) {
            // Пользователь дал разрешение, можно продолжать работу
            binding.permissionRequestFrame.visibility = View.GONE
            binding.permissionGranted.visibility = View.VISIBLE
        } else {
            // Пользователь отказал в предоставлении разрешения
            binding.permissionRequestFrame.visibility = View.VISIBLE
            binding.permissionGranted.visibility = View.GONE

            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                // Показываем диалог
                Toast.makeText(this, "Разрешение на использование камеры необходимо для доступа к функциям приложения", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 1
    }
}
