package me.liuyichen.search.rx;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

/**
 * Created by liuchen on 15/12/5.
 * and ...
 */
public class MaterialSearchViewItemClickOnSubscribe<T extends Adapter> implements Observable.OnSubscribe<T> {

    final private MaterialSearchView searchView;

    final List<T> list;
    public MaterialSearchViewItemClickOnSubscribe(MaterialSearchView searchView, List<T> list) {
        this.searchView = searchView;
        this.list = list;
    }

    @Override
    public void call(final Subscriber<? super T> subscriber) {
        checkUiThread();
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(list.get(position));
                }
            }
        };
        searchView.setOnItemClickListener(listener);

        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                searchView.setOnItemClickListener(null);
            }
        });
    }

}
