package Model;

import java.io.Serializable;
import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Stato utilizzato come nodo di uno spazio comportamentale, la ricerca di uno
 * StatoReteRidenominato avviene attraverso la variabile "nome".
 *
 * @author alber
 */
public class StatoReteRidenominato extends StatoReteAbstract implements Comparable<StatoReteRidenominato>, Serializable{

    /**
     * Crea uno StatoRete ridenominato
     *
     * @param link
     * @param stati
     * @param _numero
     */
    public StatoReteRidenominato(Evento[] link, Stato[] stati, int _numero) {
        super(link, stati, _numero);
        setNome(Integer.toString(_numero));
    }

    /**
     * Crea uno StatoRete ridenominato
     *
     * @param link
     * @param stati
     * @param _numero
     */
    public StatoReteRidenominato(StatoReteAbstract s) {
        super(s.getLink(), s.getStati(), s.getNumero());
        setTransizionePrecedente(s.getTransizionePrecedente());
        setDecorazione(s.getDecorazione());
        setNome(s);        
        setDescrizione(s.getDescrizione());
    }

    /**
     * Crea un nuovo stato con la transizione inizializzata
     *
     * @param stato
     * @param transizione
     */
    public StatoReteRidenominato(StatoReteAbstract s, Transizione t) {
        super(s.getLink(), s.getStati(), s.getNumero());
        setTransizionePrecedente(t);
        setNome(s);
        setDescrizione(s.getDescrizione());
    }

    public String getNomeToString() {
        return getNome();
    }

    /**
     * Override del metodo per poter implementare il metodo contains() in un
     * arrayList di cammini
     *
     * Due StatoReteRidenominato sono uguale se hanno lo stesso numero
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
        StatoReteRidenominato stato2 = (StatoReteRidenominato) o;

        if (getNome().equals(stato2.getNome())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(getNome());
        return hash;
    }

    @Override
    public int compareTo(StatoReteRidenominato t) {
       return this.getNome().compareTo(t.getNome());
    }

    
    
}
