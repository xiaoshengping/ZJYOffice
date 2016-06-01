# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\ZHONGJIYUN03\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}



-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

#==================gson==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

#==================protobuf======================
-dontwarn com.google.**
-keep class com.google.protobuf.** {*;}


-dontwarn com.alibaba.fastjson.**




-keep class com.baidu.** { *; }
-keep class com.alibaba.fastjson.** { *; }
-keep class com.example.zhongjiyun03.zhongjiyun.view.** { *; }

-keepclassmembers class * {
public <methods>;
}
-keepattributes Signature

-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

#优化  不优化输入的类文件
-dontoptimize

-ignorewarnings
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#预校验
-dontpreverify

#混淆时是否记录日志
-verbose


# shareSDK分享
-keep class cn.sharesdk.**{*;}

-keep class com.sina.**{*;}

-keep class **.R$* {*;}

-keep class **.R{*;}

-keep class com.mob.**{*;}

-dontwarn com.mob.**

-dontwarn cn.sharesdk.**

-dontwarn **.R$*



# 保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
native <methods>;
}
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
public void *(android.view.View);
}
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}
