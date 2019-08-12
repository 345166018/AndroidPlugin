package com.hongx.plugin2;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class PluginManager {
    
    private static final PluginManager ourInstance = new PluginManager();

    private DexClassLoader dexClassLoader;

    private Resources resources;

    private PackageInfo packageInfo;
    
    public static PluginManager getInstance(){
        return ourInstance;
    }

    public PluginManager() {
    }
    
    public void loadPath(Context context){
        File filesDir = context.getDir("plugin", Context.MODE_PRIVATE);
        String name = "plugin.apk";
        String path = new File(filesDir, name).getAbsolutePath();

        PackageManager packageManager = context.getPackageManager();
        packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);

        //activity
        File dex = context.getDir("dex", Context.MODE_PRIVATE);
        dexClassLoader = new DexClassLoader(path, dex.getAbsolutePath(), null, context.getClassLoader());

        //resource
        try {
            AssetManager manager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPath.invoke(manager, path);
            resources = new Resources(manager,
                    context.getResources().getDisplayMetrics(),
                    context.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Resources getResources(){
        return  resources;
    }

    public DexClassLoader getDexClassLoader(){
        return dexClassLoader;
    }

    public PackageInfo getPackageInfo(){
        return packageInfo;
    }
}
