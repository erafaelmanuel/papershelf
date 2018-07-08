package com.rem.papershelf.rest

import java.sql.Timestamp

class Message(var timestamp: Timestamp = Timestamp(System.currentTimeMillis()),
              var status: Int = 0,
              var error: String = "",
              var message: String = "")