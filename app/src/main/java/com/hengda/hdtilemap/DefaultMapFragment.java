package com.hengda.hdtilemap;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hengda.zwf.hdtilemap.BaseMapFragment;
import com.hengda.zwf.hdtilemap.config.MapConfig;
import com.hengda.zwf.hdtilemap.entity.BaseExhibit;
import com.skyfishjy.library.RippleBackground;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class DefaultMapFragment extends BaseMapFragment {

    List<View> highlightViews = new ArrayList();
    RippleBackground rbPositioning;
    View callOut;

    public static DefaultMapFragment newInstance(MapConfig mapConfig) {
        DefaultMapFragment fragment = new DefaultMapFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MAP_CONFIG, mapConfig);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onHightlightEvent(final HighLight highLight) {
        tileView.post(new Runnable() {
            @Override
            public void run() {
                if (highLight.isShow()) {
                    showHighlight(R.mipmap.level_1, 3);
                    showHighlight(R.mipmap.level_2, 4);
                    showHighlight(R.mipmap.level_3, 4);
                    showHighlight(R.mipmap.level_4, 4);
                } else {
                    for (View view : highlightViews) {
                        tileView.removeView(view);
                    }
                }
            }
        });
    }

    private void showHighlight(int resId, int index) {
        ImageView highlightView = new ImageView(getContext());
        highlightView.setImageResource(resId);
        highlightViews.add(highlightView);
        tileView.addView(highlightView, index, new ViewGroup.LayoutParams(tileView.getBaseWidth(), tileView.getBaseHeight()));
    }

    /**
     * 定位
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/4/28 17:41
     */
    @Override
    public void positioning(BaseExhibit exhibit) {
        if (rbPositioning != null) {
            rbPositioning.stopRippleAnimation();
            tileView.removeMarker(rbPositioning);
        }
        rbPositioning = (RippleBackground) View.inflate(getContext(), R.layout.layout_tile_map_location, null);
        rbPositioning.startRippleAnimation();
        tileView.addMarker(rbPositioning, exhibit.getLocX(), exhibit.getLocY(), mapConfig.getAnchorX(), mapConfig.getAnchorY());
    }

    /**
     * 生成展项Marker
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/4/28 14:07
     */
    @Override
    public ImageView genExhibitMarker(final BaseExhibit exhibit) {
        ImageView marker = new ImageView(getContext());
        imageLoader.displayImage(getActivity(), exhibit.getExhibitMapPicLg(), marker, 49, 59);
        marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCallout(exhibit);
            }
        });
        return marker;
    }

    /**
     * 生成聚合Marker
     *
     * @return
     */
    @Override
    public ImageView genMergeMarker() {
        ImageView marker = new ImageView(getContext());
        marker.setImageResource(R.mipmap.ic_map_merge);
        return marker;
    }

    /**
     * 显示Callout，点击或定位后的效果
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/4/28 17:14
     */
    @Override
    public void showCallout(final BaseExhibit exhibit) {
        if (callOut != null) {
            tileView.removeCallout(callOut);
        }
        final ImageView marker = exhibitMarkerMap.get(exhibit.getFileNo());
        callOut = View.inflate(getContext(), R.layout.layout_tile_map_pop, null);
        TextView tvExhName = (TextView) callOut.findViewById(R.id.tvExhName);
        ImageView ivExhPopPic = (ImageView) callOut.findViewById(R.id.ivExhPopPic);
        tvExhName.setText(exhibit.getExhibitName());
        imageLoader.displayImage(getActivity(), exhibit.getExhibitMapPicLg(), ivExhPopPic);
        callOut.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                if (marker != null)
                    tileView.removeMarker(marker);
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                if (marker != null)
                    tileView.addMarker(marker, exhibit.getLocX(), exhibit.getLocY(), mapConfig.getAnchorX(), mapConfig.getAnchorY());
            }
        });
        if (tileView.getScale() < 1) {
            tileView.setScale(1);
        }
        tileView.slideToAndCenter(exhibit.getLocX(), exhibit.getLocY());
        tileView.addCallout(callOut, exhibit.getLocX(), exhibit.getLocY(), mapConfig.getAnchorX(), mapConfig.getAnchorY());
    }

}
