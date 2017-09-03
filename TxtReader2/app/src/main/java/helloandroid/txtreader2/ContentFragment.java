package helloandroid.txtreader2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by hadesshark on 2017/9/3.
 */

class ContentFragment extends Fragment {

    private Content content;

    public ContentFragment() {
    }

    public static ContentFragment newInstance(Content content) {
        ContentFragment fragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("content", content);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            content = (Content) getArguments().getSerializable("content");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        TextView mContent = (TextView) view.findViewById(R.id.mContent);
        mContent.setText(content.getContent());
        mContent.setPadding(30,30,30,30);
        return view;
    }
}
