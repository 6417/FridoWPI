package ch.fridolins.fridowpi.joystick.joysticks;

import ch.fridolins.fridowpi.joystick.IJoystickButtonId;

public enum Logitech implements IJoystickButtonId {
    x(0);


    private final int buttonId;

    private Logitech(int id) {
        buttonId = id;
    }

    @Override
    public int getButtonId() {
        return buttonId;
    }
}
