package frc.Utils;

public interface IPos{
    String getName();
    double getPos();

    public enum ArmPosID implements IPos
    {
        //dummy inch values
        STW(0.0, "stow"),
        LOW(1.0, "low"),
        MCN(2.0, "mid cone"),
        MCB(3.0, "mid cube"),
        TCN(4.0, "top cone"),
        TCB(5.0, "top cube"),
        NON(0.0, "null");
        
        public final double pos;
        public final String name;
        private ArmPosID(double pos, String name){
            this.pos = pos;
            this.name = name;
        }

        @Override
        public String getName(){
            return name;
        }

        @Override
        public double getPos(){
            return pos;
        }
    }

    public enum ElevatorPosID implements IPos
    {
        //dummy inch values
        TOP(4.0, "stow"),
        LOW(0.0, "low"),
        MID(3.0, "mid cone"),
        STW(2.0, "mid cube"),
        PKU(1.0, "top cone");
        
        public final double pos;
        public final String name;
        private ElevatorPosID(double pos, String name){
            this.pos = pos;
            this.name = name;
        }
        
        @Override
        public String getName(){
            return name;
        }

        @Override
        public double getPos(){
            return pos;
        }
    }

    public enum IntakePosID implements IPos
    {
        //dummy
        STW(0.0, "stow"),
        POS1(1.0, "dummy position 1"),
        POS2(2.0, "dummy position 2"),
        POS3(3.0, "dummy position 3");
        
        public final double pos;
        public final String name;
        private IntakePosID(double pos, String name){
            this.pos = pos;
            this.name = name;
        }

        @Override
        public String getName(){
            return name;
        }

        @Override
        public double getPos(){
            return 0.0;
        }
    }

    public enum General implements IPos
    {
        NULL("NULL");

        private String name;
        private General(String name){
            this.name = name;
        }

        @Override
        public String getName(){
            return name;
        }

        @Override
        public double getPos(){
            return 0.0;
        }
    }
}