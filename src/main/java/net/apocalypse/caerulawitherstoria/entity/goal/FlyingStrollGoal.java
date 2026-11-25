package net.apocalypse.caerulawitherstoria.entity.goal;

import net.apocalypse.caerulawitherstoria.entity.ChitinGhastEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;

import java.nio.file.Path;

public class FlyingStrollGoal extends RandomStrollGoal {
    public FlyingStrollGoal(PathfinderMob pMob, double pSpeedModifier) {
        super(pMob, pSpeedModifier);
    }
    @Override
    protected Vec3 getPosition() {
        RandomSource random = mob.getRandom();
        double dir_x = mob.getX() + ((random.nextFloat() * 2 - 1) * 16);
        double dir_y = mob.getY() + ((random.nextFloat() * 2 - 1) * 16);
        double dir_z = mob.getZ() + ((random.nextFloat() * 2 - 1) * 16);
        return new Vec3(dir_x, dir_y, dir_z);
    }

}
