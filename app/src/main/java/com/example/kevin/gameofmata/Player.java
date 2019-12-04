package com.example.kevin.gameofmata;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by kevin on 05/12/2017.
 */

public class Player implements Parcelable, Serializable {

    String nome;
    int bola;
    int pontos;
    int tabelaPontos;

    public Player(String nome, int bola, int pontos) {
        this.nome = nome;
        this.bola = bola;
        this.pontos = pontos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getBola() {
        return bola;
    }

    public void setBola(int bola) {
        this.bola = bola;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public int getTabelaPontos() {
        return tabelaPontos;
    }

    public void setTabelaPontos(int tabelaPontos) {
        this.tabelaPontos = tabelaPontos;
    }

    protected Player(Parcel in) {
        nome = in.readString();
        bola = in.readInt();
        pontos = in.readInt();
        tabelaPontos = in.readInt();
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nome);
        parcel.writeInt(bola);
        parcel.writeInt(pontos);
        parcel.writeInt(tabelaPontos);
    }
}
