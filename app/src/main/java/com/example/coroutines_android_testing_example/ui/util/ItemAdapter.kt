package com.workshop.universityannouncementsboard.util

import android.view.View
import androidx.annotation.LayoutRes

abstract class ItemAdapter(@LayoutRes open val layoutId: Int) {
    abstract fun setupView(containerView: View)
}