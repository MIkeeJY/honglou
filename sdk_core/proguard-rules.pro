# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Program Files\Android\sdk/tools/proguard/proguard-android.txt
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
#####################基本的配置################
#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 #优化  不优化输入的类文件
-dontoptimize
 #预校验
-dontpreverify
 #混淆时是否记录日志
-verbose
 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*

-renamesourcefileattribute lightsky
-keepattributes SourceFile,LineNumberTable,Exceptions

#如果有引用v4v7包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v7.app.AppCompatActivity
#保持哪些类不被混淆
#-keep public class * extends android.app.Fragment
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#忽略警告
-ignorewarning
#####################常用的配置-end################
#####################记录生成的日志数据,gradle build时在本项目根目录输出################
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
#####################记录生成的日志数据，gradle build时 在本项目根目录输出-end################

#####################常用的配置################

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
    public static <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
   public static <methods>;
}
#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
#避免混淆泛型 如果混淆报错建议关掉
#–keepattributes Signature
#####################常用的配置-end################

#####################保护自己项目代码################
-keep class simijkplayer.**{
    public <fields>;
    public <methods>;
}
-keep class tv.danmaku.ijk.media.**{
    <fields>;
    <methods>;
}

-keep class * extends java.lang.annotation.Annotation { *; }
-keep class com.lightsky.video.R$id {*;}

#####################保护自己项目代码-end################

#####################第三方架包不混淆################


#okhttputils
-dontwarn com.http.**
-keep class com.http.**{*;}
-keep interface com.http.**{*;}



-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}



#友盟分享
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes SourceFile,LineNumberTable



-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keepnames class * implements android.os.Parcelable {
public static final ** CREATOR;
}

-keepattributes Signature
#####################第三方架包不混淆-end################

-keep class com.lightsky.video.webview.JavascriptInterface {
    <methods>;
    <fields>;
}

-keep class com.lightsky.video.redpackets.webview.RedPacketJavascriptInterface {
    <methods>;
    <fields>;
}

#WebView 4.0以上和4.0以下 支持上传文件
-keep public class * extends android.webkit.WebChromeClient {
	public void openFileChooser(android.webkit.ValueCallback, java.lang.String, java.lang.String);
	public void openFileChooser(android.webkit.ValueCallback);
}

-keep class com.lightsky.video.widget.pulltorefresh.* { *; }
###############################友盟push start#################
-keepattributes *Annotation*


-keep public class **.R$*{
   public static final int *;
}

####聚合广告sdk#####
-keep class com.ak.android.** {*;}
-keep class android.support.v4.app.NotificationCompat**{
      public *;
}
-keep class com.qihoo.livecloud.** {
    <fields>;
    <methods>;
}

-keep class com.qihoo.videocloud.** {
    <fields>;
    <methods>;
}

-keep class net.qihoo.videocloud.** {
    <fields>;
    <methods>;
}

#okio
-dontwarn okio.**
-keep class okio.**{*;}
-keep interface okio.**{*;}


#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-keep interface okhttp3.**{*;}

-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

-keep class com.lightsky.utils.ToastUtil{ *;}
-keep class com.lightsky.video.VideoHelper { *;}
-keep class com.lightsky.video.VideoSetting{ *;}
-keep class com.lightsky.video.sdk.**{ *;}
-keep class com.lightsky.video.datamanager.category.CategoryQueryNotify{ *;}

-keep class javax.annotation.**{ *;}
-keep class java.lang.String{ *;}
-keep class android.os.build.**{ *;}
-keep class com.lightsky.video.BuildConfig{ *;}

-keep public interface com.facebook.**
-keep class com.facebook.**
-keep class com.facebook.** { *; }