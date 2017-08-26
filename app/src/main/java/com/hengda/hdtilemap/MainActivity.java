package com.hengda.hdtilemap;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hengda.hdtilemap.app.AppConfig;
import com.hengda.hdtilemap.bean.ExhibitBean;
import com.hengda.hdtilemap.db.HBriteDatabase;
import com.hengda.zwf.hdtilemap.common.Intents;
import com.hengda.zwf.hdtilemap.config.MapConfig;
import com.hengda.zwf.hdtilemap.config.MapConfigBuilder;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import rx.functions.Action1;
import rx.functions.Func1;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private TextView tvRoute;
    private TextView tvMarker;
    private TextView tvAutoNo;
    private TextView tvLocation;
    private TextView tvHighLight;
    private boolean isRouteShowing;
    private boolean isMarkerShowing;
    private ArrayList<ExhibitBean> exhibits = new ArrayList<>();
    private int mapId = 3;
    private boolean highlight = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvRoute = (TextView) findViewById(R.id.tvRoute);
        tvMarker = (TextView) findViewById(R.id.tvMarker);
        tvAutoNo = (TextView) findViewById(R.id.tvAutoNo);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvHighLight = (TextView) findViewById(R.id.tvHighLight);
        //显示路线
        tvRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRouteShowing) {
                    hideRoute();
                } else {
                    showRoute(AppConfig.getMapFilePath() + "/route.png");
                }
            }
        });

        //显示Maker
        tvMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMarkerShowing) {
                    hideMarker();
                } else {
                    showMarker(exhibits);
                }
            }
        });

        //收号定位
        tvAutoNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveAutoNo(new Random().nextInt(100));
            }
        });

        //我的位置
        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMyLocation();
            }
        });

        //高亮
        tvHighLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                highlight = !highlight;
                EventBus.getDefault().post(new HighLight(highlight));
            }
        });

        MainActivityPermissionsDispatcher.showMapWithCheck(MainActivity.this);

    }

    /**
     * 显示路线
     *
     * @param routeImgPath
     * @author 祝文飞（Tailyou）
     * @time 2017/4/28 15:23
     */
    private void showRoute(String routeImgPath) {
        Intent intent = new Intent(Intents.Action.SHOW_ROUTE);
        intent.putExtra(Intents.Extra.ROUTE_IMG_PATH, routeImgPath);
        sendBroadcast(intent);
        isRouteShowing = true;
    }

    /**
     * 隐藏路线
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/4/28 15:23
     */
    private void hideRoute() {
        Intent intent = new Intent(Intents.Action.HIDE_ROUTE);
        sendBroadcast(intent);
        isRouteShowing = false;
    }

    /**
     * 显示展品Marker
     *
     * @param list
     * @author 祝文飞（Tailyou）
     * @time 2017/4/28 15:24
     */
    private void showMarker(ArrayList<ExhibitBean> list) {
        Intent intent = new Intent(Intents.Action.SHOW_MARKERS);
        intent.putExtra(Intents.Extra.MAP_ID, mapId);
        intent.putParcelableArrayListExtra(Intents.Extra.MARKERS, list);
        sendBroadcast(intent);
        isMarkerShowing = true;
    }

    /**
     * 隐藏展品Marker
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/4/28 15:24
     */
    private void hideMarker() {
        Intent intent = new Intent(Intents.Action.HIDE_MARKERS);
        intent.putExtra(Intents.Extra.MAP_ID, mapId);
        sendBroadcast(intent);
        isMarkerShowing = false;
    }

    /**
     * 收到多模号
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/4/28 15:23
     */
    private void receiveAutoNo(int autoNo) {
        Intent intent = new Intent(Intents.Action.RECEIVE_NO);
        intent.putExtra(Intents.Extra.AUTO_NO, autoNo);
        sendBroadcast(intent);
    }

    /**
     * 回到我的位置
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/4/29 10:15
     */
    private void toMyLocation() {
        Intent intent = new Intent(Intents.Action.TO_MY_LOCATION);
        intent.putExtra(Intents.Extra.MAP_ID, mapId);
        sendBroadcast(intent);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    public void showMap() {
        MapConfig mapConfig = initMapConfig();
        DefaultMapFragment mapFragment = DefaultMapFragment.newInstance(mapConfig);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flMapContainer, mapFragment)
                .commitAllowingStateLoss();
        loadExhibit();
    }

    /**
     * 配置MapConfig
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/4/28 13:16
     */
    private MapConfig initMapConfig() {
        return new MapConfigBuilder()
                .setBaseMapString(AppConfig.getMapFilePath() + mapId)
                .setMapId(mapId)
                .setMerge(true, 1f, 300f)
                .setSize(3090, 2273)
                .setInitScale(0)
                .create();
    }

    private void loadExhibit() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM EXHIBIT_CHINESE WHERE MapId = ").append(mapId);
        HBriteDatabase.getInstance(MainActivity.this)
                .getDb()
                .createQuery("EXHIBIT_CHINESE", sb.toString(), new String[]{})
                .mapToList(new Func1<Cursor, ExhibitBean>() {
                    @Override
                    public ExhibitBean call(Cursor cursor) {
                        return ExhibitBean.cursor2Exhibit(cursor);
                    }
                })
                .subscribe(new Action1<List<ExhibitBean>>() {
                    @Override
                    public void call(List<ExhibitBean> list) {
                        exhibits.clear();
                        exhibits.addAll(list);
                        showMarker(exhibits);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.e(throwable.getMessage());
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

}
