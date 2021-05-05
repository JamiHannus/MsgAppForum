package fi.oamk.msgappforum

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var messages: ArrayList<Message>
    private lateinit var edMessage: EditText
    private lateinit var rcMessageList: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var mAdapter: MyAdapter
    private var currentUser: FirebaseUser? = null
    private lateinit var key : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // init the late init on create
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        edMessage = findViewById(R.id.messageText)
        rcMessageList = findViewById(R.id.recyclerView)
        messages = arrayListOf()


        rcMessageList.layoutManager = LinearLayoutManager(this)
        mAdapter = MyAdapter(messages)
        rcMessageList.adapter = mAdapter
        edMessage.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                Log.v("msg", "msg send start")
                addMessage()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        database.child("messages").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.value != null) {
                        messages.clear()
                        for (data in dataSnapshot.children) {
                            Log.v("message load", data.toString())
                            val message = data.getValue<Message>()
                            if (message != null) {
                                messages.add(message)
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged()
                    rcMessageList.smoothScrollToPosition(mAdapter.itemCount-1)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("reading failed", "Failed to read value.", error.toException())
                }
            })
    }

    private fun addMessage() {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        // val newMessage = edMessage.text.toString()
        val newMessage = Message(
            (edMessage.text.toString()),
            currentUser?.email.toString(),
            formatter.format(LocalDateTime.now())
        )
        messages.add(newMessage)
        database.child("messages").setValue(messages)
            .addOnSuccessListener { Log.v("msg", "msg ok") }
            .addOnFailureListener { Log.v("msg", "msg fail") }
        edMessage.setText("")
        closeKeyBoard()


    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            this.showSettings()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        currentUser = auth.currentUser
        if (currentUser == null) loginDialog()
    }

    private fun loginDialog() {
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle("Login")
            setMessage("test@email.com & 123456")
            val linearLayout = LinearLayout(this@MainActivity)
            linearLayout.orientation = LinearLayout.VERTICAL
            val inputEmail = EditText(this@MainActivity)
            inputEmail.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            inputEmail.hint = "Email"
            linearLayout.addView(inputEmail)
            val inputPw = EditText(this@MainActivity)
            inputPw.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_PASSWORD
            inputPw.hint = "Password"
            linearLayout.addView(inputPw)
            builder.setView(linearLayout)

            builder.setPositiveButton("Ok") { _, _ ->
                login(inputEmail.text.toString(), inputPw.text.toString())
            }.show()

        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("auth", "singInEmail Ok")
                currentUser = auth.currentUser
                Toast.makeText(baseContext, "Logged in :)", Toast.LENGTH_SHORT).show()
            } else {
                Log.w("auth", "signInwithEmail failure", task.exception)
                Toast.makeText(baseContext, "Auth failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSettings() {
        val intent = Intent(this, Settings::class.java).apply {
            putExtra("currentUser", currentUser)
        }
        startActivity(intent)
    }
}