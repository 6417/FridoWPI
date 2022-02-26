package ch.fridolins.fridowpi.utils;

public class LatchBooleanRising implements LatchedBoolean{
    private boolean previous;
    public LatchBooleanRising(boolean initial) {
        previous = initial;
    }

    @Override
    public boolean updateAndGet(boolean val) {
        boolean result = !previous && val;
        previous = val;
        return result;
    }
}
