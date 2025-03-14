package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    // TODO entirely your job (except onCircle)

    private final Canvas canvas;

    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas; // FIXed
        this.paint = paint; // FIXed
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        int color = paint.getColor();
        paint.setColor(c.getColor());
        c.getShape().accept(this);
        paint.setColor(color);

        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        Paint.Style style = paint.getStyle();
        paint.setStyle(Style.FILL_AND_STROKE);
        f.getShape().accept(this);
        paint.setStyle(style);

        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for (Shape shape : g.getShapes()) {
            shape.accept(this);
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.save();
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);
        canvas.translate(-l.getX(), -l.getY());
        canvas.restore();

        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0,0, r.getWidth(), r.getHeight(), paint);

        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        Paint.Style style = paint.getStyle();
        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        paint.setStyle(style);

        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {

        Point[] points = new Point[s.getPoints().size()];
        for(int i = 0; i < points.length; i++) points[i] = s.getPoints().get(i);
        final float[] pts = new float[points.length * 4];
        int i = 0;
        for (int j = 0; j < points.length; j++) {
            Point p1 = points[j];
            Point p2 = points[(j + 1) % points.length];
            pts[i++] = (float) p1.getX();
            pts[i++] = (float) p1.getY();
            pts[i++] = (float) p2.getX();
            pts[i++] = (float) p2.getY();
        }
        canvas.drawLines(pts, paint);

        return null;
    }
}
