package com.google.android.gms.nearby.messages

import com.google.android.gms.common.internal.Preconditions
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.google.android.gms.nearby.messages.BetterStrategy
import java.lang.IllegalStateException
import javax.annotation.concurrent.Immutable

@Immutable
@SafeParcelable.Class(creator = "StrategyCreator")
object BetterStrategy {
    const val DISCOVERY_MEDIUM_DEFAULT = -1
    const val DISCOVERY_MEDIUM_BLE = 2
    const val DISCOVERY_MEDIUM_AUDIO = 4

    class Builder {
        private var discoveryMode = Strategy.DISCOVERY_MODE_DEFAULT
        private var ttlSeconds = 300
        private var distanceType = Strategy.DISTANCE_TYPE_DEFAULT
        private var discoveryMedium = DISCOVERY_MEDIUM_DEFAULT
        private val zzfy = 0
        fun setDiscoveryMode(var1: Int): Builder {
            discoveryMode = var1
            return this
        }

        fun setDiscoveryMedium(var1: Int): Builder {
            discoveryMedium = var1
            return this
        }

        fun setTtlSeconds(var1: Int): Builder {
            Preconditions.checkArgument(
                var1 == 2147483647 || var1 > 0 && var1 <= 86400,
                "mTtlSeconds(%d) must either be TTL_SECONDS_INFINITE, or it must be between 1 and TTL_SECONDS_MAX(%d) inclusive",
                *arrayOf<Any>(var1, 86400)
            )
            ttlSeconds = var1
            return this
        }

        fun setDistanceType(var1: Int): Builder {
            distanceType = var1
            return this
        }

        fun build(): Strategy {
            return if (discoveryMedium == DISCOVERY_MEDIUM_BLE && distanceType == Strategy.DISTANCE_TYPE_EARSHOT) {
                throw IllegalStateException("Cannot set EARSHOT with BLE only mode.")
            } else {
                Strategy(
                    2,
                    0,
                    ttlSeconds,
                    distanceType,
                    false,
                    discoveryMedium,
                    discoveryMode,
                    0
                )
            }
        }
    }
}