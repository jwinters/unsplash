package io.elapse.unsplash;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;

import io.pivotal.arca.adapters.Binding;
import io.pivotal.arca.adapters.ModernCursorAdapter;
import io.pivotal.arca.service.Operation;
import io.pivotal.arca.service.OperationService;

public class PhotosAdapter extends ModernCursorAdapter {

    public PhotosAdapter(final Context context, final Collection<Binding> bindings) {
        super(context, 0, bindings);
    }

    @Override
    public int getViewTypeCount() {
        return ViewType.values().length;
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    @Override
    public int getItemViewType(final int position) {
        return getViewType(position).ordinal();
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(ViewType.DEFAULT.getLayout(), parent, false);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        if (position < getCount() - 1) {
            return super.getView(position, convertView, parent);
        } else {
            final int page = position / 10 + 1;
            final Operation operation = new PhotosOperation(page);

            final Context context = parent.getContext();
            OperationService.start(context, operation);

            final LayoutInflater inflater = LayoutInflater.from(context);
            return inflater.inflate(ViewType.LOADING.getLayout(), parent, false);
        }
    }

    private ViewType getViewType(final int position) {
        if (position == getCount() - 1) {
            return ViewType.LOADING;
        } else {
            return ViewType.DEFAULT;
        }
    }

    public static enum ViewType {
        DEFAULT(R.layout.grid_item_photo),
        LOADING(R.layout.grid_item_photo_loading);

        private int mLayout;

        ViewType(final int layout) {
            mLayout = layout;
        }

        public int getLayout() {
            return mLayout;
        }

        public Binding newBinding(final int viewId, final String column) {
            return new Binding(ordinal(), viewId, column);
        }
    }
}
