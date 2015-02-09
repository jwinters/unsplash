package io.elapse.unsplash;

import android.app.Fragment;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.squareup.picasso.Picasso;

public class PhotoFragment extends Fragment implements View.OnClickListener {

    private View mContainer;
    private ImageView mImageView;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContainer = view.findViewById(R.id.photo_container);
        mImageView = (ImageView) view.findViewById(R.id.photo_image);

        final TransitionDrawable transition = (TransitionDrawable) view.getBackground();
        transition.startTransition(200);

        view.setOnClickListener(this);
    }

    public void setImageDetails(final String url, final int[] info) {

        final LayoutParams containerParams = (LayoutParams) mContainer.getLayoutParams();
        containerParams.leftMargin = info[0];
        containerParams.topMargin = info[1];
        containerParams.width = info[2];
        containerParams.height = info[3];

        mContainer.setLayoutParams(containerParams);

        final String urlSized = String.format("%s?w=400&h=400", url);
        Picasso.with(mImageView.getContext()).load(urlSized).into(mImageView);
    }

    @Override
    public void onClick(final View v) {
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
    }
}
