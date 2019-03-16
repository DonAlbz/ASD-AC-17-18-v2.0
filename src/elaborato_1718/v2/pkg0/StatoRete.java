/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elaborato_1718.v2.pkg0;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 *
 * @author alber
 */
public class StatoRete extends StatoReteAbstract {
    
    private Transizione transizioneEseguita; 
    

    public StatoRete(Evento[] link, Stato[] stati, int _numero) {
        super(link, stati, _numero);
    }

    public StatoRete(StatoReteAbstract statoDaClonare) {
        super(statoDaClonare.getLink().clone(), statoDaClonare.getStati().clone(), statoDaClonare.getNumero());
    }
    
    public void setTransizioneEseguita(Transizione transizioneEseguita) {
        this.transizioneEseguita = transizioneEseguita;
    }

    public Transizione getTransizioneEseguita() {
        return transizioneEseguita;
    }

    public boolean isAbilitato(List<Automa> automi){
        for(int i=0;i<automi.size();i++){
            if (automi.get(i).isAbilitato(this.getLink())){
                return true;
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
