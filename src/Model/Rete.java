/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import view.Parametri;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.LinkedList;
import java.util.List;

import java.util.Stack;

/**
 *
 * @author Alb
 */
public class Rete implements Serializable {

    private List<Automa> automi;
    private Evento[] eventi;
    private List<Cammino> cammini;
    private List<Cammino> camminiDecorati;
    private String descrizione;
    private Evento[] link;
    private int numeroStati;
    private SpazioComportamentale spazioC;
    private SpazioComportamentale spazioComportamentaleDecorato;
    private SpazioComportamentale dizionario;
    private SpazioComportamentale dizionarioParziale;
    private LinkedList<StatoReteAbstract> stati;
    private String[] etichettaOsservabilita;
    private String[] etichettaRilevanza;
    //private static Transizione transizioneAbilitata;

    public Rete(String s, Evento[] _link, Evento[] _eventi) {
        descrizione = s;
        automi = new ArrayList<Automa>();
        eventi = _eventi;
        link = _link;
        cammini = new ArrayList<Cammino>();
        spazioC = new SpazioComportamentale();
    }

    public Rete(String s, Evento[] _link, Evento[] _eventi, String[] osservabilita, String[] rilevanza) {
        this(s, _link, _eventi);
        etichettaOsservabilita = osservabilita;
        etichettaRilevanza = rilevanza;
    }

    public void creaRete(String s, Evento[] _link, Evento[] _eventi) {
        descrizione = s;
        automi = new ArrayList<Automa>();
        eventi = _eventi;
        link = _link;
        cammini = new ArrayList<Cammino>();
        spazioC = new SpazioComportamentale();
    }

    public void creaRete(String s, Evento[] _link, Evento[] _eventi, String[] osservabilita, String[] rilevanza) {
        creaRete(s, _link, _eventi);
        etichettaOsservabilita = osservabilita;
        etichettaRilevanza = rilevanza;
    }

//    public void start2() {
//        scatta2();
//        stampaCammini(cammini);
//        ArrayList<Cammino> traiettorie = potatura();
//        traiettorie = ridenominazione(traiettorie);
//        stampaTraiettorie(traiettorie);
//        spazioC = inserisciVerticiSpazioComportamentale(spazioC, traiettorie, stati);
//        inserisciLatiSpazioComportamentale(spazioC, traiettorie);
//        System.out.println(spazioC.toString());
//        SpazioComportamentale spazioComportamentaleDecorato = creaSpazioComportamentaleDecorato(spazioC);
//        stampaCammini(camminiDecorati);
//        System.out.println(spazioComportamentaleDecorato.toString());
//       
////        inserisciLatiSpazioComportamentale(spazioComportamentaleDecorato, camminiDecorati);
////        System.out.println(spazioComportamentaleDecorato);
//    }
    /**
     * Creazione dei cammini e dello spazio comportamentale
     *
     */
    public void scatta2() {
        Stack<StatoRete> pilaStato = new Stack<>();//pila dei nuovi stati
        Stack<StatoRete> pilaDiramazioni = new Stack<StatoRete>();//pila degli stati che hanno più di una transizione in uscita
        //Stack<Cammino> pilaCammino = new Stack<>();
        Cammino camminoAttuale = creaNuovoCammino();//il cammino attuale diventa un nuovo cammino con gli stati degli automi e i link azzerati
        stati = new LinkedList<>();

        numeroStati = -1;

        StatoRete statoRadice = creaStatoCorrente();
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
                setRete(statoAttuale);
                if (statoAttuale.isAbilitato(automi)) {
                    List<List<Transizione>> transizioniAbilitate = new ArrayList<List<Transizione>>(automi.size());
                    int numeroTransizioniAbilitate = 0;
                    for (int i = 0; i < automi.size(); i++) {//se nessun automa è già scattato, si itera su tutti gli automi                        
                        automi.get(i).isAbilitato(link);
                        transizioniAbilitate.add(automi.get(i).getTransizioneAbilitata());//transizione abilitata diventa la transizione abilitata allo scatto nell'automa attuale                   
                        numeroTransizioniAbilitate += transizioniAbilitate.get(i).size();
                    }
                    ArrayList<StatoRete> statiDopoLoScatto = new ArrayList<>();
                    Transizione transizioneEseguita;
                    StatoRete copiaStatoAttuale;
                    if (numeroTransizioniAbilitate == 1) {
                        for (int i = 0; i < automi.size(); i++) {
                            if (automi.get(i).isAbilitato(link)) {
                                transizioneEseguita = automi.get(i).scatta(link);//l'automa attuale viene fatto scattare e transizione eseguita diventa la transizione che è stata eseguita
//                                statoAttuale.setTransizionePrecedente(transizioneEseguita);//la transizione eseguita viene aggiunta allo StatoRete attuale
                                StatoRete statoDopoLoScatto = creaStatoCorrente(automi, link);
                                statoDopoLoScatto.setTransizionePrecedente(transizioneEseguita);
                                statiDopoLoScatto.add(statoDopoLoScatto);
                                setRete(statoAttuale);
                            }
                        }
                    } else {
                        copiaStatoAttuale = creaStatoCorrente(automi, link);

                        for (int i = 0; i < automi.size(); i++) {
                            for (int j = 0; j < transizioniAbilitate.get(i).size(); j++) {//vengono fatte scattare tutte, ognuna su un nuovo cammino
                                setRete(statoAttuale);
                                Transizione transizioneDaEseguire = transizioniAbilitate.get(i).get(j);
                                transizioneEseguita = automi.get(i).scatta(transizioneDaEseguire, link);//viene fatta scattare la transizione da eseguire
                                copiaStatoAttuale.setTransizionePrecedente(transizioneEseguita);
                                StatoRete statoDopoLoScatto = creaStatoCorrente(automi, link);
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

    }

    private void stampaCammini(List<Cammino> daStampare) {

        System.out.println(Parametri.CAMMINI_ETICHETTA);
        System.out.println();
        for (int i = 0; i < daStampare.size(); i++) {
            int numeroCammino = i + 1;
            System.out.println("numero cammino: " + numeroCammino);
            System.out.println();
            System.out.println(daStampare.get(i).toString());
            System.out.println();
        }
    }

    private StatoRete creaStatoCorrente() {
        Stato[] statoAutomi = new Stato[automi.size()];
        for (int i = 0; i < statoAutomi.length; i++) {
            statoAutomi[i] = automi.get(i).getStatoCorrente();
        }
        return new StatoRete(link.clone(), statoAutomi, numeroStati);
    }

    private StatoRete creaStatoCorrente(List<Automa> _automi, Evento[] _link) {
        Stato[] statoAutomi = new Stato[_automi.size()];
        for (int i = 0; i < statoAutomi.length; i++) {
            statoAutomi[i] = _automi.get(i).getStatoCorrente();
        }
        return new StatoRete(_link.clone(), statoAutomi, numeroStati);
    }

    public void impostaStatiIniziali() {
        Arrays.fill(link, null); // svuota l'array link

        for (Automa automa : automi) {
            automa.setStatoIniziale(link);
        }
    }

    private Cammino creaNuovoCammino() {
        Cammino nuovoCammino = new Cammino();
        impostaStatiIniziali();
        return nuovoCammino;
    }

    public void setRete(StatoRete statoAttuale) {
        for (int i = 0; i < automi.size(); i++) {
            automi.get(i).setStatoCorrente(statoAttuale.getStati()[i]);
        }
        link = statoAttuale.getLink().clone();
    }

    /**
     * Clona gli automi passati come parametri, tuttavia gli stati destinazione
     * delle transizioni associati agli stati degli automi, non puntano
     * all'indirizzo dei nuovi stati clonati, ma puntano agli stati "vecchi".
     * Ero riuscito a far puntare gli stati nuovi, ma aveva una computazione
     * temporale di tetha(n^3) Il metodo che clona anche gli indirizzi gli stati
     * destinazione delle transizioni è rimasto commentato nel metodo
     * Automa.copia().
     *
     * @param _automi
     * @return
     */
    private List<Automa> copiaAutomi(List<Automa> _automi) {
        List<Automa> daRitornare = new ArrayList<>();
        for (int i = 0; i < _automi.size(); i++) {
            daRitornare.add(_automi.get(i).copia());
        }
        return daRitornare;
    }

    /**
     * Inserisce i lati (o archi) dello spazio comportamentale
     *
     * @param traiettorie
     */
    private SpazioComportamentale inserisciLatiSpazioComportamentale(SpazioComportamentale spazioComportamentale, List<Cammino> traiettorie) {
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

    /**
     * Etichetta in maniera ordinata gli stati e li inserisce nello spazio
     * comportamentale
     *
     * @param traiettorie
     */
    private SpazioComportamentale inserisciVerticiSpazioComportamentale(SpazioComportamentale _spazioC, List<Cammino> traiettorie, LinkedList<StatoReteAbstract> _stati) {
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
     * Etichetta in maniera ordinata gli stati e li inserisce nello spazio
     * comportamentale
     *
     * @param traiettorie
     */
    private void inserisciVerticiSpazioComportamentaleDecorato(ArrayList<Cammino> traiettorie) {
        ArrayList<StatoReteAbstract> tuttiGliStatiDelleTraiettorie = new ArrayList<>();
        for (Cammino traiettoria : traiettorie) {
            tuttiGliStatiDelleTraiettorie.addAll(traiettoria.getCammino());
        }
        stati.retainAll(tuttiGliStatiDelleTraiettorie); //rimuove da stati tutti gli StatoRete che non sono contenuti nelle traiettorie
        stati.get(0).setNumero(0);
        StatoReteRidenominato root = new StatoReteRidenominato(stati.get(0));

        spazioC.aggiungiVertice(root);
        spazioC.setRoot(root);
        for (int i = 1; i < stati.size(); i++) {
            stati.get(i).setNumero(i);
            spazioC.aggiungiVertice(new StatoReteRidenominato(stati.get(i), null));
        }

    }

    private List<String> aggiornaDecorazione(List<String> decorazioneAggiornata, List<String> asList) {
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

    /**
     * metodo per testare la rete
     */
    public void test() {
        link[0] = eventi[0];
        for (Automa automa : automi) {
            automa.setStatoIniziale(link);
        }
        StatoRete statoAutoma0 = creaStatoCorrente();
        System.out.println(statoAutoma0.toString());
        automi.get(0).isAbilitato(link);
        Transizione t = automi.get(0).getTransizioneAbilitata().get(0);
        automi.get(0).scatta(t, link);
        statoAutoma0 = creaStatoCorrente();
        System.out.println(statoAutoma0.toString());
        link[0] = null;
        System.out.println("prova");

    }

    private ArrayList<Cammino> potatura() {
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

    private ArrayList<Cammino> ridenominazione(ArrayList<Cammino> traiettorie) {
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

//    private SpazioComportamentale creaSpazioComportamentaleDecorato(SpazioComportamentale _spazioComportamentale) {
//        camminiDecorati = new ArrayList<>();
//        SpazioComportamentale spazioComportamentale = _spazioComportamentale;
//
//        LinkedList<StatoReteAbstract> statiDecorati = new LinkedList<>();;
//        Stack<StatoReteDecorato> pilaStato = new Stack<>();//pila dei nuovi stati
//        Stack<StatoReteDecorato> pilaDiramazioni = new Stack<StatoReteDecorato>();//pila degli stati che hanno più di una transizione in uscita
//        //Stack<Cammino> pilaCammino = new Stack<>();
//        Cammino camminoAttuale = creaNuovoCammino();//il cammino attuale diventa un nuovo cammino con gli stati degli automi e i link azzerati
//        StatoReteDecorato root = new StatoReteDecorato(spazioComportamentale.getRoot(), null);
//        root.setTransizionePrecedente(null);
//        //spazioComportamentaleDecorato.setRoot(root);
////        spazioComportamentaleDecorato.aggiungiVertice(rootDecorata);
////        spazioComportamentaleDecorato.setRoot(rootDecorata);
//        ArrayList<String> decorazione = new ArrayList<>();
//
//        pilaStato.push(root);
//
//        while (!pilaStato.isEmpty()) {
//            StatoReteDecorato statoAttualeDecorato = pilaStato.pop();
//            StatoReteRidenominato statoAttuale = new StatoReteRidenominato(statoAttualeDecorato);
//            statoAttuale.setTransizionePrecedente(statoAttualeDecorato.getTransizionePrecedente());
//            decorazione = (ArrayList<String>) statoAttualeDecorato.getDecorazione();
//
//            if (!statiDecorati.contains(statoAttualeDecorato)) {//se è uno stato nuovo
//                StatoReteDecorato verticeDaInserire = new StatoReteDecorato(statoAttualeDecorato);
//                verticeDaInserire.setTransizionePrecedente(null);
//                statiDecorati.add(statoAttualeDecorato);//TODO: sostituito, prima era passato verticeDaInserire
////                spazioComportamentaleDecorato.aggiungiVertice(verticeDaInserire);
//                //statoAttuale.setNumero(stati.size());
////                statiDecorati.add(statoAttualeDecorato);
//                camminoAttuale.add(statoAttualeDecorato);
//                //spazioC.aggiungiVertice(new StatoReteRidenominato(statoAttuale));
////                System.out.println(statoAttuale.toString());
////                setRete(statoAttuale);
//                List<StatoReteAbstract> verticiAdiacenti = spazioComportamentale.getVerticiAdiacenti(statoAttuale);
//                if (verticiAdiacenti != null && verticiAdiacenti.size() > 0) {//se lo StatoRete non è uno stato finale            
//                    ArrayList<StatoReteDecorato> statiSuccessivi = new ArrayList<>();
//                    List<String> decorazioneAggiornata = null;
//
//                    if (verticiAdiacenti.size() == 1) {
//                        if (decorazione != null) {
//                            decorazioneAggiornata = new ArrayList<String> (decorazione);
//                        }
//                        StatoReteRidenominato statoDopoLoScatto = (StatoReteRidenominato) spazioComportamentale.getVerticiAdiacenti(statoAttuale).get(0);
//                        if (statoDopoLoScatto.getTransizionePrecedente().getRilevanza() != null) {
//                            decorazioneAggiornata = aggiornaDecorazione(decorazioneAggiornata, Arrays.asList(statoDopoLoScatto.getTransizionePrecedente().getRilevanza()));
//                        }
//                        statiSuccessivi.add(new StatoReteDecorato(statoDopoLoScatto, decorazioneAggiornata));
//
//                    } else {
//                        for (int j = 0; j < verticiAdiacenti.size(); j++) {//viene selezionato ogni StatoRete successivo
//                            if (decorazione != null) {
//                                decorazioneAggiornata = new ArrayList<String> (decorazione);
//                            } else {
//                                decorazioneAggiornata = null;
//                            }
////                                Transizione transizioneDaEseguire = transizioniAbilitate[i].get(j);
////                                transizioneEseguita = automi.get(i).scatta(transizioneDaEseguire, link);//viene fatta scattare la transizione da eseguire
////                                copiaStatoAttuale.setTransizionePrecedente(transizioneEseguita);
//                            StatoReteRidenominato statoDopoLoScatto = (StatoReteRidenominato) spazioComportamentale.getVerticiAdiacenti(statoAttuale).get(j);
//                            if (statoDopoLoScatto.getTransizionePrecedente().getRilevanza() != null) {
//                                decorazioneAggiornata = aggiornaDecorazione(decorazioneAggiornata, Arrays.asList(statoDopoLoScatto.getTransizionePrecedente().getRilevanza()));
//                            }
//                            statiSuccessivi.add(new StatoReteDecorato(statoDopoLoScatto, decorazioneAggiornata));
//                            pilaDiramazioni.add(statoAttualeDecorato);
//                        }
//                        pilaDiramazioni.pop();
//                    }
//
//                    for (StatoReteDecorato s : statiSuccessivi) {
//                        pilaStato.push(s);//                       
//                    }
//                } else {
//                    //stato senza transizioni abilitate
//                    Cammino nuovoCammino = new Cammino();
//                    nuovoCammino.copiaCammino(camminoAttuale);
//                    camminiDecorati.add(nuovoCammino);
//                    if (!pilaDiramazioni.isEmpty()) {
//                        camminoAttuale.togliFinoAPrimaDelloStato(pilaDiramazioni.pop());//gli ultimi elementi del cammini vengono rimossi finchè non si incontra il primo elemento della coda
//                    }
//                }
//            } else {
//                Cammino nuovoCammino = new Cammino();
//                camminoAttuale.add(statoAttualeDecorato);//                
//                nuovoCammino.copiaCammino(camminoAttuale);
//                camminiDecorati.add(nuovoCammino);
//                if (!pilaDiramazioni.isEmpty()) {
//                    camminoAttuale.togliFinoAPrimaDelloStato(pilaDiramazioni.pop());//gli ultimi elementi del cammini vengono rimossi finchè non si incontra il primo elemento della coda
//                }
//
//            }
//        }
//        SpazioComportamentale spazioComportamentaleDecorato = new SpazioComportamentale();
//        spazioComportamentaleDecorato=inserisciVerticiSpazioComportamentale(spazioComportamentaleDecorato, camminiDecorati, statiDecorati);
//        spazioComportamentaleDecorato=inserisciLatiSpazioComportamentale(spazioComportamentaleDecorato, camminiDecorati);
//        return spazioComportamentaleDecorato;
//    }
    private void stampaTraiettorie(ArrayList<Cammino> traiettorie) {

        System.out.println(Parametri.TRAIETTORIE_ETICHETTA);
        System.out.println();
        for (int i = 0; i < traiettorie.size(); i++) {
            int numeroCammino = i + 1;
            System.out.println("numero traiettoria: " + numeroCammino);
            System.out.println();
            System.out.println(traiettorie.get(i).toString());
            System.out.println();
            System.out.println();
        }
    }

    public Evento getEvento(String nomeEvento) {
        Evento evento = null;
        for (int i = 0; i < eventi.length; i++) {
            if (eventi[i].getDescrizione().equalsIgnoreCase(nomeEvento)) {
                evento = eventi[i];
            }
        }
        return evento;
    }

    public void addAutoma(Automa a) {
        automi.add(a);
    }

    public void setEventi(Evento[] eventi) {
        this.eventi = eventi;
    }

    public Evento getLink(int i) {
        return link[i];
    }

    public void setLink(int i, Evento evento) {
        link[i] = evento;
    }

    public String[] getEtichettaOsservabilita() {
        return etichettaOsservabilita;
    }

    public String[] getEtichettaRilevanza() {
        return etichettaRilevanza;
    }

    public List<Automa> getAutomi() {
        return automi;
    }

    public List<Cammino> getCammini() {
        return cammini;
    }

    public void setCammini(List<Cammino> cammini) {
        this.cammini = cammini;
    }

    public List<Cammino> getCamminiDecorati() {
        return camminiDecorati;
    }

    public void setCamminiDecorati(List<Cammino> camminiDecorati) {
        this.camminiDecorati = camminiDecorati;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Evento[] getLink() {
        return link;
    }

    public void setLink(Evento[] link) {
        this.link = link;
    }

    public SpazioComportamentale getSpazioC() {
        return spazioC;
    }

    public void setSpazioC(SpazioComportamentale spazioC) {
        this.spazioC = spazioC;
    }

    public SpazioComportamentale getSpazioComportamentaleDecorato() {
        return spazioComportamentaleDecorato;
    }

    public void setSpazioComportamentaleDecorato(SpazioComportamentale spazioComportamentaleDecorato) {
        this.spazioComportamentaleDecorato = spazioComportamentaleDecorato;
    }

    public SpazioComportamentale getDizionario() {
        return dizionario;
    }

    public void setDizionario(SpazioComportamentale dizionario) {
        this.dizionario = dizionario;
    }

    public void setDizionarioParziale(SpazioComportamentale dizionarioParziale) {
        this.dizionarioParziale = dizionarioParziale;
    }

}
