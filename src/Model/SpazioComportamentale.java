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
import java.util.stream.Collectors;

/**
 *
 * @author alber
 */
public class SpazioComportamentale {

    private StatoReteAbstract root;
    private Map<StatoReteAbstract, List<StatoReteAbstract>> verticiAdiacenti;

    public SpazioComportamentale() {
        verticiAdiacenti = new HashMap<>();
    }

    public void aggiungiVertice(StatoReteAbstract vertice) {
        verticiAdiacenti.putIfAbsent(vertice, new ArrayList<>());
    }

    public void rimuoviVertice(StatoReteAbstract statoDaRimuovere) {
        verticiAdiacenti.values()
                .stream()
                .map(e -> e.remove(statoDaRimuovere))
                .collect(Collectors.toList());
        verticiAdiacenti.remove(statoDaRimuovere);
    }

    public void aggiungiLato(StatoReteAbstract partenza, StatoReteAbstract arrivo) {
        if(partenza.getClass() == StatoReteDecorato.class && arrivo.getClass() == StatoReteDecorato.class){
            partenza =(StatoReteDecorato)partenza;
            arrivo=(StatoReteDecorato)arrivo;
        }
        ArrayList<StatoReteAbstract> listaDiAdiacenza;
        listaDiAdiacenza = (ArrayList<StatoReteAbstract>) verticiAdiacenti.get(partenza);
        if (!listaDiAdiacenza.contains(arrivo)) {
            listaDiAdiacenza.add(arrivo);
        }
    }

   public void rimuoviLato(StatoReteAbstract partenza, StatoReteAbstract arrivo) {
        List<StatoReteAbstract> statiArrivo = verticiAdiacenti.get(partenza);
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

    public StatoReteAbstract getRoot() {
        return root;
    }

    public void setRoot(StatoReteAbstract root) {
        this.root = root;
    }

    public boolean contains(StatoReteAbstract stato) {
        return verticiAdiacenti.containsKey(stato);
    }

    public List<StatoReteAbstract> getVerticiAdiacenti(StatoReteAbstract s) {
        return verticiAdiacenti.get(s);
    }

    public String toString() {
        boolean isDecorato = false;
        boolean isRidenominato;
        if(root.getClass()==StatoReteDecorato.class)
            isDecorato=true;
        StringBuilder s = new StringBuilder();
        s.append(Parametri.SPAZIO_COMPORTAMENTALE_ETICHETTA);
        s.append(Parametri.A_CAPO);
        for (StatoReteAbstract statoPartenza : verticiAdiacenti.keySet()) {
            s.append(statoPartenza.toString());
            s.append(Parametri.TAB);           
            s.append(Parametri.FRECCIA);
            s.append(Parametri.TAB);
            int i = 0;
            for (StatoReteAbstract statoArrivo : verticiAdiacenti.get(statoPartenza)) {
                if (i > 0) {
                    s.append(Parametri.VIRGOLA);
                    s.append(Parametri.SPAZIO);
                }
                if(isDecorato)
                    statoArrivo = (StatoReteDecorato)statoArrivo;
                s.append(Parametri.PARENTESI_TONDA_A);
                    s.append(statoArrivo.toStringShort());//                
                s.append(Parametri.PARENTESI_TONDA_C);
                i++;
            }
            s.append(Parametri.A_CAPO);
        }
        return s.toString();
    }

    
 
    
}
