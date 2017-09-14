package com.tailyou.tilemap.bean

import com.tailyou.oktilemap.entity.BaseExhibit
import com.tailyou.tilemap.app.AppConfig
import org.jetbrains.anko.db.RowParser
import java.util.*

//展项
data class Exhibit(var autoNo: Int, var fileNo: String, var name: String,
                   var locX: Double, var locY: Double, var mapPicLg: String, var mapPicSm: String) {
    companion object {
        val PARSER: RowParser<Exhibit> = object : RowParser<Exhibit> {
            override fun parseRow(columns: Array<Any?>): Exhibit {
                val autoNo = columns[1] as Long
                val fileNo = String.format("%04d", columns[0])
                val name = columns[4] as String
                val locX = columns[5] as Long
                val locY = columns[6] as Long
                val mapPicLg = "${AppConfig.defaultFileDir}exhibit/$fileNo/image/map_icon.png"
                val mapPicSm = "${AppConfig.defaultFileDir}exhibit/$fileNo/image/map_icon2.png"
                val exhibit = Exhibit(autoNo.toInt(), fileNo, name, locX.toDouble(), locY.toDouble(), mapPicLg, mapPicSm)
                return exhibit
            }
        }

        fun exhibit2BaseExhibit(exhibit: Exhibit): BaseExhibit {
            return BaseExhibit(Random().nextInt(), exhibit.fileNo, exhibit.autoNo, exhibit.name,
                    exhibit.locX, exhibit.locY, exhibit.mapPicLg, exhibit.mapPicSm)
        }
    }
}
