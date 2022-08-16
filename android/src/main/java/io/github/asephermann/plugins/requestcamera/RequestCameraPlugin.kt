package io.github.asephermann.plugins.requestcamera

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin

@CapacitorPlugin(name = "RequestCamera")
class RequestCameraPlugin : Plugin() {
    private val PERMISSION_REQUEST_CODE = 200

    private lateinit var mCall: PluginCall

    @PluginMethod
    fun checkAndRequestPermissions(call: PluginCall) {

        mCall = call

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val camera: Int = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
            )
            val write: Int =
                ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            val read: Int =
                ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            val listPermissionsNeeded: MutableList<String> = ArrayList()
            if (write != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (camera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA)
            }
            if (read != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (listPermissionsNeeded.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    activity,
                    listPermissionsNeeded.toTypedArray(),
                    PERMISSION_REQUEST_CODE
                )
                call.resolve()
            }
            call.resolve()
        } else {
            // code for lollipop and pre-lollipop devices
        }
    }

    override fun handleRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>?,
        grantResults: IntArray?
    ) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                val perms: MutableMap<String, Int> = HashMap()
                // Initialize the map with both permissions
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] =
                    PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                // Fill with actual results from user
                if (grantResults != null) {
                    if (grantResults.isNotEmpty()) {
                        var i = 0
                        if (permissions != null) {
                            while (i < permissions.size) {
                                perms[permissions[i]] = grantResults[i]
                                i++
                            }
                        }
                        // Check for both permissions
                        if (perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED) {
                            Log.d(
                                "in fragment on request",
                                "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted"
                            )
                            // process the normal flow
                            //else any one or both the permissions are not granted
                        } else {
                            Log.d(
                                "in fragment on request",
                                "Some permissions are not granted ask again "
                            )
                            //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                            //shouldShowRequestPermissionRationale will return true
                            //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    activity,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                                    activity, Manifest.permission.CAMERA
                                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                                    activity, Manifest.permission.READ_EXTERNAL_STORAGE
                                )
                            ) {
                                showDialogOK(
                                    "Camera and Storage Permission required for this app"
                                ) { _, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions(mCall)
                                        DialogInterface.BUTTON_NEGATIVE -> {}
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Go to settings and enable permissions",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                //proceed with logic by disabling the related features or quit the app.
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(activity)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", okListener)
            .create()
            .show()
    }
}