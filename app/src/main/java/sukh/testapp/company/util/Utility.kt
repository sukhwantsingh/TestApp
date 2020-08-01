package sukh.testapp.company.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import sukh.testapp.company.R


val firestoreInstnace = Firebase.firestore
val firestoreAuth = Firebase.auth
val storageReference: StorageReference? = FirebaseStorage.getInstance().reference


const val FIRESTORE_COLLECTION_ = "gallary_master_table"


fun Activity.viewFile(path: String) {

    val mimeType: String? = MimeTypeMap.getSingleton()
        .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path))

    mimeType?.let {
        val uripath = Uri.parse(path)

        val intent: Intent? = Intent(Intent.ACTION_VIEW).apply {
            putExtra("path", path)
            putExtra("mimeType", mimeType)
            type = mimeType
            setDataAndType(uripath, mimeType)
        }

        try {
            startActivity(Intent.createChooser(intent, "Open File"))
        } catch (e: ActivityNotFoundException) {
            // Instruct the user to install a PDF reader here, or something
            e.printStackTrace()
        }
    }

    // startActivity(intent)

}


fun ImageView.loadImageFromServer(mUrl: String? = "") {

    Glide.with(context).asBitmap().load(mUrl).placeholder(R.color.gray_d).skipMemoryCache(true)
        .into(this)

}

fun Activity.centerToast(toast_string: String) {
    val toast: Toast = Toast.makeText(this, toast_string, Toast.LENGTH_SHORT)
  //  toast.setGravity(Gravity.CENTER, 0, 0)
   // val view: View = toast.view
  //  view.setBackgroundColor(resources.getColor(R.color.hint_text_clr))
   // val text = view.findViewById(android.R.id.message) as TextView

    toast.show()
}

inline fun <reified T : Activity> Activity.navigate() {
    val intent = Intent(this, T::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)

}
