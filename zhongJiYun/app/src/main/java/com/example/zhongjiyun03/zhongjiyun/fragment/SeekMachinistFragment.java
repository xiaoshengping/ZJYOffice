package com.example.zhongjiyun03.zhongjiyun.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.lidroid.xutils.ViewUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeekMachinistFragment extends Fragment {




    public SeekMachinistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seek_machinist, container, false);
        ViewUtils.inject(this, view);




        return view;
    }




}
