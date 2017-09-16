package helloandroid.textreader4;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadActivity extends AppCompatActivity {

    private ContentAdapter contentAdapter;
    private ViewPager mViewPager;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        fileName = getIntent().getExtras().getString("fileName");

        test();
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
    }


    private void test() {
        InputStream is = null;
        try {
            is = getAssets().open("JsonBookstore/" + fileName + ".json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            br = new BufferedReader(new InputStreamReader(is));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }

            new GetContentTask().execute(sb.toString());

        } catch (IOException e) {
            Log.e("Main", e.toString());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class GetContentTask extends AsyncTask<String, Void, List<String>> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ReadActivity.this);
            dialog.setMessage("Now Loading...");
            dialog.show();
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            List<String> list_str = new ArrayList<>();
            try {
                JSONArray ja = new JSONArray(strings[0]);
                for (int i = 0; i < ja.length(); i++) {
                    StringBuffer sb = null;
                    String str = new String();
                    try {
                        JSONObject jo = ja.getJSONObject(i);
                        JSONArray ja2 = jo.getJSONArray("content");

                        for (int j = 0; j < ja2.length(); j++) {
                            String temp_str = ja2.getString(j).replaceAll("[\r\n\\s]+", "");
                            str += temp_str;
                            if (temp_str.length() >= 0 || temp_str.equals("")) {
                                str += "\r\n";
                            }

                        }
                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                    list_str.add(str);
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }

            return list_str;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            List<Content> list_for_content = new ArrayList<>();
            dialog.dismiss();

            for (int i = 0; i < strings.size(); i++) {
                list_for_content.add(new Content(strings.get(i)));
            }
            contentAdapter = new ContentAdapter(getSupportFragmentManager(), list_for_content);
            mViewPager.setAdapter(contentAdapter);

        }
    }

    private class ContentAdapter extends FragmentStatePagerAdapter {
        private List<Content> contentlist;

        public ContentAdapter(FragmentManager fm, List<Content> contentlist) {
            super(fm);
            this.contentlist = contentlist;
        }

        @Override
        public Fragment getItem(int position) {
            return ContentFragment.newInstance(this.contentlist.get(position));
        }

        @Override
        public int getCount() {
            return this.contentlist.size();
        }
    }
}
