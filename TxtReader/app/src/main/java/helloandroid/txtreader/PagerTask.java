package helloandroid.txtreader;

import android.os.AsyncTask;
import android.text.TextPaint;

/**
 * Created by hadesshark on 2017/9/2.
 */

class PagerTask extends AsyncTask<ReaderActivity.ViewAndPaint, ReaderActivity.ProgressTracker, Void> {

    private ReaderActivity activity;

    public PagerTask(ReaderActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(ReaderActivity.ViewAndPaint... viewAndPaints) {

        ReaderActivity.ViewAndPaint vp = viewAndPaints[0];
        ReaderActivity.ProgressTracker progress = new ReaderActivity.ProgressTracker();

//        TextPaint paint = vp.getPaint();
        TextPaint paint = vp.paint;
        int numChars = 0;
        int lineCount = 0;
//        int maxLineCount = vp.getMaxLineCount();
        int maxLineCount = vp.maxLineCount;
        int totalCharactersProcessedSOFar = 0;

        int totalPages = 0;

        while (vp.contentString != null && vp.contentString.length() != 0) {
            while ((lineCount < maxLineCount) && (numChars < vp.contentString.length())) {
                numChars = numChars + paint.breakText(vp.contentString.substring(numChars), true, vp.screenWidth, null);
                lineCount++;
            }

            String stringToBeDisplayed = vp.contentString.substring(0, numChars);
            int nextIndex = numChars;
            int nextChar = nextIndex < vp.contentString.length() ? vp.contentString.charAt(nextIndex) : ' ';
            if (!Character.isWhitespace(nextChar)) {
                stringToBeDisplayed = stringToBeDisplayed.substring(0, stringToBeDisplayed.lastIndexOf(" "));
            }
            numChars = stringToBeDisplayed.length();
            vp.setContentString(vp.getContentString().substring(numChars));

            progress.totalPages = totalPages;
            progress.addPage(totalPages, totalCharactersProcessedSOFar, totalCharactersProcessedSOFar + numChars);
            publishProgress(progress);

            totalCharactersProcessedSOFar += numChars;

            numChars = 0;
            lineCount = 0;

            totalPages++;
        }


        return null;
    }

    @Override
    protected void onProgressUpdate(ReaderActivity.ProgressTracker... values) {
        activity.onPageProcessedUpdate(values[0]);
    }
}
