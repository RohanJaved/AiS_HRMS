# For using GSON @Expose annotation

-keepattributes Signature
-keepattributes RuntimeVisibleAnnotations
-dontwarn com.google.gson.**


-keepclassmembers class com.your.package.models.** {
    public <init>();
}

-keepnames class com.appinsnap.aishrm.models.** { *; }


# Prevent Gson from stripping classes without default constructors
-keepclassmembers class * {
    public <init>();
}

-keepattributes *Annotation*


-keep class androidx.annotation.Keep
-keep @androidx.annotation.Keep class * { *; }


-keep class kotlin.Metadata { *; }
-keepclassmembers class com.appinsnap.aishrm.** {
    public <init>();
    <fields>;
}

# Keep fields annotated for Gson
-keepclassmembers class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

-keep class com.shockwave.**



-dontwarn retrofit2.**
-dontwarn okhttp3.**
-keep class com.google.gson.examples.android.model.* { *; }
-keep public class com.google.android.gms.* { public *; }
-keep class com.google.gson.examples.android.model.** { *; }
# Retrofit
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep class com.google.gson.** { *; }

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Required to preserve Kotlin metadata used in reflection
-keep class kotlin.Metadata { *; }

# Required to keep generic type info for Gson with TypeToken
-keep class * extends com.google.gson.reflect.TypeToken

##---------------End: proguard configuration for Gson  ----------

#-keep class com.JSBL.phoenixapp.** { *; }
-keep class com.google.code.gson.** { *; }
-keep class com.google.android.** { *; }
-keep class com.google.android.** { *; }
-keep class com.google.firebase.** { *; }
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-keep class com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model.RequestAttendanceInformation { *; }
-keepclassmembers class com.appinsnap.aishrm.ui.fragments.Policies.models.PDFInfo { *; }
-keep class com.appinsnap.aishrm.ui.fragments.Policies.models.PDFInfo { *; }
-keepclassmembers class com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model.RequestAttendanceInformation { *; }
-keep class com.appinsnap.aishrm.ui.fragments.leavesDetails.models.TodayModel { *; }
-keepclassmembers class com.appinsnap.aishrm.ui.fragments.leavesDetails.models.TodayModel { *; }
-keep class com.appinsnap.aishrm.ui.fragments.home.models.GetLeaveCounts { *; }
-keepclassmembers class com.appinsnap.aishrm.ui.fragments.home.models.GetLeaveCounts { *; }
-keepclassmembers class com.appinsnap.aishrm.ui.fragments.home.models.SettingApiResponse { *; }
-keep class com.appinsnap.aishrm.ui.fragments.home.models.SettingApiResponse { *; }
-keep class com.appinsnap.aishrm.ui.fragments.statics.model.StatisticAttendanceInfo { *; }
-keepclassmembers class com.appinsnap.aishrm.ui.fragments.statics.model.StatisticAttendanceInfo { *; }
-keep class com.appinsnap.aishrm.ui.fragments.employeeattendance.model.EmployeeAttendanceInfo { *; }
-keep class com.appinsnap.aishrm.ui.fragments.leavesDetails.models.TodayModel
-keep class com.appinsnap.aishrm.ui.fragments.EmployeeHistory.RequestResubmission
-keepclassmembers class com.appinsnap.aishrm.ui.fragments.employeeattendance.model.EmployeeAttendanceInfo { *; }
### implementation for camer library

-keep public class com.sandrios.** { *; }

-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-dontwarn android.support.**

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * implements java.lang.reflect.Type

