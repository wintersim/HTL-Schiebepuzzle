package cc.catgasm.HTLWSlidingPuzzle;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SlidingPuzzle {
    /*Seitenlänge*/
    private int size;

    /*Karten werden als int gespeichert
    * Jede Karte bekommt einen wert von 0 bis <size - 1>
    * Wenn das Array sortiert ist --> Spieler hat gewonnen
    */
    private List<Tile> cards;

    /*Timer start*/
    private long startingMs;

    /*Timer Ende*/
    private long endingMs;


    public SlidingPuzzle(int size) {
        this.size = size;
        cards = new ArrayList<>(size);

        init();
    }

    private void init(){
        /*Karten array befüllen*/
        for (int i = 0; i < (size * size); i++) {
            cards.add(new Tile(i % size, i / size, i));
        }

        Log.e("TEST", String.valueOf(cards));
    }

    public int getSize() {
        return size;
    }

    public List<Tile> getCards() {
        return cards;
    }

    public void switchCards(int a, int b){
        Tile ta = cards.get(a);
        Tile tb = cards.get(b);
        int tmpn = ta.getNumber();
        ta.setNumber(tb.getNumber());
        tb.setNumber(tmpn);
    }

    public Tile getTouched(int x, int y) {
        for (Tile card : cards) {
            if(card.getRect().contains(x,y)){
                return card;
            }
        }
        return null;
    }
}
