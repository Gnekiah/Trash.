package ml.xxiong.muse;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ProgressBar;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private GoogleApiClient client;

    // Sequence number, length = 20 bytes
    // Length of seed = 128 bits
    // It is one-to-one mapping between sequence number and seed, and sequence number is visible.
    private String snu = "34RH-MCE8-0XN3-CN32-VMB8";
    private String seed = "ZMWJE32JD32FHTGHFEDF43HFGREFJRH4";
    private Handler hdl = new Handler();
    private int countdown = 0;
    private TextView dp = null;
    private TextView standardtime = null;
    private TextView timestamp = null;
    private TextView timestampcut = null;
    private ProgressBar progBar = null;
    private TextView countdowntext = null;
    private TextView countdowntexttip = null;
    private FloatingActionButton fab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView sn = (TextView) findViewById(R.id.textView9);
        final TextView vseed = (TextView) findViewById(R.id.textView11);

        dp = (TextView) findViewById(R.id.textView2);
        countdowntext = (TextView) findViewById(R.id.textView13);
        countdowntext.setVisibility(View.GONE);
        countdowntexttip = (TextView) findViewById(R.id.textView12);
        countdowntexttip.setVisibility(View.GONE);
        standardtime = (TextView) findViewById(R.id.textView3);
        standardtime.setVisibility(View.GONE);
        timestamp = (TextView) findViewById(R.id.textView5);
        timestamp.setVisibility(View.GONE);
        timestampcut = (TextView) findViewById(R.id.textView7);
        timestampcut.setVisibility(View.GONE);
        progBar = (ProgressBar) findViewById(R.id.progressBar);
        progBar.setVisibility(View.GONE);
        progBar.setMax(60);

        sn.setText(snu);
        vseed.setText(seed);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if (countdown > 0) return;
                // show standard time
                SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss");
                standardtime.setText(formatter.format(new Date()));
                // show time stamp
                long ltimestamp = System.currentTimeMillis();
                timestamp.setText(String.valueOf(ltimestamp));

                // 30 seconds
                long ltimestampcut = ltimestamp / 60000;
                timestampcut.setText(String.valueOf(ltimestampcut));

                // hash seed and timestamp-cut
                String shaed = SHA1(seed + String.valueOf(ltimestampcut));
                dp.setText(shaed == null ? "ERROR!!" : Integer.valueOf(shaed.substring(0, 6), 16).toString().substring(0, 6));
                Snackbar.make(view, "Dynamic password created, available in 1 minute.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                progBar.setProgress(0);
                countdown = 0;
                countdowntext.setText(String.valueOf(60));
                countdowntexttip.setVisibility(View.VISIBLE);
                countdowntext.setVisibility(View.VISIBLE);
                standardtime.setVisibility(View.VISIBLE);
                timestamp.setVisibility(View.VISIBLE);
                timestampcut.setVisibility(View.VISIBLE);
                progBar.setVisibility(View.VISIBLE);
                hdl.post(progrun);
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    Runnable progrun = new Runnable() {
        @Override
        public void run() {
            fab.setVisibility(View.GONE);
            progBar.setProgress(++countdown);
            countdowntext.setText(String.valueOf(60-countdown));
            if (progBar.getProgress() < 60) {
                hdl.postDelayed(progrun, 1000);
            } else {
                progBar.setVisibility(View.GONE);
                countdowntexttip.setVisibility(View.GONE);
                countdowntext.setVisibility(View.GONE);
                standardtime.setVisibility(View.GONE);
                timestamp.setVisibility(View.GONE);
                timestampcut.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                hdl.removeCallbacks(progrun);
                dp.setText("OVERTIME");
                countdown = 0;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add((CharSequence) ("Exit"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            byte[] sha1hash = md.digest();
            return convertToHex(sha1hash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
