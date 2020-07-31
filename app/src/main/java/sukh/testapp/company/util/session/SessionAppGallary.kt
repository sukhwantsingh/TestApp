package sukh.testapp.company.util.session

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences


class SessionAppGallary(private val _context: Context?) {
    private val editor: SharedPreferences.Editor
    private val pref: SharedPreferences
    private val PRIVATE_MODE = 0
    private val securityQuestionLength = 0

    var isFirstTime: Boolean
        get() = pref.getBoolean("is_firstime", true)
        set(mFlag) {
            editor.putBoolean("is_firstime", mFlag)
            editor.commit()
        }

    fun clearSession() {
        editor.clear()
        editor.commit()
    }

    companion object {
        private const val PREF_NAME = "session_app_gallary"
    }


    init {
        pref = _context?.getSharedPreferences(PREF_NAME, PRIVATE_MODE)!!
        editor = pref.edit()
    }
}