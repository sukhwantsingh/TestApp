package sukh.testapp.company.ui.adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_item.view.*
import sukh.testapp.company.R
import sukh.testapp.company.util.loadImageFromServer


class AdapterGallary(
    private val context: Context,
    private val mFeedList: ArrayList<String>
) : RecyclerView.Adapter<AdapterGallary.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_item, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return mFeedList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // load image from network incase first laoding and secod time from cache
        holder.updateWithUrl(mFeedList[position])

        holder.itemView.setOnClickListener {
            // Toast.makeText(context, "Hahahaha", Toast.LENGTH_LONG).show()
            // mCallback.getDetails("1")
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mImg = view.img_
        fun updateWithUrl(url: String?) {
            mImg.loadImageFromServer(mUrl = url)
        }
    }
}
