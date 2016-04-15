# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\xiaoshengping\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
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

#-dontpreverify
#-verbose
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
##/*------------------------------需要修改的地方-------------------------------*/
##/*以下libraryjars。。。。为消除第三方的包被混淆*/
#-libraryjars libs/androidpn-client.jar
#-libraryjars ../Libcommon/libs/android-support-v4.jar
##/*------------------------------需要修改的地方，这部分要根据自己的项目修改-------------------------------*/
#
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class com.android.vending.licensing.ILicensingService
#
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#
#-keepclassmembers class * extends android.app.Activity {
#   public void *(android.view.View);
#}
#
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#
##/*------------------------------需要修改的地方-------------------------------*/
##/*以下这一句为消除v4包被混淆*/
#-keep class android.support.v4.** { *; }
#
##/*以下libraryjars。。。。为消除第三方的包被混淆，在相应的jar包下的包名*/
#-dontwarn org.androidpn.client.**
#-dontwarn com.j256.ormlite.**
#-dontwarn com.novell.sasl.**
#-dontwarn de.measite.**
#
##/*------------------------------需要修改的地方-------------------------------*/

# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html






#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-verbose
#
## Optimization is turned off by default. Dex does not like code run
## through the ProGuard optimize and preverify steps (and performs some
## of these optimizations on its own).
#-dontoptimize
#-dontpreverify
#
#-libraryjars libs/fastjson-1.1.45.android.jar
#
## Note that if you want to enable optimization, you cannot just
## include optimization flags in your own project configuration file;
## instead you will need to point to the
## "proguard-android-optimize.txt" file instead of this one from your
## project.properties file.
#
#-keepattributes *Annotation*
#-keep public class com.google.vending.licensing.ILicensingService
#-keep public class com.android.vending.licensing.ILicensingService
#
## For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#
## keep setters in Views so that animations can still work.
## see http://proguard.sourceforge.net/manual/examples.html#beans
#-keepclassmembers public class * extends android.view.View {
#   void set*(***);
#   *** get*();
#}
#
## We want to keep methods in Activity that could be used in the XML attribute onClick
#-keepclassmembers class * extends android.app.Activity {
#   public void *(android.view.View);
#}
#
## For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#
#-keepclassmembers class **.R$* {
#    public static <fields>;
#}
#
## The support library contains references to newer platform versions.
## Don't warn about those in case this app is linking against an older
## platform version.  We know about them, and they are safe.
#-dontwarn android.support.**
