package akkalomator.strategygame.game.utils;

import akkalomator.strategygame.client.MoveEndedObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveEndedNotifierTest {

    private class TestMoveEndedObserver implements MoveEndedObserver {
        boolean eventOccurred = false;

        @Override
        public void onMoveEnded() {
            eventOccurred = true;
        }

    }

    private static MoveEndedNotifier notifier;

    private List<TestMoveEndedObserver> observers = List.of(
        new TestMoveEndedObserver(),
        new TestMoveEndedObserver(),
        new TestMoveEndedObserver()
    );

    @BeforeAll
    static void init() {
        notifier = new MoveEndedNotifier();
    }

    @Test
    void attachObserver_ThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> notifier.attachObserver(null));
    }

    @Test
    void attachObserver_WorksCorrectly() {
        notifier.attachObserver(observers.get(0));
        assertEquals(observers.get(0), notifier.observers.get(0));

        notifier.attachObserver(observers.get(1));
        assertEquals(observers.get(0), notifier.observers.get(0));
        assertEquals(observers.get(1), notifier.observers.get(1));
    }

    @Test
    void notifyAllObservers_WorksCorrectly() {
        notifier.attachObserver(observers.get(0));
        notifier.attachObserver(observers.get(1));
        assertFalse(observers.get(0).eventOccurred);
        assertFalse(observers.get(1).eventOccurred);

        notifier.notifyAllObservers();

        assertTrue(observers.get(0).eventOccurred);
        assertTrue(observers.get(1).eventOccurred);
        assertFalse(observers.get(2).eventOccurred);

        notifier.attachObserver(observers.get(2));
        notifier.notifyAllObservers();

        assertTrue(observers.get(0).eventOccurred);
        assertTrue(observers.get(1).eventOccurred);
        assertTrue(observers.get(2).eventOccurred);
    }

    @Test
    void notifyAfter_ThrowsOnMillisLessThanZero() {
        assertThrows(IllegalArgumentException.class, () -> notifier.notifyAfter(-1));
    }

    @Test
    void notifyAfter_ThrowsWhenTimerIsAlreadyStarted() {
        notifier.notifyAfter(3000);
        assertThrows(IllegalStateException.class, () -> notifier.notifyAfter(3000));
    }

    @Test
    void cancelNotification_DoesNotThrowWhenAlreadyStopped() {
        assertFalse(notifier.isTimerStarted());
        assertDoesNotThrow(() -> notifier.cancelNotification());
        assertFalse(notifier.isTimerStarted());


        notifier.notifyAfter(3000);
        assertTrue(notifier.isTimerStarted());
        notifier.cancelNotification();
        assertFalse(notifier.isTimerStarted());

        assertDoesNotThrow(() -> notifier.cancelNotification());
        assertFalse(notifier.isTimerStarted());
    }

    @AfterEach
    void reset() {
        notifier.clear();
        notifier.cancelNotification();
    }
}