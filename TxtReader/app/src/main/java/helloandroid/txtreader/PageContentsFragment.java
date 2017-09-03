package helloandroid.txtreader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by hadesshark on 2017/9/2.
 */

public class PageContentsFragment extends PageContentsFragmentBase {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment, container, false);
        TextView contentTextView = (TextView) rootView.findViewById(R.id.readerText);
        String contents = ((ReaderActivity) getActivity()).getContents(PageNumber);
        contentTextView.setPadding(30, 30, 30, 30);
        contentTextView.setText(contents);
        contentTextView.setMovementMethod(new ScrollingMovementMethod());
        return rootView;

    }
}
