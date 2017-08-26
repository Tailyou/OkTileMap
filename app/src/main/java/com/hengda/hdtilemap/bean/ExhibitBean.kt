package com.hengda.hdtilemap.bean

import com.hengda.hdtilemap.app.AppConfig
import com.hengda.zwf.oktilemap.entity.BaseExhibit
import org.jetbrains.anko.db.RowParser

/**
 * 作者：Tailyou
 * 时间：2016/3/7 09:06
 * 邮箱：tailyou@163.com
 * 描述：展项
 */

//展项
class ExhibitBean(autoNo: Int, fileNo: String, name: String, locX: Double, locY: Double, mapPicLg: String, mapPicSm: String) : BaseExhibit(autoNo, fileNo, name, locX, locY, mapPicLg, mapPicSm) {
    companion object {
        val PARSER: RowParser<ExhibitBean> = object : RowParser<ExhibitBean> {
            override fun parseRow(columns: Array<Any?>): ExhibitBean {
                val autoNo = columns[1] as Long
                val fileNo = String.format("%04d", columns[0])
                val name = columns[4] as String
                val locX = columns[5] as Long
                val locY = columns[6] as Long
                val mapPicLg = "${AppConfig.defaultFileDir}exhibit/$fileNo/image/map_icon.png"
                val mapPicSm = "${AppConfig.defaultFileDir}exhibit/$fileNo/image/map_icon2.png"
                val exhibit = ExhibitBean(autoNo.toInt(), fileNo, name, locX.toDouble(), locY.toDouble(), mapPicLg, mapPicSm)
                return exhibit
            }
        }
    }
}
