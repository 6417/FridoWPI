package ch.fridolins.fridowpi.utils;

public class LatchedBooleanBoth implements LatchedBoolean {
    private boolean previous;
    public LatchedBooleanBoth(boolean initial) {
        previous = initial;
    }

    @Override
    public boolean updateAndGet(boolean val) {
        boolean result = previous != val;
        previous = val;
        return result;
    }
}
