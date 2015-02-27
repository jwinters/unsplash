package io.elapse.unsplash;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.pivotal.arca.utils.Logger;

public class UnsplashApi {

    private static interface Endpoints {
        public static final String UNSPLASH_URL = "https://unsplash.com/filter?scope[featured]=0";
    }

    public static Photos getPhotos(final int page) throws Exception {
        final String url = Endpoints.UNSPLASH_URL + "&page=" + page;

        Logger.v("Request : " + url);

        final Connection connection = Jsoup.connect(url);
        final Connection.Response response = connection.execute();

        Logger.v("Response : " + response.statusCode());

        final Document document = response.parse();
        final Elements elements = document.select("div.photo-grid img");

        return getPhotos(elements);
    }

    private static Photos getPhotos(final Elements elements) {
        final Photos photos = new Photos();

        for (final Element element : elements) {
            if (element.tagName().equals("img")) {
                photos.add(getPhoto(element));
            }
        }

        return photos;
    }

    private static Photo getPhoto(final Element element) {
        final String fullSrc = element.attr("src");
        final String src = fullSrc.substring(0, fullSrc.indexOf("?"));
        final String byLine = element.attr("alt");
        final String dataWidth = element.attr("data-width");
        final String dataHeight = element.attr("data-height");

        Logger.v("%s, %s, %s, %s", src, byLine, dataWidth, dataHeight);

        return new Photo(src, byLine, dataWidth, dataHeight);
    }
}
