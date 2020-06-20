package com.example.ttruserver2.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class OriginMenuModel(val originMenuTitle: String?, val originMenuPrice: Int) : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(originMenuTitle)
        parcel.writeInt(originMenuPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OriginMenuModel> {
        override fun createFromParcel(parcel: Parcel): OriginMenuModel {
            return OriginMenuModel(parcel)
        }

        override fun newArray(size: Int): Array<OriginMenuModel?> {
            return arrayOfNulls(size)
        }
    }
}