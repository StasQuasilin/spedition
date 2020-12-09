package ua.svasilina.spedition.utils;

import android.view.View;

public interface CustomAdapterBuilder<T> {
    void build(T item, View view, int position);
}
