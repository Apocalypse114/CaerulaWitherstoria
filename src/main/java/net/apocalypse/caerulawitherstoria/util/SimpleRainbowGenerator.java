package net.apocalypse.caerulawitherstoria.util;

import net.minecraft.world.phys.Vec3;

public class SimpleRainbowGenerator {
	public static Vec3 getRainbowColor(int t) {
        double cycleT = t % 140;

        if (cycleT < 20) {
            return interpolateRedToOrange(cycleT / 20.0);
        } else if (cycleT < 40) {
            return interpolateOrangeToYellow((cycleT - 20) / 20.0);
        } else if (cycleT < 60) {
            return interpolateYellowToGreen((cycleT - 40) / 20.0);
        } else if (cycleT < 80) {
            return interpolateGreenToBlue((cycleT - 60) / 20.0);
        } else if (cycleT < 100) {
            return interpolateBlueToIndigo((cycleT - 80) / 20.0);
        } else if (cycleT < 120) {
            return interpolateIndigoToViolet((cycleT - 100) / 20.0);
        } else {
            return interpolateVioletToRed((cycleT - 120) / 20.0);
        }
    }
    
    private static Vec3 interpolate(Vec3 a, Vec3 b, double factor) {
        double r = a.x + (b.x - a.x) * factor;
        double g = a.y + (b.y - a.y) * factor;
        double bv = a.z + (b.z - a.z) * factor;
        return new Vec3(r, g, bv);
    }
    
    private static final Vec3 RED     = new Vec3(1.0, 0.0, 0.0);   // 红
    private static final Vec3 ORANGE  = new Vec3(1.0, 0.5, 0.0);   // 橙
    private static final Vec3 YELLOW  = new Vec3(1.0, 1.0, 0.0);   // 黄
    private static final Vec3 GREEN   = new Vec3(0.0, 1.0, 0.0);   // 绿
    private static final Vec3 BLUE    = new Vec3(0.0, 0.0, 1.0);   // 蓝
    private static final Vec3 INDIGO  = new Vec3(0.29, 0.0, 0.51); // 靛
    private static final Vec3 VIOLET  = new Vec3(0.56, 0.0, 1.0);   // 紫

    private static Vec3 interpolateRedToOrange(double f) { return interpolate(RED, ORANGE, f); }
    private static Vec3 interpolateOrangeToYellow(double f) { return interpolate(ORANGE, YELLOW, f); }
    private static Vec3 interpolateYellowToGreen(double f) { return interpolate(YELLOW, GREEN, f); }
    private static Vec3 interpolateGreenToBlue(double f) { return interpolate(GREEN, BLUE, f); }
    private static Vec3 interpolateBlueToIndigo(double f) { return interpolate(BLUE, INDIGO, f); }
    private static Vec3 interpolateIndigoToViolet(double f) { return interpolate(INDIGO, VIOLET, f); }
    private static Vec3 interpolateVioletToRed(double f) { return interpolate(VIOLET, RED, f); }
}
