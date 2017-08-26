package com.hengda.zwf.oktilemap.config

import com.qozix.tileview.widgets.ZoomPanLayout

/**
 * 地图配置参数构造者
 * @author 祝文飞（Tailyou）
 * @time 2017/4/28 11:11
 */
class MapConfigBuilder {

    private var mapId: Int = 0
    private var sizeWidth: Int = 0
    private var sizeHeight: Int = 0
    private var boundLeft: Double = 0.toDouble()
    private var boundTop: Double = 0.toDouble()
    private var boundRight: Double = 0.toDouble()
    private var boundBottom: Double = 0.toDouble()
    private var initScale = 0f
    private var minScale = 0f
    private var maxScale = 2f
    private var anchorX = -0.5f
    private var anchorY = -1.0f
    private var formatNew = true
    private var downSample = true
    private var minimumScaleMode: ZoomPanLayout.MinimumScaleMode = ZoomPanLayout.MinimumScaleMode.FIT
    private lateinit var baseMapPath: String
    private var merge = false
    private var mergeScale = 1.0f
    private var mergeDistance = 300f

    fun setMapId(mapId: Int): MapConfigBuilder {
        this.mapId = mapId
        return this
    }

    fun setSize(width: Int, height: Int): MapConfigBuilder {
        this.sizeWidth = width
        this.sizeHeight = height
        return this
    }

    fun setBound(left: Double, top: Double, right: Double, bottom: Double): MapConfigBuilder {
        this.boundLeft = left
        this.boundTop = top
        this.boundRight = right
        this.boundBottom = bottom
        return this
    }

    fun setScaleLimit(minScale: Float, maxScale: Float): MapConfigBuilder {
        this.minScale = minScale
        this.maxScale = maxScale
        return this
    }

    fun setAnchorX(anchorX: Float): MapConfigBuilder {
        this.anchorX = anchorX
        return this
    }

    fun setAnchorY(anchorY: Float): MapConfigBuilder {
        this.anchorY = anchorY
        return this
    }

    fun setFormatNew(formatNew: Boolean): MapConfigBuilder {
        this.formatNew = formatNew
        return this
    }

    fun setDownSample(downSample: Boolean): MapConfigBuilder {
        this.downSample = downSample
        return this
    }

    fun setInitScale(initScale: Float): MapConfigBuilder {
        this.initScale = initScale
        return this
    }

    fun setMinScaleMode(minimumScaleMode: ZoomPanLayout.MinimumScaleMode): MapConfigBuilder {
        this.minimumScaleMode = minimumScaleMode
        return this
    }

    fun setBaseMapString(baseMapPath: String): MapConfigBuilder {
        this.baseMapPath = baseMapPath
        return this
    }

    fun setMerge(merge: Boolean, mergeScale: Float, mergeDistance: Float): MapConfigBuilder {
        this.merge = merge
        this.mergeScale = mergeScale
        this.mergeDistance = mergeDistance
        return this
    }

    fun create(): MapConfig {
        if (boundLeft == boundRight) {
            boundLeft = 0.0
            boundTop = 0.0
            boundRight = sizeWidth.toDouble()
            boundBottom = sizeHeight.toDouble()
        }
        return MapConfig(mapId, sizeWidth, sizeHeight,
                boundLeft, boundTop, boundRight, boundBottom,
                initScale, minScale, maxScale, anchorX, anchorY,
                formatNew, downSample, minimumScaleMode, baseMapPath,
                merge, mergeScale, mergeDistance)
    }

}
