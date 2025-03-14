package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    // TODO entirely your job (except onCircle)

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius,
                new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        return f.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Shape shape : g.getShapes()) {
            Shape box = shape.accept(this);
            if (box instanceof Location) {
                Location location = (Location) box;
                Shape innerShape = location.getShape();
                if (innerShape instanceof Rectangle) {
                    Rectangle rect = (Rectangle) innerShape;
                    minX = Math.min(minX, location.getX());
                    maxX = Math.max(maxX, location.getX() + rect.getWidth());
                    minY = Math.min(minY, location.getY());
                    maxY = Math.max(maxY, location.getY() + rect.getHeight());
                }
            }
        }
        return new Location(minX, minY, new Rectangle((maxX - minX), (maxY - minY)));
    }

    @Override
    public Location onLocation(final Location l) {
        final Location innerBox = l.getShape().accept(this);
        return new Location (l.getX() + innerBox.getX(), l.getY() + innerBox.getY(), innerBox.getShape());
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        return new Location(0, 0, r);
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
       return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        return o.getShape().accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point p : s.getPoints()){
            minX = Math.min(minX, p.getX());
            maxX = Math.max(maxX, p.getX());
            minY = Math.min(minY, p.getY());
            maxY = Math.max(maxY, p.getY());
        }

        int width = maxX - minX;
        int height = maxY - minY;


        return new Location(minX, minY, new Rectangle(width, height));
    }
}
