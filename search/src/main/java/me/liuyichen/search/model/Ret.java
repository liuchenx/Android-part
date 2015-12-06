package me.liuyichen.search.model;

import java.util.List;

/**
 * Created by liuchen on 15/12/4.
 * and ...
 */
public class Ret {


    private D d;

    public void setD(D d) {
        this.d = d;
    }

    public D getD() {
        return d;
    }

    public static class D {

        private List<Item> results;

        public void setResults(List<Item> results) {
            this.results = results;
        }

        public List<Item> getResults() {
            return results;
        }

    }
}
