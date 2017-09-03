package helloandroid.txtreader;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by hadesshark on 2017/9/2.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    int pages;

    public MyPagerAdapter(FragmentManager fm, int pages) {
        super(fm);
        this.pages = pages;
    }


    @Override
    public Fragment getItem(int position) {
        return PageContentsFragmentBase.create(position);
    }

    @Override
    public int getCount() {
        return pages;
    }

    public void incrementPageCount() {
        pages += 1;
        notifyDataSetChanged();
    }
}
