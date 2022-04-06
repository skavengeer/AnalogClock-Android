package com.dino.clock;

import android.view.*;
import android.content.*;
import android.util.*;
import android.os.*;
import java.util.*;
import android.app.*;
import android.content.res.*;
import android.graphics.*;

import androidx.core.view.ViewCompat;

import org.xmlpull.v1.XmlPullParser;

public class TrueAnalogClock extends View {
    private TrueAnalogClock.AfterDrawFaceListener afterDrawFaceListener;
    private TrueAnalogClock.AfterDrawGlassListener afterDrawGlassListener;
    private TrueAnalogClock.BeforeDrawFaceListener beforeDrawFaceListener;
    private TrueAnalogClock.BeforeDrawGlassListener beforeDrawGlassListener;
    private GregorianCalendar c;
    private Bitmap face;
    private Bitmap glass;
    private ArrayList hands;
    private Paint paint;
    private long time;
    private boolean useSystemTime;

    public TrueAnalogClock(final Context context) {
        super(context);
        this.c = (GregorianCalendar)Calendar.getInstance();
        this.init(null, 0);
    }

    public TrueAnalogClock(final Context context, final AttributeSet set) {
        super(context, set);
        this.c = (GregorianCalendar) Calendar.getInstance();
        this.init(set, 0);
    }

    public TrueAnalogClock(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.c = (GregorianCalendar)Calendar.getInstance();
        this.init(set, n);
    }

    private void init(final AttributeSet set, final int n) {
        if (Build.VERSION.SDK_INT >= 11) {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, (Paint)null);
        }
        this.hands = new ArrayList();
        this.paint = new Paint(7);
    }

    private Point strToPoint(final String s) {
        final String[] split = s.split(":");
        Point point;
        if (split.length == 2) {
            point = new Point(Integer.parseInt(split[0].trim()), Integer.parseInt(split[1].trim()));
        }
        else {
            point = new Point(0, 0);
        }
        return point;
    }

    public void addHand(final TrueAnalogClockHand trueAnalogClockHand) {
        this.hands.add(trueAnalogClockHand);
    }

    public Object[] findHands(final String s) {
        final ArrayList list = new ArrayList();
        for (final Object next : this.hands) {
            if (((TrueAnalogClockHand)next).getName().equals(s)) {
                list.add(next);
            }
        }
        return list.toArray();
    }

    public Bitmap getFace() {
        return this.face;
    }

    public Bitmap getGlass() {
        return this.glass;
    }

    public TrueAnalogClockHand getHand(int index) {
        return (TrueAnalogClockHand) this.hands.get(index);
    }


    public int getHandCount() {
        return this.hands.size();
    }

    public long getTime() {
        long n;
        if (this.isUseSystemTime()) {
            n = Calendar.getInstance().getTimeInMillis();
        }
        else {
            n = this.time;
        }
        return n;
    }

    public boolean isUseSystemTime() {
        return this.useSystemTime;
    }

    public void loadFromXmlResource(Activity activity, int id) {

        removeAllHands();
        setGlass((Bitmap) null);

        XmlResourceParser parser = activity.getResources().getXml(id);
        try {
            while (parser.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlResourceParser.START_TAG) {

                    String tagname = parser.getName();
                    if (tagname.equals("TrueAnalogClock")) {
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String attributeName = parser.getAttributeName(i);
                            if (attributeName.equals("useSystemTime"))
                                setUseSystemTime(parser.getAttributeBooleanValue(i, false));
                            if (attributeName.equals("face"))
                                setFace(getResources().getIdentifier(parser.getAttributeValue(i), "drawable", activity.getPackageName()));
                            if (attributeName.equals("glass"))
                                setGlass(getResources().getIdentifier(parser.getAttributeValue(i), "drawable", activity.getPackageName()));
                        }
                    }

                    if (tagname.equals("hand")) {
                        TrueAnalogClockHand hand = new TrueAnalogClockHand(TrueAnalogClockHand.HandType.Custom);
                        hand.setUseSystemTime(isUseSystemTime());

                        for (int i2 = 0; i2 < parser.getAttributeCount(); i2++) {

                            String attributeName2 = parser.getAttributeName(i2);
                            if (attributeName2.equals("shadowOffset"))
                                hand.setShadowOffset(strToPoint(parser.getAttributeValue(i2)));
                            if (attributeName2.equals("shadowColor"))
                                hand.setShadowColor(parser.getAttributeIntValue(i2, ViewCompat.MEASURED_STATE_MASK));
                            if (attributeName2.equals("valueOffset"))
                                hand.setValueOffset(Float.parseFloat(parser.getAttributeValue(i2)));
                            if (attributeName2.equals("counterclockwise"))
                                hand.setCounterclockwise(parser.getAttributeBooleanValue(i2, false));
                            if (attributeName2.equals("jumping"))
                                hand.setJumping(parser.getAttributeBooleanValue(i2, false));
                            if (attributeName2.equals("hidden"))
                                hand.setHidden(parser.getAttributeBooleanValue(i2, false));
                            if (attributeName2.equals("underFace"))
                                hand.setUnderFace(parser.getAttributeBooleanValue(i2, false));
                            if (attributeName2.equals("shadow"))
                                hand.setShadow(parser.getAttributeBooleanValue(i2, false));
                            if (attributeName2.equals("useSystemTime"))
                                hand.setUseSystemTime(parser.getAttributeBooleanValue(i2, false));
                            if (attributeName2.equals("axisFacePoint"))
                                hand.setAxisFacePoint(strToPoint(parser.getAttributeValue(i2)));
                            if (attributeName2.equals("name"))
                                hand.setName(parser.getAttributeValue(i2));
                            if (attributeName2.equals("time"))
                                hand.setTime(parser.getAttributeIntValue(i2, 0));
                            if (attributeName2.equals("type"))
                                hand.setType(TrueAnalogClockHand.HandType.valueOf(parser.getAttributeValue(i2)));
                            if (attributeName2.equals("image")) {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inScaled = false;
                                hand.setBitmap(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(parser.getAttributeValue(i2), "drawable", activity.getPackageName()), options));
                            }
                            if (attributeName2.equals("value"))
                                hand.setValue(Float.parseFloat(parser.getAttributeValue(i2)));
                            if (attributeName2.equals("angleRange"))
                                hand.setAngleRange(Float.parseFloat(parser.getAttributeValue(i2)));
                            if (attributeName2.equals("timeOffset"))
                                hand.setTimeOffset(parser.getAttributeIntValue(i2, 0));
                            if (attributeName2.equals("shadowBlurRadius"))
                                hand.setShadowBlurRadius(Float.parseFloat(parser.getAttributeValue(i2)));
                            if (attributeName2.equals("angleOffset"))
                                hand.setAngleOffset(Float.parseFloat(parser.getAttributeValue(i2)));
                            if (attributeName2.equals("axisPoint"))
                                hand.setAxisPoint(strToPoint(parser.getAttributeValue(i2)));
                            if (attributeName2.equals("valueRange"))
                                hand.setValueRange(Float.parseFloat(parser.getAttributeValue(i2)));
                        }
                        addHand(hand);
                    }
                }
                parser.next();
            }
       } catch(Exception e){
                e.printStackTrace();
       }
    }




    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap face = getFace();
        if (face != null) {
            Bitmap buffer = Bitmap.createBitmap(face.getWidth(), face.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas bufferCanvas = new Canvas(buffer);
            Iterator i$ = this.hands.iterator();
            while (i$.hasNext()) {
                Object item = i$.next();
                TrueAnalogClockHand hand = (TrueAnalogClockHand) item;
                if (!hand.isHidden() && hand.isUnderFace()) {
                    hand.DrawHand(bufferCanvas);
                }
            }
            Boolean processed = false;
            if (this.beforeDrawFaceListener != null) {
                processed = this.beforeDrawFaceListener.onBeforeDrawFace(this, bufferCanvas);
            }
            Rect faceRect = new Rect(0, 0, face.getWidth(), face.getHeight());
            if (!processed.booleanValue()) {
                bufferCanvas.drawBitmap(face, faceRect, faceRect, this.paint);
                if (this.afterDrawFaceListener != null) {
                    this.afterDrawFaceListener.onAfterDrawFace(this, bufferCanvas);
                }
            }
            Iterator i$2 = this.hands.iterator();
            while (i$2.hasNext()) {
                Object item2 = i$2.next();
                TrueAnalogClockHand hand2 = (TrueAnalogClockHand) item2;
                if (!hand2.isHidden() && !hand2.isUnderFace()) {
                    hand2.DrawHand(bufferCanvas);
                }
            }
            Boolean processed2 = false;
            if (this.beforeDrawGlassListener != null) {
                processed2 = this.beforeDrawGlassListener.onBeforeDrawGlass(this, bufferCanvas);
            }
            if (!processed2.booleanValue()) {
                Bitmap glass = getGlass();
                if (glass != null) {
                    bufferCanvas.drawBitmap(glass, faceRect, faceRect, this.paint);
                }
                if (this.afterDrawGlassListener != null) {
                    this.afterDrawGlassListener.onAfterDrawGlass(this, bufferCanvas);
                }
            }
            int w = canvas.getWidth();
            int h = canvas.getHeight();
            RectF clockRect = new RectF(0.0f, 0.0f, w, h);
            float sa1 = buffer.getWidth();
            sa1 =  sa1 / w ;
            float sa2 = buffer.getHeight();
            sa2 = sa2 / h;

            if (sa1 > sa2) {
                float scaled = (buffer.getHeight() * w) / buffer.getWidth();
                clockRect.offset(0.0f, (h - scaled) / 2.0f);
                clockRect.bottom = clockRect.top + scaled;
            } else {
                float scaled2 = (buffer.getWidth() * h) / buffer.getHeight();
                clockRect.offset((w - scaled2) / 2.0f, 0.0f);
                clockRect.right = clockRect.left + scaled2;
            }
            canvas.drawBitmap(buffer, new Rect(0, 0, buffer.getWidth(), buffer.getHeight()), clockRect, this.paint);
            buffer.recycle();
        }
    }


    public void rearrangeHands(final TrueAnalogClockHand trueAnalogClockHand, final int n) {
        this.hands.set(n, trueAnalogClockHand);
    }

    public void removeAllHands() {
        this.hands.clear();
    }

    public void removeHand(final TrueAnalogClockHand trueAnalogClockHand) {
        this.hands.remove(trueAnalogClockHand);
    }

    public void setAfterDrawFaceListener(final TrueAnalogClock.AfterDrawFaceListener afterDrawFaceListener) {
        this.afterDrawFaceListener = afterDrawFaceListener;
    }

    public void setAfterDrawGlassListener(final TrueAnalogClock.AfterDrawGlassListener afterDrawGlassListener) {
        this.afterDrawGlassListener = afterDrawGlassListener;
    }

    public void setBeforeDrawFaceListener(final TrueAnalogClock.BeforeDrawFaceListener beforeDrawFaceListener) {
        this.beforeDrawFaceListener = beforeDrawFaceListener;
    }

    public void setBeforeDrawGlassListener(final TrueAnalogClock.BeforeDrawGlassListener beforeDrawGlassListener) {
        this.beforeDrawGlassListener = beforeDrawGlassListener;
    }

    public void setFace(final int n) {
        final BitmapFactory.Options bitmapFactory$Options = new BitmapFactory.Options();
        bitmapFactory$Options.inScaled = false;
        this.setFace(BitmapFactory.decodeResource(this.getResources(), n, bitmapFactory$Options));
    }

    public void setFace(final Bitmap face) {
        if (this.face != null) {
            this.face.recycle();
        }
        this.face = face;
    }

    public void setGlass(final int n) {
        final BitmapFactory.Options bitmapFactory$Options = new BitmapFactory.Options();
        bitmapFactory$Options.inScaled = false;
        this.setGlass(BitmapFactory.decodeResource(this.getResources(), n, bitmapFactory$Options));
    }

    public void setGlass(final Bitmap glass) {
        this.glass = glass;
    }

    public void setTime() {
        this.setTime(Calendar.getInstance().getTimeInMillis());
    }

    public void setTime(final int n, final int n2, final int n3) {
        final GregorianCalendar gregorianCalendar = (GregorianCalendar)Calendar.getInstance();
        gregorianCalendar.set(0, 0, 0, n, n2, n3);
        this.setTime(gregorianCalendar.getTimeInMillis());
    }

    public void setTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        final GregorianCalendar gregorianCalendar = (GregorianCalendar)Calendar.getInstance();
        gregorianCalendar.set(n, n2, n3, n4, n5, n6);
        this.setTime(gregorianCalendar.getTimeInMillis());
    }

    public void setTime(final long time) {
        this.time = time;
        final Iterator iterator = this.hands.iterator();
        while (iterator.hasNext()) {
            ((TrueAnalogClockHand)iterator.next()).setTime(this.time);
        }
    }

    public void setTimeOffset(final int n, final int n2, final int n3) {
        final GregorianCalendar gregorianCalendar = (GregorianCalendar)Calendar.getInstance();
        gregorianCalendar.set(0, 0, 0, n, n2, n3);
        this.setTimeOffset(gregorianCalendar.getTimeInMillis());
    }

    public void setTimeOffset(final long timeOffset) {
        final Iterator iterator = this.hands.iterator();
        while (iterator.hasNext()) {
            ((TrueAnalogClockHand)iterator.next()).setTimeOffset(timeOffset);
        }
    }


    public void setUseSystemTime(boolean useSystemTime) {
        this.useSystemTime = useSystemTime;
        Iterator i$ = this.hands.iterator();
        while (i$.hasNext()) {
            Object item = i$.next();
            TrueAnalogClockHand hand = (TrueAnalogClockHand) item;
            if (hand.getType() != TrueAnalogClockHand.HandType.Custom) {
                hand.setUseSystemTime(isUseSystemTime());
            }
        }
    }



    public interface AfterDrawFaceListener {
        void onAfterDrawFace(TrueAnalogClock paramTrueAnalogClock, Canvas paramCanvas);
    }

    public interface AfterDrawGlassListener {
        void onAfterDrawGlass(TrueAnalogClock paramTrueAnalogClock, Canvas paramCanvas);
    }

    public interface BeforeDrawFaceListener {
        Boolean onBeforeDrawFace(TrueAnalogClock paramTrueAnalogClock, Canvas paramCanvas);
    }

    public interface BeforeDrawGlassListener {
        Boolean onBeforeDrawGlass(TrueAnalogClock paramTrueAnalogClock, Canvas paramCanvas);
    }
}
