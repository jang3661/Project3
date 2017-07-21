package com.example.doublejk.project3;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;

public class RegisterFragment extends Fragment {
    private Button beforeBtn, nextBtn;
    private TextView txtCount;
    private EditText editContent, editAddress, editTitle, editNumber;
    private OnAddressSendedListner mCallback;
    public RegisterFragment() {
        // Required empty public constructor
    }
    public static RegisterFragment newInstance() {
        Log.d("인스턴스", "");
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    public interface OnAddressSendedListner {
        public void onAddressSended(Restaurant restaurant);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnAddressSendedListner) context;
        }catch (ClassCastException e) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(getActivity(), v);

        beforeBtn = (Button)  v.findViewById(R.id.beforeBtn);
        nextBtn = (Button) v.findViewById(R.id.nextBtn);
        txtCount = (TextView) v.findViewById(R.id.txtCount);
        editContent = (EditText) v.findViewById(R.id.register_content);
        editAddress = (EditText) v.findViewById(R.id.register_address);
        editTitle = (EditText) v.findViewById(R.id.register_title);
        editNumber = (EditText) v.findViewById(R.id.register_number);
        //다음 버튼 클릭시
        nextBtn.setOnClickListener(new View.OnClickListener() {
            //Bundle args = new Bundle();
            @Override
            public void onClick(View v) {
                mCallback.onAddressSended(new Restaurant(editTitle.getText().toString(),
                        editAddress.getText().toString(), editNumber.getText().toString(),
                        editContent.getText().toString()));
            }
        });
        //글자수 제한 이벤트
        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 500) {
                    Toast.makeText(getActivity(), "글자는 500자 까지 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                    //editContent.setText(editContent.getText().subSequence(0, s.length()-1));
                }
                txtCount.setText("글자수 : " + s.length() + "/500" );
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
        return v;
    }
}
