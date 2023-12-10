package com.example.jachisignal.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Switch;

import com.example.jachisignal.BottomSheet;
import com.example.jachisignal.Doc.Doc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.CalendarBottomSheetBinding;
import com.example.jachisignal.databinding.FragmentMyPageBinding;
import com.example.jachisignal.MyPageActivity.mypage_gonggu;
import com.example.jachisignal.MyPageActivity.mypage_mywrite;
import com.example.jachisignal.MyPageActivity.mypage_scrap;
import com.example.jachisignal.databinding.ItemBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMyPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMyPage extends Fragment {
    FragmentMyPageBinding binding;
    private BottomSheetDialog dialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentMyPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMyPage.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMyPage newInstance(String param1, String param2) {
        FragmentMyPage fragment = new FragmentMyPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentMyPageBinding.inflate(inflater,container,false);
        binding.mypageScrapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mypage_scrap.class);
                startActivity(intent);
            }
        });
        binding.mypageWriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mypage_mywrite.class);
                startActivity(intent);
            }
        });
        binding.mypageGongguBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mypage_gonggu.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.calendarView.setOnDateChangeListener((CalendarView view1, int year, int month, int dayOfMonth) -> {
            BottomSheet bottomSheet = new BottomSheet(year,month,dayOfMonth);
            bottomSheet.show(getActivity().getSupportFragmentManager(), null);
        });
    }

}
