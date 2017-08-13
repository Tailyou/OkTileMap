package com.hengda.zwf.oktilemap.entity

import java.io.Serializable
import java.lang.RuntimeException

/**
 * 展项基类
 * @author 祝文飞（Tailyou）
 * @time 2017/4/28 15:51
 */
open class BaseExhibit(var autoNo: Int, var locX: Double, var locY: Double, var fileNo: String, var name: String, var mapPicLg: String, var mapPicSm: String) : Cloneable, Serializable {

    public override fun clone(): BaseExhibit {
        val clone: BaseExhibit
        try {
            clone = super.clone() as BaseExhibit
        } catch (e: CloneNotSupportedException) {
            throw RuntimeException(e)  // won't happen
        }
        return clone
    }

}
