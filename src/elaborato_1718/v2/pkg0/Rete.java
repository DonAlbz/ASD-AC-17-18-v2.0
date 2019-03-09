/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elaborato_1718.v2.pkg0;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

/**
 *
 * @author Alb
 */
public class Rete {

    private static Vector<Automa> automi;
    private static Evento[] eventi;
    private static Vector<Cammino> cammini;
    private static String descrizione;
    private static Evento[] link;
    private static int numeroStati;
    private static SpazioComportamentale spazioC;
    private static LinkedList<StatoRete> stati;
    //private static Transizione transizioneAbilitata;

    public static void creaRete(String s, Evento[] _link, Evento[] _eventi) {
        descrizione = s;
        automi = new Vector<Automa>();
        eventi = _eventi;
        link = _link;
        cammini = new Vector<Cammino>();
        spazioC = new SpazioComportamentale();
    }

    public static void start() {

        Cammino camminoAttuale = creaNuovoCammino();//il cammino attuale diventa un nuovo cammino con gli stati degli automi e i link azzerati
        int numeroCammino = 0;
        numeroStati = 0;
        StatoRete statoRadice = creaStatoCorrente();
        scatta(camminoAttuale, statoRadice, (Vector<Automa>) automi.clone(), link.clone());
        stampaCammini();
        potatura();

    }

    private static void scatta(Cammino camminoAttuale, StatoRete statoAttuale, Vector<Automa> _automi, Evento[] _link) {
        boolean almenoUnAutomaAbilitato;
        boolean automaAttualeAbilitato;

        if (isNuovoStato(camminoAttuale, statoAttuale)) {//fintanto che si incontra un nuovo StatoRete non finale
            numeroStati++;
            ArrayList<Transizione>[] transizioneAbilitata = new ArrayList[_automi.size()];//Array di ArrayList, nella posizione i dell'array sono contenute le transizioni abilitate per l'automa i
            Transizione transizioneEseguita;
            almenoUnAutomaAbilitato = false;
            automaAttualeAbilitato = false;
            for (int i = 0; i < _automi.size() /*&& !scattato*/; i++) {//se nessun automa è già scattato, si itera su tutti gli automi
                //setRete(statoAttuale);
                automaAttualeAbilitato = _automi.get(i).isAbilitato(_link);//true se l'automa attuale ha uno stato con una transizione abilitata
                almenoUnAutomaAbilitato = almenoUnAutomaAbilitato || automaAttualeAbilitato;//true se almeno uno tra gli automi è abilitato
                transizioneAbilitata[i] = _automi.get(i).getTransizioneAbilitata();//transizione abilitata diventa la transizione abilitata allo scatto nell'automa attuale                   

            }
            if (almenoUnAutomaAbilitato) {
                eseguiTransizioni(transizioneAbilitata.clone(), _automi, _link, statoAttuale, camminoAttuale);
//                for (int i = 0; i < _automi.size(); i++) {
//                    if (_automi.get(i).isAbilitato(_link)) {//se l'automa attuale è abilitato
//                        if (transizioneAbilitata[i].size() == 1) {
//                            //eseguiTransizione(statoAttuale, camminoAttuale, _link, _automi.get(i));
//                            transizioneEseguita = _automi.get(i).scatta(_link);//l'automa attuale viene fatto scattare e transizione eseguita diventa la transizione che è stata eseguita
//                            statoAttuale.setTransizioneEseguita(transizioneEseguita);//la transizione eseguita viene aggiunta allo StatoRete attuale
//                            camminoAttuale.add(statoAttuale);//Lo StatoRete prima dello scatto viene aggiunto al cammino attuale                    
//                            StatoRete statoDopoLoScatto = creaStatoCorrente(_automi, _link);
//                            scatta(camminoAttuale, statoDopoLoScatto, _automi, _link);
//                        }
//                        if (transizioneAbilitata[i].size() > 1) {//Se lo StatoRete attuale ha più di una transizione abilitata
//                            for (int j = 0; j < transizioneAbilitata[i].size(); j++) {//vengono fatte scattare tutte, ognuna su un nuovo cammino
//                                //setRete(statoAttuale);
//                                Transizione transizioneDaEseguire = transizioneAbilitata[i].get(j);
//                                Cammino nuovoCamminoAttuale = new Cammino();//viene creato un nuovo cammino
//                                nuovoCamminoAttuale.copiaCammino(camminoAttuale);
//                                Vector<Automa> automiAttuali = copiaAutomi(_automi);
//                                //vengono creati nuovi automi indipendenti da utilizzare su questo nuovo cammino, clonati da _automi
//                                Evento[] linkAttuali = _link.clone();//vengono creati nuovi link indipendenti da utilizzare su questo nuovo cammino
//                                StatoRete nuovoStatoAttuale;
//                                nuovoStatoAttuale = creaStatoCorrente(automiAttuali, linkAttuali, statoAttuale.getNumero());//viene clonato un nuovo stato corrente
//                                transizioneEseguita = automiAttuali.get(i).scatta(transizioneDaEseguire, linkAttuali);//viene fatta scattare la transizione da eseguire
//                                nuovoStatoAttuale.setTransizioneEseguita(transizioneEseguita);//la transizione eseguita viene aggiunta allo StatoRete attuale
//                                nuovoCamminoAttuale.add(nuovoStatoAttuale);//Lo StatoRete attuale viene aggiunto al cammino attuale                    
//                                StatoRete statoDopoLoScatto = creaStatoCorrente(automiAttuali, linkAttuali);//creazione dello stato corrente per la verifica nel while
//                                scatta(nuovoCamminoAttuale, statoDopoLoScatto, automiAttuali, linkAttuali);
//                            }
//                        }
//                    }
//                }
            } else {//se nessuno degli automi è abilitato allo scatto               
                camminoAttuale.add(statoAttuale);//lo StatoRete attuale è uno stato finale
                cammini.add(camminoAttuale);

            }
        } else {//è il caso del LOOP: lo stato corrente viene aggiunto al cammino (è uguale allo ad un altro stato del cammino)
            camminoAttuale.add(statoAttuale);
            cammini.add(camminoAttuale);
        }
    }

    private static void eseguiTransizioni(ArrayList<Transizione>[] _transizioneAbilitata, Vector<Automa> _automi,
            Evento[] _link, StatoRete statoAttuale, Cammino camminoAttuale) {
        ArrayList<Transizione>[] transizioneAbilitata = _transizioneAbilitata;
        Transizione transizioneEseguita;
        int numeroTransizioni = 0;
        for (ArrayList<Transizione> t : transizioneAbilitata) {
            numeroTransizioni += t.size();
        }
        for (int i = 0; i < _automi.size(); i++) {
            if (_automi.get(i).isAbilitato(_link)) {//se l'automa attuale è abilitato
                if (numeroTransizioni == 1) {
                    //eseguiTransizione(statoAttuale, camminoAttuale, _link, _automi.get(i));
                    transizioneEseguita = _automi.get(i).scatta(_link);//l'automa attuale viene fatto scattare e transizione eseguita diventa la transizione che è stata eseguita
                    statoAttuale.setTransizioneEseguita(transizioneEseguita);//la transizione eseguita viene aggiunta allo StatoRete attuale
                    camminoAttuale.add(statoAttuale);//Lo StatoRete prima dello scatto viene aggiunto al cammino attuale                    
                    StatoRete statoDopoLoScatto = creaStatoCorrente(_automi, _link);
                    scatta(camminoAttuale, statoDopoLoScatto, _automi, _link);
                }
                if (numeroTransizioni > 1) {//Se lo StatoRete attuale ha più di una transizione abilitata
                    for (int j = 0; j < transizioneAbilitata[i].size(); j++) {//vengono fatte scattare tutte, ognuna su un nuovo cammino
                        //setRete(statoAttuale);
                        Transizione transizioneDaEseguire = transizioneAbilitata[i].get(j);
                        Cammino nuovoCamminoAttuale = new Cammino();//viene creato un nuovo cammino
                        nuovoCamminoAttuale.copiaCammino(camminoAttuale);
                        Vector<Automa> automiAttuali = copiaAutomi(_automi);
                        //vengono creati nuovi automi indipendenti da utilizzare su questo nuovo cammino, clonati da _automi
                        Evento[] linkAttuali = _link.clone();//vengono creati nuovi link indipendenti da utilizzare su questo nuovo cammino
                        StatoRete nuovoStatoAttuale;
                        nuovoStatoAttuale = creaStatoCorrente(automiAttuali, linkAttuali, statoAttuale.getNumero());//viene clonato un nuovo stato corrente
                        transizioneEseguita = automiAttuali.get(i).scatta(transizioneDaEseguire, linkAttuali);//viene fatta scattare la transizione da eseguire
                        nuovoStatoAttuale.setTransizioneEseguita(transizioneEseguita);//la transizione eseguita viene aggiunta allo StatoRete attuale
                        nuovoCamminoAttuale.add(nuovoStatoAttuale);//Lo StatoRete attuale viene aggiunto al cammino attuale                    
                        StatoRete statoDopoLoScatto = creaStatoCorrente(automiAttuali, linkAttuali);//creazione dello stato corrente per la verifica nel while
                        scatta(nuovoCamminoAttuale, statoDopoLoScatto, automiAttuali, linkAttuali);
                    }
                }
            }
        }

    }

    public static void start2() {
        scatta2();
        stampaCammini();
        ArrayList<Cammino> traiettorie = potatura();
        stampaTraiettorie(traiettorie);
        inserisciVerticiSpazioComportamentale(traiettorie);
        inserisciLatiSpazioComportamentale(traiettorie);
        System.out.println(spazioC.toString());
   
    }

    /**
     * Creazione dei cammini e dello spazio comportamentale
     *
     */
    public static void scatta2() {
        Stack<StatoRete> pilaStato = new Stack<>();//pila dei nuovi stati
        Stack<StatoRete> pilaDiramazioni = new Stack<StatoRete>();//pila degli stati che hanno più di una transizione in uscita
        //Stack<Cammino> pilaCammino = new Stack<>();
        Cammino camminoAttuale = creaNuovoCammino();//il cammino attuale diventa un nuovo cammino con gli stati degli automi e i link azzerati
        stati = new LinkedList<>();
        int numeroCammino = 0;
        numeroStati = 0;

        StatoRete statoRadice = creaStatoCorrente();
        pilaStato.push(statoRadice);
        // pilaCammino.push(camminoAttuale);
        while (!pilaStato.isEmpty()) {
            StatoRete statoAttuale = pilaStato.pop();
//            Cammino camminoTest=pilaCammino.pop();

            if (!stati.contains(statoAttuale)) {//se è uno stato nuovo
                statoAttuale.setNumero(stati.size());
                stati.add(statoAttuale);
                camminoAttuale.add(statoAttuale);
                //spazioC.aggiungiVertice(new StatoReteRidenominato(statoAttuale));
//                System.out.println(statoAttuale.toString());
                setRete(statoAttuale);
                if (statoAttuale.isAbilitato(automi)) {
                    ArrayList<Transizione>[] transizioniAbilitate = new ArrayList[automi.size()];
                    int numeroTransizioniAbilitate = 0;
                    for (int i = 0; i < automi.size(); i++) {//se nessun automa è già scattato, si itera su tutti gli automi                        
                        automi.get(i).isAbilitato(link);
                        transizioniAbilitate[i] = automi.get(i).getTransizioneAbilitata();//transizione abilitata diventa la transizione abilitata allo scatto nell'automa attuale                   
                        numeroTransizioniAbilitate += transizioniAbilitate[i].size();
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
                            for (int j = 0; j < transizioniAbilitate[i].size(); j++) {//vengono fatte scattare tutte, ognuna su un nuovo cammino
                                setRete(statoAttuale);
                                Transizione transizioneDaEseguire = transizioniAbilitate[i].get(j);
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
//                        spazioC.aggiungiLato(statoAttuale, s);
                    }
                } else {
                    //stato senza transizioni abilitate
                    Cammino nuovoCammino = new Cammino();
                    statoAttuale.setNumero(stati.size() - 1);
                    nuovoCammino.copiaCammino(camminoAttuale);
                    cammini.add(nuovoCammino);
                    if (!pilaDiramazioni.isEmpty()) {
                        camminoAttuale.togliFinoAPrimaDelloStato(pilaDiramazioni.pop());//gli ultimi elementi del cammini vengono rimossi finchè non si incontra il primo elemento della coda
                    }
                }
            } else {
                //TODO: loop nuovoCammino;
                Cammino nuovoCammino = new Cammino();
                statoAttuale.setNumero(stati.indexOf(statoAttuale));
                StatoRete statoPrecedente = camminoAttuale.getUltimoStato();
                camminoAttuale.add(statoAttuale);
//                spazioC.aggiungiLato(statoPrecedente, statoAttuale);
                nuovoCammino.copiaCammino(camminoAttuale);
                cammini.add(nuovoCammino);
                if (!pilaDiramazioni.isEmpty()) {
                    camminoAttuale.togliFinoAPrimaDelloStato(pilaDiramazioni.pop());//gli ultimi elementi del cammini vengono rimossi finchè non si incontra il primo elemento della coda
                }

            }
        }

    }

    private static void stampaCammini() {

        System.out.println(Parametri.CAMMINI_ETICHETTA);
        System.out.println();
        for (int i = 0; i < cammini.size(); i++) {
            int numeroCammino = i + 1;
            System.out.println("numero cammino: " + numeroCammino);
            System.out.println();
            System.out.println(cammini.get(i).toString());
            System.out.println();
        }
    }

    private static void eseguiTransizione(StatoRete s, Cammino c, Evento[] _link, Automa a) {
        Transizione transizioneEseguita = a.scatta(_link);//l'automa attuale viene fatto scattare e transizione eseguita diventa la transizione che è stata eseguita
        s.setTransizioneEseguita(transizioneEseguita);//la transizione eseguita viene aggiunta allo StatoRete attuale
        c.add(s);//Lo StatoRete prima dello scatto viene aggiunto al cammino attuale    
    }

    private static Cammino scattaR(Cammino camminoAttuale) {
        return null;
    }

    public static void addAutoma(Automa a) {
        automi.add(a);
    }

    private static StatoRete creaStatoCorrente() {
        Stato[] statoAutomi = new Stato[automi.size()];
        for (int i = 0; i < statoAutomi.length; i++) {
            statoAutomi[i] = automi.get(i).getStatoCorrente();
        }
        return new StatoRete(link.clone(), statoAutomi, numeroStati);
    }

    private static StatoRete creaStatoCorrente(Vector<Automa> _automi, Evento[] _link) {
        Stato[] statoAutomi = new Stato[_automi.size()];
        for (int i = 0; i < statoAutomi.length; i++) {
            statoAutomi[i] = _automi.get(i).getStatoCorrente();
        }
        return new StatoRete(_link.clone(), statoAutomi, numeroStati);
    }

    private static StatoRete creaStatoCorrente(Vector<Automa> _automi, Evento[] _link, int nStato) {
        Stato[] statoAutomi = new Stato[_automi.size()];
        for (int i = 0; i < statoAutomi.length; i++) {
            statoAutomi[i] = _automi.get(i).getStatoCorrente();
        }
        return new StatoRete(_link.clone(), statoAutomi, nStato);
    }

    /*private static void addStatoAlCammino(int numeroCammino, StatoRete statoRete) {
        cammini.get(numeroCammino).add(statoRete);
    }*/
    private static boolean isNuovoStato(Cammino camminoAttuale, StatoRete statoRete) {
        return !camminoAttuale.contains(statoRete);
    }

    private static void inizializza() {
        cammini.add(new Cammino());
        impostaStatiIniziali();
    }

    private static void impostaStatiIniziali() {
        Arrays.fill(link, null); // svuota l'array link

        for (Automa automa : automi) {
            automa.setStatoIniziale(link);
        }

        //addStatoAlCammino(numeroCammino, statoRete);
        //       System.out.println(statoRete.getDescrizione());
    }

    private static Cammino creaNuovoCammino() {
        Cammino nuovoCammino = new Cammino();
        impostaStatiIniziali();
        return nuovoCammino;
    }

    private static void setRete(StatoRete statoAttuale) {
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
    private static Vector<Automa> copiaAutomi(Vector<Automa> _automi) {
        Vector<Automa> daRitornare = new Vector<>();
        for (int i = 0; i < _automi.size(); i++) {
            daRitornare.add(_automi.get(i).copia());
        }
        return daRitornare;
    }

    private static void inserisciLatiSpazioComportamentale(ArrayList<Cammino> traiettorie) {
        for (Cammino traiettoria : traiettorie) {
            ArrayList<StatoRete> statiTraiettoria = traiettoria.getCammino();
            if (statiTraiettoria.size() > 1) {
                for (int i = 1; i < statiTraiettoria.size(); i++) {
                    int numeroStatoPrecedente;
                    numeroStatoPrecedente=stati.indexOf(statiTraiettoria.get(i-1));
                    StatoRete statoPrecedenteTraiettoria = statiTraiettoria.get(i - 1);
                    statoPrecedenteTraiettoria.setNumero(numeroStatoPrecedente);
                    StatoReteRidenominato statoPrecedente = new StatoReteRidenominato(statoPrecedenteTraiettoria);
                    
                    int numeroStatoCorrente;
                    numeroStatoCorrente=stati.indexOf(statiTraiettoria.get(i));
                    StatoRete statoCorrenteTraiettoria = statiTraiettoria.get(i);
                    statoCorrenteTraiettoria.setNumero(numeroStatoCorrente);
                    StatoReteRidenominato statoCorrente = new StatoReteRidenominato(statoCorrenteTraiettoria);
                   
                    spazioC.aggiungiLato(statoPrecedente, statoCorrente);
                }
            }
        }
    }

    /**Etichetta in maniera ordinata gli stati e li inserisce nello spazio comportamentale
     *
     * @param traiettorie
     */
    private static void inserisciVerticiSpazioComportamentale(ArrayList<Cammino> traiettorie) {
        ArrayList<StatoRete> tuttiGliStatiDelleTraiettorie = new ArrayList<>();
        for (Cammino traiettoria : traiettorie) {
            tuttiGliStatiDelleTraiettorie.addAll(traiettoria.getCammino());
        }
        stati.retainAll(tuttiGliStatiDelleTraiettorie); //rimuove da stati tutti gli StatoRete che non sono contenuti nelle traiettorie
        for (int i = 0; i < stati.size(); i++) {
            stati.get(i).setNumero(i);
            spazioC.aggiungiVertice(new StatoReteRidenominato(stati.get(i)));
        }
        
    }

    public void setEventi(Evento[] eventi) {
        this.eventi = eventi;
    }

    public static Evento getLink(int i) {
        return link[i];
    }

    public static void setLink(int i, Evento evento) {
        link[i] = evento;
    }

    /**
     * metodo per testare la rete
     */
    public static void test() {
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

    private static ArrayList<Cammino> potatura() {
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
                if (traiettorie.get(j).contains(notTraiettorie.get(i).getUltimoStato())) {
                    notTraiettorie.get(i).setIsTraiettoria(true);
                    traiettorie.add(notTraiettorie.get(i));
                    notTraiettorie.remove(i);
                    i--;
                }
            }
        }

//        // rimozione dallo spazio comportamentale degli stati potati
//        for (int i = 0; i < notTraiettorie.size(); i++) {
//            boolean appartieneATraiettoria = false;
//            do {
//                StatoRete ultimoStato = notTraiettorie.get(i).getUltimoStato();
//                notTraiettorie.get(i).rimuoviUltimoStato();
//                StatoRete penultimoStato = notTraiettorie.get(i).getUltimoStato();
//
//                spazioC.provaARimuovereVertice(new StatoReteRidenominato(ultimoStato));
//
//                for (int j = 0; j < traiettorie.size() && !appartieneATraiettoria; j++) {
//                    if (traiettorie.get(j).contains(penultimoStato)) {
//                        appartieneATraiettoria = true;
//                    }
//                }
//            } while (!appartieneATraiettoria);
//        }
        return traiettorie;
    }

    private static void stampaTraiettorie(ArrayList<Cammino> traiettorie) {

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

}
