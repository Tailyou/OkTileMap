package com.hengda.zwf.oktilemap.util

import android.content.Context
import android.graphics.Bitmap

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.qozix.tileview.graphics.BitmapProvider
import com.qozix.tileview.tiles.Tile

import java.util.concurrent.ExecutionException

class GlideBitmapProvider : BitmapProvider {

    override fun getBitmap(tile: Tile, context: Context): Bitmap? {
        val data = tile.data
        if (data is String) {
            val unformattedFileName = tile.data as String
            val formattedFileName = java.lang.String.format(unformattedFileName, tile.column, tile.row)
            try {
                return Glide.with(context).load(formattedFileName).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(tile.width, tile.height)
                        .get()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
        }
        return null
    }

}