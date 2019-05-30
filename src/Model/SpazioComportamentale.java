/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import view.Parametri;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author alber
 */
public class SpazioComportamentale {

    private StatoInterface root;
    private Map<StatoInterface, List<StatoInterface>> verticiAdiacenti;

    public SpazioComportamentale() {
        verticiAdiacenti = new HashMap<>();
    }

    public void aggiungiVertice(StatoInterface vertice) {
        verticiAdiacenti.putIfAbsent(vertice, new ArrayList<>());
    }

    public void rimuoviVertice(StatoInterface statoDaRimuovere) {
        verticiAdiacenti.values()
                .stream()
                .map(e -> e.remove(statoDaRimuovere))
                .collect(Collectors.toList());
        verticiAdiacenti.remove(statoDaRimuovere);
    }

    public void aggiungiLato(StatoInterface partenza, StatoInterface arrivo) {
        if (partenza.getClass() == StatoReteDecorato.class && arrivo.getClass() == StatoReteDecorato.class) {
            partenza = (StatoReteDecorato) partenza;
            arrivo = (StatoReteDecorato) arrivo;
        }
        ArrayList<StatoInterface> listaDiAdiacenza;
        listaDiAdiacenza = (ArrayList<StatoInterface>) verticiAdiacenti.get(partenza);
        if (!listaDiAdiacenza.contains(arrivo)) {
            listaDiAdiacenza.add(arrivo);
        }
    }

    public void rimuoviLato(StatoInterface partenza, StatoInterface arrivo) {
        List<StatoInterface> statiArrivo = verticiAdiacenti.get(partenza);
        if (statiArrivo != null) {
            statiArrivo.remove(arrivo);
        }
        //se lo stato rimosso non ha stati destinazione, viene rimosso tra gli stati di partenza
        if (verticiAdiacenti.get(arrivo) == null || verticiAdiacenti.get(arrivo).isEmpty()) {
            rimuoviVertice(arrivo);
        }

    }

    public void provaARimuovereVertice(StatoReteRidenominato statoDaTogliere) {
        if (verticiAdiacenti.get(statoDaTogliere) == null || verticiAdiacenti.get(statoDaTogliere).isEmpty()) {
            rimuoviVertice(statoDaTogliere);
        }
    }

    public boolean isAbilitato(StatoRete statoAttuale) {
        if (verticiAdiacenti.get(statoAttuale) == null) {//TODO: forse si può togliere, da provare
            return false;
        }
        return verticiAdiacenti.get(statoAttuale).size() > 0;
    }

    public StatoInterface getRoot() {
        return root;
    }

    public void setRoot(StatoInterface root) {
        this.root = root;
    }

    public boolean contains(StatoReteAbstract stato) {
        return verticiAdiacenti.containsKey(stato);
    }

    public List<StatoInterface> getVerticiAdiacenti(StatoInterface s) {
        return verticiAdiacenti.get(s);
    }

    public String toString() {
        boolean isDecorato = false;
        boolean isRidenominato;
        if (root.getClass() == StatoReteDecorato.class) {
            isDecorato = true;
        }
        StringBuilder s = new StringBuilder();
        s.append(Parametri.SPAZIO_COMPORTAMENTALE_ETICHETTA);
        s.append(Parametri.A_CAPO);
        for (StatoInterface statoPartenza : verticiAdiacenti.keySet()) {
            s.append(statoPartenza.toString());
            s.append(Parametri.TAB);
            s.append(Parametri.FRECCIA);
            s.append(Parametri.TAB);
            int i = 0;
            for (StatoInterface statoArrivo : verticiAdiacenti.get(statoPartenza)) {
                if (i > 0) {
                    s.append(Parametri.VIRGOLA);
                    s.append(Parametri.SPAZIO);
                }
                if (isDecorato) {
                    statoArrivo = (StatoReteDecorato) statoArrivo;
                }
                s.append(Parametri.PARENTESI_TONDA_A);
//                if (((StatoReteAbstract) statoArrivo).getOsservabilita() != null) {
//                    s.append(((StatoReteAbstract) statoArrivo).getOsservabilita());
//                } else {
//                    s.append(Parametri.EVENTO_NULLO);
//                }
//                    s.append(Parametri.SPAZIO);
                    s.append(statoArrivo.getNome());
//                s.append(((StatoReteAbstract) statoArrivo).toStringShort());//                
                s.append(Parametri.PARENTESI_TONDA_C);
                i++;
            }
            s.append(Parametri.A_CAPO);
        }
        return s.toString();
    }
    
    public String toStringAutomaRiconoscitore() {
        boolean isDecorato = false;
        boolean isRidenominato;
        if (root.getClass() == StatoReteDecorato.class) {
            isDecorato = true;
        }
        StringBuilder s = new StringBuilder();
        s.append(Parametri.AUTOMA_RICONOSCITORE_ETICHETTA);
        s.append(Parametri.A_CAPO);
        for (StatoInterface statoPartenza : verticiAdiacenti.keySet()) {
            s.append(statoPartenza.toString());
            s.append(Parametri.TAB);
            s.append(Parametri.FRECCIA);
            s.append(Parametri.TAB);
            int i = 0;
            for (StatoInterface statoArrivo : verticiAdiacenti.get(statoPartenza)) {
                if (i > 0) {
                    s.append(Parametri.VIRGOLA);
                    s.append(Parametri.SPAZIO);
                }
                if (isDecorato) {
                    statoArrivo = (StatoReteDecorato) statoArrivo;
                }
                s.append(Parametri.PARENTESI_TONDA_A);
//                if (((StatoReteAbstract) statoArrivo).getOsservabilita() != null) {
//                    s.append(((StatoReteAbstract) statoArrivo).getOsservabilita());
//                } else {
//                    s.append(Parametri.EVENTO_NULLO);
//                }
//                    s.append(Parametri.SPAZIO);
                    s.append(statoArrivo.getNome());
                    StatoDFA stato = (StatoDFA) statoArrivo;
                    if(stato.isFinale()){
                        s.append(Parametri.SPAZIO);
                        s.append(Parametri.FINALE);
                    }
//                s.append(((StatoReteAbstract) statoArrivo).toStringShort());//                
                s.append(Parametri.PARENTESI_TONDA_C);
                i++;
            }
            s.append(Parametri.A_CAPO);
        }
        return s.toString();
    }

    public Set<StatoInterface> getVertici(){
       return verticiAdiacenti.keySet();
    }
    
}
