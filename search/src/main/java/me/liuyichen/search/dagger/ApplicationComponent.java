package me.liuyichen.search.dagger;

import javax.inject.Singleton;

import dagger.Component;
import me.liuyichen.search.MainActivity;

/**
 * Created by liuchen on 15/12/5.
 * and ...
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MainActivity activity);
}
