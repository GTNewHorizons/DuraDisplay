package com.caedis.duradisplay.utils;

import java.util.Objects;

public final class DurabilityLikeInfo {

    public double current;
    public double max;

    public DurabilityLikeInfo(double current, double max) {
        this.current = current;
        this.max = max;
    }

    public static final DurabilityLikeInfo empty = new DurabilityLikeInfo(0, 0);

    // Reused; only one info is alive at a time in the render loop
    private static final DurabilityLikeInfo reuse = new DurabilityLikeInfo(0, 0);

    public static DurabilityLikeInfo of(double current, double max) {
        reuse.current = current;
        reuse.max = max;
        return reuse;
    }

    public boolean isFull() {
        return current != 0 && current == max;
    }

    public boolean isEmpty() {
        return current == 0 && max != 0;
    }

    public boolean isNaN() {
        return current == 0 && max == 0;
    }

    public double percent() {
        return current / max;
    }

    public double current() {
        return current;
    }

    public double max() {
        return max;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DurabilityLikeInfo) obj;
        return Double.doubleToLongBits(this.current) == Double.doubleToLongBits(that.current)
            && Double.doubleToLongBits(this.max) == Double.doubleToLongBits(that.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(current, max);
    }

    @Override
    public String toString() {
        return "DurabilityLikeInfo[" + "current=" + current + ", " + "max=" + max + ']';
    }

}
