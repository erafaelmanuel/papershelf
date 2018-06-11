package io.ermdev.papershelf.rest

import java.sql.Timestamp

class Message(var timestamp: Timestamp = Timestamp(System.currentTimeMillis()),
              var status: Int = 0,
              var error: String = "",
              var message: String = "") {

    fun toJson(): String {
        return "{\n" +
                "    \"timestamp\": \"$timestamp\",\n" +
                "    \"status\": $status,\n" +
                "    \"error\": \"$error\",\n" +
                "    \"message\": \"$message\"\n" +
                "}"
    }
}