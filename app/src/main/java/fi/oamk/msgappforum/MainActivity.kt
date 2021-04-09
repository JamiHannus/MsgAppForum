package fi.oamk.msgappforum

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var messages: ArrayList<String>
    private lateinit var edMessage: EditText
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Firebase.database.reference
        edMessage = findViewById(R.id.messageText)
        messages = arrayListOf()

        edMessage.setOnKeyListener { v, keyCode, event ->
            if ( keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
                Log.v("msg","msg send start")
                addMessage()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

    }

    private fun addMessage(){
        val newMessage = edMessage.text.toString()
        messages.add(newMessage)
        database.child("messages").setValue(messages)
            .addOnSuccessListener { Log.v("msg","msg ok") }
            .addOnFailureListener { Log.v("msg","msg fail") }
        edMessage.setText("")
    }

}
