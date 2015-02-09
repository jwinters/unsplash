package io.elapse.unsplash;

import android.content.Context;

import io.pivotal.arca.dispatcher.Query;
import io.pivotal.arca.dispatcher.QueryResult;
import io.pivotal.arca.monitor.RequestMonitor;

public class PhotosMonitor extends RequestMonitor.AbstractRequestMonitor {

    @Override
    public int onPostExecute(final Context context, final Query request, final QueryResult result) {

        if (result.getResult().getCount() == 0) {
            return Flags.DATA_SYNCING;
        } else {
            return 0;
        }
    }
}
