package coder.seventy.two.my_messager.adapters

import coder.seventy.two.my_messager.R
import coder.seventy.two.my_messager.modals.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.new_message_user_row.view.*

class UserAdapter(val user: User) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.new_message_user_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvusername.text = user.name
        Picasso.get().load(user.image).into(viewHolder.itemView.userProfileImage)
    }
}