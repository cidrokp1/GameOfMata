package com.example.kevin.gameofmata;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kevin on 05/12/2017.
 */

public class TabelaPlayers implements Parcelable, Serializable {

    ArrayList<Player> jogadores;

    public ArrayList<Player> getJogadores() {
        return jogadores;
    }

    public TabelaPlayers() {
        this.jogadores = new ArrayList<>();
    }


    protected TabelaPlayers(Parcel in) {
        jogadores = in.createTypedArrayList(Player.CREATOR);
    }

    public static final Creator<TabelaPlayers> CREATOR = new Creator<TabelaPlayers>() {
        @Override
        public TabelaPlayers createFromParcel(Parcel in) {
            return new TabelaPlayers(in);
        }

        @Override
        public TabelaPlayers[] newArray(int size) {
            return new TabelaPlayers[size];
        }
    };

    public void resetTabela() {
        this.jogadores = new ArrayList<>();
    }


    public void sortTabela() {
        // Sorting
        Collections.sort(this.jogadores, new Comparator<Player>() {
            @Override
            public int compare(Player player, Player t1) {
                int n = jogadores.size();
                Player temp;
                for(int i=0; i < n; i++){
                    for(int j=1; j < (n-i); j++){
                        if(jogadores.get(j-1).tabelaPontos < jogadores.get(j).tabelaPontos){
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


    public void addPlayerWin(Player p) {
        boolean isThereAny=false;
        for(int i =0 ; i<this.jogadores.size(); i++){
            if(jogadores.get(i).nome.equalsIgnoreCase(p.nome)){
                isThereAny = true;
                jogadores.get(i).tabelaPontos ++;
            }
        }
        if(isThereAny==false){
            jogadores.add(p);
            jogadores.get(jogadores.size()-1).tabelaPontos++;
        }
        sortTabela();
    }

    public List<String> getPlayerList(){
        LinkedList<String> lista = new LinkedList<String>();
        for(int i =0 ; i<this.jogadores.size(); i++){
            lista.add(jogadores.get(i).nome +"  -  "+ jogadores.get(i).tabelaPontos +" pontos");
        }
        return lista;
    }

    public String playersRankString() {

        String lista = "";
        for(int i =0 ; i<this.jogadores.size(); i++){
            lista = lista + jogadores.get(i).nome +"  -  "+ jogadores.get(i).tabelaPontos +" pontos";
        }
        return lista;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(jogadores);
    }
}
