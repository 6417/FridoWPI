package ch.fridolins.fridowpi.joystick;

public interface IJoystickFactory {
    /**
     * @param id Port where the joystick is connected
     * @return Instance of IJoystick
     */
    IJoystick create(IJoystickId id);
}
