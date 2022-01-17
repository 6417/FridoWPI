package ch.fridolins.fridowpi.joystick;

public class Joysticks implements IJoystickId{
   private final int port;

   public Joysticks(int port) {
        this.port = port;
   }

    @Override
    public int getPort() {
        return port;
    }

    public class Logitech implements IJoystickButtonId {
//       X(1), Y(2);

        private final int buttonId;

        private Logitech(int port) {
            this.buttonId = port;
        }

       @Override
       public int getButtonId() {
           return buttonId;
       }

       @Override
       public IJoystickId getJoystickId() {
           return Joysticks.this;
       }
   }
}
