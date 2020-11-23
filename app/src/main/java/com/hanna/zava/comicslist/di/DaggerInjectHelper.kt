package com.hanna.zava.comicslist.di

import com.hanna.zava.comicslist.MyApp
import com.hanna.zava.comicslist.di.components.DaggerApplicationComponent
import com.hanna.zava.comicslist.ui.ComicDetailsDialog
import com.hanna.zava.comicslist.ui.ComicsListFragment

object DaggerInjectHelper {

    fun inject(fragment: ComicsListFragment) {
        DaggerApplicationComponent.builder()
            .netComponent((fragment.context?.applicationContext as MyApp).netComponent)
            .build()
            .inject(fragment)
    }

    fun inject(comicDetailsDialog: ComicDetailsDialog) {
        DaggerApplicationComponent.builder()
            .netComponent((comicDetailsDialog.context?.applicationContext as MyApp).netComponent)
            .build()
            .inject(comicDetailsDialog)
    }
}