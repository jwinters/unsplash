package io.elapse.unsplash;

import android.net.Uri;

import io.pivotal.arca.provider.Column;
import io.pivotal.arca.provider.DatabaseProvider;
import io.pivotal.arca.provider.SQLiteTable;
import io.pivotal.arca.provider.Unique;

public class UnsplashContentProvider extends DatabaseProvider {

    public static final String AUTHORITY = UnsplashContentProvider.class.getName();

    public static final class Uris {
        private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
        public static final Uri PHOTOS = Uri.withAppendedPath(BASE_URI, Paths.PHOTOS);
    }

    private static final class Paths {
        public static final String PHOTOS = "photos";
    }

    @Override
    public boolean onCreate() {
        registerDataset(AUTHORITY, Paths.PHOTOS, PhotoTable.class);
        return true;
    }

    public static class PhotoTable extends SQLiteTable {
        public static interface Columns extends SQLiteTable.Columns {

            @Unique(Unique.OnConflict.IGNORE)
            @Column(Column.Type.TEXT)
            public static final String URL = "url";

            @Column(Column.Type.TEXT)
            public static final String BY_LINE = "by_line";

            @Column(Column.Type.INTEGER)
            public static final String WIDTH = "width";

            @Column(Column.Type.INTEGER)
            public static final String HEIGHT = "height";

            @Column(Column.Type.INTEGER)
            public static final String TIMESTAMP = "timestamp";
        }
    }
}
