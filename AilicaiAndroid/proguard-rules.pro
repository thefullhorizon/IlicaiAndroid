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

-dontwarn com.tendcloud.**
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-allowaccessmodification
-keepattributes *Annotation*
-keepattributes Signature
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-repackageclasses ''
-ignorewarnings

-libraryjars ../AilicaiFramework/libs/alipaySDK-20150724.jar

##--------------ormsqlite--------------------
-dontwarn com.j256.**
-dontwarn com.j256.ormlite.**
-dontwarn com.j256.ormlite.android.**
-dontwarn com.j256.ormlite.field.**
-dontwarn com.j256.ormlite.stmt.**
-keep public class * extends com.j256.ormlite.**
-keep public class * extends com.j256.ormlite.android.**
-keep public class * extends com.j256.ormlite.field.**
-keep public class * extends com.j256.ormlite.stmt.**
-keep public class * extends com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
-keep public class * extends com.j256.ormlite.android.apptools.OpenHelperManager
-keep class com.j256.ormlite.** { *; }
-keep class com.j256.ormlite.android.** { *; }
-keep class com.j256.ormlite.field.** { *; }
-keep class com.j256.ormlite.stmt.** { *; }
-keep class org.apache.**{*;}
-keepclassmembers class * {
    @com.j256.ormlite.field.DatabaseField *;
}
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

-dontwarn com.taobao.**
-keep class com.taobao.**
-keep interface com.taobao.**

-keep public class com.nostra13.universalimageloader.**{*;}
-keepclassmembers class com.nostra13.universalimageloader.** {*;}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.google.vending.licensing.ILicensingService
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
public void *(android.view.View);
}
##------------android--------------

##----------------------Serializable-----------------
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    public <fields>;
}
-keep public class * implements java.io.Serializable {*;}
##----------------------Serializable-----------------
##-------------------jackson---------------------------
-dontwarn com.fasterxml.jackson.**
-keep class com.fasterxml.jackson.** { *;}
-keep class com.fasterxml.jackson.annotation.** { *;}
-dontwarn com.fasterxml.jackson.annotation.**
-keep class com.fasterxml.jackson.core.** { *;}
-dontwarn com.fasterxml.jackson.core.**
-keep class com.fasterxml.jackson.databind.** { *;}
-dontwarn com.fasterxml.jackson.databind.**
##-------------------jackson---------------------------

##-------------------support.v4---------------------------
-keep public class * extends android.support.v4.app.Fragment
-keep class android.support.v4.** { *;}
-keep public class * extends android.support.v4.**
-dontwarn android.support.v4.**
##-------------------support.v4---------------------------

##----------------volley----------------
-keep class com.android.volley.** { *;}
-dontwarn com.android.volley.**
##----------------volley----------------

##----------------acra----------------
-keep class org.arca.** { *;}
-dontwarn org.arca.**
##----------------acra----------------
-keep class org.androidannotations.** { *;}
-dontwarn org.androidannotations.**

-dontwarn com.umeng.**
-keep class com.umeng.** { *;}
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**
-keep class com.facebook.**
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-dontwarn com.tencent.mm.**
-keep class com.tencent.mm.** { *;}

-keep public class com.ailicai.app.R$*{
public static final int *;
}

-keep class org.slf4j.** {*;}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep class sun.misc.Unsafe.** { *; }
-keep class com.alibaba.fastjson.** { *; }

-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}

-keep class com.hp.hpl.sparta.** { *;}
-dontwarn com.hp.hpl.sparta.**

-keep class org.apache.** { *;}
-dontwarn org.apache.**

-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}
-keepclasseswithmembernames class * {
	native <methods>;
}

-keep class com.daimajia.** { *;}
-dontwarn com.daimajia.**

-dontwarn com.ailicai.app.db.DBOpenHelper
-keep class com.ailicai.app.db.DBOpenHelper { *;}
-dontwarn com.huoqiu.framework.rest.**
-keep class com.huoqiu.framework.rest.** { *;}
-keep class * extends com.huoqiu.framework.rest.** {*;}
-dontwarn com.huoqiu.framework.encrypt.**
-keep class com.huoqiu.framework.encrypt.** { *;}
-keep class com.ailicai.app.common.hybrid.utils.DownloadVersion$DownInfo { *;}

-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.tencent.weibo.sdk.**

-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

-dontwarn org.mockito.**
-keep class org.mockito.** {*;}
-dontwarn org.junit.**
-keep class org.junit.** {*;}
-dontwarn java.beans.**
-keep class java.beans.** {*;}
-dontwarn org.objenesis.instantiator.sun.**
-keep class org.objenesis.instantiator.sun.** {*;}

-dontwarn org.codehaus.mojo.**
-keep class org.codehaus.mojo.** {*;}

-dontwarn java.nio.file.**
-keep class java.nio.file.** {*;}

-keep class * extends com.huoqiu.framework.commhttp.JsonHttpResponseListener {*;}
-keep class com.huoqiu.framework.commhttp.JsonHttpResponseListener {*;}
-keep class com.alibaba.fastjson.** { *; }

-keep class com.manyi.lovehouse.reqaction.IwjwRespListener {*;}
-keep class com.ailicai.app.model.** {*;}
-keep class * extends java.util.Observable {*;}
-keep class com.manyi.lovehouse.wxapi.WXEntryActivity {*;}
-keep class com.manyi.lovehouse.wxapi.WXPayEntryActivity {*;}

-keep class com.parse.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.apache.** {*;}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#realm
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class * { *; }
-dontwarn javax.**
-dontwarn io.realm.**

-keep public class com.yintong.pay.utils.** {
    <fields>;
    <methods>;
}
-keep class com.yintong.secure.activityproxy.PayIntro$LLJavascriptInterface{*;}
-keep public class com.yintong.** {
    <fields>;
    <methods>;
}
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-keep class com.drew.** {*;}
-keep interface com.drew.** {*;}
-keep enum com.drew.** {*;}

-dontwarn tv.danmaku.ijk.media.player.**
-keep class tv.danmaku.ijk.media.player.** {*;}
-keep interface tv.danmaku.ijk.media.player.* {*;}
-keep class net.sourceforge.zbar.** { *; }


#oneAPM
-keep class org.apache.http.impl.client.**
-dontwarn org.apache.commons.**
-keep class com.blueware.** { *; }
-dontwarn com.blueware.**
-keep class com.oneapm.** {*;}
-dontwarn com.oneapm.**
-keepattributes Exceptions, Signature, InnerClasses
-keepattributes SourceFile, LineNumberTable

#-------RocooFix--------
-keep class com.dodola.** {*;}
-keep class com.lody.legend.* {*;}

# okhttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# okio

-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

# ----------------growingio------------
-keep class com.growingio.android.sdk.** {
 *;
}
-dontwarn com.growingio.android.sdk.**
-keepnames class * extends android.view.View

-keep class * extends android.app.Fragment {
 public void setUserVisibleHint(boolean);
 public void onHiddenChanged(boolean);
 public void onResume();
 public void onPause();
}
-keep class android.support.v4.app.Fragment {
 public void setUserVisibleHint(boolean);
 public void onHiddenChanged(boolean);
 public void onResume();
 public void onPause();
}
-keep class * extends android.support.v4.app.Fragment {
 public void setUserVisibleHint(boolean);
 public void onHiddenChanged(boolean);
 public void onResume();
 public void onPause();
}
#---------------------TalkingData----------------------
-keep class com.talkingdata.sdk.** {*;}
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.** {  public protected *;}

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

#-------------------------realm----------------------
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class * { *; }
-dontwarn javax.**
-dontwarn io.realm.**

#------------------growingio-----------------------
-keep class com.growingio.android.sdk.** {
    *;
}
-dontwarn com.growingio.android.sdk.**
-keepnames class * extends android.view.View
-keep class * extends android.app.Fragment {
    public void setUserVisibleHint(boolean);
    public void onHiddenChanged(boolean);
    public void onResume();
    public void onPause();
}
-keep class android.support.v4.app.Fragment {
    public void setUserVisibleHint(boolean);
    public void onHiddenChanged(boolean);
    public void onResume();
    public void onPause();
}
-keep class * extends android.support.v4.app.Fragment {
    public void setUserVisibleHint(boolean);
    public void onHiddenChanged(boolean);
    public void onResume();
    public void onPause();
}
