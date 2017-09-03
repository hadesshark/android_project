package helloandroid.txtreader2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReadActivity extends AppCompatActivity {

    private List<Content> contentList;
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
        System.out.println(fileName);

        contentList = getContentList();
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        ContentAdapter contentAdapter = new ContentAdapter(getSupportFragmentManager(), contentList);
        mViewPager.setAdapter(contentAdapter);
    }

    private class ContentAdapter extends FragmentStatePagerAdapter {
        private List<Content> contentlist;

        public ContentAdapter(FragmentManager fm, List<Content> contentlist) {
            super(fm);
            this.contentlist = contentlist;
        }

        @Override
        public Fragment getItem(int position) {
            return ContentFragment.newInstance(contentList.get(position));
        }

        @Override
        public int getCount() {
            return contentList.size();
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

    class ContentTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            temp_content = readTxt();
            System.out.println(temp_content);
            return null;
        }
    }


    private String readTxt() {
        BufferedReader br = null;
        try {
            InputStream is = getAssets().open(openBookStr);
            // 藉由InputStreamReader水管將InputStream與BufferedReader串接在一起
            br = new BufferedReader(new InputStreamReader(is));
            // 使用StringBuilder做字串append以節省記憶體使用
            StringBuilder sb = new StringBuilder();
            String text;
            // BufferedReader才有的功能"readLine()"
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
