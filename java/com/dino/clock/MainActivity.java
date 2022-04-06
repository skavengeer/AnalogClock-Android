package com.dino.clock;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private GregorianCalendar calendar;
    private TrueAnalogClock clock;
    private String[] descs;
    private String[] ids;
    private String[] names;
    private int pageIndex;
    private int speed;
    private TextView text;

    public MainActivity() {
        this.speed = 0;
    }


    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle(" dinoClock "); // set the top title
        String title = actionBar.getTitle().toString(); // get the title
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ab);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        this.ids = this.getResources().getStringArray(R.array.clockIds);
        this.names = this.getResources().getStringArray(R.array.clocksNames);
        this.descs = this.getResources().getStringArray(R.array.clocksDescriptions);
        this.text = (TextView)this.findViewById(R.id.buttonMore);
        this.clock = (TrueAnalogClock)this.findViewById(R.id.clockView);
        final Timer timer = new Timer();
        final MainActivity.ClockTimerTask clockTimerTask = new MainActivity.ClockTimerTask(this);
        this.calendar = new GregorianCalendar();
        timer.schedule((TimerTask)clockTimerTask, 1000 - this.calendar.get(14), 1000L);
        this.updateClock();
    }

    public class ClockTimerTask extends TimerTask
    {
        final MainActivity this$0;

        ClockTimerTask(final MainActivity this$0) {
            this.this$0 = this$0;
        }

        @Override
        public void run() {
            this.this$0.runOnUiThread((Runnable)new ClockTimerTask$1(this));
        }
    }



    static GregorianCalendar access$000(final MainActivity mainActivity) {
        return mainActivity.calendar;
    }

    static String[] access$100(final MainActivity mainActivity) {
        return mainActivity.descs;
    }

    static int access$200(final MainActivity mainActivity) {
        return mainActivity.pageIndex;
    }

    static int access$300(final MainActivity mainActivity) {
        return mainActivity.speed;
    }

    static TrueAnalogClock access$400(final MainActivity mainActivity) {
        return mainActivity.clock;
    }

    static String[] access$500(final MainActivity mainActivity) {
        return mainActivity.ids;
    }

    private int getXmlIdByName(final String s) {
        return this.getResources().getIdentifier(s, "xml", this.getPackageName());
    }

    private void showAbout(final Boolean b) {
        final LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.layoutAbout);
        if (b) {
            linearLayout.setVisibility(View.VISIBLE);
        }
        else {
            linearLayout.setVisibility(View.GONE);
        }
        final LinearLayout linearLayout2 = (LinearLayout)this.findViewById(R.id.layoutMain);
        if (b) {
            linearLayout2.setVisibility(View.GONE);
        }
        else {
            linearLayout2.setVisibility(View.VISIBLE);
        }
    }

    private void updateClock() {
        final int n = 0;
        this.text.setText((CharSequence)this.names[this.pageIndex]);
        this.clock.loadFromXmlResource((Activity)this, this.getXmlIdByName(this.ids[this.pageIndex]));
        final String s = this.ids[this.pageIndex];
        int n2 = 0;
        Label_0090: {
            switch (s.hashCode()) {
                case -1797402275: {
                    if (s.equals("from_photo")) {
                        n2 = 0;
                        break Label_0090;
                    }
                    break;
                }
                case -165199327: {
                    if (s.equals("classic_clock")) {
                        n2 = 1;
                        break Label_0090;
                    }
                    break;
                }
                case -705112156: {
                    if (s.equals("kitchen")) {
                        n2 = 2;
                        break Label_0090;
                    }
                    break;
                }
            }
            n2 = -1;
        }
        switch (n2) {
            default: {
                this.clock.setBackgroundColor(-16777216);
                break;
            }
            case 0:
            case 1:
            case 2: {
                this.clock.setBackgroundColor(-1);
                break;
            }
        }
        final String s2 = this.ids[this.pageIndex];
        int n3 = 0;
        Label_0158: {
            switch (s2.hashCode()) {
                case 904047131: {
                    if (s2.equals("rainbow_hands")) {
                        n3 = 0;
                        break Label_0158;
                    }
                    break;
                }
            }
            n3 = -1;
        }
        switch (n3) {
            case 0: {
                for (int i = 0; i < this.clock.getHandCount(); ++i) {

                    this.clock.getHand(i).setBeforeDrawListener((TrueAnalogClockHand.BeforeDrawListener)new MainActivity.MainActivity$1(this));
                }
                break;
            }
        }
        final String s3 = this.ids[this.pageIndex];
        int n4 = 0;
        Label_0210: {
            switch (s3.hashCode()) {
                case 1137481542: {
                    if (s3.equals("rainbow_face")) {
                        n4 = n;
                        break Label_0210;
                    }
                    break;
                }
            }
            n4 = -1;
        }
        switch (n4) {
            default: {
                this.clock.setBeforeDrawFaceListener((TrueAnalogClock.BeforeDrawFaceListener)null);
                break;
            }
            case 0: {

                this.clock.setBeforeDrawFaceListener((TrueAnalogClock.BeforeDrawFaceListener) new MainActivity.MainActivity$2(this));
                break;
            }
        }
    }

    class MainActivity$1 implements TrueAnalogClockHand.BeforeDrawListener {
        final MainActivity this$0;

        MainActivity$1(MainActivity mainActivity) {
            this.this$0 = mainActivity;
        }


        private void modifyBitmap(final TrueAnalogClockHand trueAnalogClockHand, final int color) {
            final Bitmap bitmap = trueAnalogClockHand.getBitmap();
            final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap2);
            final Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            canvas.drawRect(0.0f, 0.0f, (float)bitmap2.getWidth(), (float)bitmap2.getHeight(), paint);
            trueAnalogClockHand.setBitmap(bitmap2);
        }

        public Boolean onBeforeDraw(final TrueAnalogClockHand trueAnalogClockHand, final Canvas canvas) {
            this.modifyBitmap(trueAnalogClockHand, Color.HSVToColor(new float[] { trueAnalogClockHand.calculateAngle(), 1.0f, 1.0f }));
            return false;
        }
    }

    public class MainActivity$2 implements TrueAnalogClock.BeforeDrawFaceListener {
        final MainActivity this$0;

        MainActivity$2(MainActivity mainActivity) {
            this.this$0 = mainActivity;
        }

        public Boolean onBeforeDrawFace(TrueAnalogClock trueAnalogClock, Canvas canvas) {
            MainActivity.access$000((MainActivity)this.this$0).setTimeInMillis(trueAnalogClock.getTime());
            float f = MainActivity.access$000((MainActivity)this.this$0).get(11) * 15;
            Paint paint = new Paint();
            paint.setColor(Color.HSVToColor((float[])new float[]{f, 1.0f, 0.5f}));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle((float)(canvas.getWidth() / 2), (float)(canvas.getHeight() / 2), (float)(canvas.getWidth() / 2),  paint);
            return false;
        }

    }



    public void buttonCloseAboutClick(final View view) {
        this.showAbout(false);
    }

    public void buttonMoreClick(final View view) {
        new MainActivity.MainActivity$3(this, (Context)this);
    }

    public class MainActivity$3 extends AlertDialog.Builder {
        final MainActivity this$0;

        MainActivity$3(MainActivity mainActivity, Context context) {
            super(context);
            this.this$0 = mainActivity;
            this.setMessage(MainActivity.access$100((MainActivity) this.this$0)[MainActivity.access$200((MainActivity) this.this$0)]);
            this.setCancelable(true);
            this.create().show();
        }
    }

    public void buttonMoreDetailsClick(final View view) {
        this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/skavengeer/")));
    }

    public void buttonNextClick(final View view) {
        ++this.pageIndex;
        if (this.pageIndex >= this.ids.length) {
            this.pageIndex = 0;
        }
        this.updateClock();
    }

    public void buttonPrevClick(final View view) {
        --this.pageIndex;
        if (this.pageIndex < 0) {
            this.pageIndex = this.ids.length - 1;
        }
        this.updateClock();
    }

    public void buttonSourceCodesClick(final View view) {
        this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/skavengeer/")));
    }

    public void buttonTimeClick(final View view) {
        this.calendar = (GregorianCalendar) Calendar.getInstance();
        this.clock.setUseSystemTime(true);
        this.speed = 0;
    }

    public void buttonXClick(final View view) {
        this.calendar.setTimeInMillis(this.clock.getTime());
        this.clock.setUseSystemTime(false);
        this.speed = Integer.parseInt((String)view.getTag());
    }

    public void clockClick(final View view) {
        this.showAbout(true);
    }



    public class ClockTimerTask$1 implements Runnable {

        final MainActivity.ClockTimerTask this$1;

        ClockTimerTask$1(MainActivity.ClockTimerTask clockTimerTask) {
            this.this$1 = clockTimerTask;
        }

        @Override
        public void run() {
            if (MainActivity.access$300(this.this$1.this$0) != 0) {
                MainActivity.access$000(this.this$1.this$0).add(13, MainActivity.access$300(this.this$1.this$0));
                MainActivity.access$400(this.this$1.this$0).setTime(MainActivity.access$000(this.this$1.this$0).getTimeInMillis());
            }
            final String s = MainActivity.access$500(this.this$1.this$0)[MainActivity.access$200(this.this$1.this$0)];
            int n = 0;
            Label_0130: {
                switch (s.hashCode()) {
                    case 453994370: {
                        if (s.equals("power_reserve")) {
                            n = 0;
                            break Label_0130;
                        }
                        break;
                    }
                    case -1252995430: {
                        if (s.equals("gauges")) {
                            n = 1;
                            break Label_0130;
                        }
                        break;
                    }
                    case -975807792: {
                        if (s.equals("big_day_date")) {
                            n = 2;
                            break Label_0130;
                        }
                        break;
                    }
                }
                n = -1;
            }
            switch (n) {
                case 0:
                case 1: {
                    final Object[] hands = MainActivity.access$400(this.this$1.this$0).findHands("Power");
                    if (hands.length > 0) {
                        final Intent registerReceiver = this.this$1.this$0.registerReceiver((BroadcastReceiver)null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
                        for (int length = hands.length, i = 0; i < length; ++i) {
                            final TrueAnalogClockHand trueAnalogClockHand = (TrueAnalogClockHand)hands[i];
                            if (registerReceiver != null) {
                                trueAnalogClockHand.setValueRange((float)registerReceiver.getIntExtra("scale", -1));
                            }
                            else {
                                trueAnalogClockHand.setValueRange(10.0f);
                            }
                            if (registerReceiver != null) {
                                trueAnalogClockHand.setValue((float)registerReceiver.getIntExtra("level", -1));
                            }
                            else {
                                trueAnalogClockHand.setValue(0.0f);
                            }
                        }
                        break;
                    }
                    break;
                }
                case 2: {
                    final int value = MainActivity.access$000(this.this$1.this$0).get(5);
                    final Object[] hands2 = MainActivity.access$400(this.this$1.this$0).findHands("DateUnits");
                    for (int length2 = hands2.length, j = 0; j < length2; ++j) {
                        ((TrueAnalogClockHand)hands2[j]).setValue((float)(value % 10));
                    }
                    final Object[] hands3 = MainActivity.access$400(this.this$1.this$0).findHands("DateTens");
                    for (int length3 = hands3.length, k = 0; k < length3; ++k) {
                        ((TrueAnalogClockHand)hands3[k]).setValue((float)(value / 10));
                    }
                    break;
                }
            }
            MainActivity.access$400(this.this$1.this$0).invalidate();
        }
    }

}