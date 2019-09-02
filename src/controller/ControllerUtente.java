/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Model.*;
import view.*;
import Utilita.InputDati;
import Utilita.MyMenu;
import Utilita.ServizioFile;
import view.Parametri;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author alber
 */
public class ControllerUtente {

//    public static File fileSalvataggio = new File(Parametri.PATH_FILE_INPUT + "/salvataggio.dat");
    

    public static Rete start() {

        Rete rete = menuAvvio();
        if (rete != null) {
            menuRete(rete);
        }

        return rete;
    }

    public static void startDebug(Rete rete) {
        distillaDiagnosiDaDizionarioParziale(rete);

    }

    /**
     * Metodo che crea la rete attravero il menu iniziale (o la importa o la
     * carica da file)
     */
    public static Rete menuAvvio() {
        MyMenu menu = new MyMenu(Parametri.TITOLO_MENU_INIZIALE, Parametri.VOCI_MENU_INIZIALE);
        int selezione = menu.scegli();
        Rete rete = null;
        switch (selezione) {
            case 1:
                rete = importa();
                Controller.creaSpazioComportamentaleDecoratoMetodoAlternativo(rete, System.currentTimeMillis());
                
                break;

            case 2:
//                rete = carica();
                rete = caricaNuovo();
                break;

        }
        return rete;
    }

    /**
     * Metodo che permette di eseguire delle osservazioni o delle operazioni
     * sulla rete considerata
     *
     * @param rete
     */
    public static void menuRete(Rete rete) {
        boolean fineProgramma = false;
        MyMenu menu = new MyMenu(Parametri.TITOLO_MENU_RETE, Parametri.VOCI_MENU_RETE);
        do {
            int selezione = menu.scegli();
            fineProgramma = selezioneMenuRete(rete, selezione);
        } while (!fineProgramma);
    }

    private static Rete importa() {
        File cartella = new File(Parametri.PATH_FILE_INPUT);

        // inizializzazione della classe che mi permette di filtrare i txt
        FilenameFilter textFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(Parametri.FORMATO_TXT)) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        File[] listaFile = cartella.listFiles(textFilter);
        String[] nomiFile = new String[listaFile.length];
        for (int i = 0; i < listaFile.length; i++) {
            nomiFile[i] = listaFile[i].getName();
        }

        MyMenu menuSelezione = new MyMenu(Parametri.IMPORT_SELEZIONE, nomiFile);
        int selezione = menuSelezione.scegliSenzaUscita();
        File fileScelto = listaFile[selezione - 1];
        String pathCompletoFile = fileScelto.getAbsolutePath();
        Import nuovoImport = new Import();
        Rete rete = nuovoImport.caricaReteDaFile(pathCompletoFile);

        View.messaggioImportCorretto(rete);
        return rete;
    }

    private static Rete caricaNuovo() {
        Rete rete = null;
        File cartella = new File(Parametri.PATH_FILE_INPUT);

        // inizializzazione della classe che mi permette di filtrare i dat
        FilenameFilter textFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(Parametri.FORMATO_DAT)) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        File[] listaFile = cartella.listFiles(textFilter);
        String[] nomiFile = new String[listaFile.length];
        for (int i = 0; i < listaFile.length; i++) {
            nomiFile[i] = listaFile[i].getName();
        }

        MyMenu menuSelezione = new MyMenu(Parametri.CARICAMENTO_SELEZIONE, nomiFile);
        int selezione = menuSelezione.scegliSenzaUscita();
        File fileScelto = listaFile[selezione - 1];
        String pathCompletoFile = fileScelto.getAbsolutePath();
        Import nuovoImport = new Import();
//        rete = nuovoImport.caricaReteDaFile(pathCompletoFile);
        File fileSalvataggio = new File(pathCompletoFile);
        if (fileSalvataggio.exists()) {
            try {
                rete = (Rete) ServizioFile.caricaSingoloOggetto(fileSalvataggio);
            } catch (ClassCastException exc) {
                System.out.println("Non c'è il cast");
            } finally {
                if (rete != null) {
                    View.messaggioCaricamentoCorretto(rete);
                } else {
                    System.out.println("Problemi con il caricameto");
                }
            }
        } else {
            System.out.println("Problemi con il file");
        }

        return rete;
    }

    // metodo deprecato
    private static Rete carica() {
        Rete rete = null;
        File fileSalvataggio = null;

        // impostazione della directory di partenza
        JFileChooser fileScelto = new JFileChooser(Parametri.PATH_FILE_INPUT);
        // creazione del filtro dat
        FileFilter filtroDat = new FileNameExtensionFilter("DAT file", "dat");
        // inserisco nella scelta del tipo di file un filtro dat
        fileScelto.addChoosableFileFilter(filtroDat);
        // di default filtro solo i file dat (lo aggiunge in automatico alla combo sottostante)
        fileScelto.setFileFilter(filtroDat);
        // imposto di default il nome del file da cercare
        fileScelto.setSelectedFile(new File("salvataggio.dat"));

        int selezione = fileScelto.showDialog(null, "Seleziona il file da caricare");
        if (selezione == JFileChooser.APPROVE_OPTION) {
            String path = fileScelto.getSelectedFile().getPath();
//            System.out.println(fileScelto.getSelectedFile());
//            Import nuovoImport = new Import();
//            rete = nuovoImport.caricaReteDaFile(path);
            fileSalvataggio = new File(path);
        }

        if (fileSalvataggio.exists()) {
            try {
                rete = (Rete) ServizioFile.caricaSingoloOggetto(fileSalvataggio);
            } catch (ClassCastException exc) {
                System.out.println("Non c'è il cast");
            } finally {
                if (rete != null) {
                    System.out.println("Caricamento corretto");
                } else {
                    System.out.println("Problemi con il caricameto");
                }
            }
        } else {
            System.out.println("Problemi con il file");
        }
        return rete;
    }

    private static boolean selezioneMenuRete(Rete rete, int selezione) {
        boolean end = false;
        switch (selezione) {
            case 1:
                menuInformazioniRete(rete);
                break;

            case 2:
                menuCompiti(rete);
                break;

            case 3:
                salva(rete);
                break;

            case 4:
                cambiaRete(rete);
                break;

            case 0:
                end = InputDati.yesOrNo(Parametri.MESSAGGIO_FINE_PROGRAMMA);
        }
        return end;
    }

    /**
     * Permette di visualizzare varie informazioni sulla rete
     *
     * @param rete
     */
    private static void menuInformazioniRete(Rete rete) {
        boolean fineMenu = false;
        MyMenu menu = new MyMenu(Parametri.TITOLO_MENU_INFORMAZIONI_RETE, Parametri.VOCI_MENU_INFORMAZIONI_RETE);
        do {
            int selezione = menu.scegliMenuInterno();
            switch (selezione) {
                case 1:
                    System.out.println("");
                    View.stampaNomeRete(rete);
                    break;

                case 2:
                    System.out.println("");
                    View.stampaAutomi(rete);
                    break;

                case 3:
                    System.out.println("");
                    View.stampaStati(rete);
                    break;

                case 4:
                    System.out.println("");
                    View.stampaTransizioni(rete);
                    break;

                case 5:
                    System.out.println("");
                    View.stampaGlobaleRete(rete);
                    break;

                case 0:
                    fineMenu = true;

            }
        } while (!fineMenu);
    }

    /**
     * Permette di eseguire i compiti assegnati sulla rete
     *
     * @param rete
     */
    private static void menuCompiti(Rete rete) {
        boolean fineMenu = false;
        MyMenu menu = new MyMenu(Parametri.TITOLO_MENU_COMPITI, Parametri.VOCI_MENU_COMPITI);
        do {
            int selezione = menu.scegliMenuInterno();

            switch (selezione) {
                case 1:
                    creaSpazioComportamentale(rete);
                    break;

                case 2:
                    creaSpazioComportamentaleDecorato(rete);
                    break;

                case 3:
                    distillaDiagnosi(rete);
                    break;
                case 4:
                    distillaDiagnosiDaDizionarioParziale(rete);
                    break;

                case 5:
                    distillaDiagnosiDaDizionarioParzialeIncrementale(rete);
                    break;

                case 6:
                    costruzioneDiSpaziVincolati(rete);
                    break;

                case 0:
                    fineMenu = true;
            }
        } while (!fineMenu);
    }

    /**
     * Salva la rete considerata
     *
     * @param rete
     */
    private static void salva(Rete rete) {
        File fileSalvataggio = new File(Parametri.PATH_FILE_INPUT + "/" + rete.getDescrizione() + ".dat");
        ServizioFile.salvaSingoloOggetto(fileSalvataggio, rete);
        View.messaggioSalvataggioCorretto(rete);
    }

    /**
     * crea lo spazioComportamentale della rete (e visualizza info)
     *
     * @param rete
     */
    private static void creaSpazioComportamentale(Rete rete) {
        long tempoIniziale = System.currentTimeMillis();
        Controller.creaSpazioComportamentale(rete, tempoIniziale);
        long tempoTotale = System.currentTimeMillis() - tempoIniziale;

        System.out.println("TEMPO COSTRUZIONE SPAZIO COMPORTAMENTALE: " + tempoTotale + "ms");
    }

    /**
     * crea lo spazioComportamentaleDecorato della rete (e visualizza info)
     *
     * @param rete
     */
    private static SpazioComportamentale creaSpazioComportamentaleDecorato(Rete rete) {
        long tempoIniziale = System.currentTimeMillis();

        SpazioComportamentale spazioComportamentaleDecorato = null;
        spazioComportamentaleDecorato = Controller.creaSpazioComportamentaleDecorato(rete, tempoIniziale);

        long tempoTotale = System.currentTimeMillis() - tempoIniziale;
        System.out.println("TEMPO COSTRUZIONE SPAZIO COMPORTAMENTALE DECORATO: " + tempoTotale + "ms");
        return spazioComportamentaleDecorato;
    }

    private static SpazioComportamentale creaDizionario(Rete rete, SpazioComportamentale spazioComportamentaleDecorato, long tempoIniziale) {
        SpazioComportamentale dizionario = Controller.creaDizionario(rete, spazioComportamentaleDecorato, tempoIniziale);
        rete.setDizionario(dizionario);
        return dizionario;
    }

    private static void distillaDiagnosi(Rete rete) {
        long tempoIniziale = System.currentTimeMillis();

        SpazioComportamentale dizionario = null;
        if (rete.getDizionario() == null) {
            SpazioComportamentale spazioComportamentaleDecorato = creaSpazioComportamentaleDecorato(rete);
            if (spazioComportamentaleDecorato != null) {
                dizionario = creaDizionario(rete, spazioComportamentaleDecorato, tempoIniziale);
            }
        } else {
            dizionario = rete.getDizionario();
        }
        if (dizionario != null) {
            long tempoTotale = System.currentTimeMillis() - tempoIniziale;
            System.out.println("TEMPO COSTRUZIONE DIZIONARIO: " + tempoTotale + "ms");
            List<String> etichetteOsservabilita = acquisisciStringaEtichetteOsservabilita(rete);
            tempoIniziale = System.currentTimeMillis();
            String diagnosi = Controller.distillaDiagnosi(dizionario, etichetteOsservabilita, tempoIniziale);
            if (diagnosi != null) {
                System.out.println(diagnosi);
                tempoTotale = System.currentTimeMillis() - tempoIniziale;
                System.out.println("TEMPO DISTILLAZIONE DIAGNOSI: " + tempoTotale + "ms");
            } else {
                if (System.currentTimeMillis() - tempoIniziale > Parametri.getTempoEsecuzioneMax()) {
                    //TODO CAMO SCRIVERE CHE IL TEMPO NON E' BASTATO PER FARE LA DIAGNOSI

                } else {
                    //SE DIAGNOSI == NULL  o non e' uno stato finale, oppure e' andato storto qualcosa;
                    View.messaggioErroreDiagnosi();
                }
            }
        }
    }

    private static List<String> acquisisciStringaEtichetteOsservabilita(Rete rete) {
        List<String> daCercare = null;
        View.messaggioAquisizioneEtichettaOsservabilita();

        List<String> etichetteOsservabilita = Controller.getEtichetteOsservabilita(rete);
        daCercare = InputDati.leggiInserimentoStringaEtichette(Parametri.MESSAGGIO_INSERISCI, etichetteOsservabilita);
        return daCercare;
    }

    private static void distillaDiagnosiDaDizionarioParziale(Rete rete) {
        View.stampaLegendaEspressioneRegolareOsservazioni(rete);
        String osservazione = InputDati.inserimentoEspressioneRegolare(Parametri.MESSAGGIO_INSERISCI_ESPRESSIONE_REGOLARE, rete.getEtichettaOsservabilita());
        rete.setDizionarioParziale(null);
        long tempoIniziale = System.currentTimeMillis();

        SpazioComportamentale dizionarioParziale = Controller.creaDizionarioParziale(rete, osservazione, tempoIniziale);
        if (dizionarioParziale != null) {
            long tempoTotale = System.currentTimeMillis() - tempoIniziale;
            System.out.println("TEMPO COSTRUZIONE DIZIONARIO PARZIALE: " + tempoTotale + "ms");
            rete.setDizionarioParziale(dizionarioParziale);
            List<String> etichetteOsservabilita = acquisisciStringaEtichetteOsservabilita(rete);
            tempoIniziale = System.currentTimeMillis();
            String diagnosi = Controller.distillaDiagnosi(dizionarioParziale, etichetteOsservabilita, tempoIniziale);
            if (diagnosi != null) {
                View.stampaDiagnosi(diagnosi);
                tempoTotale = System.currentTimeMillis() - tempoIniziale;
                System.out.println("TEMPO DISTILLAZIONE DIAGNOSI: " + tempoTotale + "ms");
            } else {
                if (System.currentTimeMillis() - tempoIniziale > Parametri.getTempoEsecuzioneMax()) {

                    //TODO SCRIVERE CHE IL TEMPO NON E' BASTATO
                } else {

                    //SE DIAGNOSI == NULL  o non e' uno stato finale, oppure e' andato storto qualcosa;
                    View.messaggioErroreDiagnosi();
                }
            }

        } else {
            //TODO CAMO: non e' possibile eseguire una diagnosi
        }
    }

    private static void distillaDiagnosiDaDizionarioParzialeIncrementale(Rete rete) {

        ////DA TOGLIERE: Per il momento faccio acquisire 2 soli dizionari parziali
        boolean condizioneDiFineInserimentoDizionariParziali = true;
        int contatore = numeroDizionariParzialiDaInserire();

        while (condizioneDiFineInserimentoDizionariParziali) {
            View.stampaLegendaEspressioneRegolareOsservazioni(rete);
            String osservazione = InputDati.inserimentoEspressioneRegolare(Parametri.MESSAGGIO_INSERISCI_ESPRESSIONE_REGOLARE, rete.getEtichettaOsservabilita());
            long tempoIniziale = System.currentTimeMillis();
            SpazioComportamentale dizionarioParziale = Controller.creaDizionarioParziale(rete, osservazione, tempoIniziale);
            if (System.currentTimeMillis() - tempoIniziale > Parametri.getTempoEsecuzioneMax()) {
                condizioneDiFineInserimentoDizionariParziali = false;
                //TODO SCRIVERE CHE IL TEMPO NON E' BASTATO
            } else {
                rete.addDizionarioParziale(dizionarioParziale);
                //Il prefisso degli statiDecorati appartenenti allo stesso spazio incrementale, viene incrementato di una posizione alfabetica
                Parametri.incrementaPrefissoStatoDecorato();

                //DA TOGLIERE: Per il momento faccio acquisire 2 soli dizionari parziali
                contatore--;
                if (contatore == 0) {
                    condizioneDiFineInserimentoDizionariParziali = false;
                    Parametri.resettaPrefissoStatoDecorato();
                }

            }
        }
        if(rete.getDizionariParziali()!=null && rete.getDizionariParziali().size()>1){ 
        long tempoIniziale = System.currentTimeMillis();

        SpazioComportamentale dizionarioParzialeIncrementale = Controller.unisciDizionari(rete, rete.getDizionariParziali());
        if (dizionarioParzialeIncrementale != null) {
            long tempoTotale = System.currentTimeMillis() - tempoIniziale;
            System.out.println("TEMPO UNIONE DIZIONARI: " + tempoTotale + "ms");
            List<String> etichetteOsservabilita = acquisisciStringaEtichetteOsservabilita(rete);
            tempoIniziale = System.currentTimeMillis();
            String diagnosi = Controller.distillaDiagnosi(dizionarioParzialeIncrementale, etichetteOsservabilita, tempoIniziale);
            if (diagnosi != null) {
                System.out.println(diagnosi);
                tempoTotale = System.currentTimeMillis() - tempoIniziale;
                System.out.println("TEMPO DISTILLAZIONE DIAGNOSI: " + tempoTotale + "ms");
            } else {
                if (System.currentTimeMillis() - tempoIniziale > Parametri.getTempoEsecuzioneMax()) {
                    // TODO SCRIVERE CHE IL TEMPO NON E' BASTATO
                } else {
                    //SE DIAGNOSI == NULL  o non e' uno stato finale, oppure e' andato storto qualcosa;
                    View.messaggioErroreDiagnosi();
                }
            }
        } else {
            System.out.println("NON E' STATO POSSIBILE CREARE IL DIZIONARIO INCREMENTALE");
        }}
    }

    private static void costruzioneDiSpaziVincolati(Rete rete) {
        View.stampaLegendaEspressioneRegolareTransizioni(rete);

        List<Transizione> transizioni = new ArrayList<Transizione>();
        List<Automa> automi = rete.getAutomi();
        for (Automa automa : automi) {
            List<Stato> stati = automa.getStati();
            for (Stato stato : stati) {
                List<Transizione> transizioniParziali = stato.getTransizioni();
                for (Transizione transizione : transizioniParziali) {
                    transizioni.add(transizione);
                }
            }
        }
        String[] nomiTransizioni = new String[transizioni.size()];
        for (int i = 0; i < transizioni.size(); i++) {
            nomiTransizioni[i] = transizioni.get(i).getDescrizione();
        }
        String osservazione = InputDati.inserimentoEspressioneRegolare(Parametri.MESSAGGIO_INSERISCI_ESPRESSIONE_REGOLARE, nomiTransizioni);
        long tempoIniziale = System.currentTimeMillis();
        //SpazioComportamentale spaziVincolati = Controller.creaRiconoscitoreEspressione(rete, osservazione);
        rete.setDizionarioParzialeVincolato(null);
        SpazioComportamentale dizionarioParzialeVincolato = Controller.creaDizionarioParzialeVincolato(rete, osservazione, tempoIniziale);
         if (System.currentTimeMillis() - tempoIniziale > Parametri.getTempoEsecuzioneMax()) {
                    // TODO SCRIVERE CHE IL TEMPO NON E' BASTATO
                } else {
        if (dizionarioParzialeVincolato != null) {

            long tempoTotale = System.currentTimeMillis() - tempoIniziale;
            System.out.println("TEMPO COSTRUZIONE DIZIONARIO VINCOLATO: " + tempoTotale + "ms");
            rete.setDizionarioParzialeVincolato(dizionarioParzialeVincolato);
            List<String> etichetteOsservabilita = acquisisciStringaEtichetteOsservabilita(rete);
            tempoIniziale = System.currentTimeMillis();
            String diagnosi = Controller.distillaDiagnosi(dizionarioParzialeVincolato, etichetteOsservabilita, tempoIniziale);
            if (diagnosi != null) {
                View.stampaDiagnosi(diagnosi);
                tempoTotale = System.currentTimeMillis() - tempoIniziale;
                System.out.println("TEMPO DISTILLAZIONE DIAGNOSI: " + tempoTotale + "ms");
            } else {
                if (System.currentTimeMillis() - tempoIniziale > Parametri.getTempoEsecuzioneMax()) {
                    //TODO SCRIVERE CHE IL TEMPO NON E' BASTATO
                } else {
                    //SE DIAGNOSI == NULL  o non e' uno stato finale, oppure e' andato storto qualcosa;
                    View.messaggioErroreDiagnosi();
                }
            }
        } else {
            //TODO CAMO: "non è possibile eseguire una diagnosi
        }}
    }

    /**
     * Chiede all'utente quanti dizionari parziali vuole inserire
     *
     * @return il numero di dizionari parziali
     */
    private static int numeroDizionariParzialiDaInserire() {
        //TODO CAMO     QUESTO METODO DEVE CHIEDERE ALL'UTENTE QUANTI DIZIONARI PARZIALI DEVE INSERIRE

        return 2; // per i test ho impostato 2 di default
    }

    public static void cambiaRete(Rete rete) {
        if (InputDati.yesOrNo(Parametri.RICHIESTA_CAMBIO_RETE)) {
            if(InputDati.yesOrNo(Parametri.RICHIESTA_SALVATAGGIO_RETE)){
                salva(rete);
            }
            caricaNuovo();
        } 
    }

}
