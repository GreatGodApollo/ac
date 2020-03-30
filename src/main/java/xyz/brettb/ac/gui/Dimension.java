package xyz.brettb.ac.gui;

import lombok.Getter;
import lombok.Setter;

public class Dimension {
    public static final Dimension ONE = Dimension.square(1);

    @Getter @Setter private int x;
    @Getter @Setter private int y;

    private Dimension(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Dimension of(int x, int y) {
        return new Dimension(x, y);
    }

    public static Dimension square(int xy) {
        return Dimension.of(xy, xy);
    }

    public boolean fitsInside(Dimension other) {
        return x >= 0 && y >= 0 && x <= other.getX() && y <= other.getY();
    }

    public Dimension add(Dimension other) {
        return Dimension.of(x + other.getX(), y + other.getY());
    }

    public Dimension subtract(Dimension other) {
        return Dimension.of(x - other.getX(), y - other.getY());
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

}
