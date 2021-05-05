package fi.oamk.msgappforum

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Settings: AppCompatActivity() {

    private  lateinit var  tvEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        tvEmail = findViewById(R.id.email)

        supportActionBar?.apply {
            title= "Settings"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        val currentUser = intent.getParcelableExtra<FirebaseUser>("currentUser")
        tvEmail.text = currentUser?.email
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        tvEmail.text = ""
    }

}