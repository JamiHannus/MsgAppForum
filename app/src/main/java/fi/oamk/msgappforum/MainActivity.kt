package fi.oamk.msgappforum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.TtsSpan
import android.util.Log
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")
        myRef.setValue("hello 1!")
        val myRef2 = database.getReference("items")
        myRef2.get().addOnSuccessListener {
            val items =it.value
        }


    }
}