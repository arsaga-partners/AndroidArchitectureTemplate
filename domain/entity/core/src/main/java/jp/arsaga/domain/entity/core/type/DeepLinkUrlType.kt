package jp.arsaga.domain.entity.core.type

import android.net.Uri

sealed class DeepLinkUrlType {

    abstract val uri: Uri

    companion object {
        fun resolver(uri: Uri): DeepLinkUrlType? =null
    }
}