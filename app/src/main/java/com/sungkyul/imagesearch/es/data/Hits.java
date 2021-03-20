package com.sungkyul.imagesearch.es.data;

import java.util.List;

public class Hits<T> {
    private SearchTotal total;
    private float max_score;
    private List<SearchHit<T>> hits;

    public Hits(){}

    public SearchTotal getTotal() {
        return total;
    }

    public void setTotal(SearchTotal total) {
        this.total = total;
    }

    public float getMax_score() {
        return max_score;
    }

    public void setMax_score(float max_score) {
        this.max_score = max_score;
    }

    public List<SearchHit<T>> getHits() {
        return hits;
    }

    public void setHits(List<SearchHit<T>> hits) {
        this.hits = hits;
    }

    @Override
    public String toString() {
        return "Hits{" +
                "total=" + total +
                ", max_score=" + max_score +
                ", hits=" + hits +
                '}';
    }
}

class SearchTotal{
    private int value;
    private String _relation;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String get_relation() {
        return _relation;
    }

    public void set_relation(String _relation) {
        this._relation = _relation;
    }

    @Override
    public String toString() {
        return "SearchTotal{" +
                "value=" + value +
                ", _relation='" + _relation + '\'' +
                '}';
    }
}