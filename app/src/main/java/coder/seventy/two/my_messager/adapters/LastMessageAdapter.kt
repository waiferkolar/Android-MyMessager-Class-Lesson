package coder.seventy.two.my_messager.adapters

import coder.seventy.two.my_messager.R
import coder.seventy.two.my_messager.modals.MyMessage
import coder.seventy.two.my_messager.modals.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.last_message_row.view.*

class LastMessageAdapter(val myMessage: MyMessage) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.last_message_row
    }

    var friendUser: User? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvUserMsg.text = myMessage.text
        val frienId: String

        if (myMessage.fromId == FirebaseAuth.getInstance().uid) {
            frienId = myMessage.toId
        } else {
            frienId = myMessage.fromId
        }
        val ref = FirebaseDatabase.getInstance().getReference("/users/$frienId")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                friendUser = p0.getValue(User::class.java)
                viewHolder.itemView.tvUsername.text = friendUser?.name
                Picasso.get().load(friendUser?.image).into(viewHolder.itemView.last_message_image)

            }

        })

    }
}