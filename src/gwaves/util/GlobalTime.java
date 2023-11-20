package gwaves.util;

public class GlobalTime {
    private static int globaltime = 0;

    private GlobalTime() {}

    public void setTime(int timestamp)
    {
        GlobalTime.globaltime = timestamp;
    }

    public int getTime()
    {
        return GlobalTime.globaltime;
    }
}
