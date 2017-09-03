package helloandroid.txtreader;

import android.os.AsyncTask;

/**
 * Created by hadesshark on 2017/9/2.
 */

public class ContentTask extends AsyncTask<ReaderActivity.SettingContent, Void, Void> {

    private ReaderActivity readerActivity;

    public ContentTask(ReaderActivity readerActivity) {
        this.readerActivity = readerActivity;
    }

    public ReaderActivity getReaderActivity() {
        return readerActivity;
    }

    @Override
    protected Void doInBackground(ReaderActivity.SettingContent... settingContents) {

        ReaderActivity.SettingContent sc = settingContents[0];
        sc.content = sc.readTXT();

        return null;
    }
}
