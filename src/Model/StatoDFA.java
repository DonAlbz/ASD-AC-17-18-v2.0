/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import view.Parametri;

/**
 *
 * @author alber
 */
public class StatoDFA implements StatoInterface, Serializable {

    private List<StatoInterface> stati;
    private String osservabilita;
    private String nome;
    private boolean isFinale;
    private List<List<String>> diagnosi;
    private String statoRiconoscitoreEspressione;
    //Da controllare nel debug
    int hashCode;

    public StatoDFA(List<StatoInterface> stati, String osservabilita) {
        this.stati = new ArrayList<>();

        if (stati.get(0).getClass() == StatoReteRidenominato.class) {
            //Metodo un po' ripetitivo, cercando alternative non e' trato trovato nulla.       
            List<StatoReteRidenominato> statiRidenominati = new ArrayList<>();
            for (StatoInterface s : stati) {
                statiRidenominati.add((StatoReteRidenominato) s);
            }
            java.util.Collections.sort(statiRidenominati);
            this.stati.addAll(stati);
        }
        this.osservabilita = osservabilita;
        this.nome = creaNome();
        this.isFinale = calcolaIsFinale();
        if (isFinale) {
//            System.out.printf(this.nome + "\t");
            this.diagnosi = effettuaDiagnosi();
//            System.out.println(diagnosi.toString());
        }
    }

    public StatoDFA(String nome, String osservabilita) {
        this.nome = nome;
        this.osservabilita = osservabilita;
    }

    public StatoDFA(StatoDFA statoDaCopiare, String nome) {
        this.stati = statoDaCopiare.getStati();
        this.isFinale = statoDaCopiare.isFinale();
        this.diagnosi = statoDaCopiare.getDiagnosi();
        this.osservabilita = statoDaCopiare.getOsservabilita();
        this.nome = nome;
        //hashCode = hashCode();
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

    public List<StatoInterface> getStati() {
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
        
        if (getOsservabilita() == null ^ stato2.getOsservabilita() == null) { //^ e' l'operatore logico XOR
            return false;
        }
        if (getOsservabilita() == null && stato2.getOsservabilita() == null) {
            if (getNome().equals(stato2.getNome())) {
                return true;
            }
            return false;
        }
        if (this.getNome().equals(stato2.getNome()) && this.getOsservabilita().equals(stato2.getOsservabilita())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash;
        if (this.osservabilita != null) {
            hash = Objects.hash(getNome() + getOsservabilita());
        } else {
            hash = Objects.hash(getNome());
        }
        hashCode = hash;
        return hash;
    }

    private boolean calcolaIsFinale() {
        for (StatoInterface s : stati) {
            if (s.getClass() == StatoReteRidenominato.class) {
                if (((StatoReteRidenominato) s).isFinale()) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<List<String>> effettuaDiagnosi() {
        List<List<String>> daRitornare = new ArrayList<>();
        for (StatoInterface s : stati) {
//            if (s.getDecorazione() != null) {
            if (s.getClass() == StatoReteRidenominato.class) {
                List<String> decorazioneDaInserire = ((StatoReteRidenominato) s).getDecorazione();
                if (decorazioneDaInserire == null) {
                    decorazioneDaInserire = new ArrayList<>();
                    decorazioneDaInserire.add(Parametri.INSIEME_VUOTO);
                }
                if (!daRitornare.contains(decorazioneDaInserire)) {
                    daRitornare.add(decorazioneDaInserire);
                }

//            }
            }
        }
        return daRitornare;
    }

    public boolean isFinale() {
        return isFinale;
    }

    public List<List<String>> getDiagnosi() {
        return diagnosi;
    }

    public void setIsFinale(boolean valore) {
        isFinale = valore;
    }

    public void setNome(String nuovoNome) {
        this.nome = nuovoNome;
    }

    public String getStatoRiconoscitoreEspressione() {
        return statoRiconoscitoreEspressione;
    }

    public void setStatoRiconoscitoreEspressione(String statoRiconoscitoreEspressione) {
        this.statoRiconoscitoreEspressione = statoRiconoscitoreEspressione;
    }
}
