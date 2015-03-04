package com.mvgv70.mtcmusic_usbmount;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.app.Activity;

public class Main implements IXposedHookLoadPackage {
	
  private static final String TAG = "mtcmusicusbmount";
  
  @Override
  public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
    if (!lpparam.packageName.equals("com.microntek.music")) return;
    Log.d(TAG,"com.microntek.music");
    // onCreate
    XC_MethodHook onCreate = new XC_MethodHook() {
           
      @Override
      protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Log.d(TAG,"onCreate()");
      	BroadcastReceiver usbReceiver = (BroadcastReceiver)XposedHelpers.getObjectField(param.thisObject, "UsbCardBroadCastReceiver");
      	if (usbReceiver != null)
      	{
      	  // выключаем receiver на монтирование флешки
      	  ((Activity)param.thisObject).unregisterReceiver(usbReceiver);
      	  // заменяем его другим, который никогда не вызывается
      	  ((Activity)param.thisObject).registerReceiver(usbReceiver, new IntentFilter());
      	  Log.d(TAG,"UsbCardBroadCastReceiver changed");
      	}
      }
    };
    findAndHookMethod("com.microntek.music.MusicActivity", lpparam.classLoader, "onCreate", Bundle.class, onCreate);
    // OK
    Log.d(TAG,"com.microntek.music.MusicActivity hook OK");
  }
  
}
	