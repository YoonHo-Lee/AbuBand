package com.example.tacademy.abuband.Temperature;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dacer.androidcharts.LineView;
import com.example.tacademy.abuband.NetworkManager;
import com.example.tacademy.abuband.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TemperatureFragment extends Fragment {


    public TemperatureFragment() {
        // Required empty public constructor
    }


    ArrayList<Integer> tempList;
    ArrayList<String> dateList;
    ArrayList<String> x_axis;
    ArrayList<ArrayList<Integer>> dataLists;
    TextView textMainMessage, textMaintempState;

    LineView lineView;

    //그래프에 찍히는 갯수
    int randomint = 15;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //루트뷰 선언
        final View rootView = inflater.inflate(R.layout.fragment_temperature, container, false);
        //그래프 선언
        lineView = (LineView)rootView.findViewById(R.id.graph_mainTemp);

        textMainMessage = (TextView) rootView.findViewById(R.id.text_mainMessage);
        textMaintempState = (TextView) rootView.findViewById(R.id.main_tempState);


        //초기화
        tempList = new ArrayList<Integer>();    //온도 데이터
        dateList = new ArrayList<String>();     //시간 데이터
        x_axis = new ArrayList<String>();       //x축에 표시

//        //x축에 표시될 내용
//        for (int i=0; i<randomint; i++){
//            x_axis.add(String.valueOf(i + 1 + "09:42:16"));
//        }
//        lineView.setBottomTextList(x_axis);





        //그래프 내의 x축을 도트로 표시
        lineView.setDrawDotLine(true);

        //누르면 수치 표시되는거같음
        lineView.setShowPopup(LineView.SHOW_POPUPS_NONE);

        Button lineButton = (Button)rootView.findViewById(R.id.line_button);
        lineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressBar mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
                mProgressBar.setProgress((int) (Math.random() * 1000 % 61));

            }
        });
        searchTemps("LEEKR203NR5");

        return rootView;
    }





    /******************** 네 트 워 크 ********************/
    private void searchTemps(final String serial) {
        if (!TextUtils.isEmpty(serial)) {
            NetworkManager.getInstance().getTemperature(getContext(), serial, new NetworkManager.OnResultListener<AbuTemps>() {

                StringBuffer sb = new StringBuffer();

                @Override
                public void onSuccess(AbuTemps result) {
                    for (TemperatureItemData item : result.result) {
                        tempList.add((int)((item.temp - 35) * 10));
                        dateList.add(item.date.substring(11,19));
                    }
                    //온도 데이터 테스트
//                    for (int i : tempList) {
//                        sb.append(i + " / ");
//                    }

                    //시간 데이터 테스트
                    for(String s : dateList)   {
                        sb.append(s + " / ");
                    }


                    textMainMessage.setText(sb);
//                    Toast.makeText(getContext(), serial + "성공...!!!", Toast.LENGTH_SHORT).show();

                    //그래프를 그려주는 dataLists를 생성하고, 온도데이터가 있는 tempList를 넣어준다.
                    dataLists = new ArrayList<ArrayList<Integer>>();
                    dataLists.add(tempList);

                    //x축에 표시될 내용
                    lineView.setBottomTextList(dateList);
                    //그래프 데이터를 그래프에 출력
                    lineView.setDataList(dataLists);

                }

                @Override
                public void onFail(int code) {
                    Toast.makeText(getContext(), "error : " + code, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    /************* End of Network **************************/



}