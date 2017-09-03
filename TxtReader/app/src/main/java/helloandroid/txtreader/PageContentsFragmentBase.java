package helloandroid.txtreader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by hadesshark on 2017/9/2.
 */

public class PageContentsFragmentBase extends Fragment {

    public static final String ARG_PAGE = "page";

    public int getPageNumber() {
        return PageNumber;
    }

    protected int PageNumber;


    public PageContentsFragmentBase() {
    }

    public static PageContentsFragmentBase create(int pageNumber) {
        PageContentsFragmentBase fragment = null;

        fragment = new PageContentsFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PageNumber = getArguments().getInt(ARG_PAGE);
    }


}
