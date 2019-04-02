package coder.seventy.two.my_messager

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import coder.seventy.two.my_messager.libby.H
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.title = "Login"

        tv_to_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val email: String = login_et_email.text.toString()
            val password: String = login_et_password.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                toast("Please fill all required fields!")
                return@setOnClickListener
            }

            if (password.length < 6) {
                toast("Password must be at least 6 characters")
                return@setOnClickListener
            }

            startLogin(email, password)
        }
    }

    private fun startLogin(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                val intent = Intent(this@LoginActivity, LastChatActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                H.l("Fail cause ${it.message}")
            }
    }
}
