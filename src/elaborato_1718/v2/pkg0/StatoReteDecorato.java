package elaborato_1718.v2.pkg0;

import java.util.List;
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
public class StatoReteDecorato extends StatoRete {

    private List<String> decorazione;

    public StatoReteDecorato(Evento[] link, Stato[] stati, int _numero, List<String> _decorazione) {
        super(link, stati, _numero);
        decorazione = _decorazione;
        java.util.Collections.sort(decorazione);
//      Implementation note: This implementation is a stable, adaptive,
//        iterative mergesort that requires far fewer than n lg(n) comparisons when
//        the input array is partially sorted, while offering the performance of a
//        traditional mergesort when the input array is randomly ordered.If the 
//        input array is nearly sorted, the implementation requires approximately n comparisons.
//        Temporary storage
//        requirements vary from a small constant for nearly sorted  input arrays
//        to n/2 object references for randomly ordered  input arrays.
//    
    }

    public StatoReteDecorato(StatoReteAbstract stato, List<String> _decorazione) {
        super(stato);
        if (stato.getTransizionePrecedente() != null) {
            setTransizionePrecedente(stato.getTransizionePrecedente());
        }
        decorazione = _decorazione;
        if (decorazione != null) {
            java.util.Collections.sort(decorazione);
        }
    }

    
    
    public StatoReteDecorato(StatoReteDecorato statoReteDecorato) {
        super(statoReteDecorato);
        decorazione = statoReteDecorato.getDecorazione();
    }

    public String getNumeroToString() {
        StringBuilder s = new StringBuilder();
        s.append(getNumero());
        s.append(Parametri.SPAZIO);
        s.append(Parametri.PARENTESI_GRAFFA_A);
        if (decorazione == null) {
            s.append(Parametri.INSIEME_VUOTO);
        } else {
            for (int i = 0; i < decorazione.size(); i++) {
                if (i > 0) {
                    s.append(Parametri.VIRGOLA);
                    s.append(Parametri.SPAZIO);
                }
                s.append(decorazione.get(i));
            }
        }
        s.append(Parametri.PARENTESI_GRAFFA_C);
        return s.toString();

    }

//    public String toString() {
//        StringBuilder s = new StringBuilder();
//        s.append(super.toString());
//
//        return s.toString();
//    }
    /**
     * Override del metodo per poter implementare il metodo contains() in un
     * arrayList di cammini
     *
     * Due StatoReteDecorato sono uguale se hanno lo stesso numero e la stessa
     * decorazione
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatoReteDecorato)) {
            return false;
        }
        StatoReteDecorato stato2 = (StatoReteDecorato) o;

        if (decorazione == null ^ stato2.getDecorazione() == null) { //^ e' l'operatore logico XOR
            return false;
        }
        if (decorazione == null && stato2.getDecorazione() == null) {
            if (getNumero() == stato2.getNumero()) {
                return true;
            }
            return false;
        }

        if (getNumero() == stato2.getNumero() && decorazione.equals(stato2.getDecorazione())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(getNumero(), decorazione);
        return hash;
    }

    public List<String> getDecorazione() {
        return decorazione;
    }

    public void setDecorazione(List<String> decorazione) {
        this.decorazione = decorazione;
    }

    
    
}
