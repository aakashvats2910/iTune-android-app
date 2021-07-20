package org.kashisol.musik.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.kashisol.musik.R
import org.kashisol.musik.model.ITuneResults
import org.kashisol.musik.util.ImageCache
import java.lang.Exception

class ITuneResultAdapter(var arrayList: ArrayList<ITuneResults>, var context: Context) : RecyclerView.Adapter<ITuneResultAdapter.MyViewHolder>() {

    private var userCachedImage: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = inflater.inflate(R.layout.single_result_layout, null, false) as View
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var iTuneResults = arrayList[position]

        // set the data in the holder
        holder.title_field.setText(iTuneResults.trackName)
        holder.artist_name_field.setText("By " + iTuneResults.artistName)
        holder.track_price_country_field.setText("" + iTuneResults.trackPrice + " " + iTuneResults.currency)

        // since the view is created new, make the image as null
        // and show the progress loader again to show the loading process of image is taking place.
        holder.preview_image_field.setImageBitmap(null)
        holder.image_loader_progress_field.visibility = View.VISIBLE

        // check if the image is cached or not.
        // if the image is cached
        val bmp = ImageCache.getCacheImage(iTuneResults.artworkUrl100, context)
        if (bmp != null && userCachedImage) {
            println("()()()() CACHE_IMG_USED")
            // it means the image exists in the cache
            holder.preview_image_field.setImageBitmap(bmp)
            // if the image is loaded successfully then disable the progress bar
            holder.image_loader_progress_field.visibility = View.GONE
        } else {
            // load the image from the web
            Picasso.get().load(iTuneResults.artworkUrl100)
                .into(holder.preview_image_field, object: Callback {
                    override fun onSuccess() {
                        holder.image_loader_progress_field.visibility = View.GONE
                        var drawable = holder.preview_image_field.drawable as BitmapDrawable
                        var bitmap = drawable.bitmap as Bitmap
                        // since we know the image is not cached, then cache the image
                        ImageCache.saveCacheImage(bitmap, iTuneResults.artworkUrl100, context)
                    }

                    override fun onError(e: Exception?) {

                    }
                })
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class MyViewHolder// initializing the widgets
        (itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title_field: TextView = itemView.findViewById(R.id.title_field)
        var artist_name_field: TextView = itemView.findViewById(R.id.artist_name_field)
        var track_price_country_field: TextView = itemView.findViewById(R.id.track_price_country_field)
        var preview_image_field: ImageView = itemView.findViewById(R.id.preview_image_field)
        var image_loader_progress_field: ProgressBar = itemView.findViewById(R.id.image_loader_progress_field)

    }

}