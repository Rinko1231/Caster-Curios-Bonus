package com.rinko1231.ccb.capability;

public class OverloadClientData {

    private static int overload;
    private static int silentTicks;
    private static int maxOverload;

    public static void setData(int o, int s, int max) {
        overload = o;
        silentTicks = s;
        maxOverload = max;
    }

    public static int getOverload() { return overload; }
    public static int getSilentTicks() { return silentTicks; }
    public static int getMaxOverload() { return maxOverload; }
}
