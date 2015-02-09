package io.elapse.unsplash;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;

import io.pivotal.arca.provider.DataUtils;
import io.pivotal.arca.service.SimpleOperation;
import io.pivotal.arca.utils.Logger;

public class PhotosOperation extends SimpleOperation {

    private final int mPage;

    public PhotosOperation(final int page) {
        super(UnsplashContentProvider.Uris.PHOTOS);
        mPage = page;
    }

    private PhotosOperation(final Parcel in) {
        super(in);
        mPage = in.readInt();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(mPage);
    }

    @Override
    public ContentValues[] onExecute(final Context context) throws Exception {
        final Photos photos = UnsplashApi.getPhotos(mPage);

        Logger.v("Found " + photos.size() + " photos.");

        return DataUtils.getContentValues(photos);
    }

    public void onPostExecute(final Context context, final ContentValues[] values) throws Exception {
        final ContentResolver resolver = context.getContentResolver();

        if (mPage == 1) {
            final long deleted = resolver.delete(getUri(), null, null);

            Logger.v("Deleted " + deleted + " photos.");
        }

        final long inserted = resolver.bulkInsert(getUri(), values);

        Logger.v("Inserted " + inserted + " photos.");

        if (inserted == 0) {
            throw new RuntimeException();
        }
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public PhotosOperation createFromParcel(final Parcel in) {
            return new PhotosOperation(in);
        }

        @Override
        public PhotosOperation[] newArray(final int size) {
            return new PhotosOperation[size];
        }
    };
}
