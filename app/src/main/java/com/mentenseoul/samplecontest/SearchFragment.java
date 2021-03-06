package com.mentenseoul.samplecontest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class SearchFragment extends Fragment {
    static EditText searchText;
    static String modelName;
    Button listButton;
    private FragmentActivity myContext;
    String KEY = "vAcRfh9RCNLcxdQqt1DrGHvWEiAuMlci";
    static String addr;
    XmlData xmlData = new XmlData();
    String name;
    String model;
    String rank;
    String company;
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchText = view.findViewById(R.id.searchText);
        listButton = view.findViewById(R.id.listButton);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                modelName = searchText.getText().toString(); // modelname ????????????
                if(searchText.getText().toString().equals("")){
                    Toast.makeText(myContext, "???????????? ??????????????????!", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    running();
                    return true;
                }
            }

        });
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] list = {"???????????????", "?????????????????????", "????????????", "????????????????????????", "?????????", "???????????????", "???????????????"};

                AlertDialog.Builder oDialog = new AlertDialog.Builder(getContext(),
                        android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                oDialog.setTitle("????????? ???????????????")
                        .setItems(list, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                listButton.setText(list[which]);
                                addr="";
                                if (list[which] == "???????????????"){
                                    addr = "https://eep.energy.or.kr/new_api/certi_148.aspx";
                                } else if (list[which] == "?????????????????????"){
                                    addr = "https://eep.energy.or.kr/new_api/certi_143.aspx";
                                } else if (list[which] == "????????????"){
                                    addr = "https://eep.energy.or.kr/new_api/certi_128.aspx";
                                } else if (list[which] == "????????????????????????"){
                                    addr = "https://eep.energy.or.kr/new_api/certi_129.aspx";
                                } else if (list[which] == "?????????"){
                                    addr = "https://eep.energy.or.kr/new_api/certi_145.aspx";
                                } else if (list[which] == "???????????????"){
                                    addr = "https://eep.energy.or.kr/new_api/certi_121.aspx";
                                } else if (list[which] == "???????????????") {
                                    addr = "https://eep.energy.or.kr/new_api/certi_132.aspx";
                                }
                            }
                        })
                        .setCancelable(true)
                        .show();

            }
        });
    }

    private void FragmentView(int activity){

        switch (activity){
            case 1:
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("list", xmlData);
                startActivity(intent);
                break;

        }

    }

    public class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            Document doc = null;
            URL url;
            StringBuffer sb;
            try {
                InputStream is = null;
                InputStreamReader isr = null;
                url = new URL(addr + "?key=" + KEY + "&modelname=" + modelName);
                is = url.openStream();
                isr = new InputStreamReader(is, "utf-8");

                Log.d("test2", isr.toString());

                sb = new StringBuffer();
                int c;
                while ((c = isr.read()) != -1) {sb.append((char) c);}
                isr.close(); is.close();

//                Log.d("test3", sb.toString());

                return sb.toString();

            } catch (Exception e) {
                Log.d("test", e.toString());

            }
            return "Error!!";
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String name;
            String model;
            String rank;
            String company;
            try{
                String[] array1 = s.split("<???????????????>");
                name = array1[1].split("</???????????????>")[0];
                String[] array2 = s.split("<?????????>");
                model = array2[1].split("</?????????>")[0];
                String[] array3 = s.split("<????????????>");
                rank = array3[1].split("</????????????>")[0];
                String[] array4 = s.split("<????????????>");
                company = array4[1].split("</????????????>")[0];

            } catch (Exception e){
                name = "??????";
                model = "??????";
                rank = "??????";
                company = "??????";
            }
            xmlData.setName(name);
            xmlData.setCompany(company);
            xmlData.setModel(model);
            xmlData.setRank(rank);
//            Log.d("test10", "name: " + name + " model: " + model);
            FragmentView(1);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    final Handler handler = new Handler();

    private void running() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Message msg = handler.obtainMessage();
//                handler.sendMessage(msg);

            }
        }).start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new MyAsyncTask().execute();
            }
        },1000);
    }
}