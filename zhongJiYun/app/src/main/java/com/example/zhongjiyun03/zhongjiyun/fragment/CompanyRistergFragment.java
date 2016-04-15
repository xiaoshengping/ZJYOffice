package com.example.zhongjiyun03.zhongjiyun.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.zhongjiyun03.zhongjiyun.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyRistergFragment extends Fragment {


    public CompanyRistergFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
              View view=inflater.inflate(R.layout.fragment_company_risterg, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        return view;
    }

}
