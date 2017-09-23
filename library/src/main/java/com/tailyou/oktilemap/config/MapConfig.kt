package com.tailyou.oktilemap.config

import com.qozix.tileview.widgets.ZoomPanLayout

import java.io.Serializable

//瓦片地图参数配置类
class MapConfig(var mapId: Int, var sizeWidth: Int, var sizeHeight: Int,
                var boundLeft: Double, var boundTop: Double, var boundRight: Double, var boundBottom: Double,
                var initScale: Float, var minScale: Float, var maxScale: Float, var provider: Int,
                var anchorX: Float, var anchorY: Float, var isFormatNew: Boolean, var isDownSample: Boolean,
                var minimumScaleMode: ZoomPanLayout.MinimumScaleMode, var baseMapPath: String,
                var isMerge: Boolean, var mergeScale: Float, var mergeDistance: Float) : Serializable
