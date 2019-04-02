package coder.seventy.two.my_messager

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import coder.seventy.two.my_messager.libby.H
import coder.seventy.two.my_messager.modals.User
import coder.seventy.two.my_messager.adapters.UserAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_chat_message.*

class NewChatMessage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat_message)

        supportActionBar?.title = "New MyMessage"

        fetchAllUser()
    }

    private fun fetchAllUser() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {

                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    val user = it.getValue(User::class.java)
                    if (user != null && user.name != H.user?.name) {
                        adapter.add(UserAdapter(user!!))
                    }
                }

                adapter.setOnItemClickListener { item, view ->
                    val toUser = item as UserAdapter
                    val intent = Intent(this@NewChatMessage, ChatLogActivity::class.java)
                    intent.putExtra("touser", toUser.user)
                    startActivity(intent)
                }

                new_message_recycler.adapter = adapter
            }
        })
    }
}
