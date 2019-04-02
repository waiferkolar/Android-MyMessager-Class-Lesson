package coder.seventy.two.my_messager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import coder.seventy.two.my_messager.adapters.ChatLeftAdapter
import coder.seventy.two.my_messager.adapters.ChatRightAdapter
import coder.seventy.two.my_messager.libby.H
import coder.seventy.two.my_messager.modals.MyMessage
import coder.seventy.two.my_messager.modals.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import org.jetbrains.anko.toast

class ChatLogActivity : AppCompatActivity() {
    var toUser: User? = null
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        supportActionBar?.title = "Chat Log"

        toUser = intent.getParcelableExtra("touser")

        toast(toUser!!.name)

        btnSend.setOnClickListener {
            sendMessage()
        }
        chat_log_recycler.adapter = adapter
        checkNewMessageArrive()
    }

    private fun checkNewMessageArrive() {
        val fromId = H.user?.uid
        val toId = toUser?.uid

        val ref = FirebaseDatabase.getInstance().getReference("/messages/$fromId/$toId")
        ref.addChildEventListener(object : ChildEventListener {


            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val my_message = p0.getValue(MyMessage::class.java)

                if (my_message != null) {
                    if (my_message.fromId == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatLeftAdapter(my_message.text, H.user!!))
                    } else {
                        adapter.add(ChatRightAdapter(my_message.text, toUser!!))
                    }
                }
                chat_log_recycler.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildRemoved(p0: DataSnapshot) {}
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
        })
    }

    private fun sendMessage() {
        val msg = etChatMesage.text.toString()
        val fromId = H.user?.uid
        val toId = toUser?.uid


        val fromRef = FirebaseDatabase.getInstance().getReference("/messages/$fromId/$toId").push()
        val toRef = FirebaseDatabase.getInstance().getReference("/messages/$toId/$fromId").push()

        val message = MyMessage(fromRef.key!!, msg, fromId!!, toId!!, System.currentTimeMillis() / 1000)

        fromRef.setValue(message)
            .addOnSuccessListener {
                toast("Message Already Send")
            }

        toRef.setValue(message)

        val latestMessageFromRef = FirebaseDatabase.getInstance().getReference("/latest-message/$fromId/$toId")
        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-message/$toId/$fromId")

        latestMessageFromRef.setValue(message)
        latestMessageToRef.setValue(message)

        etChatMesage.text = null
    }
}
