package com.caoye.mp3player;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.caoye.download.HttpDownloader;
import com.caoye.model.Mp3Info;
import com.caoye.mp3player.service.DownloadService;
import com.caoye.xml.Mp3ListContentHandler;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

public class RemoteMp3ListFragment extends Fragment {
    private static final int UPDATE = 1;
    private static final int ABOUT = 2;
    private List<Mp3Info> infos = null;
    ListView mp3InfoList;
    private View v = null;

    public class MyAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String xmlPath = AppConstant.URL.BASE_URL + "resources.xml";
            HttpDownloader httpDownloader = new HttpDownloader();
            String xmlStr = httpDownloader.download(xmlPath);
            return xmlStr;
        }

        @Override
        protected void onPostExecute(String xmlStr) {
            updateListView(xmlStr);
            super.onPostExecute(xmlStr);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.mp3_list, container,false);
        mp3InfoList = (ListView) v.findViewById(R.id.mp3Info_list);
        mp3InfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mp3Info info = infos.get(i);
                Intent intent = new Intent();
                intent.putExtra("mp3Info", info);
                intent.setClass(adapterView.getContext(), DownloadService.class);
                getActivity().startService(intent);
            }
        });

        MyAsyncTask myTask = new MyAsyncTask();
        myTask.execute();

        return v;
    }

    //when user clicks MENU button, call this method
    //we can add buttons in this method
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, UPDATE, 1, R.string.mp3list_update);
        menu.add(0, ABOUT, 2, R.string.mp3list_about);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == UPDATE) {
            MyAsyncTask myTask = new MyAsyncTask();
            myTask.execute();
        } else if (item.getItemId() == ABOUT) {

        }
        return super.onOptionsItemSelected(item);
    }

    private void updateListView(String xmlStr) {
        infos = parseXml(xmlStr);
        SimpleAdapter adapter = buildAdapter(infos);
        ListView mp3InfoList = (ListView) v.findViewById(R.id.mp3Info_list);
        mp3InfoList.setAdapter(adapter);
    }

    private SimpleAdapter buildAdapter(List<Mp3Info> infos) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for(Iterator iterator = infos.iterator();iterator.hasNext();) {
            Mp3Info info = (Mp3Info) iterator.next();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("mp3_name", info.getMp3Name());
            map.put("mp3_size", info.getMp3Size());
            list.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), list, R.layout.mp3_info_item,
                new String[] {"mp3_name", "mp3_size"},
                new int[] {R.id.mp3_name, R.id.mp3_size});

        return adapter;
    }

    private List<Mp3Info> parseXml(String xmlStr) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        List<Mp3Info> infos = new ArrayList<>();
        try {
            XMLReader reader = factory.newSAXParser().getXMLReader();
            Mp3ListContentHandler handler = new Mp3ListContentHandler();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new StringReader(xmlStr)));

            infos = handler.getInfos();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return infos;
    }
}
