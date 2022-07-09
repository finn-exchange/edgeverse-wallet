package com.edgeverse.wallet.common.interfaces

import android.net.Uri
import java.io.File

interface FileProvider {

    suspend fun getFileInExternalCacheStorage(fileName: String): File

    suspend fun getFileInInternalCacheStorage(fileName: String): File

    suspend fun generateTempFile(fixedName: String? = null): File

    suspend fun uriOf(file: File): Uri
}
