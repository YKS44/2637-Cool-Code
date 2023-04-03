package frc.Utils;

public class MotorTrajectory{
    private double initPos;
    private double decelInitTime;
    private double maxSpeed;
    private double accel;
    private double maxTime;

    /**
     * positive distance, positive speed, negative acceleration
     * <p>
     * or:
     * <p>
     * negative distance, negative speed, positive acceleration
     */

    public MotorTrajectory(double initPos, double finalPos, double maxSpeed, double accel){
        double dist = finalPos - initPos;

        this.initPos = initPos;
        if(Math.abs(maxSpeed * maxSpeed / accel / 2.0) >= Math.abs(dist)){
            this.maxSpeed = maxSpeed;
            this.decelInitTime = dist / maxSpeed + maxSpeed / accel / 2.0;
            this.maxTime = decelInitTime - maxSpeed / accel;
        }
        else{
            this.maxSpeed = Math.sqrt(-2 * accel * dist) * Math.signum(maxSpeed);
            this.decelInitTime = 0.0;
            this.maxTime = - maxSpeed / accel;
        }
        this.accel = accel;
    }

    public double getVelocity(double time){
        double vel = maxSpeed;

        if(time > decelInitTime){
            vel += accel * (time - decelInitTime);
        }

        return vel;
    }

    public double getPosition(double time){
        double pos = initPos + Math.min(time, decelInitTime) * maxSpeed;

        if(time > decelInitTime){
            pos += (maxSpeed + getVelocity(time)) / 2.0 * (time - decelInitTime);
        }

        return pos;
    }

    public double getDuration(){
        return maxTime;
    }
}