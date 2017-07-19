# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#----------------------xiaomi push----------------------
-keep class com.xiaomi.** {*;}
-dontwarn com.manyi.inthingsq.**
-keep class com.manyi.inthingsq.** {*;}
-keep class org.eclipse.paho.** {*;}
-keep class com.xiaomi.mipush.sdk.PushMessageReceiver {*;}
-keep public class * extends com.xiaomi.mipush.sdk.PushMessageReceiver

#----------------------huawei push----------------------
-dontwarn com.ailicai.app.common.push.bridge.**
-keep class com.ailicai.app.common.push.bridge.** {*;}
-keep public class * extends com.huawei.android.pushagent.api.PushMessageReceiver

-keep class com.huawei.android.pushagent.**{*;}
-keep class com.huawei.android.pushselfshow.**{*;}
-keep class com.huawei.android.microkernel.**{*;}

#-------------------------jpush----------------------
-dontoptimize
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
