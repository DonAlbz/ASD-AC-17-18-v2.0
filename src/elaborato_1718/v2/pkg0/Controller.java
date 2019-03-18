/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elaborato_1718.v2.pkg0;

import view.Parametri;
import Model.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import view.View;

/**
 *
 * @author alber
 */
public class Controller {

    /**
     * Creazione dei cammini
     *
     */
    public static void creaSpazioComportamentale(Rete rete) {
        List<Cammino> cammini;
        LinkedList<StatoReteAbstract> statiSpazioC = new LinkedList<>();
        cammini = trovaCammini(rete, statiSpazioC);
        View.stampaCammini(cammini);
        List<Cammino> traiettorie = potatura(cammini);
        traiettorie = ridenominazione(traiettorie, statiSpazioC);
        View.stampaTraiettorie(traiettorie);
        SpazioComportamentale spazioC = new SpazioComportamentale();
        spazioC = inserisciVerticiSpazioComportamentale(spazioC, traiettorie, statiSpazioC);
        spazioC = inserisciLatiSpazioComportamentale(spazioC, traiettorie);
        rete.setSpazioC(spazioC);
        System.out.println(spazioC.toString());
//        SpazioComportamentale spazioComportamentaleDecorato = creaSpazioComportamentaleDecorato(spazioC);
//        stampaCammini(camminiDecorati);
//        System.out.println(spazioComportamentaleDecorato.toString());
    }

    public static void creaSpazioComportamentaleDecorato(Rete rete) {
        List<Cammino> camminiDecorati;
        LinkedList<StatoReteAbstract> statiDecoratiSpazioC = new LinkedList<>();
        camminiDecorati = trovaCamminiDecorati(rete.getSpazioC(), statiDecoratiSpazioC);
        View.stampaCammini(camminiDecorati);
        SpazioComportamentale spazioComportamentaleDecorato = new SpazioComportamentale();
        spazioComportamentaleDecorato = inserisciVerticiSpazioComportamentale(spazioComportamentaleDecorato, camminiDecorati, statiDecoratiSpazioC);
        spazioComportamentaleDecorato = inserisciLatiSpazioComportamentale(spazioComportamentaleDecorato, camminiDecorati);
        System.out.println(spazioComportamentaleDecorato.toString());      
    }

    private static List<Cammino> trovaCammini(Rete rete, LinkedList<StatoReteAbstract> stati) {
        List<Cammino> cammini = new ArrayList<>();
        Stack<StatoRete> pilaStato = new Stack<>();//pila dei nuovi stati
        Stack<StatoRete> pilaDiramazioni = new Stack<StatoRete>();//pila degli stati che hanno più di una transizione in uscita
        //Stack<Cammino> pilaCammino = new Stack<>();
        Cammino camminoAttuale = creaNuovoCammino(rete);//il cammino attuale diventa un nuovo cammino con gli stati degli automi e i link azzerati
        int numeroStati = -1;

        StatoRete statoRadice = creaStatoCorrente(rete, numeroStati);
        pilaStato.push(statoRadice);

        while (!pilaStato.isEmpty()) {
            StatoRete statoAttuale = pilaStato.pop();
//            

            if (!stati.contains(statoAttuale)) {//se è uno stato nuovo
                //statoAttuale.setNumero(stati.size());
                stati.add(statoAttuale);
                camminoAttuale.add(statoAttuale);
                //spazioC.aggiungiVertice(new StatoReteRidenominato(statoAttuale));
//                System.out.println(statoAttuale.toString());
                rete.setRete(statoAttuale);
                if (statoAttuale.isAbilitato(rete.getAutomi())) {
                    ArrayList<Transizione>[] transizioniAbilitate = new ArrayList[rete.getAutomi().size()];
                    int numeroTransizioniAbilitate = 0;
                    for (int i = 0; i < rete.getAutomi().size(); i++) {//se nessun automa è già scattato, si itera su tutti gli automi                        
                        rete.getAutomi().get(i).isAbilitato(rete.getLink());
                        transizioniAbilitate[i] = rete.getAutomi().get(i).getTransizioneAbilitata();//transizione abilitata diventa la transizione abilitata allo scatto nell'automa attuale                   
                        numeroTransizioniAbilitate += transizioniAbilitate[i].size();
                    }
                    ArrayList<StatoRete> statiDopoLoScatto = new ArrayList<>();
                    Transizione transizioneEseguita;
                    StatoRete copiaStatoAttuale;
                    if (numeroTransizioniAbilitate == 1) {
                        for (int i = 0; i < rete.getAutomi().size(); i++) {
                            if (rete.getAutomi().get(i).isAbilitato(rete.getLink())) {
                                transizioneEseguita = rete.getAutomi().get(i).scatta(rete.getLink());//l'automa attuale viene fatto scattare e transizione eseguita diventa la transizione che è stata eseguita
//                                statoAttuale.setTransizionePrecedente(transizioneEseguita);//la transizione eseguita viene aggiunta allo StatoRete attuale
                                StatoRete statoDopoLoScatto = creaStatoCorrente(rete.getAutomi(), rete.getLink(), numeroStati);
                                statoDopoLoScatto.setTransizionePrecedente(transizioneEseguita);
                                statiDopoLoScatto.add(statoDopoLoScatto);
                                rete.setRete(statoAttuale);
                            }
                        }
                    } else {
                        copiaStatoAttuale = creaStatoCorrente(rete.getAutomi(), rete.getLink(), numeroStati);//Provare a sostituire con creaStatoCorrente(rete)

                        for (int i = 0; i < rete.getAutomi().size(); i++) {
                            for (int j = 0; j < transizioniAbilitate[i].size(); j++) {//vengono fatte scattare tutte, ognuna su un nuovo cammino
                                rete.setRete(statoAttuale);
                                Transizione transizioneDaEseguire = transizioniAbilitate[i].get(j);
                                transizioneEseguita = rete.getAutomi().get(i).scatta(transizioneDaEseguire, rete.getLink());//viene fatta scattare la transizione da eseguire
                                copiaStatoAttuale.setTransizionePrecedente(transizioneEseguita);
                                StatoRete statoDopoLoScatto = creaStatoCorrente(rete.getAutomi(), rete.getLink(), numeroStati);
                                statoDopoLoScatto.setTransizionePrecedente(transizioneEseguita);
                                statiDopoLoScatto.add(statoDopoLoScatto);
                                pilaDiramazioni.add(statoAttuale);
                            }
                        }
                        pilaDiramazioni.pop();
                    }

                    for (StatoRete s : statiDopoLoScatto) {
                        pilaStato.push(s);
//                       
                    }
                } else {
                    //stato senza transizioni abilitate
                    Cammino nuovoCammino = new Cammino();
//                    statoAttuale.setNumero(stati.size() - 1);
                    nuovoCammino.copiaCammino(camminoAttuale);
                    cammini.add(nuovoCammino);
                    if (!pilaDiramazioni.isEmpty()) {
                        camminoAttuale.togliFinoAPrimaDelloStato(pilaDiramazioni.pop());//gli ultimi elementi del cammini vengono rimossi finchè non si incontra il primo elemento della coda
                    }
                }
            } else {
                Cammino nuovoCammino = new Cammino();
//                statoAttuale.setNumero(stati.indexOf(statoAttuale));
                StatoRete statoPrecedente = (StatoRete) camminoAttuale.getUltimoStato();
                camminoAttuale.add(statoAttuale);//                
                nuovoCammino.copiaCammino(camminoAttuale);
                cammini.add(nuovoCammino);
                if (!pilaDiramazioni.isEmpty()) {
                    camminoAttuale.togliFinoAPrimaDelloStato(pilaDiramazioni.pop());//gli ultimi elementi del cammini vengono rimossi finchè non si incontra il primo elemento della coda
                }

            }
        }
        rete.setCammini(cammini);
        return cammini;
    }

    private static ArrayList<Cammino> potatura(List<Cammino> cammini) {
        ArrayList<Cammino> traiettorie = new ArrayList<>();
        ArrayList<Cammino> notTraiettorie = new ArrayList<>();
        for (Cammino c : cammini) {
            if (c.isTraiettoria()) {
                traiettorie.add(c);
            } else {
                notTraiettorie.add(c);
            }
        }
        for (int j = 0; j < traiettorie.size(); j++) {
            for (int i = 0; i < notTraiettorie.size(); i++) {
                if (traiettorie.get(j).contains((StatoRete) notTraiettorie.get(i).getUltimoStato())) {
                    notTraiettorie.get(i).setIsTraiettoria(true);
                    traiettorie.add(notTraiettorie.get(i));
                    notTraiettorie.remove(i);
                    i--;
                }
            }
        }
        return traiettorie;
    }

    private static StatoRete creaStatoCorrente(Rete rete, int numeroStati) {
        Stato[] statoAutomi = new Stato[rete.getAutomi().size()];
        for (int i = 0; i < statoAutomi.length; i++) {
            statoAutomi[i] = rete.getAutomi().get(i).getStatoCorrente();
        }
        return new StatoRete(rete.getLink().clone(), statoAutomi, numeroStati);
    }

    private static StatoRete creaStatoCorrente(List<Automa> _automi, Evento[] _link, int numeroStati) {
        Stato[] statoAutomi = new Stato[_automi.size()];
        for (int i = 0; i < statoAutomi.length; i++) {
            statoAutomi[i] = _automi.get(i).getStatoCorrente();
        }
        return new StatoRete(_link.clone(), statoAutomi, numeroStati);
    }

    private static Cammino creaNuovoCammino(Rete rete) {
        Cammino nuovoCammino = new Cammino();
        rete.impostaStatiIniziali();
        return nuovoCammino;
    }

    private static List<Cammino> ridenominazione(List<Cammino> traiettorie, LinkedList<StatoReteAbstract> stati) {
        for (Cammino traiettoria : traiettorie) {
            ArrayList<StatoReteAbstract> statiTraiettoria = traiettoria.getCammino();
            for (int i = 0; i < statiTraiettoria.size(); i++) {
                int numeroStato;
                numeroStato = stati.indexOf(statiTraiettoria.get(i));
                StatoReteAbstract statoTraiettoria = statiTraiettoria.get(i);
                statoTraiettoria.setNumero(numeroStato);
            }
        }
        return traiettorie;
    }

    /**
     * Etichetta in maniera ordinata gli stati e li inserisce nello spazio
     * comportamentale
     *
     * @param traiettorie
     */
    private static SpazioComportamentale inserisciVerticiSpazioComportamentale(SpazioComportamentale _spazioC, List<Cammino> traiettorie, LinkedList<StatoReteAbstract> _stati) {
        ArrayList<StatoReteAbstract> tuttiGliStatiDelleTraiettorie = new ArrayList<>();
        for (Cammino traiettoria : traiettorie) {
            tuttiGliStatiDelleTraiettorie.addAll(traiettoria.getCammino());
        }
        _stati.retainAll(tuttiGliStatiDelleTraiettorie); //rimuove da stati tutti gli StatoRete che non sono contenuti nelle traiettorie
        StatoReteAbstract root = _stati.get(0);
        if (root.getClass() == StatoRete.class) {
            root.setNumero(0);
            root = new StatoReteRidenominato(_stati.get(0));
        }

        if (root.getClass() == StatoReteDecorato.class) {
            String nomeRoot = Parametri.STATO_DECORATO_PREFISSO + Integer.toString(0);
            root.setNome(nomeRoot);
            root = new StatoReteRidenominato(_stati.get(0));
        }

        _spazioC.aggiungiVertice(root);
        _spazioC.setRoot(root);
        for (int i = 1; i < _stati.size(); i++) {
            if (_stati.get(i).getClass() == StatoRete.class) {
                _stati.get(i).setNumero(i);
            }
            StatoReteRidenominato statoDaAggiungere = new StatoReteRidenominato(_stati.get(i), null);
            if (_stati.get(i).getClass() == StatoReteDecorato.class) {
                String nome = Parametri.STATO_DECORATO_PREFISSO + Integer.toString(i);
                _stati.get(i).setNome(nome);
                statoDaAggiungere.setNome(nome);
            }
            _spazioC.aggiungiVertice(statoDaAggiungere);
        }
        return _spazioC;
    }

    /**
     * Inserisce i lati (o archi) dello spazio comportamentale
     *
     * @param traiettorie
     */
    private static SpazioComportamentale inserisciLatiSpazioComportamentale(SpazioComportamentale spazioComportamentale, List<Cammino> traiettorie) {
        for (Cammino traiettoria : traiettorie) {
            ArrayList<StatoReteAbstract> statiTraiettoria = traiettoria.getCammino();
            if (statiTraiettoria.size() > 1) {
                for (int i = 1; i < statiTraiettoria.size(); i++) {

                    StatoReteAbstract statoPrecedenteTraiettoria = statiTraiettoria.get(i - 1);
                    StatoReteAbstract statoPrecedente = null;
                    if (statoPrecedenteTraiettoria.getClass() == StatoRete.class) {
                        statoPrecedente = new StatoReteRidenominato(statoPrecedenteTraiettoria);
                    }
                    if (statoPrecedenteTraiettoria.getClass() == StatoReteDecorato.class) {
                        statoPrecedente = new StatoReteRidenominato(statoPrecedenteTraiettoria);
                    }

                    StatoReteAbstract statoCorrenteTraiettoria = statiTraiettoria.get(i);
                    StatoReteAbstract statoCorrente = null;
                    if (statoCorrenteTraiettoria.getClass() == StatoRete.class) {
                        statoCorrente = new StatoReteRidenominato(statoCorrenteTraiettoria);
                    }
                    if (statoCorrenteTraiettoria.getClass() == StatoReteDecorato.class) {
                        statoCorrente = new StatoReteRidenominato(statoCorrenteTraiettoria);
//                        statoCorrente.setTransizionePrecedente(statoCorrenteTraiettoria.getTransizionePrecedente());
                    }

                    spazioComportamentale.aggiungiLato(statoPrecedente, statoCorrente);
                }
            }
        }
        return spazioComportamentale;
    }

    private static List<Cammino> trovaCamminiDecorati(SpazioComportamentale _spazioComportamentale, LinkedList<StatoReteAbstract> statiDecorati) {
        List<Cammino> camminiDecorati = new ArrayList<>();
        SpazioComportamentale spazioComportamentale = _spazioComportamentale;

//        LinkedList<StatoReteAbstract> statiDecorati = new LinkedList<>();;
        Stack<StatoReteDecorato> pilaStato = new Stack<>();//pila dei nuovi stati
        Stack<StatoReteDecorato> pilaDiramazioni = new Stack<StatoReteDecorato>();//pila degli stati che hanno più di una transizione in uscita
        //Stack<Cammino> pilaCammino = new Stack<>();
        Cammino camminoAttuale = new Cammino();//il cammino attuale diventa un nuovo cammino con gli stati degli automi e i link azzerati
        StatoReteDecorato root = new StatoReteDecorato(spazioComportamentale.getRoot(), null);
        root.setTransizionePrecedente(null);
        //spazioComportamentaleDecorato.setRoot(root);
//        spazioComportamentaleDecorato.aggiungiVertice(rootDecorata);
//        spazioComportamentaleDecorato.setRoot(rootDecorata);
        ArrayList<String> decorazione = new ArrayList<>();

        pilaStato.push(root);

        while (!pilaStato.isEmpty()) {
            StatoReteDecorato statoAttualeDecorato = pilaStato.pop();
            StatoReteRidenominato statoAttuale = new StatoReteRidenominato(statoAttualeDecorato);
            statoAttuale.setTransizionePrecedente(statoAttualeDecorato.getTransizionePrecedente());
            decorazione = (ArrayList<String>) statoAttualeDecorato.getDecorazione();

            if (!statiDecorati.contains(statoAttualeDecorato)) {//se è uno stato nuovo
                StatoReteDecorato verticeDaInserire = new StatoReteDecorato(statoAttualeDecorato);
                verticeDaInserire.setTransizionePrecedente(null);
                statiDecorati.add(statoAttualeDecorato);//TODO: sostituito, prima era passato verticeDaInserire
//                spazioComportamentaleDecorato.aggiungiVertice(verticeDaInserire);
                //statoAttuale.setNumero(stati.size());
//                statiDecorati.add(statoAttualeDecorato);
                camminoAttuale.add(statoAttualeDecorato);
                //spazioC.aggiungiVertice(new StatoReteRidenominato(statoAttuale));
//                System.out.println(statoAttuale.toString());
//                setRete(statoAttuale);
                List<StatoReteAbstract> verticiAdiacenti = spazioComportamentale.getVerticiAdiacenti(statoAttuale);
                if (verticiAdiacenti != null && verticiAdiacenti.size() > 0) {//se lo StatoRete non è uno stato finale            
                    ArrayList<StatoReteDecorato> statiSuccessivi = new ArrayList<>();
                    List<String> decorazioneAggiornata = null;

                    if (verticiAdiacenti.size() == 1) {
                        if (decorazione != null) {
                            decorazioneAggiornata = (ArrayList<String>) decorazione.clone();
                        }
                        StatoReteRidenominato statoDopoLoScatto = (StatoReteRidenominato) spazioComportamentale.getVerticiAdiacenti(statoAttuale).get(0);
                        if (statoDopoLoScatto.getTransizionePrecedente().getRilevanza() != null) {
                            decorazioneAggiornata = aggiornaDecorazione(decorazioneAggiornata, Arrays.asList(statoDopoLoScatto.getTransizionePrecedente().getRilevanza()));
                        }
                        statiSuccessivi.add(new StatoReteDecorato(statoDopoLoScatto, decorazioneAggiornata));

                    } else {
                        for (int j = 0; j < verticiAdiacenti.size(); j++) {//viene selezionato ogni StatoRete successivo
                            if (decorazione != null) {
                                decorazioneAggiornata = (ArrayList<String>) decorazione.clone();
                            } else {
                                decorazioneAggiornata = null;
                            }
//                                Transizione transizioneDaEseguire = transizioniAbilitate[i].get(j);
//                                transizioneEseguita = automi.get(i).scatta(transizioneDaEseguire, link);//viene fatta scattare la transizione da eseguire
//                                copiaStatoAttuale.setTransizionePrecedente(transizioneEseguita);
                            StatoReteRidenominato statoDopoLoScatto = (StatoReteRidenominato) spazioComportamentale.getVerticiAdiacenti(statoAttuale).get(j);
                            if (statoDopoLoScatto.getTransizionePrecedente().getRilevanza() != null) {
                                decorazioneAggiornata = aggiornaDecorazione(decorazioneAggiornata, Arrays.asList(statoDopoLoScatto.getTransizionePrecedente().getRilevanza()));
                            }
                            statiSuccessivi.add(new StatoReteDecorato(statoDopoLoScatto, decorazioneAggiornata));
                            pilaDiramazioni.add(statoAttualeDecorato);
                        }
                        pilaDiramazioni.pop();
                    }

                    for (StatoReteDecorato s : statiSuccessivi) {
                        pilaStato.push(s);//                       
                    }
                } else {
                    //stato senza transizioni abilitate
                    Cammino nuovoCammino = new Cammino();
                    nuovoCammino.copiaCammino(camminoAttuale);
                    camminiDecorati.add(nuovoCammino);
                    if (!pilaDiramazioni.isEmpty()) {
                        camminoAttuale.togliFinoAPrimaDelloStato(pilaDiramazioni.pop());//gli ultimi elementi del cammini vengono rimossi finchè non si incontra il primo elemento della coda
                    }
                }
            } else {
                Cammino nuovoCammino = new Cammino();
                camminoAttuale.add(statoAttualeDecorato);//                
                nuovoCammino.copiaCammino(camminoAttuale);
                camminiDecorati.add(nuovoCammino);
                if (!pilaDiramazioni.isEmpty()) {
                    camminoAttuale.togliFinoAPrimaDelloStato(pilaDiramazioni.pop());//gli ultimi elementi del cammini vengono rimossi finchè non si incontra il primo elemento della coda
                }

            }
        }

        return camminiDecorati;
    }

    private static List<String> aggiornaDecorazione(List<String> decorazioneAggiornata, List<String> asList) {
        List<String> daRitornare = new ArrayList<>();
        if (decorazioneAggiornata != null) {
            daRitornare.addAll(decorazioneAggiornata);
        }
        if (asList != null) {
            if (daRitornare == null) {
                return asList;
            }
            for (String s : asList) {
                if (!daRitornare.contains(s)) {
                    daRitornare.add(s);
                }
            }
        }
        return daRitornare;
    }
    
    
    
}
