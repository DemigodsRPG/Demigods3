package com.demigodsrpg.demigods.engine.location;

import com.google.common.base.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DemigodsRegion {
    public final static int REGION_LENGTH = 8;
    public final static int HALF_REGION_LENGTH = REGION_LENGTH / 2;

    private final int x;
    private final int z;
    private final String world;

    private DemigodsRegion(int x, int z, String world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }

    public Location getCenter() {
        return new Location(Bukkit.getWorld(world), x, 128, z);
    }

    public DemigodsRegion[] getSurroundingRegions() {
        DemigodsRegion[] area = new DemigodsRegion[9];
        area[0] = new DemigodsRegion(x - REGION_LENGTH, z - REGION_LENGTH, world);
        area[1] = new DemigodsRegion(x - REGION_LENGTH, z, world);
        area[2] = new DemigodsRegion(x - REGION_LENGTH, z + REGION_LENGTH, world);
        area[3] = new DemigodsRegion(x, z - REGION_LENGTH, world);
        area[4] = this;
        area[5] = new DemigodsRegion(x, z + REGION_LENGTH, world);
        area[6] = new DemigodsRegion(x + REGION_LENGTH, z - REGION_LENGTH, world);
        area[7] = new DemigodsRegion(x + REGION_LENGTH, z, world);
        area[8] = new DemigodsRegion(x + REGION_LENGTH, z + REGION_LENGTH, world);
        return area;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("world", world).add("x", x).add("z", z).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(world, x, z);
    }

    @Override
    public boolean equals(Object object) {
        return Objects.equal(this, object);
    }

    public class X implements Comparable<X> {
        @Override
        public int compareTo(X regionX) {
            return x() == regionX.x() ? 0 : (x() > regionX.x() ? 1 : -1);
        }

        protected int x() {
            return getX();
        }

        public DemigodsRegion getRegion() {
            return DemigodsRegion.this;
        }
    }

    public class Z implements Comparable<Z> {
        @Override
        public int compareTo(Z regionZ) {
            return z() == regionZ.z() ? 0 : (z() > regionZ.z() ? 1 : -1);
        }

        protected int z() {
            return getZ();
        }

        public DemigodsRegion getRegion() {
            return DemigodsRegion.this;
        }
    }

    public static DemigodsRegion at(Location location) {
        return new DemigodsRegion(getCoordinate(location.getBlockX()), getCoordinate(location.getBlockZ()), location.getWorld().getName());
    }

    public static DemigodsRegion at(int X, int Z, String world) {
        return new DemigodsRegion(getCoordinate(X), getCoordinate(Z), world);
    }

    private static int getCoordinate(int number) {
        int temp = number % REGION_LENGTH;
        if (temp >= HALF_REGION_LENGTH) return number + REGION_LENGTH - temp;
        return number - temp;
    }
}
