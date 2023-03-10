package tkachgeek.tkachutils.vector;

import org.bukkit.util.Vector;

public class VectorUtils {
  public static Vector rotateVector(Vector v, float yawDegrees, float pitchDegrees) {
    double yaw = Math.toRadians(-1 * (yawDegrees + 90));
    double pitch = Math.toRadians(-pitchDegrees);
    
    double cosYaw = Math.cos(yaw);
    double cosPitch = Math.cos(pitch);
    double sinYaw = Math.sin(yaw);
    double sinPitch = Math.sin(pitch);
    
    double initialX, initialY, initialZ;
    double x, y, z;
    
    // Z_Axis rotation (Pitch)
    initialX = v.getX();
    initialY = v.getY();
    x = initialX * cosPitch - initialY * sinPitch;
    y = initialX * sinPitch + initialY * cosPitch;
    
    // Y_Axis rotation (Yaw)
    initialZ = v.getZ();
    initialX = x;
    z = initialZ * cosYaw - initialX * sinYaw;
    x = initialZ * sinYaw + initialX * cosYaw;
    
    return new Vector(x, y, z);
  }
}
