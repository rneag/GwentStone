package resources;

public final class Coordinates {
    private int x, y;

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public Coordinates(final fileio.Coordinates coordinates) {
        if (coordinates != null) {
            this.x = coordinates.getX();
            this.y = coordinates.getY();
        }
    }

}
