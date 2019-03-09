/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elaborato_1718.v2.pkg0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author alber
 */
public class SpazioComportamentale {

    private Map<StatoReteAbstract, List<StatoReteAbstract>> verticiAdiacenti;

    public SpazioComportamentale() {
        verticiAdiacenti = new HashMap<>();
    }

    void aggiungiVertice(StatoReteAbstract vertice) {
        verticiAdiacenti.putIfAbsent(new StatoReteRidenominato(vertice), new ArrayList<>());
    }

    public void rimuoviVertice(StatoReteAbstract statoDaRimuovere) {
        verticiAdiacenti.values()
                .stream()
                .map(e -> e.remove(statoDaRimuovere))
                .collect(Collectors.toList());
        verticiAdiacenti.remove(statoDaRimuovere);
    }

    public void aggiungiLato(StatoReteAbstract partenza, StatoReteAbstract arrivo) {
        ArrayList<StatoReteAbstract> listaDiAdiacenza;
        listaDiAdiacenza = (ArrayList<StatoReteAbstract>) verticiAdiacenti.get(partenza);
        if (!listaDiAdiacenza.contains(arrivo)) {
            listaDiAdiacenza.add(arrivo);
        }
    }

    void rimuoviLato(StatoReteAbstract partenza, StatoReteAbstract arrivo) {
        List<StatoReteAbstract> statiArrivo = verticiAdiacenti.get(partenza);
        if (statiArrivo != null) {
            statiArrivo.remove(arrivo);
        }
        //se lo stato rimosso non ha stati destinazione, viene rimosso tra gli stati di partenza
        if (verticiAdiacenti.get(arrivo) == null || verticiAdiacenti.get(arrivo).isEmpty()) {
            rimuoviVertice(arrivo);
        }

    }

    void provaARimuovereVertice(StatoReteRidenominato statoDaTogliere) {
        if (verticiAdiacenti.get(statoDaTogliere) == null || verticiAdiacenti.get(statoDaTogliere).isEmpty()) {
            rimuoviVertice(statoDaTogliere);
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(Parametri.SPAZIO_COMPORTAMENTALE_ETICHETTA);
        s.append(Parametri.A_CAPO);
        for (StatoReteAbstract statoPartenza : verticiAdiacenti.keySet()) {
            s.append(statoPartenza);
            s.append(Parametri.TAB);
            s.append(Parametri.FRECCIA);
            s.append(Parametri.TAB);
            for (StatoReteAbstract statoArrivo : verticiAdiacenti.get(statoPartenza)) {
                s.append(Parametri.PARENTESI_A);
                s.append(statoArrivo.getTransizionePrecedente());
                s.append(Parametri.VIRGOLA);
                s.append(Parametri.SPAZIO);
                
                s.append(Parametri.PARENTESI_A);
                s.append(statoArrivo.toString());
                s.append(Parametri.PARENTESI_C);
                s.append(Parametri.PARENTESI_C);
                s.append(Parametri.TAB);
            }
            s.append(Parametri.A_CAPO);
        }

        return s.toString();
    }

}
