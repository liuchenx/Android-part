package me.liuyichen.search.network;

import me.liuyichen.search.model.Ret;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by liuchen on 15/12/4.
 * and ...
 */
public interface Bing {

    String BASE_URL = "https://api.datamarket.azure.com/";

    @GET("Bing/Search/v1/RelatedSearch")
    Observable<Ret> search(@Query(value = "Query", encoded = true) String query
            , @Query("$format") String json);

}
