/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Automa;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author alber
 */
public class StatoRete extends StatoReteAbstract implements Serializable {

    private Transizione transizioneEseguita;

    public StatoRete(Evento[] link, Stato[] stati, int _numero) {
        super(link, stati, _numero);
    }

    public StatoRete(StatoReteAbstract statoDaClonare) {
        super(statoDaClonare.getLink().clone(), statoDaClonare.getStati().clone(), statoDaClonare.getNumero());
        setNome(statoDaClonare);
    }

    public void setTransizioneEseguita(Transizione transizioneEseguita) {
        this.transizioneEseguita = transizioneEseguita;
    }

    public Transizione getTransizioneEseguita() {
        return transizioneEseguita;
    }

    public boolean isAbilitato(List<Automa> automi) {
        for (int i = 0; i < automi.size(); i++) {
            if (automi.get(i).isAbilitato(this.getLink())) {
                return true;
            }
        }
        return false;
    }

    public boolean isAbilitato(List<Automa> automi, List<StatoInterface> statiAutomaRiconoscitore) {
        boolean abilitato = false;
        List<List<Transizione>> transizioniAbilitate = new ArrayList<>();
        for (int i = 0; i < automi.size(); i++) {
            if (automi.get(i).isAbilitato(this.getLink())) {
                abilitato = true;
                transizioniAbilitate.add(automi.get(i).getTransizioneAbilitata());
            }
        }
        //Controllo se le transizioni abilitate soddisfano l'automa riconoscitore dell'espressione
        if(statiAutomaRiconoscitore.size()!=0){
        for(int i=0; i< statiAutomaRiconoscitore.size() && abilitato; i++){
            StatoDFA statoConsiderato = (StatoDFA) statiAutomaRiconoscitore.get(i);
            for(int j = 0 ; j< transizioniAbilitate.size(); j++){
                for ( int k = 0; k< transizioniAbilitate.get(j).size(); k++){
                    String osservabilitaTransizione = transizioniAbilitate.get(j).get(k).getOsservabilita();
                    if (osservabilitaTransizione == null || osservabilitaTransizione.equals(statoConsiderato.getOsservabilita()))
                        return true;
                }
            }
        }
        }
        else{
            for(int j = 0 ; j< transizioniAbilitate.size(); j++){
                for ( int k = 0; k< transizioniAbilitate.get(j).size(); k++){
                    String osservabilitaTransizione = transizioniAbilitate.get(j).get(k).getOsservabilita();
                    if (osservabilitaTransizione == null )
                        return true;
                }
                }
        }
        return false;
    }

    
    
    public boolean isAbilitatoScenario(List<Automa> automi, List<StatoInterface> statiAutomaRiconoscitore) {
        boolean abilitato = false;
        List<List<Transizione>> transizioniAbilitate = new ArrayList<>();
        for (int i = 0; i < automi.size(); i++) {
            if (automi.get(i).isAbilitato(this.getLink())) {
                abilitato = true;
                transizioniAbilitate.add(automi.get(i).getTransizioneAbilitata());
            }
        }
        //Controllo se le transizioni abilitate soddisfano l'automa riconoscitore dell'espressione
        if(statiAutomaRiconoscitore.size()!=0){
        for(int i=0; i< statiAutomaRiconoscitore.size() && abilitato; i++){
            StatoDFA statoConsiderato = (StatoDFA) statiAutomaRiconoscitore.get(i);
            for(int j = 0 ; j< transizioniAbilitate.size(); j++){
                for ( int k = 0; k< transizioniAbilitate.get(j).size(); k++){
                    String descrizioneTransizione = transizioniAbilitate.get(j).get(k).getDescrizione();
                    if (descrizioneTransizione.equals(statoConsiderato.getOsservabilita()))
                        return true;
                }
            }
        }
        }       
        return false;
    }
    
    
    
    
    
    
    /**
     * Override del metodo per poter implementare il metodo contains() in un
     * arrayList di cammini
     *
     * Due StatoRete sono uguali se hanno la stessa descrizione
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatoReteAbstract)) {
            return false;
        }
        StatoReteAbstract stato2 = (StatoReteAbstract) o;
        /*
        for (int i = 0; i < link.length; i++) {
            if (!this.link[i].equals(stato2.getLink()[i])) {
                return false;
            }
        }
        for (int i = 0; i < stati.length; i++) {
            if (!this.stati[i].equals(stato2.getStati()[i])) {
                return false;
            }
        }*/
        if (getDescrizione().equals(stato2.getDescrizione())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(getDescrizione());
        /*
        int hash = 7;        
        hash = 97 * hash + Arrays.deepHashCode(this.link);
        hash = 97 * hash + Arrays.deepHashCode(this.stati);*/
        return hash;
    }

    

}
