package io.elapse.unsplash;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

public class ViewUtils {

    public static int[] getInfo(final View view) {
        final int[] info = new int[4];
        view.getLocationInWindow(info);

        info[1] -= getStatusBarHeight(view.getContext());
        info[2] = view.getWidth();
        info[3] = view.getHeight();

        return info;
    }

    private static int getStatusBarHeight(final Context context) {
        final Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }
}
