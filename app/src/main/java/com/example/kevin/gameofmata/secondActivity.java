package com.example.kevin.gameofmata;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.acl.Group;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class secondActivity extends AppCompatActivity {

    LinearLayout colunaPlayers;
    ArrayList<Player> jogadores = new ArrayList<>();
    TabelaPlayers tabelaPlayers;
    int winner;
    ImageButton rankingButton;
    boolean answer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent caller = getIntent();
        winner=0;
        tabelaPlayers = loadRankGame("rankingFile");
        jogadores = caller.getParcelableArrayListExtra("players");
        colunaPlayers = (LinearLayout) findViewById(R.id.playerColuna);
        rankingButton = (ImageButton) findViewById(R.id.rankingButton);


//        SharedPreferences sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
//        for(int i = 0 ; i<jogadores.size() ; i++){
//            jogadores.get(i).nome = sharedPref.getString("PlayerName"+i,"nothing found");
//            jogadores.get(i).bola = sharedPref.getInt("PlayerBall"+i,0);
//            jogadores.get(i).pontos = sharedPref.getInt("PlayerPontos"+i,0);
//        }
        createLayout();
    }


    public void createLayout() {
        for (int i = 0; i < jogadores.size(); i++) {
            sortPlayers();
            //Criar os elementos
            final LinearLayout playerRow = new LinearLayout(getApplicationContext());
            TextView playerInfo = new TextView(getApplicationContext());
            Button add = new Button(getApplicationContext());
            Button remove = new Button(getApplicationContext());

            //Copiar os estilos dos elementos (parametros dos que já estão no XML)
            final LinearLayout row = (LinearLayout) findViewById(R.id.playerRowPoints);
            final TextView playerInfoParm = (TextView) findViewById(R.id.playerInfo);
            Button addInfoParm = (Button) findViewById(R.id.playerButtonAdd);
            Button remInfoParm = (Button) findViewById(R.id.playerButtonRemove);
            remove.setText("-"); remove.setTextSize(18);
            add.setText("+");add.setTextSize(18);

            add.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    LinearLayout columLayParent = (LinearLayout) v.getParent();
                    int position = colunaPlayers.indexOfChild(columLayParent);
                    TextView playerInfo = new TextView(getApplicationContext());

                    playerInfo.setText(editPontuation(1, position));
                    columLayParent.removeViewAt(0);
                    columLayParent.addView(playerInfo, 0, playerInfoParm.getLayoutParams());
                    winnerOfGame();
                }
            });

            remove.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    LinearLayout columLayParent = (LinearLayout) v.getParent();
                    int position = colunaPlayers.indexOfChild(columLayParent);

                    TextView playerInfo = new TextView(getApplicationContext());
                    playerInfo.setText(editPontuation(0, position));

                    columLayParent.removeViewAt(0);
                    columLayParent.addView(playerInfo, 0, playerInfoParm.getLayoutParams());
                }
            });

            playerInfo.setText(editPontuation(-1, i));
            playerRow.addView(playerInfo, playerInfoParm.getLayoutParams());
            playerRow.addView(add, addInfoParm.getLayoutParams());
            playerRow.addView(remove, remInfoParm.getLayoutParams());
            colunaPlayers.addView(playerRow, row.getLayoutParams());
        }

    }


    public String editPontuation(int operation, int position) {

        if (operation == 0) {   //subtrair
            if (jogadores.get(position).pontos > 0) {
                jogadores.get(position).pontos = jogadores.get(position).pontos - 1;
            }
        } else if (operation == 1) {  //adicionar
            if (jogadores.get(position).pontos < 6) {
                jogadores.get(position).pontos = jogadores.get(position).pontos + 1;
            }
        }
        String texto = "";
        switch (jogadores.get(position).pontos) {
            case 0:
                texto = jogadores.get(position).nome + " (Bola " + jogadores.get(position).bola + ")  -           ";
                break;
            case 1:
                texto = jogadores.get(position).nome + " (Bola " + jogadores.get(position).bola + ")  -  /        ";
                break;
            case 2:
                texto = jogadores.get(position).nome + " (Bola " + jogadores.get(position).bola + ")  -  X        ";
                break;
            case 3:
                texto = jogadores.get(position).nome + " (Bola " + jogadores.get(position).bola + ")  -  X /      ";
                break;
            case 4:
                texto = jogadores.get(position).nome + " (Bola " + jogadores.get(position).bola + ")  -  X X      ";
                break;
            case 5:
                texto = jogadores.get(position).nome + " (Bola " + jogadores.get(position).bola + ")  -  X X /    ";
                break;
            case 6:
                texto = jogadores.get(position).nome + " (Bola " + jogadores.get(position).bola + ")  -  X X X    ";
                break;
            default:
                break;
        }
        return texto;
    }


    public void winnerOfGame() {
        int playersLeft = 0;
        for(int i=0 ; i<jogadores.size(); i++){
            if(jogadores.get(i).pontos<6){
                playersLeft++;
                winner=i;
            }
        }
        if(playersLeft==1) {
            AlertDialog.Builder builder = new  AlertDialog.Builder(secondActivity.this);
            builder.setTitle("Fim do Jogo");
            builder.setMessage("Tem a certeza que o jogo acabou?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    tabelaPlayers.addPlayerWin(jogadores.get(winner));
                    saveRankingFile();
                    rankingButton.performClick();
                }
            });
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.create();
            AlertDialog dialog = builder.create();
            dialog.show();
        }
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


    public TabelaPlayers loadRankGame(String fileName) {

        TabelaPlayers tabelaRank = new TabelaPlayers();
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


    public void saveFileGame(View view) {

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy(HH:mm:ss)");
        Date today = Calendar.getInstance().getTime();
        String time = df.format(today);
        try{
            FileOutputStream fos = openFileOutput("Saved_Game_" + time, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(jogadores);
            oos.close();
            fos.close();

            Toast toast = Toast.makeText(getApplicationContext(), "Jogo guardado com sucesso!", Toast.LENGTH_SHORT);
            toast.show();
        } catch(FileNotFoundException ex){
            Toast toast = Toast.makeText(getApplicationContext(), "Erro ao guardar o jogo!", Toast.LENGTH_SHORT);
            toast.show();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }


    public void sortPlayers() {
        // Sorting
        Collections.sort(this.jogadores, new Comparator<Player>() {
            @Override
            public int compare(Player player, Player t1) {
                int n = jogadores.size();
                Player temp;
                for(int i=0; i < n; i++){
                    for(int j=1; j < (n-i); j++){
                        if(jogadores.get(j-1).bola > jogadores.get(j).bola){
                            //swap the elements!
                            temp = jogadores.get(j-1);
                            jogadores.set(j-1,jogadores.get(j));
                            jogadores.set(j,temp);
                        }
                    }
                }
                return 0;
            }
        });
    }


    public void startActivity(View view) {
        Intent goToRecordsActivity = new Intent();
        goToRecordsActivity.setClass(this, RecordsActivity.class);
        //goToRecordsActivity.putExtra("tabelaPlayers",tabelaPlayers);
        startActivity(goToRecordsActivity);
    }
}
