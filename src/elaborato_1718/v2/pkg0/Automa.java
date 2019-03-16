/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elaborato_1718.v2.pkg0;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Alb
 */
public class Automa {
    
    private List<Stato> stati;
    private Stato statoCorrente;
    private String descrizione;
    private ArrayList<Transizione> transizioniAbilitate;
    private Transizione transizioneEseguita;
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public Automa(String s) {
        this.descrizione = s;
        stati = new ArrayList<>();
        transizioniAbilitate = new ArrayList<>();
    }
    
    boolean isAbilitato(Evento[] _link) {
        boolean resp = statoCorrente.isAbilitato(_link);
        if (resp) {
            transizioniAbilitate = statoCorrente.getTransizioniAbilitate();
        }else{
            transizioniAbilitate.clear();
        }
        return resp;
    }
    
    Transizione scatta(Transizione t, Evento[] _link) {
        transizioneEseguita = t;
        this.statoCorrente = statoCorrente.scatta(t, _link);
        return t;
    }
    
    Transizione scatta(Evento[] _link) {
        transizioneEseguita = statoCorrente.getTransizioniAbilitate().get(0);
        this.statoCorrente = statoCorrente.scatta(_link);
        return transizioneEseguita;
    }
    
    Transizione scatta(int i, Evento[] _link) {
        transizioneEseguita = statoCorrente.getTransizioniAbilitate().get(i);
        this.statoCorrente = statoCorrente.scatta(_link);
        return transizioneEseguita;
    }
    
    void addStato(Stato s) {
        stati.add(s);
    }
    
    void setStatoIniziale(Evento[] _link) {
        statoCorrente = stati.get(0);
        if (statoCorrente.isAbilitato(_link)) {
            transizioniAbilitate = statoCorrente.getTransizioniAbilitate();
        }
    }
    
    Stato getStatoCorrente() {
        return statoCorrente;
    }
    
    ArrayList<Transizione> getTransizioneAbilitata() {
        return transizioniAbilitate;
    }
    
    public void setStatoCorrente(Stato statoCorrente) {
        this.statoCorrente = statoCorrente;
    }
    
    public Automa copia() {
        Automa daRitornare = new Automa(this.getDescrizione());
        for (int i = 0; i < stati.size(); i++) {
            Stato statoDaAggiungere = new Stato(stati.get(i).getDescrizione(), stati.get(i).getTransizioni());
            
            daRitornare.addStato(statoDaAggiungere);
            if (stati.get(i).getDescrizione().equals(statoCorrente.getDescrizione())) {
                daRitornare.setStatoCorrente(statoDaAggiungere);
            }
        }
        
        //Copia delle transizioni degli stati e settaggio dello stato di destinazione delle nuove transizioni
        /* ATTENZIONE: NON CANCELLARE!!!
        for (int i = 0; i < stati.size(); i++) {
            Vector<Transizione> transizioniDelloStato = stati.get(i).getTransizioni();
            for (int j = 0; j < transizioniDelloStato.size(); j++) {
                for (int k = 0; k < stati.size(); k++) {
                    if (transizioniDelloStato.get(j).getStatoDestinazione() == stati.get(k)) {
                        Transizione t = new Transizione(transizioniDelloStato.get(j).getDescrizione(),
                                daRitornare.getStati().get(k),
                                transizioniDelloStato.get(j).getLinkIn(),
                                transizioniDelloStato.get(j).getEventoRichiesto(),
                                transizioniDelloStato.get(j).getLinkOut());
                        daRitornare.getStati().get(k).addTransazione(t);
                    }
                }
            }
            
        }*/
        return daRitornare;
    }
    
    public List<Stato> getStati() {
        return stati;
    }
    
    public String toString() {
        StringBuilder stringa = new StringBuilder();
        for (Stato s : stati) {
            stringa.append(s.getDescrizione() + " ");
        }
        stringa.append("s.corrente: " + statoCorrente.getDescrizione());
        return stringa.toString();
    }
    
    /**
     *
     * @param statoDaCercare e' la stringa che identifica lo stato da cercare nell'array 
     * @return ritorna lo stato che si stava cercando, null se lo stato non esiste
     */
    public Stato getStato(String statoDaCercare){
        Stato cercato = null;
        for(int i = 0; i<stati.size(); i++){
            if(statoDaCercare.equalsIgnoreCase(stati.get(i).getDescrizione())){
                cercato = stati.get(i);
            }
        }
        return cercato;
    }
    
    
}
