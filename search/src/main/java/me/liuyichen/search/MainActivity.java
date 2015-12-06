package me.liuyichen.search;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.liuyichen.search.adapter.RetAdapter;
import me.liuyichen.search.dagger.ApplicationModule;
import me.liuyichen.search.dagger.DaggerApplicationComponent;
import me.liuyichen.search.model.Item;
import me.liuyichen.search.model.Ret;
import me.liuyichen.search.network.Bing;
import me.liuyichen.search.rx.RxMaterialSearchView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    @Inject
    Bing bing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaggerApplicationComponent.builder().applicationModule(new ApplicationModule()).build().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

//        final ArrayAdapter<Item> adapter = new ArrayAdapter<>(
//                                MainActivity.this,
//                                android.R.layout.simple_expandable_list_item_1);
//        searchView.setAdapter(adapter);
        adapter = new RetAdapter(MainActivity.this);
        searchView.setAdapter(adapter);

        RxMaterialSearchView
                .queryTextChanges(searchView)
                .subscribeOn(AndroidSchedulers.mainThread())
                .debounce(200, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        return charSequence.length() > 0;
                    }
                })

                .switchMap(new Func1<CharSequence, Observable<Ret>>() {
                    @Override
                    public Observable<Ret> call(CharSequence charSequence) {
                        String s = "'" + charSequence + "'";
                        return bing.search(s, "json");
                    }
                })
                .map(new Func1<Ret, List<Item>>() {

                    @Override
                    public List<Item> call(Ret ret) {
                        return ret.getD().getResults();
                    }
                })
                .filter(new Func1<List<Item>, Boolean>() {
                    @Override
                    public Boolean call(List<Item> items) {
                        return items.size() > 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<Item>, Observable<Item>>() {
                    @Override
                    public Observable<Item> call(List<Item> items) {

                        adapter.clear();
                        adapter.addAll(items);
                        searchView.showSuggestions();
                        return RxMaterialSearchView.itemClicks(searchView, items);
                    }
                })
                .subscribe(new Action1<Item>() {

                    @Override
                    public void call(Item item) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(item.getBingUrl());
                        intent.setData(content_url);
                        startActivity(intent);
                    }
                });

//                .subscribe(new Subscriber<List<Item>>() {
//                    @Override
//                    public void onCompleted() {
//                        android.util.Log.e("111", "item: " + "onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        android.util.Log.e("111", "item onError: " + e);
//                    }
//
//                    @Override
//                    public void onNext(List<Item> items) {
//                        android.util.Log.e("111", "item: " + items.get(0).getTitle());
//
//                        adapter.clear();
//                        adapter.addAll(items);
//                        searchView.showSuggestions();
//                    }
//                });

//        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
//
//        adapter.addAll();
    }


    RetAdapter adapter;
    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

}
