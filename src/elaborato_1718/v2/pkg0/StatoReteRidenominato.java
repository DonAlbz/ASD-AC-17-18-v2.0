package elaborato_1718.v2.pkg0;

import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alber
 */
public class StatoReteRidenominato extends StatoReteAbstract {
    
    public StatoReteRidenominato(Evento[] link, Stato[] stati, int _numero) {
        super(link, stati, _numero);
    }
    
    public StatoReteRidenominato(StatoReteAbstract s){
        super (s.getLink(), s.getStati(), s.getNumero());       
        setTransizionePrecedente(s.getTransizionePrecedente());        
    }
    
    /**Crea un nuovo stato con la transizione inizializzata
     *
     * @param stato
     * @param transizione
     */
    public StatoReteRidenominato(StatoReteAbstract s, Transizione t){
        super (s.getLink(), s.getStati(), s.getNumero());       
        setTransizionePrecedente(t);        
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
        StatoReteAbstract stato2 = (StatoReteAbstract) o;
     
        if (getNumero()==stato2.getNumero()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(getNumero());
        return hash;
    }
    
    
    
}
