package sukh.testapp.company.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sukh.testapp.company.R
import sukh.testapp.company.ui.adpater.AdapterGallary
import sukh.testapp.company.ui.viewmodel.GallaryViewModel
import sukh.testapp.company.util.FIRESTORE_COLLECTION_
import sukh.testapp.company.util.centerToast
import sukh.testapp.company.util.firestoreInstnace
import java.io.IOException


class MainActivity : AppCompatActivity() {

    lateinit var mViewModel: GallaryViewModel

    private var filePath: Uri? = null
    val mListImages = arrayListOf<String>()

    val mAdapterImages: AdapterGallary = AdapterGallary(this, mListImages)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewModel = ViewModelProviders.of(this).get(GallaryViewModel::class.java)
        recycle_galary.adapter = mAdapterImages

        initViews()
        onClicks()

    }

    private fun initViews() {
        // add the sanp listener
        snapListener()

        mViewModel.mLiveResponse.observe(this, Observer {
            it?.let {
                progress_.visibility = View.GONE
                centerToast(it)
            }
        })

    }


    private fun setupGallary() {
        mAdapterImages.notifyDataSetChanged()
    }


    private fun onClicks() {
        btn_upload.setOnClickListener {
            // upload image
            imageDialog()
        }

    }

    private fun imageDialog() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
            .setCropMenuCropButtonTitle("Done")
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this@MainActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == RESULT_OK) {
                //  File path
                filePath = result.uri
                Log.wtf("path-", "" + filePath)
                // upload the path to the plasy store
                try {
                    // uplaod image to
                    progress_.visibility = View.VISIBLE

                    mViewModel.uploadImage(filePath)
                } catch (e: IOException) {
                    e.printStackTrace()
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                centerToast(result.error.toString())
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun snapListener() {
        GlobalScope.launch(Dispatchers.Main) {
            firestoreInstnace
                .collection(FIRESTORE_COLLECTION_)
                .addSnapshotListener { documents, e ->
                    if (e != null) {
                        Log.wtf("TAG", "listen:error", e)
                        return@addSnapshotListener
                    }

                    documents?.documents?.forEach {
                        Log.wtf("ft", "${it.get("imageUrl")}")
                        if (!mListImages.contains("${it.get("imageUrl")}")) {
                            mListImages.add("${it.get("imageUrl")}")
                        }
                    }

                    // update the data once uplaoded
                    setupGallary()

                }
        }


    }


}























