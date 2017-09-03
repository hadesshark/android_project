package helloandroid.txtreader;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class ReaderActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ViewPager pager;
    private LinearLayout pageIndicator;

    private String bookString = "12345678910111213";
    private DisplayMetrics metrics;

    private Map<String, String> Pages = new HashMap<String, String>();
    private FragmentPagerAdapter PagerAdapter;

    private Display mDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        initView();
    }


    private void initView() {
        mDisplay = getWindowManager().getDefaultDisplay();
        progressBar = (ProgressBar) findViewById(R.id.progress);
        pager = (ViewPager) findViewById(R.id.pager);

        bookString = readTXT();

        ViewGroup TxtViewPage = (ViewGroup) getLayoutInflater().inflate(R.layout.fragment, (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content), false);
        TextView ReaderText = (TextView) TxtViewPage.findViewById(R.id.readerText);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        ViewAndPaint vp = new ViewAndPaint(ReaderText.getPaint(), TxtViewPage, getScreenWidth(), getMaxLineForTextView(ReaderText), bookString);

        PagerTask pt = new PagerTask(this);
        pt.execute(vp);
    }

    static class ViewAndPaint {
        private ViewGroup textviewPage;
        private TextPaint paint;
        private int screenWidth;
        private int maxLineCount;
        private String contentString;

        public TextPaint getPaint() {
            return paint;
        }

        public int getMaxLineCount() {
            return maxLineCount;
        }

        public int getScreenWidth() {
            return screenWidth;
        }

        public String getContentString() {
            return contentString;
        }

        public void setContentString(String contentString) {
            this.contentString = contentString;
        }

        public ViewAndPaint(TextPaint paint, ViewGroup textviewPage, int screenWidth, int maxLineCount, String contentString) {
            this.textviewPage = textviewPage;
            this.paint = paint;
            this.screenWidth = screenWidth;
            this.maxLineCount = maxLineCount;
            this.contentString = contentString;
        }
    }

    public String readTXT() {
        BufferedReader br = null;
        try {
            InputStream is = getAssets().open("bookstore/Emma - Jane Austen.txt");
            // 藉由InputStreamReader水管將InputStream與BufferedReader串接在一起
            Charset charset = CharsetDetector.detect(is);
            br = new BufferedReader(new InputStreamReader(is, charset));
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

    static class ProgressTracker {

        private int totalPages;
        private Map<String, String> pages = new HashMap<String, String>();

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public Map<String, String> getPages() {
            return pages;
        }


        public void addPage(int page, int startIndex, int endIndex) {
            String thePage = String.valueOf(page);
            String indexMarker = String.valueOf(startIndex) + "," + String.valueOf(endIndex);
            pages.put(thePage, indexMarker);
        }
    }

    private int getMaxLineForTextView(TextView view) {
        float verticalMargin = getResources().getDimension(R.dimen.activity_vertical_margin) * 2;
//        int screenHeight = metrics.heightPixels;
        int screenHeight = mDisplay.getHeight();
        TextPaint paint = view.getPaint();

        //Working Out How Many Lines Can Be Entered In The Screen
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.top - fm.bottom;
        textHeight = Math.abs(textHeight);

        int maxLineCount = (int) ((screenHeight - verticalMargin) / textHeight);

        // add extra spaces at the bottom, remove 2 lines
        maxLineCount -= 0;

        return maxLineCount;
    }

    private int getScreenWidth() {
        float horizontalMargin = getResources().getDimension(R.dimen.activity_horizontal_margin) * 2;

        int screenWidth = (int) (metrics.widthPixels - horizontalMargin);


        return screenWidth;
    }

    public void onPageProcessedUpdate(ProgressTracker progress) {
        Pages = progress.getPages();

        if (PagerAdapter == null) {
            initViewPager();
            hideProgress();
        } else {
            ((MyPagerAdapter) PagerAdapter).incrementPageCount();
        }
        addPageIndicator(progress.getTotalPages());
    }

    private void initViewPager() {
        PagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), 1);
        pager.setAdapter(PagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                showPageIndicatior(position);
            }
        });
    }

    protected void showPageIndicatior(int position) {
        try {
            pageIndicator = (LinearLayout) findViewById(R.id.pageIndicator);
            View selectedIndexIndicator = pageIndicator.getChildAt(position);
            selectedIndexIndicator.setBackgroundResource(R.drawable.current_page_indicator);

            if (position > 0) {
                View leftView = pageIndicator.getChildAt(position - 1);
                leftView.setBackgroundResource(R.drawable.indicator_background);
            }
            if (position < Pages.size()) {
                View rightView = pageIndicator.getChildAt(position + 1);
                rightView.setBackgroundResource(R.drawable.indicator_background);
            }
        } catch (Exception e) {
            Log.e("test::", e.toString());
        }
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void addPageIndicator(int totalPages) {
        pageIndicator = (LinearLayout) findViewById(R.id.pageIndicator);
        View view = new View(this);
        ViewGroup.LayoutParams params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        view.setLayoutParams(params);
        view.setBackgroundResource((totalPages == 0 ? R.drawable.current_page_indicator : R.drawable.indicator_background));
        view.setTag(totalPages);
        pageIndicator.addView(view);
    }

    public String getContents(int pageNumber) {
        // setting text ,
        String page = String.valueOf(pageNumber);
        String textBounderies = Pages.get(page);
        if (textBounderies != null) {
            String[] bounds = textBounderies.split(",");
            int startIndex = Integer.valueOf(bounds[0]);
            int endIndex = Integer.valueOf(bounds[1]);
            System.out.println(startIndex);
            System.out.println(endIndex);


            return bookString.substring(startIndex, endIndex).trim();
        }
        return "";
    }

}
