/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elaborato_1718.v2.pkg0;

import java.util.ArrayList;

/**
 *
 * @author Alb
 */
public class Cammino {

    private ArrayList<StatoReteAbstract> cammino;
    private boolean isTraiettoria;

    public Cammino() {
        cammino = new ArrayList<>();
        isTraiettoria = false;
    }

    public void add(StatoRete statoRete) {
        cammino.add(statoRete);
    }

    public boolean contains(StatoRete s) {
        return cammino.contains(s);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (StatoReteAbstract stato : cammino) {
            if (stato.getTransizionePrecedente() != null) {
                s.append(stato.getTransizionePrecedente().toString());
                s.append(Parametri.A_CAPO);
            }
            s.append(stato.toString());
            s.append(Parametri.A_CAPO);
        }
        return s.toString();
    }

    /*
    public StatoRete duplicaUltimoStato() {
        StatoRete ultimoStatoDuplicato = new StatoRete(cammino.get(cammino.size() - 1).getLink(), cammino.get(cammino.size() - 1).getStati());
        return cammino.set(cammino.size() - 1, ultimoStatoDuplicato);
    }
     */
    /**
     * this ha tutti gli n-1 elementi uguali a camminoAttuale (con stesso
     * indirizzo di memoria, l'ultimo elemento è ancora uguale, ma ha un
     * indirizzo di memoria diverso
     *
     * @param camminoAttuale
     *//*
    void copiaCammino(Cammino camminoAttuale) {
        ArrayList<StatoRete> vecchioCammino = camminoAttuale.getCammino();
        for (int i = 0; i < vecchioCammino.size() - 1; i++) {//copia tutti gli elementi fino al penultimo
            this.cammino.add(vecchioCammino.get(i));
        }
        StatoRete ultimoStato = new StatoRete(vecchioCammino.get(vecchioCammino.size() - 1).getLink(), vecchioCammino.get(vecchioCammino.size() - 1).getStati());
        this.cammino.add(ultimoStato);
    }*/

    void copiaCammino(Cammino camminoAttuale) {
        ArrayList<StatoReteAbstract> vecchioCammino = camminoAttuale.getCammino();
        for (int i = 0; i < vecchioCammino.size(); i++) {//copia tutti gli elementi
            this.cammino.add(vecchioCammino.get(i));
        }
    }

    public StatoReteAbstract getUltimoStato() {
        return this.cammino.get(this.cammino.size() - 1);
    }
    
    public void rimuoviUltimoStato(){
        this.cammino.remove(cammino.size()-1);
    }

    public ArrayList<StatoReteAbstract> getCammino() {
        return cammino;
    }

    /**
     * Controlla se e' una traiettoria, ovvero se il suo ultimo stato e' uno
     * stato finale
     *
     * @return
     */
    public boolean isTraiettoria() {
        if (cammino.get(cammino.size() - 1).isFinale()) {
            isTraiettoria = true;
        }
        return isTraiettoria;
    }

    public void setIsTraiettoria(boolean isTraiettoria) {
        this.isTraiettoria = isTraiettoria;
    }

    public void togliFinoAPrimaDelloStato(StatoRete s) {
        int j = cammino.size() - 1;
        while (cammino.get(j) != s) {
            cammino.remove(j);
            j--;
        }
    }

}
