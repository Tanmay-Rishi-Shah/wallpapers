import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    private var noInternetDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkChangeReceiver = NetworkChangeReceiver { isConnected ->
            if (isConnected) {
                noInternetDialog?.dismiss()
            } else {
                showNoInternetDialog()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(networkChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        if (!NetworkUtil.isInternetAvailable(this)) {
            showNoInternetDialog()
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkChangeReceiver)
    }

    private fun showNoInternetDialog() {
        if (noInternetDialog?.isShowing == true) {
            return
        }

        noInternetDialog = AlertDialog.Builder(this)
            .setTitle("No Internet Connection")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("Retry") { dialog, which ->
                // Open WiFi settings
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(intent)
            }
            .setCancelable(false)
            .create()

        noInternetDialog?.show()
    }
}
