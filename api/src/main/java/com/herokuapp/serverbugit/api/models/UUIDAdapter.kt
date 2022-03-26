package com.herokuapp.serverbugit.api.models

import com.squareup.moshi.*
import java.util.*

class UUIDAdapter:JsonAdapter<UUID>(){
    @FromJson
    override fun fromJson(reader: JsonReader): UUID? {
        return UUID.fromString(reader.readJsonValue().toString())
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: UUID?) {
        writer.jsonValue(value.toString())
    }
}
