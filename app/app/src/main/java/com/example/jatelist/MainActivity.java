package com.example.jatelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private JatetxeaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rvMusicas = (RecyclerView) findViewById(R.id.rv_musicas);

        glm = new GridLayoutManager(this, 2);
        rvMusicas.setLayoutManager(glm);
        adapter = new JatetxeaAdapter(dataSet());
        rvMusicas.setAdapter(adapter);

    }

    private ArrayList<Jatetxea> dataSet() {
        ArrayList<Jatetxea> data = new ArrayList<>();
        data.add(new Jatetxea("Radioactive", "Imagine Dragons", R.drawable.img_imagine_dragons));
        data.add(new Jatetxea("Radioactive", "Imagine Dragons", R.drawable.img_imagine_dragons));

        return data;
    }

}