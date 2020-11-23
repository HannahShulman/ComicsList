package com.hanna.zava.comicslist.di.components;

import com.hanna.zava.comicslist.MainActivity;
import com.hanna.zava.comicslist.di.scope.FragmentScoped;
import com.hanna.zava.comicslist.ui.ComicDetailsDialog;
import com.hanna.zava.comicslist.ui.ComicsListFragment;

import dagger.Component;

@FragmentScoped
@Component(dependencies = NetComponent.class)
public interface ApplicationComponent {

    void inject(MainActivity activity);
    void inject(ComicsListFragment fragment);
    void inject(ComicDetailsDialog fragment);
}
