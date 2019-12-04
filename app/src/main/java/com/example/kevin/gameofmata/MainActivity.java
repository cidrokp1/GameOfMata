package com.example.kevin.gameofmata;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static android.widget.GridLayout.OnClickListener;

public class MainActivity extends AppCompatActivity {

    ArrayList<Player> jogadores = new ArrayList<>();
    ArrayList<Player> listOfPlayers = new ArrayList<>();

    NumberPicker numPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LinearLayout colunaPlayers = (LinearLayout) findViewById(R.id.colunaPlayers);
        FloatingActionButton randomBallsButton = (FloatingActionButton) findViewById(R.id.shuffleButton);
        FloatingActionButton addPlayerButton = (FloatingActionButton) findViewById(R.id.add) ;
        FloatingActionButton startPlayerButton = (FloatingActionButton) findViewById(R.id.start);
        FloatingActionButton removePlayerButton = (FloatingActionButton) findViewById(R.id.remove);
        numPicker = (NumberPicker) findViewById(R.id.numbPicker7);
        NumberPicker numPicker1 = (NumberPicker) findViewById(R.id.numbPicker8);

        numPicker1.setMinValue(1);
        numPicker1.setMaxValue(13);
        numPicker1.setWrapSelectorWheel(false);
        numPicker.setMinValue(1);
        numPicker.setMaxValue(13);
        numPicker.setWrapSelectorWheel(true);


        randomBallsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                int row = colunaPlayers.getChildCount();
                Integer[] ballNumbers = new Integer[row];
                for (int i = 1; i < row+1; i++) {
                    ballNumbers[i-1] = i;
                }
               Collections.shuffle(Arrays.asList(ballNumbers));
                LinearLayout linha;

                for(int i=0;i<row;i++){
                    NumberPicker numberPicker = new NumberPicker(getApplicationContext());
                    numberPicker.setMaxValue(13);
                    numberPicker.setMinValue(1);
                    linha = (LinearLayout) colunaPlayers.getChildAt(i);

                    numberPicker.setValue(ballNumbers[i]);
                    linha.removeViewAt(2);
                    linha.addView(numberPicker,2,numPicker.getLayoutParams());
                }
            }
        });

        //BOTÃO ADICIONAR ------------------------------------------------------------------------
        addPlayerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int row = colunaPlayers.getChildCount();
                if(row<13){
                    addPlayer(row+1,(getString(R.string.newName)).concat(" "+(row+1)) , 1);
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Só existe a possibilidade de jogar 13 pessoas de cada vez!", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        //BOTÃO REMOVER ------------------------------------------------------------------------
        removePlayerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int row = colunaPlayers.getChildCount();
                if(row>2){
                    colunaPlayers.removeViewAt(row-1);
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Não é possível jogar o jogo do Mata sozinho!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void addPlayer(int position, String name, int ball) {
        LinearLayout colunaPlayers = (LinearLayout) findViewById(R.id.colunaPlayers);
        TextView playerNum = new TextView(getApplicationContext());
        playerNum.setText(new StringBuilder().append(getString(R.string.newPlayer)).append(" "+(position)));
        EditText playerName = new EditText(getApplicationContext());
        playerName.setText(name);

        NumberPicker numberPicker = new NumberPicker(getApplicationContext());
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(13);

        LinearLayout playerRow = new LinearLayout(getApplicationContext());
        playerRow.setLayoutParams(((LinearLayout)findViewById(R.id.playerRow)).getLayoutParams());
        playerRow.setBackgroundResource(R.color.white);
        TextView playerNumero = (TextView)findViewById(R.id.textView9);
        EditText playerNome =  (EditText)findViewById(R.id.editText8);
        playerName.setTextSize(12);

        playerRow.addView(playerNum, playerNumero.getLayoutParams());
        playerRow.addView(playerName, playerNome.getLayoutParams());
        playerRow.addView(numberPicker, numPicker.getLayoutParams());
        colunaPlayers.addView(playerRow);
    }


    public void startSecondActivity(View view) {
        SharedPreferences sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        LinearLayout colunaPlayers = (LinearLayout) findViewById(R.id.colunaPlayers);
        int rows = colunaPlayers.getChildCount();
        Player p ;
        listOfPlayers = new ArrayList<Player>();
        for(int i=0 ; i<rows; i++){
            LinearLayout linha = (LinearLayout) colunaPlayers.getChildAt(i);
            EditText nome = (EditText) linha.getChildAt(1);
            NumberPicker bola = (NumberPicker) linha.getChildAt(2);
            p = new Player(nome.getText().toString(),bola.getValue(),0);
            listOfPlayers.add(p);
            editor.putInt("PlayerBall"+i,bola.getValue());
            editor.putString("PlayerName"+i,nome.getText().toString());
            editor.putInt("PlayerPoints"+i,0);
        }
        editor.putInt("size",listOfPlayers.size());
        editor.commit();

        if((verifyBall(listOfPlayers) && verifyNames(listOfPlayers))){
            Intent goToSecondActivity = new Intent();
            goToSecondActivity.setClass(this, secondActivity.class);
            goToSecondActivity.putParcelableArrayListExtra("players", listOfPlayers);
            startActivity(goToSecondActivity);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "Nomes dos jogadores ou bolas repetidos!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    private boolean verifyBall(ArrayList<Player> jogadores) {
        int num=0;
        for(int i=0 ; i<jogadores.size(); i++){
            int bola = jogadores.get(i).bola;
            for(int j=0 ; j<jogadores.size(); j++){
                if(jogadores.get(j).bola == bola){
                    num ++;
                }
                if(num>1){
                    return false;
                }
            }
            num=0;
        }

        return true;

    }

    private boolean verifyNames(ArrayList<Player> jogadores) {
        int num=0;
        for(int i=0 ; i<jogadores.size(); i++){
           String nome = jogadores.get(i).nome;
            for(int j=0 ; j<jogadores.size(); j++){
                if(jogadores.get(j).nome.equalsIgnoreCase(nome)){
                    num ++;
                }
                if(num>1){
                    return false;
                }
            }
            num=0;
        }
       return true;
    }

    public void loadSavedGame(View view) {
        Intent goToSecondActivity = new Intent();
        goToSecondActivity.setClass(this, savedGames.class);
        startActivity(goToSecondActivity);
    }
}
