/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import view.Parametri;
import Model.*;
import Utilita.InputDati;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

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
//        View.stampaCammini(cammini);
        List<Cammino> traiettorie = potatura(cammini);
        traiettorie = ridenominazione(traiettorie, statiSpazioC);
//        View.stampaTraiettorie(traiettorie);
        SpazioComportamentale spazioC = new SpazioComportamentale();
        spazioC = inserisciVerticiSpazioComportamentale(spazioC, traiettorie, statiSpazioC);
        spazioC = inserisciLatiSpazioComportamentale(spazioC, traiettorie);
        rete.setSpazioC(spazioC);
//        System.out.println(spazioC.toString());
//        SpazioComportamentale spazioComportamentaleDecorato = creaSpazioComportamentaleDecorato(spazioC);
//        stampaCammini(camminiDecorati);
        System.out.println(spazioC.toString());
    }

    public static void creaSpazioComportamentaleDecorato(Rete rete) {
        List<Cammino> camminiDecorati;
        LinkedList<StatoReteAbstract> statiDecoratiSpazioC = new LinkedList<>();
        camminiDecorati = trovaCamminiDecorati(rete.getSpazioC(), statiDecoratiSpazioC);
//        View.stampaCammini(camminiDecorati);
        SpazioComportamentale spazioComportamentaleDecorato = new SpazioComportamentale();
        spazioComportamentaleDecorato = inserisciVerticiSpazioComportamentale(spazioComportamentaleDecorato, camminiDecorati, statiDecoratiSpazioC);
        spazioComportamentaleDecorato = inserisciLatiSpazioComportamentale(spazioComportamentaleDecorato, camminiDecorati);
        spazioComportamentaleDecorato = etichettaOsservabilita(spazioComportamentaleDecorato);
        rete.setSpazioComportamentaleDecorato(spazioComportamentaleDecorato);
        System.out.println(spazioComportamentaleDecorato.toString());
    }

    static void creaDizionario(Rete rete) {
        determinizzazione(rete);
    }

    static String distillaDiagnosi(Rete rete, List<String> osservazioni) {
        String diagnosi;
        StatoDFA statoRaggiunto = raggiungiStatoOsservabile(rete.getDizionario(), osservazioni);
        diagnosi = getDiagnosi(statoRaggiunto);
        return diagnosi;
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
                    List<List<Transizione>> transizioniAbilitate = new ArrayList<List<Transizione>>(rete.getAutomi().size());
                    int numeroTransizioniAbilitate = 0;
                    for (int i = 0; i < rete.getAutomi().size(); i++) {//se nessun automa è già scattato, si itera su tutti gli automi                        
                        rete.getAutomi().get(i).isAbilitato(rete.getLink());
                        transizioniAbilitate.add(rete.getAutomi().get(i).getTransizioneAbilitata());//transizione abilitata diventa la transizione abilitata allo scatto nell'automa attuale                   
                        numeroTransizioniAbilitate += transizioniAbilitate.get(i).size();
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
                            for (int j = 0; j < transizioniAbilitate.get(i).size(); j++) {//vengono fatte scattare tutte, ognuna su un nuovo cammino
                                rete.setRete(statoAttuale);
                                Transizione transizioneDaEseguire = transizioniAbilitate.get(i).get(j);
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
                statoTraiettoria.setNumero(numeroStato);    //rinomina numero
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

//                if(_stati.get(i).getDescrizione().equals("20 31 Ɛ e3")){
//                    System.out.println("elaborato_1718.v2.pkg0.Controller.inserisciVerticiSpazioComportamentale()");
//                }
                String nome = Parametri.STATO_DECORATO_PREFISSO + Integer.toString(i);
//                _stati.get(i).setNome(nome);
                statoDaAggiungere.setNome(nome);
                while (tuttiGliStatiDelleTraiettorie.lastIndexOf(_stati.get(i)) != -1) {//Rinomina tutti gli stati delle traiettorie
                    tuttiGliStatiDelleTraiettorie.get(tuttiGliStatiDelleTraiettorie.lastIndexOf(_stati.get(i))).setNome(nome);
                    tuttiGliStatiDelleTraiettorie.remove(tuttiGliStatiDelleTraiettorie.lastIndexOf(_stati.get(i)));
                }
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
        StatoReteDecorato root = new StatoReteDecorato((StatoReteAbstract) spazioComportamentale.getRoot(), null);
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
//                StatoReteDecorato verticeDaInserire = new StatoReteDecorato(statoAttualeDecorato);
//                verticeDaInserire.setTransizionePrecedente(null);
                statiDecorati.add(statoAttualeDecorato);//TODO: sostituito, prima era passato verticeDaInserire
//                spazioComportamentaleDecorato.aggiungiVertice(verticeDaInserire);
                //statoAttuale.setNumero(stati.size());
//                statiDecorati.add(statoAttualeDecorato);
                camminoAttuale.add(statoAttualeDecorato);
                //spazioC.aggiungiVertice(new StatoReteRidenominato(statoAttuale));
//                System.out.println(statoAttuale.toString());
//                setRete(statoAttuale);
                List<StatoInterface> verticiAdiacenti = spazioComportamentale.getVerticiAdiacenti(statoAttuale);
                if (verticiAdiacenti != null && verticiAdiacenti.size() > 0) {//se lo StatoRete non è uno stato finale            
                    ArrayList<StatoReteDecorato> statiSuccessivi = new ArrayList<>();
                    List<String> decorazioneAggiornata = null;

                    if (verticiAdiacenti.size() == 1) {
                        if (decorazione != null) {
                            decorazioneAggiornata = new ArrayList<String>(decorazione);
                        }
                        StatoReteRidenominato statoDopoLoScatto = (StatoReteRidenominato) spazioComportamentale.getVerticiAdiacenti(statoAttuale).get(0);
                        if (statoDopoLoScatto.getTransizionePrecedente().getRilevanza() != null) {
                            decorazioneAggiornata = aggiornaDecorazione(decorazioneAggiornata, Arrays.asList(statoDopoLoScatto.getTransizionePrecedente().getRilevanza()));
                        }
                        statiSuccessivi.add(new StatoReteDecorato(statoDopoLoScatto, decorazioneAggiornata));

                    } else {
                        for (int j = 0; j < verticiAdiacenti.size(); j++) {//viene selezionato ogni StatoRete successivo
                            if (decorazione != null) {
                                decorazioneAggiornata = new ArrayList<String>(decorazione);
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
                statoAttualeDecorato.setNome(statiDecorati.get(statiDecorati.indexOf(statoAttualeDecorato)).getNome());
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

    private static SpazioComportamentale etichettaOsservabilita(SpazioComportamentale spazioComportamentale) {

        Set<StatoInterface> vertici = spazioComportamentale.getVertici();
        for (StatoInterface v : vertici) {
            List<StatoInterface> adj = spazioComportamentale.getVerticiAdiacenti(v);
            for (StatoInterface s : adj) {
                if (((StatoReteAbstract) s).getTransizionePrecedente().getOsservabilita() != null) {
                    ((StatoReteAbstract) s).setOsservabilita(((StatoReteAbstract) s).getTransizionePrecedente().getOsservabilita());
                }
            }
        }

        return spazioComportamentale;
    }

    /**
     * Determinata il DFA dello spazio NFA La struttura dati del DFA e'
     * SpazioComportamentale Il DFA trovato viene salvato nella rete considerata
     *
     * @param rete
     * @return spazioDFA
     */
    public static SpazioComportamentale determinizzazione(Rete rete) {
        SpazioComportamentale spazioComportamentaleDecorato = rete.getSpazioComportamentaleDecorato();
        SpazioComportamentale spazioDFA = new SpazioComportamentale();
        Stack<StatoDFA> stack = new Stack<>();
        List<StatoDFA> verticiSpazio = new ArrayList<>();
        List<StatoReteRidenominato> statiRaggiunti;
        StatoInterface root = spazioComportamentaleDecorato.getRoot();
        List<String> etichetteOsservabilita = Arrays.asList(rete.getEtichettaOsservabilita());
        //statiRaggiuntiDaOsservabilita e' una lista di liste, contiene, per ogni etichetta di osservabilita contenuta nella rete,
        //la lista degli stati NFA raggiunti attraverso quella etichetta
        //
        //es: statiRaggiuntiDaOsservabilita.get(1) restituisce tutti gli stati NFA dello spazio comportamentale doppiamente decorato
        //raggiunti attraverso l'etichetta di osservabilita che si trova in posizione 1
        //ovvero: rete.getEtichettaOsservabilita()[1]

        List<List<StatoReteRidenominato>> statiReggiuntiDaOsservabilita = new ArrayList<List<StatoReteRidenominato>>();
        for (String etichettaOsservabilita : etichetteOsservabilita) {
            statiReggiuntiDaOsservabilita.add(new ArrayList<StatoReteRidenominato>());
        }
        //creazione dello stato statoDFA root a partire dall'epsilon-CLOSURE
        statiRaggiunti = epsilon_CLOSURE(spazioComportamentaleDecorato, root);
        root = new StatoDFA(statiRaggiunti, null);
        spazioDFA.aggiungiVertice(root);
        spazioDFA.setRoot(root);
        stack.push((StatoDFA) root);
        //dello stato root NDA
        //fintanto che la stack e' vuota
        while (!stack.isEmpty()) {
            StatoDFA statoAnalizzato = stack.pop();
//            System.out.println(statoAnalizzato.getNome());
//            System.out.printf(statoAnalizzato.toString() + " ");
            if (!verticiSpazio.contains(statoAnalizzato)) {
                spazioDFA.aggiungiVertice(statoAnalizzato);
//                System.out.println("non contenuto");
                verticiSpazio.add(statoAnalizzato);
                //rimuovi dalla pila il primo stato DFA e prendi tutti gli stati NDA
                statiRaggiunti = statoAnalizzato.getStati();
                for (StatoReteRidenominato statoRaggiunto : statiRaggiunti) {//per ogni stato NDA
                    //controlla nello spazio doppiamente decorato gli stati adiacenti
                    List<StatoInterface> statiAdiacenti = spazioComportamentaleDecorato.getVerticiAdiacenti(statoRaggiunto);
                    //per ogni stato adiacente
                    for (StatoInterface statoAdiacente : statiAdiacenti) {
                        //se lo stato raggiunto ha una transizione osservabile
                        if (((StatoReteAbstract) statoAdiacente).getOsservabilita() != null) {
                            int posizioneOsservabilita = etichetteOsservabilita.indexOf(((StatoReteAbstract) statoAdiacente).getOsservabilita());
                            statiReggiuntiDaOsservabilita.get(posizioneOsservabilita).add((StatoReteRidenominato) statoAdiacente);
                        }
                    }
                }
                List<StatoReteRidenominato> statiTemporanei;
                //Per ogni stato raggiunto da una etichetta di osservabilita'
                for (int i = 0; i < statiReggiuntiDaOsservabilita.size(); i++) {
                    if (!statiReggiuntiDaOsservabilita.get(i).isEmpty()) {
                        //prendi l'insieme degli stati DFA raggiunti dall'osservabilita' i 
                        statiTemporanei = statiReggiuntiDaOsservabilita.get(i);
                        //Esegui l'epsilon-CLOSURE sull'insieme di stati considerato
                        statiTemporanei = epsilon_CLOSURE(spazioComportamentaleDecorato, statiTemporanei);
                        //Il nuovo stato DFA e' l'epsilon-CLOSURE CALCOLATO
                        StatoDFA nuovoStato = new StatoDFA(new ArrayList<StatoReteRidenominato>(statiTemporanei), etichetteOsservabilita.get(i));
                        //Il nuovo statoDFA viene insierito nella pila
                        stack.push(nuovoStato);
                        //Aggiunta del lato nello spazioDFA
                        spazioDFA.aggiungiLato(statoAnalizzato, nuovoStato);
                        //si svuota l'insieme degli stati raggiunti dall'osservabilita' i
                        statiReggiuntiDaOsservabilita.get(i).clear();
                    }

                }
            }
        }

//        for(StatoDFA s :verticiSpazio){
//            System.out.println(s.getNome());
//        }
        rete.setDizionario(spazioDFA);
        return spazioDFA;
    }

    /**
     * Ritorna l'insieme degli stati del DFA ottenuto applicando
     * l'epsilon-CLOSURE ad uno stato del NFA
     *
     * @param spazioComportamentale
     * @param statoSpazio
     * @return
     */
    private static List<StatoReteRidenominato> epsilon_CLOSURE(SpazioComportamentale spazioComportamentale, StatoInterface statoSpazio) {
        List<StatoReteRidenominato> insiemeStati = new ArrayList<>();
        Stack<StatoInterface> stack = new Stack<>();
        stack.push(statoSpazio);
        while (!stack.isEmpty()) {
            StatoInterface statoAnalizzato = stack.pop();
            if (statoAnalizzato.getClass() == StatoReteRidenominato.class) {
                insiemeStati.add((StatoReteRidenominato) statoAnalizzato);
                List<StatoInterface> statiAdiacenti = spazioComportamentale.getVerticiAdiacenti(statoAnalizzato);
                for (StatoInterface s : statiAdiacenti) {
                    if (s.getClass() == StatoReteRidenominato.class) {
                        if (((StatoReteAbstract) s).getOsservabilita() == null && !insiemeStati.contains(s)) {
                            stack.push((StatoReteRidenominato) s);
                        }
                    }
                }
            }
        }

        return insiemeStati;
    }

    /**
     * Restituisce uno statoDFA ottenuto dall'epsilon-CLOSURE su uno statoDFA
     *
     * @param spazioComportamentaleDecorato
     * @param statiRaggiunti
     * @return
     */
    private static List<StatoReteRidenominato> epsilon_CLOSURE(SpazioComportamentale spazioComportamentaleDecorato, List<StatoReteRidenominato> statiRaggiunti) {
        List<StatoReteRidenominato> statiDaRitornare = new ArrayList<StatoReteRidenominato>(statiRaggiunti);
        List<StatoReteRidenominato> epsilonStati = new ArrayList<StatoReteRidenominato>();
        for (StatoReteRidenominato stato : statiRaggiunti) {
            epsilonStati = epsilon_CLOSURE(spazioComportamentaleDecorato, stato);
            epsilonStati.removeAll(statiDaRitornare);
            statiDaRitornare.addAll(epsilonStati);
        }

        return statiDaRitornare;
    }

    public static StatoDFA raggiungiStatoOsservabile(SpazioComportamentale spazioDFA, List<String> _osservazioni) {
        Queue<String> osservazioni = new LinkedList<>(_osservazioni);
        List<StatoInterface> listaStatiPartenza = new ArrayList<>();
        List<StatoInterface> listaStatiRaggiuntiOsservabili = new ArrayList<>();
        List<StatoInterface> listaStatiRaggiuntiTutti = new ArrayList<>();
        listaStatiPartenza.add(spazioDFA.getRoot());
        while (!osservazioni.isEmpty()) {
            String osservazione = osservazioni.poll();
            listaStatiRaggiuntiOsservabili.clear();
            for (StatoInterface statoPartenza : listaStatiPartenza) {
                listaStatiRaggiuntiTutti = spazioDFA.getVerticiAdiacenti(statoPartenza);
                for (StatoInterface s : listaStatiRaggiuntiTutti) {
                    if (((StatoDFA) s).getOsservabilita().equals(osservazione)) {
                        listaStatiRaggiuntiOsservabili.add(s);
                    }
                }
            }
            listaStatiPartenza = new ArrayList<>(listaStatiRaggiuntiOsservabili);
        }
        if (listaStatiRaggiuntiOsservabili.size() == 1) {
            return (StatoDFA) listaStatiRaggiuntiOsservabili.get(0);
        } else {
            return null;
        }
    }

    private static String getDiagnosi(StatoDFA statoRaggiunto) {
        List<List<String>> diagnosi = statoRaggiunto.getDiagnosi();
        StringBuilder s = new StringBuilder();
        for (List<String> decorazione : diagnosi) {
            s.append(decorazione.toString());
        }
        return s.toString();
    }

    public static List<String> getEtichetteOsservabilita(Rete rete) {
        List<String> etichette = new ArrayList<String>();
        String[] temp = rete.getEtichettaOsservabilita();
        for (int i = 0; i < temp.length; i++) {
            etichette.add(temp[i]);
        }
        return etichette;
    }
    
    static void creaDizionarioParziale(Rete rete, String osservazione) {
        //TODO CAMO
        // parte relativa alla creazione dello spazio comportamentale
        SpazioComportamentale automaRiconoscitore = creaRiconoscitoreEspressione(rete, osservazione);
        SpazioComportamentale spazioComportamentaleParziale = creaSpazioComportamentaleParziale(rete, automaRiconoscitore);
        
        //TODO Alby
        SpazioComportamentale dizionarioParziale = creaDizionarioParziale(spazioComportamentaleParziale);
        rete.setDizionarioParziale(dizionarioParziale);
    }
    
    private static SpazioComportamentale creaRiconoscitoreEspressione(Rete rete, String osservazione){
        // copia dello spazio comportamentale decorato calcolato prima
        SpazioComportamentale spazioComportamentaleDecorato = rete.getSpazioComportamentaleDecorato();
        //System.out.println(spazioComportamentaleDecorato.toString());
        // inizializzazione della root e dell'automa riconoscitore
        StatoInterface rootSpazioComportamentaleDecorato = spazioComportamentaleDecorato.getRoot();
        StatoReteAbstract rootAbstract = (StatoReteAbstract) rootSpazioComportamentaleDecorato;
        SpazioComportamentale automaRiconoscitore = new SpazioComportamentale();
        int nomeStatoDFA = 0;
        StatoDFA root = new StatoDFA(String.valueOf(nomeStatoDFA), null);   // la root non ha etichette di osservabilita
        automaRiconoscitore.setRoot(root);
        automaRiconoscitore.aggiungiVertice(root);
        
        // inizializzo pile
        Stack <StatoDFA> pilaDFA = new Stack<StatoDFA>(); // da usare in caso di loop
        pilaDFA.push(root);
        //Stack <StatoInterface> pilaInterface = new Stack <StatoInterface>();
//        pilaInterface.push(rootSpazioComportamentaleDecorato);
        // inizializzo osservazione
        String[] listaOsservazioni = osservazione.split(Parametri.SPAZIO);
        
        // INIZIO ALGORITMO
        // controllo se l'osservazione è vuota
        if(listaOsservazioni[0].equalsIgnoreCase("")){
            // cerco stato finale, se esiste
            if (rootAbstract.isFinale()) {
                System.out.println("Lo stato iniziale coincide con quello finale");
                root.setIsFinale(true);
                return automaRiconoscitore;
            } else {
                // messaggio di errore perché siamo in uno stato finale in cui non c'è lo stato finale
                System.out.println("Errore: l'osservazione inserita non corrisponde a nessun stato finale");
                return null;
            }
        } else {
            if (osservazione.contains(Parametri.APICE)) {
                if (osservazione.contains(Parametri.PARENTESI_TONDA_A)) {
                    // ALGORITMO NON LINEARE CON OPZIONALITÀ/MULTIPLICITÀ
                    StatoDFA statoPrecedente = root;
                    StatoDFA primoStatoNellaParentesi = null;
                    StatoDFA ultimoStatoFuoriParentesi = root;
                    int controlloUltimaOsservazioneLoop = 0;
                    boolean internoParentesi = false;
                    for (int i = 0; i < listaOsservazioni.length; i++) {
                        if (listaOsservazioni[i].contains(Parametri.PARENTESI_TONDA_A)) {
                            internoParentesi = true;
                            String singolaOsservazione = listaOsservazioni[i];
                            singolaOsservazione = InputDati.pulisciOsservazione(singolaOsservazione);
                            nomeStatoDFA++;
                            StatoDFA statoDaAggiungere = new StatoDFA(String.valueOf(nomeStatoDFA), singolaOsservazione);
                            automaRiconoscitore.aggiungiVertice(statoDaAggiungere);
                            automaRiconoscitore.aggiungiLato(statoPrecedente, statoDaAggiungere);
                            statoPrecedente = statoDaAggiungere;
                            primoStatoNellaParentesi = statoDaAggiungere;
                        } else if (internoParentesi) {
                            if (listaOsservazioni[i].contains(Parametri.PARENTESI_TONDA_C)) {
                                internoParentesi = false;
                                if (listaOsservazioni[i].contains(Parametri.ASTERISCO)) {
                                    // parte opzionale
                                    String singolaOsservazione = listaOsservazioni[i];
                                    singolaOsservazione = InputDati.pulisciOsservazione(singolaOsservazione);
//                                    nomeStatoDFA++;
                                    
                                    StatoDFA statoDaAggiungere = new StatoDFA(ultimoStatoFuoriParentesi.getNome(), singolaOsservazione);
                                    automaRiconoscitore.aggiungiVertice(statoDaAggiungere);
                                    automaRiconoscitore.aggiungiLato(statoPrecedente, statoDaAggiungere);
                                    //collego all'ultimo stato fuori parentesi
//                                    automaRiconoscitore.aggiungiLato(statoDaAggiungere, ultimoStatoFuoriParentesi);

                                    // collego allo stato precedente per loop
//                                    automaRiconoscitore.aggiungiLato(statoDaAggiungere, statoPrecedente);
                                    statoPrecedente = statoDaAggiungere;
//                                    statoPrecedente = ultimoStatoFuoriParentesi;
                                    int controlloUltimaOsservazione = i + 1;
                                    if (controlloUltimaOsservazione == listaOsservazioni.length) {
                                        // lo stato finale è l'ultimo stato valido fuori dalla parentesi
                                        ultimoStatoFuoriParentesi.setIsFinale(true);
                                    }

                                } else if (listaOsservazioni[i].contains(Parametri.PIU)) {
                                    // parte multipla
                                    String singolaOsservazione = listaOsservazioni[i];
//                                    System.out.println("l'etichetta che sto analizzando è: " + singolaOsservazione);
                                    singolaOsservazione = InputDati.pulisciOsservazione(singolaOsservazione);
                                    nomeStatoDFA++;

                                    StatoDFA statoDaAggiungere = new StatoDFA(String.valueOf(nomeStatoDFA), singolaOsservazione);
                                    automaRiconoscitore.aggiungiVertice(statoDaAggiungere);
                                    automaRiconoscitore.aggiungiLato(statoPrecedente, statoDaAggiungere);
                                    controlloUltimaOsservazioneLoop = i + 1;
                                    if (controlloUltimaOsservazioneLoop == listaOsservazioni.length) {
                                        // se è l'ultimo della lista deve essere finale
                                        statoDaAggiungere.setIsFinale(true);
                                    }
                                    automaRiconoscitore.aggiungiLato(statoDaAggiungere, primoStatoNellaParentesi);
                                    statoPrecedente = statoDaAggiungere;
                                }
                            } else {
                                String singolaOsservazione = listaOsservazioni[i];
                                singolaOsservazione = InputDati.pulisciOsservazione(singolaOsservazione);
                                nomeStatoDFA++;
//                                System.out.println("il nome DFA è: " + nomeStatoDFA);
                                StatoDFA statoDaAggiungere = new StatoDFA(String.valueOf(nomeStatoDFA), singolaOsservazione);
                                automaRiconoscitore.aggiungiVertice(statoDaAggiungere);
                                automaRiconoscitore.aggiungiLato(statoPrecedente, statoDaAggiungere);
                                statoPrecedente = statoDaAggiungere;
                            }
                        } else {
                            // inserimento normale
                            String osservazionePulita = null;
                            String singolaOsservazione = listaOsservazioni[i];
                            osservazionePulita = InputDati.pulisciOsservazione(singolaOsservazione);
                            if (singolaOsservazione.contains(Parametri.ASTERISCO)) {
                                automaRiconoscitore.aggiungiLato(statoPrecedente, statoPrecedente);
                                int controllaNumeroOsservazioni = i + 1;
                                if(controllaNumeroOsservazioni == listaOsservazioni.length){
                                    statoPrecedente.setIsFinale(true);
                                }
                            }
                            if (singolaOsservazione.contains(Parametri.PIU)) {
                                nomeStatoDFA++;
                                osservazionePulita = InputDati.pulisciOsservazione(singolaOsservazione);
                                StatoDFA statoDaAggiungere = new StatoDFA(String.valueOf(nomeStatoDFA), osservazionePulita);
                                automaRiconoscitore.aggiungiVertice(statoDaAggiungere);
                                automaRiconoscitore.aggiungiLato(statoPrecedente, statoDaAggiungere);
                                int controlloUltimaOsservazione = i + 1;
                                if (controlloUltimaOsservazione == listaOsservazioni.length) {
                                    // se è l'ultimo della lista deve essere finale
                                    statoDaAggiungere.setIsFinale(true);
                                }
                                // loop
                                automaRiconoscitore.aggiungiLato(statoDaAggiungere, statoDaAggiungere);
                                statoPrecedente = statoDaAggiungere;
                                ultimoStatoFuoriParentesi = statoDaAggiungere;
                            }
                            if (!singolaOsservazione.contains(Parametri.ASTERISCO) && !singolaOsservazione.contains(Parametri.PIU)) {
                                nomeStatoDFA++;
                                StatoDFA statoDaAggiungere = new StatoDFA(String.valueOf(nomeStatoDFA), singolaOsservazione);
                                automaRiconoscitore.aggiungiVertice(statoDaAggiungere);
                                // controllo che lo stato precedente non sia uno stato loop opzionale
                                if(i>0){
                                    // se si collego l'ultimo stato fuori parentesi
                                    int temp = i - 1;
                                    if(listaOsservazioni[temp].contains(Parametri.PARENTESI_TONDA_C) && listaOsservazioni[temp].contains(Parametri.ASTERISCO)){
                                        automaRiconoscitore.aggiungiLato(ultimoStatoFuoriParentesi, statoDaAggiungere);
                                    }
                                }
                                automaRiconoscitore.aggiungiLato(statoPrecedente, statoDaAggiungere);
                                int controlloUltimaOsservazione = i + 1;
                                if (controlloUltimaOsservazione == listaOsservazioni.length) {
                                    // se è l'ultimo della lista deve essere finale
                                    statoDaAggiungere.setIsFinale(true);
                                }
                                statoPrecedente = statoDaAggiungere;
                                ultimoStatoFuoriParentesi = statoDaAggiungere;
                            }
                        }
                    }

                } else {
                    // ALGORITMO LINEARE CON OPZIONALITÀ/MULTIPLICITÀ
                    StatoDFA statoPrecedente = root;
                    StatoDFA statoDaAggiungere = null;
                    StatoDFA penultimoStatoOpzionale = null;
                    StatoDFA ultimoStatoValido = root; // è l'ultimo stato non opzionale
                    StatoDFA statoPrecedenteOpzionale = null;
                    int indicePrecedenteOpzionale = -1; // diverso da null se il precedente è opzionale
                    int controlloUltimaOsservazione = 0;
                    for (int i = 0; i < listaOsservazioni.length; i++) {
                        String singolaOsservazione = listaOsservazioni[i];
                        if (singolaOsservazione.contains(Parametri.ASTERISCO)) {
                            // loop sullo stato precedente
                            statoDaAggiungere = new StatoDFA(statoPrecedente.getNome(), InputDati.pulisciOsservazione(singolaOsservazione));
                            automaRiconoscitore.aggiungiVertice(statoDaAggiungere);
                            automaRiconoscitore.aggiungiLato(statoPrecedente, statoPrecedente);
                            //controllo se il precedente è opzionale
                            if (i > 0) {
                                if (indicePrecedenteOpzionale == (i - 1)) {
                                    String etichettaPrecedente = listaOsservazioni[i - 1];
                                    etichettaPrecedente = InputDati.pulisciOsservazione(etichettaPrecedente);
                                    nomeStatoDFA++;
                                    statoDaAggiungere = new StatoDFA(String.valueOf(nomeStatoDFA), etichettaPrecedente);
                                    automaRiconoscitore.aggiungiVertice(statoDaAggiungere);
                                    automaRiconoscitore.aggiungiLato(statoPrecedente, statoDaAggiungere);
                                    // loop
                                    automaRiconoscitore.aggiungiLato(statoDaAggiungere, statoDaAggiungere);
                                    controlloUltimaOsservazione = i + 1;
                                    if (controlloUltimaOsservazione == listaOsservazioni.length) {
                                        // se è l'ultimo della lista deve essere finale
                                        statoDaAggiungere.setIsFinale(true);
                                    }
                                    // collegamento con l'ultimo stato valido
                                    automaRiconoscitore.aggiungiLato(ultimoStatoValido, statoDaAggiungere);
                                    statoPrecedenteOpzionale = statoDaAggiungere;
                                }
                                controlloUltimaOsservazione = i + 1;
                                if (controlloUltimaOsservazione == listaOsservazioni.length) {
                                    // se è l'ultimo della lista deve essere finale
                                    statoPrecedente.setIsFinale(true);
                                }
                                indicePrecedenteOpzionale = i;
                            }
                        }
                        if (singolaOsservazione.contains(Parametri.PIU)) {
                            //aggiunta dello stato
                            nomeStatoDFA++;
                            statoDaAggiungere = new StatoDFA(String.valueOf(nomeStatoDFA), InputDati.pulisciOsservazione(singolaOsservazione));
                            automaRiconoscitore.aggiungiVertice(statoDaAggiungere);
                            automaRiconoscitore.aggiungiLato(statoPrecedente, statoDaAggiungere);
                            controlloUltimaOsservazione = i + 1;
                            if (controlloUltimaOsservazione == listaOsservazioni.length) {
                                // se è l'ultimo della lista deve essere finale
                                statoDaAggiungere.setIsFinale(true);
                            }
                            statoPrecedente = statoDaAggiungere;
                            ultimoStatoValido = statoDaAggiungere;
                            //lopp sullo stato creato
                            statoDaAggiungere = new StatoDFA(String.valueOf(nomeStatoDFA), InputDati.pulisciOsservazione(singolaOsservazione));
                            automaRiconoscitore.aggiungiVertice(statoDaAggiungere);
                            automaRiconoscitore.aggiungiLato(statoPrecedente, statoDaAggiungere);
                        }
                        if (!singolaOsservazione.contains(Parametri.ASTERISCO) && !singolaOsservazione.contains(Parametri.PIU)){
                            nomeStatoDFA++;
                            statoDaAggiungere = new StatoDFA(String.valueOf(nomeStatoDFA), singolaOsservazione);
//                            System.out.println(statoDaAggiungere.toString());
                            automaRiconoscitore.aggiungiVertice(statoDaAggiungere);
                            automaRiconoscitore.aggiungiLato(statoPrecedente, statoDaAggiungere);
                            controlloUltimaOsservazione = i + 1;
                            if (controlloUltimaOsservazione == listaOsservazioni.length) {
                                // se è l'ultimo della lista deve essere finale
                                statoDaAggiungere.setIsFinale(true);
                            }
                            statoPrecedente = statoDaAggiungere;
                            if (i != 0) {
                                ultimoStatoValido = statoDaAggiungere;
                            }
                            //controllo se prima ho più di uno stato opzionale indietro in modo da collegare tutti gli stati passati con quello nuovo
                            if (penultimoStatoOpzionale != null) {
                                automaRiconoscitore.aggiungiLato(penultimoStatoOpzionale, statoDaAggiungere);
                            }
                            // nel caso di due opzionalità collego l'ultima opzionalità
                            if(statoPrecedenteOpzionale != null){
                                automaRiconoscitore.aggiungiLato(statoPrecedenteOpzionale, statoDaAggiungere);
                                statoPrecedenteOpzionale = null;
                            }
                        }
                    }
                }
            } else {
                // ALGORITMO LINEARE DELLE OSSERVAZIONI
                StatoDFA statoPrecedente = root;
                for (int i = 0; i < listaOsservazioni.length; i++) {
                    String singolaOsservazione = listaOsservazioni[i];
                    nomeStatoDFA++;
                    StatoDFA statoDaAggiungere = new StatoDFA(String.valueOf(nomeStatoDFA), singolaOsservazione);
                    automaRiconoscitore.aggiungiVertice(statoDaAggiungere);
                    automaRiconoscitore.aggiungiLato(statoPrecedente, statoDaAggiungere);
                    int controlloUltimaOsservazione = i + 1;
                    if (controlloUltimaOsservazione == listaOsservazioni.length){
                        // se è l'ultimo della lista deve essere finale
                        statoDaAggiungere.setIsFinale(true);
                    }
                    statoPrecedente = statoDaAggiungere;
                }
            }
        }
        
        System.out.println(automaRiconoscitore.toStringAutomaRiconoscitore());
        return automaRiconoscitore;
    }
    
    
   
    private static SpazioComportamentale creaSpazioComportamentaleParziale(Rete rete, SpazioComportamentale automaRiconoscitore) {
        //TO-DO: CAMO
        SpazioComportamentale spazioComportamentaleParziale = new SpazioComportamentale();
        

        //Questo non si tocca, si guarda e basta
        SpazioComportamentale spazioComportamentaleIntero = rete.getSpazioComportamentaleDecorato(); 
        
        return spazioComportamentaleParziale;
    }

    private static SpazioComportamentale creaDizionarioParziale(SpazioComportamentale spazioComportamentaleParziale) {
       //TODO Alby
        return null;
    }


}
