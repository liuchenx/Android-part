package me.liuyichen.search.rx;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import rx.Observable;

/**
 * Created by liuchen on 15/12/4.
 * and ...
 */
public final class RxMaterialSearchView {



    public static Observable<CharSequence> queryTextChanges(MaterialSearchView view) {
        return Observable.create(new MaterialSearchViewQueryTextChangesOnSubscribe(view));
    }

    public static <T extends Object> Observable<T> itemClicks(MaterialSearchView view, List<T> list) {
        return Observable.create(new MaterialSearchViewItemClickOnSubscribe(view, list));
    }
}
