package fi.oamk.msgappforum

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MyAdapter(private val messageHistory: ArrayList<Message>):
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val message: TextView = itemView.findViewById(R.id.message)
        val author: TextView= itemView.findViewById(R.id.author)
        //val parent: LinearLayout = itemView.findViewById(R.id.parent)
       // val myDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.rounded_corner_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_row,parent,false)
        return MyViewHolder(myView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // was experimenting with making the current user one color and other msg other color
//       val user = FirebaseAuth.getInstance().currentUser.email
//        Log.d("email", "$user")
//            val currentColor =
//                if (user == messageHistory[position].author.toString()) {
//                    Color.argb(255, 255, 0, 0)
//                } else {
//                    Color.argb(0, 0, 0, 0,)
//                }
//        holder.myDrawable?.setTint(currentColor)
        holder.message.text = messageHistory[position].message
        val msgAuth = messageHistory[position].author.toString()
        val msgTime = messageHistory[position].time
        ("By: $msgAuth on :$msgTime").also { holder.author.text = it }

    }

    override fun getItemCount()= messageHistory.size

}