package com.example.kevin.gameofmata;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RecordsActivity extends AppCompatActivity {

    TabelaPlayers tabelaPlayers = new TabelaPlayers();
    ListView listaDeJogadores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        Intent caller = getIntent();
        //tabelaPlayers = caller.getParcelableExtra("tabelaPlayers");

        tabelaPlayers = loadSavedGame("rankingFile");
        listaDeJogadores = (ListView) findViewById(R.id.listView);
        if(tabelaPlayers!=null){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tabelaPlayers.getPlayerList());
            listaDeJogadores.setAdapter(adapter);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Tabela de Ranking vazia!", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    public TabelaPlayers loadSavedGame(String fileName) {

        TabelaPlayers tabelaRank = null;
        try
        {
            FileInputStream fis = this.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            tabelaRank = (TabelaPlayers) ois.readObject();
            ois.close();
            fis.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
            return tabelaRank;
        }catch(ClassNotFoundException c){
            Toast toast = Toast.makeText(getApplicationContext(), "Erro ao abrir ficheiro do Ranking!", Toast.LENGTH_SHORT);
            toast.show();
            c.printStackTrace();
            return tabelaRank;
        }
        return tabelaRank;
    }


    public void resetTable(View view) {
        tabelaPlayers.resetTabela();
//        File dir = getFilesDir();
//        File file = new File(dir, "rankingFile");
//        file.delete();
        saveRankingFile();
        listaDeJogadores = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tabelaPlayers.getPlayerList());
        listaDeJogadores.setAdapter(adapter);
    }

    private void saveRankingFile() {

        try{
            FileOutputStream fos = openFileOutput("rankingFile", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tabelaPlayers);
            oos.close();
            fos.close();
        } catch(FileNotFoundException ex){
            Toast toast = Toast.makeText(getApplicationContext(), "Erro ao guardar Rankings!", Toast.LENGTH_SHORT);
            toast.show();
        }
        catch(IOException ex){
            Toast toast = Toast.makeText(getApplicationContext(), "Erro ao guardar Rankings!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
