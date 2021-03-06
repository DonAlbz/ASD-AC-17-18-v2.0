package Model;

import java.io.Serializable;
import view.Parametri;
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
public class StatoReteDecorato extends StatoRete implements Serializable {

    public StatoReteDecorato(Evento[] link, Stato[] stati, int _numero, List<String> _decorazione) {
        super(link, stati, _numero);
        setDecorazione(_decorazione);
        java.util.Collections.sort(getDecorazione());
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
        setDecorazione(_decorazione);
        if (getDecorazione() != null) {
            java.util.Collections.sort(getDecorazione());
        }
    }

    public StatoReteDecorato(StatoReteDecorato statoReteDecorato) {
        super(statoReteDecorato);
        setDecorazione(statoReteDecorato.getDecorazione());
    }
    
     public StatoReteDecorato(StatoReteDecorato statoReteDecorato, int numero) {
        super(statoReteDecorato);
        super.setNumero(numero);
        setDecorazione(statoReteDecorato.getDecorazione());
    }

    public String getNumeroToString() {
        StringBuilder s = new StringBuilder();
        s.append(getNumero());
        s.append(Parametri.SPAZIO);
        s.append(Parametri.PARENTESI_GRAFFA_A);
        if (getDecorazione() == null) {
            s.append(Parametri.INSIEME_VUOTO);
        } else {
            for (int i = 0; i < getDecorazione().size(); i++) {
                if (i > 0) {
                    s.append(Parametri.VIRGOLA);
                    s.append(Parametri.SPAZIO);
                }
                s.append(getDecorazione().get(i));
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

        if (getDecorazione() == null ^ stato2.getDecorazione() == null) { //^ e' l'operatore logico XOR
            return false;
        }
        if (getNumero() != -1 && stato2.getNumero() != -1) {//AGGIUNTO DI RECENTE
            if (getDecorazione() == null && stato2.getDecorazione() == null) {
                if (getDescrizione().equals(stato2.getDescrizione())) {
                    return true;
                }
                return false;
            }

            if (getNumero() == stato2.getNumero() && getDecorazione().equals(stato2.getDecorazione())) {
                return true;
            }
        }
        if(getNumero()==-1 || stato2.getNumero()==-1){//AGGIUNTO DI RECENTE
            if(getDecorazione()==null && stato2.getDecorazione()==null){
                if(getDescrizione().equals(stato2.getDescrizione())){
                    return true;
                }
                return false;
            }
        }
        
        if(getDescrizione().equals(stato2.getDescrizione()) && getDecorazione().equals(stato2.getDecorazione())){
            return true;
        }
        return false;

    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(getNumero(), getDecorazione());
        return hash;
    }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        if (super.getTransizionePrecedente() != null) {
            s.append(super.getTransizionePrecedente());
            s.append(Parametri.VIRGOLA);
            s.append(Parametri.SPAZIO);
        }
        s.append(Parametri.PARENTESI_QUADRA_A);
        s.append(getNomeToString());
        s.append(Parametri.PARENTESI_QUADRA_C);
        s.append(Parametri.TAB);
        if (s.length() < 9) // serve per intabellare lo spazio decorato se ha uno stato iniziale più lungo di 9 caratteri
        {
            s.append(Parametri.TAB);
        }
        s.append(super.getDescrizione());
        s.append(" ");
        s.append(getDecorazione());
        return s.toString();
    }

}
