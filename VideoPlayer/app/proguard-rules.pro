# Room database rules
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Media3 rules
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# Coroutines and serialization
-keepattributes *Annotation*,InnerClasses,EnclosingMethod
-keepclassmembers class * {
    @androidx.room.* *;
}
