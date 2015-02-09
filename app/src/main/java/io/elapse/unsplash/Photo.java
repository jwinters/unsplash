package io.elapse.unsplash;

import io.elapse.unsplash.UnsplashContentProvider.PhotoTable;

import io.pivotal.arca.provider.ColumnName;

public class Photo {
    @ColumnName(PhotoTable.Columns.URL)
    public final String mUrl;

    @ColumnName(PhotoTable.Columns.WIDTH)
    public final String mWidth;

    @ColumnName(PhotoTable.Columns.HEIGHT)
    public final String mHeight;

    @ColumnName(PhotoTable.Columns.TIMESTAMP)
    public final long mTimestamp;

    public Photo(final String url, final String width, final String height) {
        mUrl = url;
        mWidth = width;
        mHeight = height;
        mTimestamp = System.currentTimeMillis();
    }
}
