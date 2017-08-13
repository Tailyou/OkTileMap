# HdTileMap-瓦片地图导览功能库

## 一、概述
基于瓦片地图(TileView)开发室内导览模块其实并不复杂，无非就是以下5点：
* 1、瓦片地图基础配置；
* 2、显示/隐藏导览路线；
* 3、显示/隐藏展项Marker；
* 4、收号之后的定位效果；
* 5、回到我的位置。

对上述功能进行底层封装，`上层不再直接操作TileView`，直接调用即可。调用时传入配置参数，能极大的减少这一部分的开发工作量，封装此库的目的即在于此。

## 二、版本
```groovy
	dependencies {
	        compile 'com.github.Tailyou:OkTileMap:0.0.1'
	}
```

## 三、使用

HdTileMap中内置`BaseMapFragment`，封装了大量的共性功能，对于需要定制的个性功能则提供了抽象方法供上层实现，如点击Marker后的Callout样式等。提供了`BaseMapFragment`的默认实现`DefaultMapFragment`。

内置展项基类`BaseExhibit`，包含多模号、X坐标、Y坐标、展项名称、展项定位小图路径、展项定位大图路径。

内置地图参数配置类`MapConfig`，可配置尺寸宽`sizeWidth`、尺寸长`sizeHeight`、最小缩放限制`minScale`，最大缩放限制`maxScale`、地图图片基路径`baseMapPath`、是否设置scale==0`scale0`、最小缩放模式`minimumScaleMode`及`BitmapProvider`。

采用建造者模式构造参数配置类，内置构造器`MapConfigBuilder`，需要配置的参数部分提供了默认值。

#### 1）地图基本参数配置

```
    /**
     * 配置MapConfig
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/4/28 13:16
     */
    private MapConfig initMapConfig() {
        return new MapConfigBuilder()
                .setBaseMapString(AppConfig.getMapFilePath())
                .setSizeWidth(2372)
                .setSizeHeight(3470)
                .setScale0(true)
                .setBitmapProvider(new BitmapProviderFile())
                .create();
    }
```

```
    MapConfig mapConfig = initMapConfig();
    getSupportFragmentManager().beginTransaction()
         .replace(R.id.flMapContainer, DefaultMapFragment.newInstance(mapConfig))
         .commit();
```

#### 2）显示展项Marker

```
    //传入展项
    private void showMarker(ArrayList<ExhibitBean> list) {
        Intent intent = new Intent(Intents.Action.SHOW_MARKERS);
        intent.putParcelableArrayListExtra(Intents.EXTRA.MARKERS, list);
        sendBroadcast(intent);
    }
```

#### 3）隐藏展项Marker

```
    private void hideMarker() {
        Intent intent = new Intent(Intents.Action.HIDE_MARKERS);
        sendBroadcast(intent);
    }
```

#### 4）显示路线

```
    //传入路线图片路径
    private void showRoute(String routeImgPath) {
        Intent intent = new Intent(Intents.Action.SHOW_ROUTE);
        intent.putExtra(Intents.EXTRA.ROUTE_IMG_PATH, routeImgPath);
        sendBroadcast(intent);
    }
```

#### 5）隐藏路线

```
    private void hideRoute() {
        Intent intent = new Intent(Intents.Action.HIDE_ROUTE);
        sendBroadcast(intent);
    }
```

#### 6）收号
```
    //传入多模号
    private void receiveAutoNo(int autoNo) {
        Intent intent = new Intent(Intents.Action.RECEIVE_NO);
        intent.putExtra(Intents.EXTRA.AUTO_NO, autoNo);
        sendBroadcast(intent);
    }
```

#### 7）回到我的位置

```
    private void toMyLocation() {
        Intent intent = new Intent(Intents.Action.TO_MY_LOCATION);
        sendBroadcast(intent);
    }
```

详细用法参见demo，项目地址：https://git.oschina.net/tailyou/HD_Frame_TileMap

