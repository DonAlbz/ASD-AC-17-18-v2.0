/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import view.Parametri;

/**
 *
 * @author alber
 */
public class StatoDFA implements StatoInterface {

    private List<StatoReteRidenominato> stati;
    private String osservabilita;
    private String nome;
    private boolean isFinale;
    private List<List<String>> diagnosi;

    public StatoDFA(List<StatoReteRidenominato> stati, String osservabilita) {
        this.stati = stati;
        java.util.Collections.sort(stati);
        this.osservabilita = osservabilita;
        this.nome = creaNome();
        this.isFinale = calcolaIsFinale();
        if (isFinale) {
//            System.out.printf(this.nome + "\t");
            this.diagnosi = effettuaDiagnosi();
//            System.out.println(diagnosi.toString());
        }
    }

    public String toString() {
//        StringBuilder s = new StringBuilder();
//        s.append(Parametri.PARENTESI_TONDA_A);
//        for (StatoReteRidenominato statoRete : stati) {
//            s.append(statoRete.toString());
//        }
//        s.append(Parametri.PARENTESI_TONDA_C);
//        
//        return s.toString();
        return nome;
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
        for (int i = 1; i < stati.size(); i++) {
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
        if (!(o instanceof StatoDFA)) {
            return false;
        }
        StatoDFA stato2 = (StatoDFA) o;

        if (this.getNome().equals(stato2.getNome()) && this.getOsservabilita().equals(stato2.getOsservabilita())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(getNome() + getOsservabilita());
        return hash;
    }

    private boolean calcolaIsFinale() {
        for (StatoReteRidenominato s : stati) {
            if (s.isFinale()) {
                return true;
            }
        }
        return false;
    }

    private List<List<String>> effettuaDiagnosi() {
        List<List<String>> daRitornare = new ArrayList<>();
        for (StatoReteRidenominato s : stati) {
//            if (s.getDecorazione() != null) {
            List<String> decorazioneDaInserire = s.getDecorazione();
            if (decorazioneDaInserire == null) {
                decorazioneDaInserire = new ArrayList<>();
                decorazioneDaInserire.add(Parametri.INSIEME_VUOTO);
            }
            if (!daRitornare.contains(decorazioneDaInserire)) {
                daRitornare.add(decorazioneDaInserire);
            }

//            }
        }
        return daRitornare;
    }

}
