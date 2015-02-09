package io.elapse.unsplash;

import io.pivotal.arca.dispatcher.Query;

public class PhotosQuery extends Query {

    public PhotosQuery() {
        super(UnsplashContentProvider.Uris.PHOTOS);
    }
}
