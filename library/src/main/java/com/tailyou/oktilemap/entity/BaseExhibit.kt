package com.tailyou.oktilemap.entity

import java.io.Serializable
import java.lang.RuntimeException

//展项基类
open class BaseExhibit(var autoNo: Int, var fileNo: String, var name: String,
                       var locX: Double, var locY: Double,
                       var mapPicLg: String, var mapPicSm: String) : Cloneable, Serializable {

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