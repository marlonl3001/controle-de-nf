package br.com.mdr.controledenf.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(
    private val spaceSize: Int = 32,
    private val spanCount: Int = 1,
    private val orientation: Int = GridLayoutManager.HORIZONTAL
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        with(outRect) {
            if (orientation == GridLayoutManager.VERTICAL) {
                if (parent.getChildAdapterPosition(view) < spanCount) {
                    top = spaceSize
                }
                if (parent.getChildAdapterPosition(view) % spanCount == 0) {
                    left = spaceSize
                }
                bottom = spaceSize
            } else {
                if (parent.getChildAdapterPosition(view) < spanCount) {
                    left = spaceSize
                }
                if (parent.getChildAdapterPosition(view) % spanCount != 0) {
                    bottom = spaceSize
                }
            }

            right = spaceSize
        }
    }
}
