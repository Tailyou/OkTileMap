package com.tailyou.oktilemap.entity

import java.io.Serializable
import java.lang.RuntimeException

//展项基类
open class BaseExhibit(open var autoNo: Int, open var fileNo: String, open var name: String,
                       open var locX: Double, open var locY: Double, open var mapPicLg: String,
                       open var mapPicSm: String) : Cloneable, Serializable {

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
