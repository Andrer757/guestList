package pt.simdea.guestlist.ui.widgets

import android.view.View
import androidx.databinding.BindingAdapter
import pt.simdea.guestlist.utils.helper.extensions.showCondition

object DataBindingAdapter {

    @JvmStatic
    @BindingAdapter("bind:show")
    fun show(view: View, show: Boolean) {
        view.showCondition(show)
    }

}