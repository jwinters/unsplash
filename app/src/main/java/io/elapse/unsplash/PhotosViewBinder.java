package io.elapse.unsplash;

import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import io.pivotal.arca.adapters.Binding;
import io.pivotal.arca.adapters.ViewBinder;

public class PhotosViewBinder implements ViewBinder {

    @Override
    public boolean setViewValue(final View view, final Cursor cursor, final Binding binding) {
        return setMessageImage((ImageView) view, cursor, binding);
    }

    private boolean setMessageImage(final ImageView view, final Cursor cursor, final Binding binding) {
        final String url = cursor.getString(binding.getColumnIndex());
        final String urlSized = String.format("%s?w=400&h=400", url);
        Picasso.with(view.getContext()).load(urlSized).into(view);
        return true;
    }
}
