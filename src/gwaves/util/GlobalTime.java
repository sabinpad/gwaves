package gwaves.util;

public class GlobalTime {
    private static int globaltime = 0;

    private GlobalTime() {}

    public static void setTime(int timestamp)
    {
        GlobalTime.globaltime = timestamp;
    }

    public static int getTime()
    {
        return GlobalTime.globaltime;
    }
}
