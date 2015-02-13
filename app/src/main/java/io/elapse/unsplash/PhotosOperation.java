package io.elapse.unsplash;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;

import io.pivotal.arca.provider.DataUtils;
import io.pivotal.arca.service.OperationService;
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

        Logger.v("Found " + photos.size() + " new photos.");

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

        final Cursor cursor = resolver.query(getUri(), null, null, null, null);
        final int count = cursor.getCount();
        cursor.close();

        Logger.v("Found " + count + " photos after insert.");

        if (inserted == 0) {
            Logger.v("Moving on to next page.");
            OperationService.start(context, new PhotosOperation(mPage + 1));

            throw new RuntimeException();
        }

        if (count % 10 != 0) {
            Logger.v("Starting pagination over.");
            OperationService.start(context, new PhotosOperation(1));

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
