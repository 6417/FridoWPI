package ch.fridolins.fridowpi;

import edu.wpi.first.wpilibj.DriverStation;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class Utils {
    public static void withRobotEnabled(Runnable function) {
        try (MockedStatic<DriverStation> utilities = Mockito.mockStatic(DriverStation.class)) {
            utilities.when(DriverStation::isEnabled).thenReturn(true);

            function.run();
        }
    }
}
