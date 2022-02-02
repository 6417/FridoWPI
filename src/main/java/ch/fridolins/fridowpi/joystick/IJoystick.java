package ch.fridolins.fridowpi.joystick;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;

public interface IJoystick {
    /**
     * @param id id of the button to return
     * @return instance of the button at the given id
     */
    Button getButton(IJoystickButtonId id);

    /**
     * Get the X value of the joystick. This depends on the mapping of the joystick connected to the
     * current port.
     *
     * @return The X value of the joystick.
     */
    double getX();

    /**
     * Get the Y value of the joystick. This depends on the mapping of the joystick connected to the
     * current port.
     *
     * @return The Y value of the joystick.
     */
    double getY();

    /**
     * Get the z position of the HID.
     *
     * @return the z position
     */
    double getZ();

    /**
     * Get the twist value of the current joystick. This depends on the mapping of the joystick
     * connected to the current port.
     *
     * @return The Twist value of the joystick.
     */
    double getTwist();

    /**
     * Get the throttle value of the current joystick. This depends on the mapping of the joystick
     * connected to the current port.
     *
     * @return The Throttle value of the joystick.
     */
    double getThrottle();

    /**
     * Read the state of the trigger on the joystick.
     *
     * @return The state of the trigger.
     */
    boolean getTrigger();

    /**
     * Whether the trigger was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    boolean getTriggerPressed();

    /**
     * Whether the trigger was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    boolean getTriggerReleased();

    /**
     * Read the state of the top button on the joystick.
     *
     * @return The state of the top button.
     */
    boolean getTop();

    /**
     * Whether the top button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    boolean getTopPressed();

    /**
     * Whether the top button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    boolean getTopReleased();

    /**
     * Get the magnitude of the direction vector formed by the joystick's current position relative to
     * its origin.
     *
     * @return The magnitude of the direction vector
     */
    double getMagnitude();

    /**
     * Get the direction of the vector formed by the joystick and its origin in radians.
     *
     * @return The direction of the vector in radians
     */
    double getDirectionRadians();

    /**
     * Read the state of the top button on the joystick.
     *
     * @return The state of the top button.
     */
    double getDirectionDegrees();
}