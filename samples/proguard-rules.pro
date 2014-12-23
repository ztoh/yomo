# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/Cellar/android-sdk/23.0.2/tools/proguard/proguard-android.txt
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
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
#-keepclasseswithmembernames class * {
# native <methods>;
#}
#-keepclasseswithmembers class * {
# public <init>(android.content.Context, android.util.AttributeSet);
#}
#-keepclasseswithmembers class * {
# public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#-keepclassmembers class * extends android.app.Activity {
# public void *(android.view.View);
#}
#-keepclassmembers enum * {
# public static **[] values();
# public static ** valueOf(java.lang.String);
#}
#-keep class * implements android.os.Parcelable {
# public static final android.os.Parcelable$Creator *;
#}
##########################
### For robobinding
##########################
-keepattributes *Annotation*,Signature
-keep,allowobfuscation @interface org.robobinding.annotation.PresentationModel
-keep @org.robobinding.annotation.PresentationModel class * {
public *** *(...);
}
-keep class * implements org.robobinding.itempresentationmodel.ItemPresentationModel{
public *** *(...);
}
-keepclassmembers class * implements org.robobinding.viewattribute.ViewListeners {
public <init>(...);
}
-keep class * extends org.robobinding.presentationmodel.AbstractPresentationModelObject{
public <init>(...);
}
-keep class * extends org.robobinding.presentationmodel.AbstractItemPresentationModelObject{
public <init>(...);
}
#-dontwarn android.widget.AbsListView, android.view.View
-dontwarn javax.annotation.**
