package akkalomator.strategygame.game.utils;

import akkalomator.strategygame.client.MoveEndedObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MoveEndedNotifier {

    List<MoveEndedObserver> observers;

    private Timer timer;

    private boolean isTimerStarted;

    public MoveEndedNotifier() {
        observers = new ArrayList<>();
        isTimerStarted = false;
    }

    public void attachObserver(MoveEndedObserver observer) {

        if (observer == null) {
            throw new NullPointerException("MoveEndedObserver cannot be null");
        }
        observers.add(observer);
    }

    public void notifyAllObservers() {
        for (MoveEndedObserver observer : observers) {
            observer.onMoveEnded();
        }
    }

    public void notifyAfter(long millis) {

        if (isTimerStarted) {
            throw new IllegalStateException("Notification is already scheduled");
        }

        if (millis < 0) {
            throw new IllegalArgumentException("Millis must be non less than zero");
        }

        isTimerStarted = true;
        timer = new Timer(true);
        timer.schedule(
            new TimerTask() {
                @Override
                public void run() {
                    notifyAllObservers();
                }
            },
            millis
        );
    }

    public void cancelNotification() {
        if (timer != null) {
            timer.cancel();
        }
        isTimerStarted = false;
    }

    public boolean isTimerStarted() {
        return isTimerStarted;
    }

    void clear() {
        observers.clear();
    }
}
