package com.tailyou.oktilemap.util

import com.tailyou.oktilemap.entity.BaseExhibit
import com.tailyou.oktilemap.entity.Location
import java.util.*

object MergeUtil {

    /**
     * 聚合
     * @param exhibits
     * @param distance
     */
    fun merge(exhibits: MutableList<BaseExhibit>, distance: Float): List<List<BaseExhibit>> {
        val mergeList = ArrayList<List<BaseExhibit>>()
        var iterator: MutableIterator<BaseExhibit> = exhibits.iterator()
        while (iterator.hasNext()) {
            val temp = ArrayList<BaseExhibit>()
            val reference = iterator.next()
            temp.add(reference)
            iterator.remove()
            while (iterator.hasNext()) {
                val exhibit = iterator.next()
                if (isRelate(reference, exhibit, distance)) {
                    temp.add(exhibit)
                    iterator.remove()
                }
            }
            mergeList.add(temp)
            iterator = exhibits.iterator()
        }
        return mergeList
    }

    /**
     * 判断是否相关
     * @param reference 参照展项
     * @param exhibit   比较展项
     * @param distance
     * @return
     */
    fun isRelate(reference: BaseExhibit, exhibit: BaseExhibit, distance: Float): Boolean {
        val gapX = reference.locX - exhibit.locX
        val gapY = reference.locY - exhibit.locY
        val v = gapX * gapX + gapY * gapY
        val sqrt = Math.sqrt(v)
        return sqrt < distance
    }

    /**
     * 计算平均坐标
     * @param list
     * @return
     */
    fun calAverCoords(list: List<BaseExhibit>): Location {
        var locXSum = 0.0
        var locYSum = 0.0
        for (baseExhibit in list) {
            locXSum += baseExhibit.locX
            locYSum += baseExhibit.locY
        }
        return Location(locXSum / list.size, locYSum / list.size)
    }

    /**
     * 集合深拷贝
     * @param list
     * @return
     */
    fun copy(list: MutableList<BaseExhibit>): MutableList<BaseExhibit> {
        val copy = ArrayList<BaseExhibit>()
        val iterator = list.iterator()
        while (iterator.hasNext()) {
            copy.add(iterator.next().clone())
        }
        return copy
    }

}
