package io.ermdev.papershelf.rest

import io.ermdev.papershelf.exception.ResourceException
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class ResourceFinder {

    companion object {

        @Throws(ResourceException::class)
        fun getLocalFile(path: String): InputStream {
            try {
                return FileInputStream(File(path))
            } catch (e: Exception) {
                throw ResourceException("File is missing!")
            }
        }

        @Throws(ResourceException::class)
        fun getHostedFile(path: String): InputStream {
            val input = ResourceFinder::class.java.classLoader.getResourceAsStream(path)
            return input ?: throw ResourceException("File is missing!")
        }
    }
}