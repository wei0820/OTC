# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Volumes/Work/androidIDE/sdk/tools/proguard/proguard-android.txt
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

# 代码混淆压缩比，在0~7之间
 -optimizationpasses 5

 # 混合时不使用大小写混合，混合后的类名为小写
 -dontusemixedcaseclassnames

 # 指定不去忽略非公共库的类
 -dontskipnonpubliclibraryclasses

 # 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
 -dontpreverify

 # 避免混淆Annotation、内部类、泛型、匿名类
# -keepattributes *Annotation*,InnerClasses,Signature,EnclosingMethod

 # 重命名抛出异常时的文件名称
 -renamesourcefileattribute SourceFile

 # 抛出异常时保留代码行号
 -keepattributes SourceFile,LineNumberTable

# ===============

# 抑制警告（沒有啟用會編譯不過，Java compiler warnings）
-ignorewarnings

-assumenosideeffects class android.util.Log{
   public static boolean isLoggable(java.lang.String,int);
   public static int v(...);
   public static int i(...);
   public static int w(...);
   public static int d(...);
   public static int e(...);
}
