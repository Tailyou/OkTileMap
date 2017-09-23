package com.tailyou.oktilemap.util

import android.content.Context
import android.graphics.Bitmap

import com.qozix.tileview.graphics.BitmapProvider
import com.qozix.tileview.tiles.Tile
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

class PicassoBitmapProvider : BitmapProvider {

    override fun getBitmap(tile: Tile, context: Context): Bitmap? {
        val data = tile.data
        if (data is String) {
            val unformattedFileName = tile.data as String
            val formattedFileName = String.format(unformattedFileName, tile.column, tile.row)
            try {
                return Picasso.with(context).load(formattedFileName)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .get()
            } catch (e: Exception) {
                //ignore
            }
        }
        return null
    }

}
