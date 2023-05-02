package koyami.nekochan.util;

import java.util.Timer;
import java.util.TimerTask;

public class MyTimer extends Timer {
    long delay;
    long period;
    TimerTask task;
    public MyTimer(int delay, long period) {
        this.delay = delay;
        this.period = period;
        this.task = null;
    }

    public void runAtScheduleTimeOnInitiation(TimerTask task) {
        this.task = task;
        scheduleAtFixedRate(task, delay, period);
    }

    public long getDelay() {
        return delay;
    }

    public long getPeriod() {
        return period;
    }

    public TimerTask getTask() {
        return task;
    }
}
