package helloandroid.txtreader2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReadActivity extends AppCompatActivity {

    private List<Content> contentList;

    //    private List<String> contentsList;
    private ContentAdapter contentAdapter;
    private ViewPager mViewPager;
    private String[] bookfile;
    String openBookStr;
    String fileName;

    String temp_content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        fileName = getIntent().getExtras().getString("fileName");

        test();
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);

//        contentList = getContentList();
//        contentAdapter = new ContentAdapter(getSupportFragmentManager(), contentList);
//        mViewPager.setAdapter(contentAdapter);
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
            System.out.println(this.contentlist.size());
            return this.contentlist.size();
        }
    }

    private List<Content> getContentList() {
        List<Content> contentList = new ArrayList<>();

        try {
            bookfile = getAssets().list("Bookstore/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String sub_fileName : bookfile) {
            System.out.println(sub_fileName);
            openBookStr = "Bookstore/" + fileName + "/" + sub_fileName;

            if (sub_fileName.equals("init.txt")) {
                temp_content = "Coming soon...";
            } else {
                temp_content = readTxt();
            }
            contentList.add(new Content(temp_content));
        }
        return contentList;
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
//                        System.out.println(ja2);

                        for (int j = 0; j < ja2.length(); j++) {
                            str += ja2.getString(j);
                        }
                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
//                    System.out.println(str);
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
//                System.out.println(strings.get(i));
//                System.out.println("==============================");
                list_for_content.add(new Content(strings.get(i)));
            }
            System.out.println(list_for_content.size());
            contentAdapter = new ContentAdapter(getSupportFragmentManager(), list_for_content);
            mViewPager.setAdapter(contentAdapter);

        }
    }


    class ContentTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            temp_content = readTxt();
//            System.out.println(temp_content);
            return null;
        }
    }


    private String readTxt() {
        BufferedReader br = null;
        try {
            InputStream is = getAssets().open(openBookStr);
            br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text);
                sb.append("\n");
            }
            return sb.toString();
        } catch (IOException ie) {
            Log.e("MainActivity", ie.toString());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ie) {
                    Log.e("MainActivity", ie.toString());
                }
            }
        }
        return " ";
    }
}
