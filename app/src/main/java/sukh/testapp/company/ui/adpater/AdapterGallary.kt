package sukh.testapp.company.ui.adpater

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_item.view.*
import sukh.testapp.company.R
import sukh.testapp.company.util.loadImageFromServer
import sukh.testapp.company.util.viewFile


class AdapterGallary(
    private val context: Activity
) : ListAdapter<String, AdapterGallary.ViewHolder>(GallaryDIffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // load image from network incase first laoding and secod time from cache
        holder.updateWithUrl(getItem(position))

        holder.mImg.setOnClickListener {
            context.viewFile(getItem(position))
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         val mImg = view.img_
        fun updateWithUrl(url: String?) {
            mImg.loadImageFromServer(mUrl = url)
        }
    }
}

class GallaryDIffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }


}



































