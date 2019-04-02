package coder.seventy.two.my_messager.adapters

import coder.seventy.two.my_messager.R
import coder.seventy.two.my_messager.modals.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.message_right_row.view.*

class ChatRightAdapter(val message: String, val user: User) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.message_right_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_right_row_msg.text = message
        Picasso.get().load(user.image).into(viewHolder.itemView.chat_right_row_image)
    }
}