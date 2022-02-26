package ch.fridolins.fridowpi.utils;

public class LatchedBooleanFalling implements LatchedBoolean{
    private boolean previous;
    public LatchedBooleanFalling(boolean initial) {
        previous = initial;
    }

    @Override
    public boolean updateAndGet(boolean val) {
        boolean result = previous && !val;
        previous = val;
        return result;
    }
}
