package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import view.Parametri;
import view.View;
import Model.Stato;
import Model.Rete;
import Model.Evento;
import Model.Transizione;
import Model.Automa;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Import {

    //private static final String path = "./src/FileInput/input.txt";
    private static List<String> file;

    public Rete caricaReteDaFile(String path) {
        try {
            apriFileTxt(path);
        } catch (IOException ex) {
            Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
        }
        //View.stampaFileTxt(file);
        return inizializzazioneRete();
    }

    private Rete inizializzazioneRete() {
        Rete rete;

        // nome della rete
        String nomeRete = getNomeRete(file);

        // lista eventi inizialmente vuota
        List<Evento> eventiIn = new ArrayList<>();
        List<Evento> eventiOut = new ArrayList<>();

        // Inizializzazione dei link
        String[] linkString = getLink(file);
        Evento[] link = new Evento[linkString.length];

        // Inizializzazione degli eventi
        String[] eventiString = getEventi(file);
        Evento[] eventi = new Evento[eventiString.length];
        for (int j = 0; j < eventi.length; j++) {
            Evento evento = new Evento(eventiString[j]);
            eventi[j] = evento;
        }

        // Inizializzazione dizionario osservabilita
        String[] etichettaOsservabilita = getEtichettaOsservabilita(file);

        // Inizializzazione dizionario rilevanza
        String[] etichettaRilevanza = getEtichettaRilevanza(file);

        // Creazione della rete
        rete = new Rete(nomeRete, link, eventi, etichettaOsservabilita, etichettaRilevanza);

        // Ciclo di inizializzazione degli automi
        String[] automi = getAutomi(file);
        for (String automa_str : automi) {
            // aggiunta dell'automa
            Automa automa = new Automa(automa_str);
            rete.addAutoma(automa);

            // aggiunta degli stati di automa
            ArrayList<String> statiAutoma = getStatiDaAutoma(automa, file);
            int i = 1;
            for (String stato_str : statiAutoma) {
                Stato stato = new Stato(stato_str);
                automa.addStato(stato);
                i++;
            }
        }
        
        // stampa delle caratteristiche
        System.out.println(Parametri.INIZIALIZZAZIONE_RETE);
        View.stampaNomeRete(rete);
        View.stampaAutomi(rete);
        View.stampaStati(rete);
        View.stampaEtichettaOsservabilita(rete);
        View.stampaEtichettaRilevanza(rete);
        System.out.println(getOsservabilita(rete.getAutomi().get(0), "t2a", file));
        System.out.println(getRilevanza(rete.getAutomi().get(0), "t2b", file));

        // Ciclo inizializzazione delle transizioni
        for (Automa automa : rete.getAutomi()) {
            for (Stato stato : automa.getStati()) {
                ArrayList<String> transizioniUscenti = getStatiDiPartenza(stato, automa, file);
                for (String transizione : transizioniUscenti) {
                    System.out.println("Automa " + automa.getDescrizione() + " - dallo stato " + stato.getDescrizione() + " la transazione uscente è: " + transizione);
                    String nomeTransizione = transizione;
                    String nomeStatoDestinazione = getStatoDestinazioneDiTransizione(automa, transizione, file);
                    System.out.println("Lo stato di destinazione è: " + nomeStatoDestinazione);
                    Stato statoDestinazione = automa.getStato(nomeStatoDestinazione);
                    int posizioneLinkIn = getLinkInDiTransizione(automa, transizione, linkString, file);
                    System.out.println("Posizione link in: " + posizioneLinkIn);
                    int indiceEventoRichiesto = getIndiceEventoRichiesto(automa, nomeTransizione, eventi, file);
                    if (indiceEventoRichiesto == -1) {
                        System.out.println("Nessun evento richiesto");
                        String[] eventiInUscitaString = getEventiInUscita(automa, transizione, file);
                        Evento[] eventiInUscita = getEventiInUscita(rete, eventiString, eventiInUscitaString);
                        String rilevanza = getRilevanza(automa, nomeTransizione, file);
                        System.out.println("La sua rilevanza è: " + rilevanza);
                        String osservabilita = getOsservabilita(automa, nomeTransizione, file);
                        System.out.println("La sua osservabilita è: " + osservabilita);
                        Transizione transizioneDaInserire = new Transizione(nomeTransizione, statoDestinazione, posizioneLinkIn, null, eventiInUscita, rilevanza, osservabilita);
                        stato.addTransazione(transizioneDaInserire);
                    } else {
                        Evento eventoRichiesto = eventi[getIndiceEventoRichiesto(automa, nomeTransizione, eventi, file)];
                        System.out.println("Evento richiesto: " + eventoRichiesto.getDescrizione());
                        String[] eventiInUscitaString = getEventiInUscita(automa, transizione, file);
                        Evento[] eventiInUscita = getEventiInUscita(rete, eventiString, eventiInUscitaString);
                        String rilevanza = getRilevanza(automa, nomeTransizione, file);
                        System.out.println("La sua rilevanza è: " + rilevanza);
                        String osservabilita = getOsservabilita(automa, nomeTransizione, file);
                        System.out.println("La sua osservabilita è: " + osservabilita);
                        Transizione transizioneDaInserire = new Transizione(nomeTransizione, statoDestinazione, posizioneLinkIn, eventoRichiesto, eventiInUscita, rilevanza, osservabilita);
                        stato.addTransazione(transizioneDaInserire);
                    }
                    System.out.println("");
                }
            }
        }
        return rete;

    }

    static Rete primoScenario() {
        Rete rete;
        Automa c2 = new Automa("c2");
        Stato s20 = new Stato("20");
        Stato s21 = new Stato("21");
        Evento e2 = new Evento("e2");
        Evento e3 = new Evento("e3");
        Evento[] eventi = {e2, e3};
        Evento[] link = new Evento[2];
        String[] osservabilita = {"o2", "o3"};
        String[] rilevanza = {"r", "f"};
        rete = new Rete("primo scenario", link, eventi, osservabilita, rilevanza);

        List<Evento> eventiIn = new ArrayList<>();
        List<Evento> eventiOut = new ArrayList<>();

        Evento[] eventoOut = {null, e3};
        String osservabilitaT2a;
        String rilevanzaT2a = null;
        osservabilitaT2a = rete.getEtichettaOsservabilita()[0];
        Transizione t2a = new Transizione("t2a", s21, 0, e2, eventoOut, rilevanzaT2a, osservabilitaT2a);
        s20.addTransazione(t2a);

        Evento[] eventot2b = {null, e3};
        String osservabilitaT2b = null;
        String rilevanzaT2b;
        rilevanzaT2b = rete.getEtichettaRilevanza()[0];
        Transizione t2b = new Transizione("t2b", s20, -1, null, eventot2b, rilevanzaT2b, osservabilitaT2b);
        s21.addTransazione(t2b);

        c2.addStato(s20);
        c2.addStato(s21);

        rete.addAutoma(c2);

        Automa c3 = new Automa("c3");

        Stato s30 = new Stato("30");
        Stato s31 = new Stato("31");

        Evento[] eventit3a = {e2, null};
        String osservabilitaT3a;
        String rilevanzaT3a = null;
        osservabilitaT3a = rete.getEtichettaOsservabilita()[1];
        Transizione t3a = new Transizione("t3a", s31, -1, null, eventit3a, rilevanzaT3a, osservabilitaT3a);
        s30.addTransazione(t3a);

        String osservabilitaT3c = null;
        String rilevanzaT3c;

        rilevanzaT3c = rete.getEtichettaRilevanza()[1];
        Transizione t3c = new Transizione("t3c", s31, 1, e3, null, rilevanzaT3c, osservabilitaT3c);
        Transizione t3b = new Transizione("t3b", s30, 1, e3, null, null, null);
        s31.addTransazione(t3b);
        s31.addTransazione(t3c);

        c3.addStato(s30);
        c3.addStato(s31);

        rete.addAutoma(c3);
        return rete;
    }

    public void apriFileTxt(String path) throws FileNotFoundException, IOException {
        File filetxt = new File(path);
        BufferedReader textReader = new BufferedReader(new FileReader(filetxt));

        List<String> fileToString = new ArrayList<>();
        try {
            int i = 0;
            String lineaDaCopiare;
            while ((lineaDaCopiare = textReader.readLine()) != null) {
                fileToString.add(lineaDaCopiare);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        file = fileToString;
        textReader.close();
    }

    private String getNomeRete(List<String> fileInput) {
        String nomeRete = fileInput.get(Parametri.NUMERO_RIGA_NOME_RETE);
        return nomeRete;
    }

    private String[] getAutomi(List<String> fileInput) {
        String daSplittare = fileInput.get(Parametri.NUMERO_RIGA_NOMI_AUTOMI);
        String[] automi = daSplittare.split("\t");
        return automi;
    }

    private String[] getLink(List<String> fileInput) {
        String daSplittare = fileInput.get(Parametri.NUMERO_RIGA_NOMI_LINK);
        String[] link = daSplittare.split("\t");
        return link;
    }

    private String[] getEventi(List<String> fileInput) {
        String daSplittare = fileInput.get(Parametri.NUMERO_RIGA_NOMI_EVENTI);
        String[] eventi = daSplittare.split("\t");
        return eventi;
    }
    
    private String[] getEtichettaOsservabilita(List<String> fileInput){
        String daSplittare = fileInput.get(Parametri.NUMERO_RIGA_NOMI_OSSERVABILITA);
        String[] dizionario = daSplittare.split("\t");
        return dizionario;
    }
    
    private String[] getEtichettaRilevanza(List<String> fileInput){
        String daSplittare = fileInput.get(Parametri.NUMERO_RIGA_NOMI_RILEVANZA);
        String[] dizionario = daSplittare.split("\t");
        return dizionario;
    }

    private ArrayList<String> getStatiDaAutoma(Automa automa, List<String> fileInput) {
        ArrayList<String> statiDiAutoma = new ArrayList<>();
        for (int i = 0; i < fileInput.size(); i++) {
            // non devo accettare la riga 3 perché nel file input è l'elenco degli automi
            if (fileInput.get(i).equals(automa.getDescrizione()) && i != 3) {
                int j = i + 1;
                String daSplittare = fileInput.get(j);
                String[] stati = daSplittare.split("\t");
                for (String stringa : stati) {
                    statiDiAutoma.add(stringa);
                }
            }
        }
        return statiDiAutoma;
    }

    private ArrayList<String> getNomiTransizioni(Automa automa, List<String> fileInput) {
        ArrayList<String> nomiTransizioni = new ArrayList<>();
        for (int i = 0; i < fileInput.size(); i++) {
            // la posizione delle transazioni è sempre sotto di 3 righe
            if (fileInput.get(i).equals(automa.getDescrizione()) && i != 3) {
                int j = i + 3;
                while (fileInput.get(j).length() != 0) {
                    String daSplittare = fileInput.get(j);
                    String[] splittato = daSplittare.split("\t");
                    String transizione = splittato[1];
                    nomiTransizioni.add(transizione);
                    j++;
                }
            }
        }
        return nomiTransizioni;
    }

    /**
     *
     * @param automa automa che contiene tutti gli stati
     * @param nomeTransizione nome della transizione di riferimento per cercare il suo stato di destinazione
     * @param fileInput lista contenente le stringhe del file input.txt
     * @return viene ritornato il nome dello stato destinazione della transizione
     */
    private String getStatoDestinazioneDiTransizione(Automa automa, String nomeTransizione, List<String> fileInput) {
        String statoDestinazione = null;
        for (int i = 0; i < fileInput.size(); i++) {
            // la posizione delle transazioni è sempre a partire dalla riga 3 del blocco di riferimento dell'automa
            if (fileInput.get(i).equals(automa.getDescrizione()) && i != 3) {
                int j = i + 3;
                while (fileInput.get(j).length() != 0) {
                    String daSplittare = fileInput.get(j);
                    String[] splittato = daSplittare.split("\t");
                    String transizioneDaConfrontare = splittato[1];
                    if (transizioneDaConfrontare.equalsIgnoreCase(nomeTransizione)) {
                        statoDestinazione = splittato[2];
                    }
                    j++;
                }
            }
        }
        return statoDestinazione;
    }

    private int getIndiceEventoRichiesto(Automa automa, String nomeTransizione, Evento[] elencoEventi, List<String> fileInput) {
        int indiceEventoRichiesto = -1;
        for (int i = 0; i < fileInput.size(); i++) {
            if (fileInput.get(i).equals(automa.getDescrizione()) && i != 3) {
                while (!fileInput.get(i).equals(Parametri.SEPARATORE)) {
                    if (fileInput.get(i).length() != 0 && fileInput.get(i).contains("t")) {
                        String transizioneDaConfrontare = fileInput.get(i);
                        transizioneDaConfrontare = transizioneDaConfrontare.substring(0, 3);
                        if (transizioneDaConfrontare.equalsIgnoreCase(nomeTransizione)) {
                            // controllo se la stringa ha un evento in ingresso o no
                            String eventoRichiestoString = fileInput.get(i).substring(5, 6);
                            if (eventoRichiestoString.equalsIgnoreCase("/")) {
                                return indiceEventoRichiesto;
                            } else {
                                eventoRichiestoString = fileInput.get(i).substring(5, 7);
                                for (int j = 0; j < elencoEventi.length; j++) {
                                    if (elencoEventi[j].getDescrizione().equalsIgnoreCase(eventoRichiestoString)) {
                                        indiceEventoRichiesto = j;
                                    }
                                }
                                return indiceEventoRichiesto;
                            }
                        }
                    }
                    i++;
                }
            }
        }
        return indiceEventoRichiesto;
    }

    private String[] getEventiInUscita(Automa automa, String nomeTransizione, List<String> fileInput) {
        String[] eventiInUscita = null;
        for (int i = 0; i < fileInput.size(); i++) {
            if (fileInput.get(i).equals(automa.getDescrizione()) && i != 3) {
                while (!fileInput.get(i).equals(Parametri.SEPARATORE)) {
                    if (fileInput.get(i).length() != 0 && fileInput.get(i).contains("t")) {
                        String transizioneDaConfrontare = fileInput.get(i);
                        String sottoStringa = transizioneDaConfrontare.substring(0, 3);
                        if (sottoStringa.equalsIgnoreCase(nomeTransizione)) {
                            // controllare se la stringa ha un uscita o meno
                            int j = transizioneDaConfrontare.indexOf("/");
                            // il metodo ritorna -1 se non trova "/"
                            if (j == -1) {
                                // la transizione non ha eventi in uscita
                                return eventiInUscita;
                            } else {
                                // salto il primo carattere perche' e' "{"
                                String eventiDaSplittare = transizioneDaConfrontare.substring(j + 2, transizioneDaConfrontare.length() - 1);
                                // lo split dei successivi eventi in uscita avviene soltanto dividendo con la virgola
                                eventiInUscita = eventiDaSplittare.split(",");
                                for (int x = 0; x < eventiInUscita.length; x++) {
                                    eventiInUscita[x] = rimuoviParentesi(eventiInUscita[x]);
                                }
                                return eventiInUscita;
                            }
                        }
                    }
                    i++;
                }
            }
        }
        return eventiInUscita;
    }

    /**
     * e' un metodo di conversione da String[] a Evento[] necessario per la costruzione dell'istanza di transizione
     *
     * @param eventi array che contiene tutti gli eventi della rete
     * @param eventiInUscita eventi in uscita dalla transizione considerata
     * @return ritorna l'array di Evento nel caso ci sono eventiInUscita dalla transizione, altrimenti ritorna null
     */
    private Evento[] getEventiInUscita(Rete rete, String[] eventi, String[] eventiInUscita) {
        Evento[] eventiFinale = new Evento[eventi.length];
        for (int x = 0; x < eventi.length; x++) {
            //eventiFinale[x] = new Evento(null);
            eventiFinale[x] = null;
        }

        int i = 0;
        if (eventiInUscita != null) {
            while (i < eventiInUscita.length) {
                for (int j = 0; j < eventi.length; j++) {
                    if (eventiInUscita[i].equalsIgnoreCase(eventi[j])) {
                        String daCopiare = eventiInUscita[i];
                        //eventiFinale[j] = new Evento(daCopiare);
                        eventiFinale[j] = rete.getEvento(daCopiare);
                    }
                }
                i++;
            }
        } else {
            eventiFinale = null;
        }

        return eventiFinale;
    }

    private ArrayList<Evento> getEventiInUscitaList(String[] eventi, String[] eventiInUscita) {
        ArrayList<Evento> eventiFinale = new ArrayList<>();
        int i = 0;
        if (eventiInUscita.length != 0) {
            while (i < eventiInUscita.length) {
                for (int j = 0; j < eventi.length; j++) {
                    if (eventiInUscita[i].equalsIgnoreCase(eventi[j])) {
                        Evento e = new Evento(eventi[i]);
                        eventiFinale.add(e);
                    }
                }
                i++;
            }
        }
        return eventiFinale;
    }

    private Evento[] convertiListInArray(ArrayList<Evento> eventi) {
        Evento[] eventiArray = new Evento[eventi.size()];
        for (int i = 0; i < eventi.size(); i++) {
            eventiArray[i] = eventi.get(i);
        }
        return eventiArray;
    }

    private static String rimuoviParentesi(String stringa) {
        String valida = stringa.substring(0, 2);
        return valida;
    }

    /**
     *
     * @param automa automa in cui e' presente la trasizione considerata
     * @param nomeTransizione e' il nome della transizione che sto considerando
     * @param elencoLink
     * @param fileInput
     * @return la funzione ritorna un int in cui, se esiste, ritorna la posizione del link "attivatore" della transizione altrimenti ritorna -1. 
     * Di riferimento si veda il metodo getLink in cui si calcolano quanti link ci sono nella rete
     */
    private int getLinkInDiTransizione(Automa automa, String nomeTransizione, String[] elencoLink, List<String> fileInput) {
        int posizioneLink = -1;
        for (int i = 0; i < fileInput.size(); i++) {
            if (fileInput.get(i).equals(automa.getDescrizione()) && i != 3) {
                while (!fileInput.get(i).equals(Parametri.SEPARATORE)) {
                    if (fileInput.get(i).length() != 0 && fileInput.get(i).contains("t")) {
                        String transizioneIntera = fileInput.get(i);
                        String transizione = transizioneIntera.substring(0, 3);
                        if (transizione.equalsIgnoreCase(nomeTransizione)) {
                            // controllo che la transizione non abbia linkIn vuoti
                            String separatore = transizioneIntera.substring(5, 6);
                            if (!separatore.equalsIgnoreCase("/")) {
                                // cerco il linkIn della transizione nella posizione 8-10
                                String linkIn = transizioneIntera.substring(8, 10);
                                // controllo nel vettore Link in che posizione sia e modifico posizioneLink
                                for (int j = 0; j < elencoLink.length; j++) {
                                    if (linkIn.equalsIgnoreCase(elencoLink[j])) {
                                        posizioneLink = j;
                                    }
                                }
                            }
                        }
                    }
                    i++;
                }
            }
        }
        return posizioneLink;
    }

    /**
     *
     * @param stato stato in cui si vuole cercare se esce qualche transizione.
     * @param automa automa dove e' contenuto lo stato considerato
     * @param fileInput fine inpunt contenente il txt
     * @return vengono ritornate le strighe con i nomi delle transizioni che escono dallo stato, null se non esce alcuna transizione
     */
    private ArrayList<String> getStatiDiPartenza(Stato stato, Automa automa, List<String> fileInput) {
        ArrayList<String> transizioniUscenti = new ArrayList<>();
        for (int i = 0; i < fileInput.size(); i++) {
            if (fileInput.get(i).equals(automa.getDescrizione()) && i != 3) {
                int j = i + 3;
                while (fileInput.get(j).length() != 0) {
                    String daSplittare = fileInput.get(j);
                    String[] splittato = daSplittare.split("\t");
                    String statoDaConfrontare = splittato[0];
                    if (statoDaConfrontare.equalsIgnoreCase(stato.getDescrizione())) {
                        String transizione = splittato[1];
                        transizioniUscenti.add(transizione);
                    }
                    j++;
                }
            }
        }
        return transizioniUscenti;
    }
    
    private String getOsservabilita(Automa automa, String nomeTransizione, List<String> fileInput) {
        String osservabilita = null;
        for (int i = 0; i < fileInput.size(); i++) {
            if (fileInput.get(i).equals(automa.getDescrizione()) && i != 3) {
                while (!fileInput.get(i).equals(Parametri.SEPARATORE)) {
                    if (fileInput.get(i).contains(nomeTransizione) && fileInput.get(i).contains(Parametri.OSSERVABILITA)) {
                        int j = fileInput.get(i).indexOf(":");
                        osservabilita = fileInput.get(i).substring(j + 2, fileInput.get(i).length());
                    }
                    i++;
                }
            }
        }
        return osservabilita;
    }
    
    private String getRilevanza(Automa automa, String nomeTransizione, List<String> fileInput){
        String rilevanza = null;
        for (int i = 0; i < fileInput.size(); i++) {
            if (fileInput.get(i).equals(automa.getDescrizione()) && i != 3) {
                while (!fileInput.get(i).equals(Parametri.SEPARATORE)) {
                    if (fileInput.get(i).contains(nomeTransizione) && fileInput.get(i).contains(Parametri.RILEVANZA)) {
                        int j = fileInput.get(i).indexOf(":");
                        rilevanza = fileInput.get(i).substring(j + 2, fileInput.get(i).length());
                    }
                    i++;
                }
            }
        }
        return rilevanza;
    }

}
