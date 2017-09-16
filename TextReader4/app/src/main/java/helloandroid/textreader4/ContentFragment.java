package helloandroid.textreader4;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by hadesshark on 2017/9/3.
 */

class ContentFragment extends Fragment {

    private Content content;
    private int text_size;

    public ContentFragment () {

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
        final TextView mContent = (TextView) view.findViewById(R.id.mContent);
        mContent.setText(content.getContent());
        mContent.setPadding(30, 30, 30, 30);

        final RelativeLayout relativeLayout_setting = (RelativeLayout) view.findViewById(R.id.relativeLayout_Setting);
        relativeLayout_setting.setVisibility(View.GONE);
        mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(relativeLayout_setting.getVisibility() == View.GONE) {
                    relativeLayout_setting.setVisibility(View.VISIBLE);
                } else {
                    relativeLayout_setting.setVisibility(View.GONE);
                }
            }
        });

        Button BigBtn = (Button) view.findViewById(R.id.BigBtn);
        Button MidBtn = (Button) view.findViewById(R.id.MidBtn);
        Button SmallBtn = (Button) view.findViewById(R.id.SmallBtn);

        final TextView info = (TextView) view.findViewById(R.id.info);
        info.setText("目前文字大小： 小");

        System.out.println(inflater.getContext());
        //inflater.getContext();

        BigBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, 70);
                info.setText("目前文字大小： 大");

            }
        });
        MidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50);
                info.setText("目前文字大小： 中");
            }
        });
        SmallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
                info.setText("目前文字大小： 小");
            }
        });

        return view;
    }
}
