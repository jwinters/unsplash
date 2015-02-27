package io.elapse.unsplash;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class PhotoActivity extends ActionBarActivity {

    private static class Extras {
        public static final String URL = "url";
        public static final String INFO = "info";
        public static final String BY_LINE = "by_line";
    }

    public static void newInstance(final Activity activity, final String url, final int[] info, final String byLine) {
        final Intent intent = new Intent(activity, PhotoActivity.class);
        intent.putExtra(Extras.URL, url);
        intent.putExtra(Extras.INFO, info);
        intent.putExtra(Extras.BY_LINE, byLine);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        final String url = getIntent().getStringExtra(Extras.URL);
        final int[] info = getIntent().getIntArrayExtra(Extras.INFO);
        final String byLine = getIntent().getStringExtra(Extras.BY_LINE);

        getPhotoFragment().setImageDetails(url, info, byLine);
    }

    private PhotoFragment getPhotoFragment() {
        final FragmentManager manager = getFragmentManager();
        return (PhotoFragment) manager.findFragmentById(R.id.fragment_photo);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}