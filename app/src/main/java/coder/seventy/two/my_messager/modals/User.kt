package coder.seventy.two.my_messager.modals

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val uid: String, val name: String, val image: String) : Parcelable{
    constructor() : this("", "", "")
}