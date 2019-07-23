package com.github.predasorinionut.currates.common

import android.content.Context
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class GlideAppConfigModule : AppGlideModule() {
    override fun isManifestParsingEnabled() = false

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setMemoryCache(LruResourceCache(1024 * 1024 * 20))
        builder.setLogLevel(Log.VERBOSE)
    }
}