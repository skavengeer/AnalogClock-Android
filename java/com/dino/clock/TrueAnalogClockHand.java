package com.dino.clock;

import java.util.*;
import android.graphics.*;
public class TrueAnalogClockHand {
    private TrueAnalogClockHand.AfterDrawListener afterDrawListener;
    private float angleOffset;
    private float angleRange;
    private Point axisFacePoint;
    private Point axisPoint;
    private TrueAnalogClockHand.BeforeDrawListener beforeDrawListener;
    private Bitmap bitmap;
    private boolean counterclockwise;
    private boolean hidden;
    private boolean jumping;
    private String name;
    private Paint p;
    private boolean shadow;
    private Bitmap shadowBitmap;
    private float shadowBlurRadius;
    private int shadowColor;
    private Point shadowOffset;
    private long time;
    private long timeOffset;
    private TrueAnalogClockHand.HandType type;
    private boolean underFace;
    private boolean useSystemTime;
    private float value;
    private float valueOffset;
    private float valueRange;

    public TrueAnalogClockHand(final TrueAnalogClockHand.HandType type) {
        this.p = new Paint(7);
        this.axisPoint = new Point(0, 0);
        this.axisFacePoint = new Point(0, 0);
        this.angleRange = 360.0f;
        this.valueRange = 100.0f;
        this.shadowColor = Integer.MIN_VALUE;
        this.shadowBlurRadius = 10.0f;
        this.shadowOffset = new Point(0, 0);
        this.setType(type);
        this.setName(type.toString());
        this.init();
    }

    private void init() {
    }

    private void offsetTime(final Calendar calendar) {
        if (this.getTimeOffset() != 0L) {
            final GregorianCalendar gregorianCalendar = (GregorianCalendar)Calendar.getInstance();
            gregorianCalendar.setTimeInMillis(this.getTimeOffset());
            calendar.add(11, gregorianCalendar.get(11));
            calendar.add(12, gregorianCalendar.get(12));
            calendar.add(13, gregorianCalendar.get(13));
        }
    }

    private void updateShadowBitmap() {
        if (this.isShadow()) {
            final Bitmap bitmap = this.getBitmap();
            if (bitmap != null) {
                final BlurMaskFilter maskFilter = new BlurMaskFilter(this.getShadowBlurRadius(), BlurMaskFilter.Blur.NORMAL);
                final Paint paint = new Paint(7);
                paint.setMaskFilter((MaskFilter)maskFilter);
                final int[] array = new int[2];
                if (this.shadowBitmap != null) {
                    this.shadowBitmap.recycle();
                }
                this.shadowBitmap = bitmap.extractAlpha(paint, array);
            }
            else if (this.shadowBitmap != null) {
                this.shadowBitmap.recycle();
            }
        }
        else if (this.shadowBitmap != null) {
            this.shadowBitmap.recycle();
        }
    }

    public void DrawHand(final Canvas canvas) {
        Boolean b = false;
        if (this.beforeDrawListener != null) {
            b = this.beforeDrawListener.onBeforeDraw(this, canvas);
        }
        if (!b) {
            if (this.getBitmap() != null) {
                final Point axisPoint = this.getAxisPoint();
                final Point axisFacePoint = this.getAxisFacePoint();
                final Point point = new Point(axisFacePoint.x - axisPoint.x, axisFacePoint.y - axisPoint.y);
                final float calculateAngle = this.calculateAngle();
                final Matrix matrix = new Matrix();
                if (this.isShadow() && this.shadowBitmap != null) {
                    final float shadowBlurRadius = this.getShadowBlurRadius();
                    final Point shadowOffset = this.getShadowOffset();
                    this.p.setColor(this.getShadowColor());
                    matrix.setTranslate(point.x - shadowBlurRadius, point.y - shadowBlurRadius);
                    matrix.postRotate(calculateAngle, (float)axisFacePoint.x, (float)axisFacePoint.y);
                    matrix.postTranslate((float)shadowOffset.x, (float)shadowOffset.y);
                    canvas.drawBitmap(this.shadowBitmap, matrix, this.p);
                    matrix.reset();
                    this.p.reset();
                }
                matrix.setTranslate((float)point.x, (float)point.y);
                matrix.postRotate(calculateAngle, (float)axisFacePoint.x, (float)axisFacePoint.y);
                canvas.drawBitmap(this.getBitmap(), matrix, this.p);
            }
            if (this.afterDrawListener != null) {
                this.afterDrawListener.onAfterDraw(this, canvas);
            }
        }
    }

    public float calculateAngle() {
        float n = this.getAngleOffset() + this.getAngleRange() * (this.calculateValue() - this.getValueOffset()) / this.getValueRange();
        if (this.isCounterclockwise()) {
            n = -n;
        }
        return n;
    }

    public float calculateValue() {
        boolean jmp = isJumping();
        switch (getType()) {
            case Custom:
                return getValue();
            default:
                GregorianCalendar c = (GregorianCalendar) GregorianCalendar.getInstance();
                if (!isUseSystemTime()) {
                    c.setTimeInMillis(getTime());
                }
                offsetTime(c);
                switch (getType()) {
                    case DayOfMonth:
                        return c.get(5);
                    case DayOfWeek:
                        return c.get(7);
                    case Month:
                        return c.get(2);
                    case Hour24:
                        float val = c.get(11);
                        if (!jmp) {
                            val += (c.get(12) / 60.0f) + (c.get(13) / 3600.0f);
                        }
                        return val;
                    case Hour:
                        float val2 = c.get(10);
                        if (!jmp) {
                            val2 += (c.get(12) / 60.0f) + (c.get(13) / 3600.0f);
                        }
                        return val2;
                    case Minute:
                        float val3 = c.get(12);
                        if (!jmp) {
                            val3 += c.get(13) / 60.0f;
                        }
                        return val3;
                    case Second:
                        return c.get(13);
                    default:
                        return 0.0f;
                }
        }
    }


    public float getAngleOffset() {
        return this.angleOffset;
    }

    public float getAngleRange() {
        return this.angleRange;
    }

    public Point getAxisFacePoint() {
        return this.axisFacePoint;
    }

    public Point getAxisPoint() {
        return this.axisPoint;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public String getName() {
        return this.name;
    }

    public float getShadowBlurRadius() {
        return this.shadowBlurRadius;
    }

    public int getShadowColor() {
        return this.shadowColor;
    }

    public Point getShadowOffset() {
        return this.shadowOffset;
    }

    public long getTime() {
        return this.time;
    }

    public long getTimeOffset() {
        return this.timeOffset;
    }

    public TrueAnalogClockHand.HandType getType() {
        return this.type;
    }

    public float getValue() {
        return this.value;
    }

    public float getValueOffset() {
        switch (getType()) {
            case DayOfMonth:
            case DayOfWeek:
                return 1.0f;
            case Month:
            case Hour24:
            case Hour:
            case Minute:
            case Second:
                return 0.0f;
            default:
                return this.valueOffset;
        }
    }

    public float getValueRange() {
        switch (getType()) {
            case DayOfMonth:
                return 31.0f;
            case DayOfWeek:
                return 7.0f;
            case Month:
            case Hour:
                return 12.0f;
            case Hour24:
                return 24.0f;
            case Minute:
            case Second:
                return 60.0f;
            default:
                return this.valueRange;
        }
    }

    public boolean isCounterclockwise() {
        return this.counterclockwise;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public boolean isJumping() {
        return this.jumping;
    }

    public boolean isShadow() {
        return this.shadow;
    }

    public boolean isUnderFace() {
        return this.underFace;
    }

    public boolean isUseSystemTime() {
        return this.useSystemTime;
    }

    public void setAfterDrawListener(final TrueAnalogClockHand.AfterDrawListener afterDrawListener) {
        this.afterDrawListener = afterDrawListener;
    }

    public void setAngleOffset(final float angleOffset) {
        this.angleOffset = angleOffset;
    }

    public void setAngleRange(final float angleRange) {
        this.angleRange = angleRange;
    }

    public void setAxisFacePoint(final Point axisFacePoint) {
        this.axisFacePoint = axisFacePoint;
    }

    public void setAxisPoint(final Point axisPoint) {
        this.axisPoint = axisPoint;
    }

    public void setBeforeDrawListener(final TrueAnalogClockHand.BeforeDrawListener beforeDrawListener) {
        this.beforeDrawListener = beforeDrawListener;
    }

    public void setBitmap(final Bitmap bitmap) {
        if (this.bitmap != null) {
            this.bitmap.recycle();
        }
        this.bitmap = bitmap;
        this.updateShadowBitmap();
    }

    public void setCounterclockwise(final boolean counterclockwise) {
        this.counterclockwise = counterclockwise;
    }

    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }

    public void setJumping(final boolean jumping) {
        this.jumping = jumping;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setShadow(final boolean shadow) {
        this.shadow = shadow;
        this.updateShadowBitmap();
    }

    public void setShadowBlurRadius(final float shadowBlurRadius) {
        this.shadowBlurRadius = shadowBlurRadius;
        this.updateShadowBitmap();
    }

    public void setShadowColor(final int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public void setShadowOffset(final Point shadowOffset) {
        this.shadowOffset = shadowOffset;
    }

    public void setTime(final long time) {
        this.time = time;
    }

    public void setTimeOffset(final int n, final int n2, final int n3) {
        final GregorianCalendar gregorianCalendar = (GregorianCalendar)Calendar.getInstance();
        gregorianCalendar.set(0, 0, 0, n, n2, n3);
        this.setTimeOffset(gregorianCalendar.getTimeInMillis());
    }

    public void setTimeOffset(final long timeOffset) {
        this.timeOffset = timeOffset;
    }

    public void setType(final TrueAnalogClockHand.HandType type) {
        this.type = type;
    }

    public void setUnderFace(final boolean underFace) {
        this.underFace = underFace;
    }

    public void setUseSystemTime(final boolean useSystemTime) {
        this.useSystemTime = useSystemTime;
    }

    public void setValue(final float value) {
        this.value = value;
    }

    public void setValueOffset(final float valueOffset) {
        this.valueOffset = valueOffset;
    }

    public void setValueRange(final float valueRange) {
        this.valueRange = valueRange;
    }

    public interface AfterDrawListener {
        void onAfterDraw(TrueAnalogClockHand paramTrueAnalogClockHand, Canvas paramCanvas);
    }

    public interface BeforeDrawListener {
        Boolean onBeforeDraw(TrueAnalogClockHand paramTrueAnalogClockHand, Canvas paramCanvas);
    }

    public enum HandType {
        Custom, DayOfMonth, DayOfWeek, Hour, Hour24, Minute, Month, Second;
    }

}
