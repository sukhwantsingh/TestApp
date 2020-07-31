package sukh.testapp.company.ui.viewmodel

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import sukh.testapp.company.util.FIRESTORE_COLLECTION_
import sukh.testapp.company.util.firestoreInstnace
import sukh.testapp.company.util.storageReference
import java.util.*

class GallaryViewModel : ViewModel() {

    val mResponse = MutableLiveData<String>().apply {
        value = null
    }

    // live response
    val mLiveResponse: LiveData<String> = mResponse

    private fun updateImageUrlToFirestore(mImageUrl: String) {
        viewModelScope.launch {
            // Create a new user with a first and last name
            val user = hashMapOf(
                "imageUrl" to mImageUrl,
                "created at" to Calendar.getInstance().timeInMillis
            )

            // Add a new document with a generated ID
            firestoreInstnace
                .collection("$FIRESTORE_COLLECTION_")
                .add(user)
                .addOnSuccessListener { docref ->
                    Log.wtf("Gallary", "added successfully ${docref.id}")
                    mResponse.value = "Added successfully"

                }
                .addOnFailureListener { e ->
                    Log.wtf("Gallary", "Error in uploading", e)
                    mResponse.value = "Error in uploading: $e"
                }

        }

    }

     fun uploadImage(filePath: Uri?) {
        if (filePath != null) {
            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath)

            val urlTask =
                uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                        mResponse.value = "Something went wrong"
                    }
                    return@Continuation ref.downloadUrl

                })?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mResponse.value = ""
                        val downloadUri = task.result
                        updateImageUrlToFirestore(downloadUri.toString())

                    } else {
                        // Handle failures
                        mResponse.value = "Something went wrong"
                    }
                }?.addOnFailureListener {
                    mResponse.value = "Failure!"
                }
        } else {
            mResponse.value = "Please Upload an Image"
        }
    }


    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}