/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.List;
import java.util.Objects;
import view.Parametri;

/**
 *
 * @author alber
 */
public class StatoFDA {

    private List<StatoReteRidenominato> stati;
    private String osservabilita;
    private String nome;
    
    public StatoFDA(List<StatoReteRidenominato> stati, String osservabilita) {
        this.stati = stati;
        java.util.Collections.sort(stati);        
        this.osservabilita = osservabilita;
        this.nome = creaNome();
    }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(Parametri.PARENTESI_TONDA_A);
        for (StatoReteRidenominato statoRete : stati) {
            s.append(statoRete.toString());
        }
        s.append(Parametri.PARENTESI_TONDA_C);
        
        return s.toString();
        
    }
    
    public List<StatoReteRidenominato> getStati() {
        return stati;
    }
    
    public String getOsservabilita() {
        return osservabilita;
    }
    
    public void setOsservabilita(String osservabilita) {
        this.osservabilita = osservabilita;
    }

    public String getNome() {
        return nome;
    }
    
    private String creaNome() {
        StringBuilder s = new StringBuilder();
        s.append(this.stati.get(0).getNome());
        for (int i=1; i< stati.size();i++) {
            s.append(Parametri.SPAZIO);
            s.append(stati.get(i).getNome());
        }
    return s.toString();
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
    
}
