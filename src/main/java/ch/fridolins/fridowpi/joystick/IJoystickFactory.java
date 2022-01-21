package ch.fridolins.fridowpi.joystick;

public interface IJoystickFactory {
    IJoystick create(IJoystickId id);
}
