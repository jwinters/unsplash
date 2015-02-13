package io.elapse.unsplash;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;

import java.util.Arrays;
import java.util.Collection;

import io.elapse.unsplash.UnsplashContentProvider.PhotoTable;
import io.pivotal.arca.adapters.Binding;
import io.pivotal.arca.dispatcher.Error;
import io.pivotal.arca.dispatcher.QueryResult;
import io.pivotal.arca.fragments.ArcaAdapterFragment;
import io.pivotal.arca.fragments.ArcaViewManager;
import io.pivotal.arca.monitor.ArcaDispatcher;

public class PhotosFragment extends ArcaAdapterFragment implements AdapterView.OnItemClickListener {

    private static final Collection<Binding> BINDINGS = Arrays.asList(
        PhotosAdapter.ViewType.DEFAULT.newBinding(R.id.photo_image, PhotoTable.Columns.URL),
        PhotosAdapter.ViewType.LOADING.newBinding(R.id.photo_loading, PhotoTable.Columns._ID)
    );

    private ArcaViewManager mManager;

    private View mSelectedView;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_photos, container, false);
        final AbsListView listView = (AbsListView) view.findViewById(getAdapterViewId());
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {

        final Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        final String url = cursor.getString(cursor.getColumnIndex(PhotoTable.Columns.URL));

        selectPhoto(view, url);
    }

    private void selectPhoto(final View view, final String url) {

        final int[] info = ViewUtils.getInfo(view);

        PhotoActivity.newInstance(getActivity(), url, info);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hidePhoto(view);
            }
        }, 100);
    }

    @Override
    public CursorAdapter onCreateAdapter(final AdapterView<CursorAdapter> adapterView, final Bundle savedInstanceState) {
        final PhotosAdapter adapter = new PhotosAdapter(getActivity(), BINDINGS);
        adapter.setViewBinder(new PhotosViewBinder());
        return adapter;
    }

    @Override
    public ArcaDispatcher onCreateDispatcher(final Bundle savedInstanceState) {
        final ArcaDispatcher dispatcher = super.onCreateDispatcher(savedInstanceState);
        dispatcher.setRequestMonitor(new PhotosMonitor());
        return dispatcher;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mManager = new ArcaViewManager(view);
        mManager.showProgressView();

        execute(new PhotosQuery());
    }

    @Override
    public void onContentChanged(final QueryResult result) {
        mManager.checkResult(result);
    }

    @Override
    public void onContentError(final Error error) {
        mManager.checkError(error);
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
