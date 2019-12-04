package com.example.kevin.gameofmata;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class savedGames extends AppCompatActivity {

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_games);

        lv = (ListView) findViewById(R.id.savedGamesList);
        createList();

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long l) {
                final String selectedItem = (String) parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new  AlertDialog.Builder(savedGames.this);
                builder.setTitle("Apagar Jogo");
                builder.setMessage("Quer realmente apagar o jogo selecionado?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(deleteSavedGame(selectedItem)){
                            Toast.makeText(getApplicationContext(),"Jogo apagado com sucesso.", Toast.LENGTH_SHORT).show();
                            createList();
                        }
                    }
                });
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing.
                    }
                });
                builder.create();
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);
                ArrayList<Player> jogadores = loadSavedGame(selectedItem);
                SharedPreferences sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                for(int i=0 ; i<jogadores.size(); i++){
                    editor.putInt("PlayerBall"+i,jogadores.get(i).bola);
                    editor.putString("PlayerName"+i,jogadores.get(i).nome);
                    editor.putInt("PlayerPoints"+i,jogadores.get(i).pontos);
                }
                editor.putInt("size",jogadores.size());
                editor.commit();

                Intent goToSecondActivity = new Intent();
                goToSecondActivity.setClass(getApplicationContext(), secondActivity.class);
                goToSecondActivity.putParcelableArrayListExtra("players", jogadores);
                startActivity(goToSecondActivity);
            }
        });

    }

    private void createList() {
        ArrayList<String> filesinfolder = GetFiles("/data/data/com.example.kevin.gameofmata/files");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filesinfolder);
        lv.setAdapter(adapter);
    }


    public ArrayList<String> GetFiles(String directorypath) {
        ArrayList<String> Myfiles = new ArrayList<String>();
        File f = new File(directorypath);
        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0) {
            return null;
        } else {
            for (int i = 0; i < files.length; i++)
                Myfiles.add(files[i].getName());
        }

        ArrayList<String> savedFiles = new ArrayList<String>();
        for(int i = 0 ; i< Myfiles.size() ; i++){
            if(Myfiles.get(i).contains("Saved_Game"))
                savedFiles.add(Myfiles.get(i));
        }
        return savedFiles;
    }


    public ArrayList<Player> loadSavedGame(String fileName) {

        ArrayList<Player> arraylist = new ArrayList<Player>();
        try
        {
            FileInputStream fis = this.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            arraylist = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
            return arraylist;
        }catch(ClassNotFoundException c){
            System.out.println("Class not found");
            c.printStackTrace();
            return arraylist;
        }
        return arraylist;
    }


    public boolean deleteSavedGame(String fileName) {
        File dir = getFilesDir();
        File file = new File(dir, fileName);
        boolean deleted = file.delete();
        return deleted;
    }
}
