package com.tailyou.tilemap


import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.hengda.hdtilemap.R
import com.skyfishjy.library.RippleBackground
import com.tailyou.oktilemap.BaseMapFragment
import com.tailyou.tilemap.anim.Jumper
import org.jetbrains.anko.find

class MapFragment : BaseMapFragment() {

    internal var rbPositioning: RippleBackground? = null
    internal var callOut: View? = null

    companion object {
        fun newInstance(mapConfig: MapConfig): MapFragment {
            val fragment = MapFragment()
            val bundle = Bundle()
            bundle.putSerializable(BaseMapFragment.MAP_CONFIG, mapConfig)
            fragment.arguments = bundle
            return fragment
        }
    }

    //定位
    override fun positioning(exhibit: BaseExhibit) {
        if (rbPositioning != null) {
            rbPositioning!!.stopRippleAnimation()
            tileView.removeMarker(rbPositioning)
        }
        rbPositioning = View.inflate(context, R.layout.layout_tile_map_location, null) as RippleBackground
        rbPositioning!!.startRippleAnimation()
        tileView.addMarker(rbPositioning, exhibit.locX, exhibit.locY, mapConfig.anchorX, mapConfig.anchorY)
    }

    //生成展项Marker
    override fun genExhibitMarker(exhibit: BaseExhibit): ImageView {
        val marker = ImageView(context)
        imageLoader.displayImage(context, exhibit.mapPicLg, marker, 49, 59)
        marker.setOnClickListener { showCallout(exhibit) }
        return marker
    }

    //生成聚合Marker
    override fun genMergeMarker(list: List<BaseExhibit>, averLoc: Location): ImageView {
        val marker = ImageView(context)
        marker.setImageResource(R.mipmap.ic_map_merge)
        marker.setOnClickListener {
            tileView.scale = mapConfig.mergeScale
            tileView.slideToAndCenter(averLoc.locX, averLoc.locY)
            showMarker()
        }
        return marker
    }

    //点击或定位后显示Callout
    override fun showCallout(exhibit: BaseExhibit) {
        if (callOut != null) tileView.removeCallout(callOut)
        callOut = View.inflate(context, R.layout.layout_tile_map_pop, null)
        val tvExhName = callOut?.find<TextView>(R.id.tvExhName)
        val ivExhPopPic = callOut?.find<ImageView>(R.id.ivExhPopPic)
        tvExhName?.text = exhibit.name
        imageLoader.displayImage(activity, exhibit.mapPicLg, ivExhPopPic!!)
        val jumper = Jumper(600L, 30F)
        jumper.attachToView(ivExhPopPic)
        val marker = exhibitMarkerMap[exhibit.fileNo]
        callOut?.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                tileView.removeMarker(marker)
            }

            override fun onViewDetachedFromWindow(view: View) {
                tileView.addMarker(marker, exhibit.locX, exhibit.locY, mapConfig.anchorX, mapConfig.anchorY)
            }
        })
        if (tileView.scale < 1) {
            tileView.scale = 1f
        }
        tileView.slideToAndCenter(exhibit.locX, exhibit.locY)
        tileView.addCallout(callOut, exhibit.locX, exhibit.locY, mapConfig.anchorX, mapConfig.anchorY)
    }

}
