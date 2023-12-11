package com.example.jachisignal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jachisignal.Doc.Doc;
import com.example.jachisignal.Fragment.FragmentMyPage;
import com.example.jachisignal.databinding.CalendarBottomSheetBinding;
import com.example.jachisignal.databinding.ItemBinding;
import com.example.jachisignal.databinding.ItemMoneyBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheet extends BottomSheetDialogFragment{
//    private OnDismissListener dismissListener;
//    public interface OnDismissListener {
//        void onDismiss(boolean isSwitchOn);
//    }

    int year, month, dayOfMonth;
    CalendarBottomSheetBinding binding;
    List<Doc> list;
    SharedPreferences moneyPrefs;
    FragmentMyPage fragmentMyPage;
    MyAdapter adapter = new MyAdapter();
    int listSize;
    public BottomSheet(int year, int month, int dayOfMonth, FragmentMyPage fragmentMyPage){
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.year = year;
        this.fragmentMyPage = fragmentMyPage;
    }
    public BottomSheet(){}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CalendarBottomSheetBinding.inflate(inflater,container,false);
        moneyPrefs = getActivity().getSharedPreferences(year+"_"+month+"_"+dayOfMonth, Context.MODE_PRIVATE);
        list = new ArrayList<>();
        listSize = moneyPrefs.getInt("size",0);

        String item;
        int money;
        for(int i=0; i<listSize; i++) {
            item = moneyPrefs.getString("item"+i,null);
            money = moneyPrefs.getInt("money"+i,0);
            list.add(new Doc(item,money));
        }
        adapter.setList(list);
        binding.moneyListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.moneyListRecyclerView.setAdapter(adapter);

        binding.date.setText(year+"년 "+(month+1)+"월 "+dayOfMonth+"일");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences.Editor editor = moneyPrefs.edit();
        binding.addBtn.setOnClickListener(v -> {
            String item = binding.itemTxt.getText().toString();
            listSize = moneyPrefs.getInt("size",0);
            int money = Integer.parseInt(binding.moneyTxt.getText().toString());
            if(item.equals("")) {
                Toast.makeText(this.getActivity(), "항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
            else if(money == 0){
                Toast.makeText(this.getActivity(), "가격을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
            else{
                editor.putInt("size",listSize+1).commit();
                editor.putString("item"+listSize,item).commit();
                editor.putInt("money"+listSize,money).commit();
                Toast.makeText(this.getActivity(), "가계부에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                int total = moneyPrefs.getInt("total",0);
                editor.putInt("total",total+money).commit();
                fragmentMyPage.update();
                dismiss();
            }
        });
    }
    public class MyHolder extends RecyclerView.ViewHolder {
        ItemMoneyBinding binding;

        public MyHolder(ItemMoneyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class MyAdapter extends  RecyclerView.Adapter<MyHolder> {
        List<Doc> list;
        ItemMoneyBinding binding;

        public MyAdapter(List<Doc> list) {
            this.list = list;
        }
        public MyAdapter() {
        }

        public void setList(List<Doc> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            binding = ItemMoneyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MyHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.binding.itemTxt.setText(list.get(position).getItem());
            holder.binding.moneyTxt.setText(Integer.toString(list.get(position).getMoney())+"원");
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}
