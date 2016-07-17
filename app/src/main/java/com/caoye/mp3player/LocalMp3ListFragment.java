package com.caoye.mp3player;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.caoye.model.Mp3Info;
import com.caoye.utils.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class LocalMp3ListFragment extends Fragment {
    private ListView mp3InfoList;
    private List<Mp3Info> infos = null;
    private View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.mp3_list, container, false);

        FileUtils fileUtils = new FileUtils();
        infos = fileUtils.getMp3Files();

        List<HashMap<String, String>> list = new ArrayList<>();
        for (Iterator iterator = infos.iterator(); iterator.hasNext();) {
            Mp3Info info = (Mp3Info) iterator.next();
            HashMap<String, String> map = new HashMap<>();
            map.put("mp3_name", info.getMp3Name());
            map.put("mp3_size", info.getMp3Size());
            list.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), list, R.layout.mp3_info_item,
                new String[] {"mp3_name", "mp3_size"},
                new int[] {R.id.mp3_name, R.id.mp3_size});

        mp3InfoList = (ListView) v.findViewById(R.id.mp3Info_list);
        mp3InfoList.setAdapter(adapter);
        mp3InfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (infos != null) {
                    Mp3Info info = infos.get(i);
                    Intent intent = new Intent();
                    intent.putExtra("mp3Info", info);
                    intent.setClass(getContext(), PlayerActivity.class);
                    startActivity(intent);
                }
            }
        });

        return v;
    }


}
