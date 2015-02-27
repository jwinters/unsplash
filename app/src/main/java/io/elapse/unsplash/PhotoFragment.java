package io.elapse.unsplash;

import android.app.Fragment;
import android.graphics.Point;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoFragment extends Fragment implements View.OnClickListener {

    private View mContainer;
    private ImageView mImageView;
    private TextView mTextView;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContainer = view.findViewById(R.id.photo_container);
        mImageView = (ImageView) view.findViewById(R.id.photo_image);
        mTextView = (TextView) view.findViewById(R.id.photo_by_line);

        final TransitionDrawable transition = (TransitionDrawable) view.getBackground();
        transition.startTransition(400);

        view.setOnClickListener(this);
    }

    public void setImageDetails(final String url, final int[] info, final String byLine) {
        final LayoutParams containerParams = getLayoutParams(info);
        final AnimationSet photoAnimation = getPhotoAnimation(containerParams);
        final Animation textAnimation = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);

        mContainer.setLayoutParams(containerParams);
        mContainer.startAnimation(photoAnimation);

        mTextView.setText(byLine);
        mTextView.startAnimation(textAnimation);

        final String urlSized = String.format("%s?w=400&h=400", url);
        Picasso.with(mImageView.getContext()).load(urlSized).into(mImageView);
    }

    private LayoutParams getLayoutParams(final int[] info) {
        final LayoutParams containerParams = (LayoutParams) mContainer.getLayoutParams();
        containerParams.leftMargin = info[0];
        containerParams.topMargin = info[1];
        containerParams.width = info[2];
        containerParams.height = info[3];
        return containerParams;
    }

    private AnimationSet getPhotoAnimation(final LayoutParams containerParams) {
        final Point size = getWindowSize();
        final float viewWidth = containerParams.width;
        final float viewHeight = containerParams.height;
        final float xMargin = (size.x - 2 * viewWidth) / 2f;
        final float yMargin = (size.y - 2 * viewHeight) / 2f;
        final float scale = (size.x - 2 * xMargin) / viewWidth;
        final float positionLeft = -containerParams.leftMargin + xMargin;
        final float positionTop = -containerParams.topMargin + yMargin;

        return getAnimation(scale, positionLeft, positionTop);
    }

    private AnimationSet getAnimation(final float scale, final float positionLeft, final float positionTop) {
        final int type = Animation.RELATIVE_TO_SELF;
        final AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(new ScaleAnimation(1, scale, 1, scale, type, 0, type, 0));
        animation.addAnimation(new TranslateAnimation(0, positionLeft, 0, positionTop));
        animation.setFillAfter(true);
        animation.setDuration(400);
        return animation;
    }

    private Point getWindowSize() {
        final Point size = new Point();
        getDisplay().getSize(size);
        return size;
    }

    private Display getDisplay() {
        final WindowManager manager = getActivity().getWindowManager();
        return manager.getDefaultDisplay();
    }

    @Override
    public void onClick(final View v) {
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
    }
}
