package com.ailicai.app.common.constants;


import com.ailicai.app.BuildConfig;

/**
 * Created by Ted on 2014/11/4.
 */
public class IWBuildConfig {
    public static String BuildRELEASE = "RELEASE";
    public static String BuildBeta = "BETA";
    public static String BuildDev = "DEV";
    public static String BuildDebug = "DEBUG";

    /*
        public static boolean isProduction() {
            return BuildConfig.BUILDVERSION.equalsIgnoreCase(BuildPro);
        }
    */
    public static boolean isProduction() {
        return BuildConfig.BUILDVERSION.equalsIgnoreCase(BuildRELEASE);
    }


    public static boolean isBeta() {
        return BuildConfig.BUILDVERSION.equalsIgnoreCase(BuildBeta);
    }

    public static boolean isDebug() {
        return BuildConfig.BUILDVERSION.equalsIgnoreCase(BuildDebug);
    }

    public static String getBuildVersion() {
        return BuildConfig.BUILDVERSION;
    }
}
