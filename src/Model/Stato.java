/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alb
 */
public class Stato implements Serializable{

    private List<Transizione> transizioni;
    private ArrayList<Transizione> transizioniAbilitate;
    private Transizione transizioneEseguita;
    private String descrizione;

    public Stato(String s) {
        this.descrizione = s;
        transizioni = new ArrayList<>();
        transizioniAbilitate = new ArrayList<>();
    }

    public Stato(String s, List<Transizione> _transizioni) {
        this.descrizione = s;
        transizioni = _transizioni;
        transizioniAbilitate = new ArrayList<>();
    }

    boolean isAbilitato(Evento[] _link) {
        transizioniAbilitate.clear();
        boolean resp = false;
        for (int i = 0; i < transizioni.size(); i++) {
            if (transizioni.get(i).isAbilitato(_link)) {
                transizioniAbilitate.add(transizioni.get(i));
                resp = true;
            }
        }
        return resp;
    }

    ArrayList<Transizione> getTransizioniAbilitate() {
        return transizioniAbilitate;
    }

    public Stato scatta(Transizione t, Evento[] _link) {
        transizioneEseguita = t;
        return t.scatta(_link);
    }

    public Stato scatta(Evento[] _link) {
        transizioneEseguita = transizioniAbilitate.get(0);
        return transizioneEseguita.scatta(_link);
    }

    public void addTransazione(Transizione t) {
        transizioni.add(t);
    }

    public boolean equals(Stato s2) {
        return descrizione.equals(s2.getDescrizione());
    }

    public String toString() {
        return descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public List<Transizione> getTransizioni() {
        return transizioni;
    }

    public Transizione getTransizioneEseguita() {
        return transizioneEseguita;
    }

 

    
    
}
