package com.dfinn.wallet.test_shared

import com.dfinn.wallet.common.data.storage.encrypt.EncryptedPreferences

class HashMapEncryptedPreferences : EncryptedPreferences {
    private val delegate = mutableMapOf<String, String>()

    override fun putEncryptedString(field: String, value: String) {
        delegate[field] = value
    }

    override fun getDecryptedString(field: String): String? = delegate[field]

    override fun hasKey(field: String): Boolean = field in delegate

    override fun removeKey(field: String) {
        delegate.remove(field)
    }
}
