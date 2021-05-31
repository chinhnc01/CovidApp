package com.example.vqhcovid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class InformationRssActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayList,arrLink;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_rss);

    // https://vnexpress.net/rss/suc-khoe.rss, https://vietnamnet.vn/rss/suc-khoe.rss

        new ReadRss().execute("https://vtc.vn/rss/suc-khoe.rss");

        listView = (ListView) findViewById(R.id.lvtitle);
        arrayList = new ArrayList<>();
        arrLink = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(InformationRssActivity.this,WebviewActivity.class);
                intent.putExtra("link tin tuc",arrLink.get(i));
                startActivity(intent);
            }
        });

    }

    // ddocj tep rss
    private class ReadRss extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line="";
                while ((line = bufferedReader.readLine()) != null){
                    content.append(line);
                }
                bufferedReader.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            XMLDOMParser parser = new XMLDOMParser();

            Document document = parser.getDocument(s);
            NodeList nodeList =document.getElementsByTagName("item");


            String tieude = "";

            // doc list
            for(int i=0;i< nodeList.getLength();i++){
                Element element = (Element) nodeList.item(i);
                tieude = parser.getValue(element,"title");
                arrayList.add(tieude);
                arrLink.add(parser.getValue(element,"link"));


            }
            adapter.notifyDataSetChanged();


        }
    }
}