package moch.marcin.globetrotter

import android.view.View
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
        text = "%d km".format(item)
    }
}

// https://developer.android.com/topic/libraries/data-binding/index.html
@BindingAdapter("goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}
