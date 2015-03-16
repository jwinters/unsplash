package io.elapse.unsplash;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;

import java.util.Arrays;
import java.util.Collection;

import io.elapse.unsplash.PhotosAdapter.ViewType;
import io.elapse.unsplash.UnsplashContentProvider.PhotoTable;
import io.pivotal.arca.adapters.Binding;
import io.pivotal.arca.fragments.ArcaFragment;
import io.pivotal.arca.fragments.ArcaFragmentBindings;
import io.pivotal.arca.fragments.ArcaSimpleAdapterFragment;

@ArcaFragment(
    fragmentLayout = R.layout.fragment_photos,
    monitor = PhotosMonitor.class
)
public class PhotosFragment extends ArcaSimpleAdapterFragment {

    @ArcaFragmentBindings
    private static final Collection<Binding> BINDINGS = Arrays.asList(
        ViewType.DEFAULT.newBinding(R.id.photo_image, PhotoTable.Columns.URL),
        ViewType.LOADING.newBinding(R.id.photo_loading, PhotoTable.Columns._ID)
    );

    private View mSelectedView;

    @Override
    public CursorAdapter onCreateAdapter(final AdapterView<CursorAdapter> adapterView, final Bundle savedInstanceState) {
        final PhotosAdapter adapter = new PhotosAdapter(getActivity(), BINDINGS);
        adapter.setViewBinder(new PhotosViewBinder());
        return adapter;
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {

        final Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        final String url = cursor.getString(cursor.getColumnIndex(PhotoTable.Columns.URL));
        final String byLine = cursor.getString(cursor.getColumnIndex(PhotoTable.Columns.BY_LINE));

        selectPhoto(view, url, byLine);
    }

    private void selectPhoto(final View view, final String url, final String byLine) {

        final int[] info = ViewUtils.getInfo(view);
        PhotoActivity.newInstance(getActivity(), url, info, byLine);

        hidePhotoDelayed(view);
    }

    private void hidePhotoDelayed(final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hidePhoto(view);
            }
        }, 100);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        execute(new PhotosQuery());
    }

    @Override
    public void onResume() {
        super.onResume();

        unhidePhoto();
    }

    private void unhidePhoto() {
        if (mSelectedView != null) {
            mSelectedView.setVisibility(View.VISIBLE);
        }
    }

    private void hidePhoto(final View view) {
        mSelectedView = view;
        mSelectedView.setVisibility(View.INVISIBLE);
    }
}
