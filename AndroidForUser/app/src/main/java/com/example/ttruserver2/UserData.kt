package com.example.ttruserver2

class UserData {
    companion object{
        private val TAG = UserData::class.java.simpleName
        private var objectId: String? = null
        private var userId: String? = null
        private var name: String? = null
        private var address: String? = null
        private var lng: Double? = null
        private var lat: Double? = null

        fun setOid(id: String){ objectId = id }
        fun getOid() : String? { return objectId }
        fun setUid(id: String){ userId = id }
        fun getUid() : String? { return userId }
        fun setName(id: String){ name = id }
        fun getName() : String? { return name }
        fun setAddress(id: String){ address = id }
        fun getAddress() : String? { return address }
        fun setLng(id: Double){ lng = id }
        fun getLng() : Double? { return lng }
        fun setLat(id: Double){ lat = id }
        fun getLat() : Double? { return lat }
    }
}