package coder.seventy.two.my_messager

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View
import coder.seventy.two.my_messager.libby.H
import coder.seventy.two.my_messager.modals.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.register_activity.*
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.toast
import java.io.InputStream
import java.util.*

class RegisterActivity : AppCompatActivity() {
    var uri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        supportActionBar?.title = "Register"

        tv_go_to_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        profileImage.setOnClickListener {
            requestPermit()
        }
        tvUploadImagePointer.setOnClickListener {
            requestPermit()
        }
        registerButton.setOnClickListener {
            val username: String = register_et_username.text.toString()
            val email: String = register_et_email.text.toString()
            val password: String = register_et_password.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                toast("Please fill all required fields!")
                return@setOnClickListener
            }

            if (password.length < 6) {
                toast("Password must be at least 6 characters")
                return@setOnClickListener
            }

            startRegister(email, password)
        }
    }

    private fun startRegister(email: String, password: String) {
        if (uri == null) return

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                H.l("User create Successfully with user id of ${it.result.user.uid}")
                userProfileUploadNow()
            }
            .addOnFailureListener {
                H.l("Fail to create user : ${it.message}")
            }
    }

    private fun userProfileUploadNow() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference(("/images/$filename"))
        ref.putFile(uri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    H.l("Image Download Uri is $it")
                    storeUserDataToDb("$it")
                }
            }
            .addOnFailureListener {
                H.l("Image Upload Fail ${it.message}")
            }
    }

    private fun storeUserDataToDb(profilImageUri: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val username = register_et_username.text.toString()

        val user = User(uid, username, profilImageUri)

        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.setValue(user)
            .addOnSuccessListener {
                val intent = Intent(this@RegisterActivity, LastChatActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                H.l("Fail due to ${it.message}")
            }
    }

    private fun requestPermit() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    toast("Permission deny")
                } else {
                    startImagePick()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun startImagePick() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Title Of Choose"), 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.data

            val inputStr: InputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStr)
            profileImage.imageBitmap = bitmap
            tvUploadImagePointer.visibility = View.GONE
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
