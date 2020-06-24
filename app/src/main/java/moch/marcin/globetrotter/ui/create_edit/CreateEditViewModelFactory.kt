package moch.marcin.globetrotter.ui.create_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CreateEditViewModelFactory(
    private val placeId: String?
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateEditViewModel::class.java)) {
            return CreateEditViewModel(placeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
