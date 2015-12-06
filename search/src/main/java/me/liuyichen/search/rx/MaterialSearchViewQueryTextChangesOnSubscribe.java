package me.liuyichen.search.rx;

import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by liuchen on 15/12/4.
 * and ...
 */
public class MaterialSearchViewQueryTextChangesOnSubscribe implements Observable.OnSubscribe<CharSequence> {

    private final MaterialSearchView view;

    public MaterialSearchViewQueryTextChangesOnSubscribe(MaterialSearchView view) {
        this.view = view;
    }

    @Override
    public void call(final Subscriber<? super CharSequence> subscriber) {

        MaterialSearchView.OnQueryTextListener watcher = new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(newText);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
        };

        view.setOnQueryTextListener(watcher);

        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                view.setOnQueryTextListener(null);
            }
        });
    }
}
