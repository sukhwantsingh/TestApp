package sukh.testapp.company.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login_screen.*
import sukh.testapp.company.R
import sukh.testapp.company.util.centerToast
import sukh.testapp.company.util.firestoreAuth
import sukh.testapp.company.util.navigate
import sukh.testapp.company.util.session.SessionAppGallary

class LoginScreen : AppCompatActivity() {

    lateinit var mSession: SessionAppGallary

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        initialisations()
        onClicks()
    }

    private fun initialisations() {
        mSession = SessionAppGallary(applicationContext)

        if(!mSession.isFirstTime){
            txt_heading.text = "Welcome again. Please sign in!"
        }


    }

    private fun onClicks() {
        login_btn.setOnClickListener {
            if (mSession.isFirstTime)  // for first time
            {
                registerNewUser()

            } else {
                signInAuth()
            }


        }
    }


    fun registerNewUser() {
        if (email_et.text.toString().isBlank()) {
            email_et.error = "Provide value!"
            email_et.requestFocus()
            return
        }
        if (pass_et.text.toString().isBlank()) {
            pass_et.error = "Provide value!"
            pass_et.requestFocus()
            return
        }

        firestoreAuth.createUserWithEmailAndPassword(
            email_et.text.toString(),
            pass_et.text.toString()
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                mSession.isFirstTime = false
                // Sign in success, update UI with the signed-in user's information
                navigate<MainActivity>()
            } else {
                // If sign in fails, display a message to the user.
                centerToast("Authentication failed")
            }


        }
    }


    private fun signInAuth() {
        if (email_et.text.toString().isBlank()) {
            email_et.error = "Provide value!"
            email_et.requestFocus()
            return
        }
        if (pass_et.text.toString().isBlank()) {
            pass_et.error = "Provide value!"
            pass_et.requestFocus()
            return
        }

        firestoreAuth.signInWithEmailAndPassword(
            email_et.text.toString(),
            pass_et.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    navigate<MainActivity>()
                } else {
                    // If sign in fails, display a message to the user.
                    centerToast("You are not authorised")
                }

            }
    }


}
