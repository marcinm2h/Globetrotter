package moch.marcin.globetrotter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import moch.marcin.globetrotter.service.Place
import moch.marcin.globetrotter.ui.home.PlaceGridAdapter


@BindingAdapter("placesListData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Place>?) {
    val adapter = recyclerView.adapter as PlaceGridAdapter
    adapter.submitList(data)
}

@BindingAdapter("radius")
fun TextView.setRadius(item: Int?) {
    item?.let {
        text = "%d m".format(item)
    }
}

// https://developer.android.com/topic/libraries/data-binding/index.html
@BindingAdapter("goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

const val FALLBACK_IMAGE_RESOURCE = R.drawable.ic_camera_alt_black_24dp // FIXME: second arg
@BindingAdapter("imageBase64")
fun imageBase64(imageView: ImageView, imageBase64: String?) {
    if (imageBase64 == null) {
        imageView.setImageResource(FALLBACK_IMAGE_RESOURCE)
        return
    }
    imageBase64?.let {
        val decodedString = Base64.decode(it, Base64.DEFAULT)
        imageView.setImageBitmap(
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        )
    }
}
