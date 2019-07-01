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
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author alber
 */
public class ControllerUtente {

    public static File fileSalvataggio = new File(Parametri.PATH_FILE_INPUT + "/salvataggio.dat");

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
                break;

            case 2:
                rete = carica();
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

    // DA SISTEMARE
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
                System.out.println("Cambia rete");
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
                    // PARTE DA CANCELLARE APPENA FINISCE IL COMPITO 4
                    // HO FATTO COSì COSì NON RICHIAMO TUTTE LE VOLTE IL COMPITO 1 E POI IL 2
                    //
                    creaSpazioComportamentale(rete);
                    creaSpazioComportamentaleDecorato(rete);
                    distillaDiagnosiDaDizionarioParziale(rete);
                    break;

                case 5:
                    //TO-DO: DA TOGLIERE APPENA VIENE FIXATO IL METODO creaRiconoscitoreEspressione;
                    creaSpazioComportamentale(rete);
                    creaSpazioComportamentaleDecorato(rete);

                    distillaDiagnosiDaDizionarioParzialeIncrementale(rete);
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
        ServizioFile.salvaSingoloOggetto(fileSalvataggio, rete);
    }

    /**
     * crea lo spazioComportamentale della rete (e visualizza info)
     *
     * @param rete
     */
    private static void creaSpazioComportamentale(Rete rete) {
        Controller.creaSpazioComportamentale(rete);
    }

    /**
     * crea lo spazioComportamentaleDecorato della rete (e visualizza info)
     *
     * @param rete
     */
    private static SpazioComportamentale creaSpazioComportamentaleDecorato(Rete rete) {
        SpazioComportamentale spazioComportamentaleDecorato = null;
        if (rete.getSpazioC() != null) {
            spazioComportamentaleDecorato = Controller.creaSpazioComportamentaleDecorato(rete);
        } else {
            //TO-DO: messaggio di errore
        }
        return spazioComportamentaleDecorato;
    }

    private static SpazioComportamentale creaDizionario(Rete rete, SpazioComportamentale spazioComportamentaleDecorato) {
        SpazioComportamentale dizionario = Controller.creaDizionario(rete, spazioComportamentaleDecorato);
        rete.setDizionario(dizionario);
        return dizionario;
    }

    private static void distillaDiagnosi(Rete rete) {
        SpazioComportamentale dizionario;
        if (rete.getDizionario() == null) {
            creaSpazioComportamentale(rete);
            SpazioComportamentale spazioComportamentaleDecorato = creaSpazioComportamentaleDecorato(rete);
            dizionario = creaDizionario(rete, spazioComportamentaleDecorato);
        } else {
            dizionario = rete.getDizionario();
        }
        List<String> etichetteOsservabilita = acquisisciStringaEtichetteOsservabilita(rete);
        String diagnosi = Controller.distillaDiagnosi(dizionario, etichetteOsservabilita);
        if (diagnosi != null) {
            System.out.println(diagnosi);
        } else {
            //TO-DO: messaggio errore: SE DIAGNOSI == NULL  o non e' uno stato finale, oppure e' andato storto qualcosa;
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
        View.stampaLegendaEspressioneRegolare(rete);
        String osservazione = InputDati.inserimentoEspressioneRegolare(Parametri.MESSAGGIO_INSERISCI_ESPRESSIONE_REGOLARE, rete.getEtichettaOsservabilita());
        SpazioComportamentale dizionarioParziale = Controller.creaDizionarioParziale(rete, osservazione);
        rete.setDizionarioParziale(dizionarioParziale);
        List<String> etichetteOsservabilita = acquisisciStringaEtichetteOsservabilita(rete);
        String diagnosi = Controller.distillaDiagnosi(dizionarioParziale, etichetteOsservabilita);
        if (diagnosi != null) {
            System.out.println(diagnosi);
        } else {
            //TO-DO: messaggio errore: SE DIAGNOSI == NULL  o non e' uno stato finale, oppure e' andato storto qualcosa;
        }

    }

    private static void distillaDiagnosiDaDizionarioParzialeIncrementale(Rete rete) {
        View.stampaLegendaEspressioneRegolare(rete);
        while (true) {
            String osservazione = InputDati.inserimentoEspressioneRegolare(Parametri.MESSAGGIO_INSERISCI_ESPRESSIONE_REGOLARE, rete.getEtichettaOsservabilita());
            SpazioComportamentale dizionarioParziale = Controller.creaDizionarioParziale(rete, osservazione);
            rete.addDizionarioParziale(dizionarioParziale);
        }
        SpazioComportamentale dizionarioParzialeIncrementale = Controller.unisciDizionari(rete.getDizionariParziali());
        List<String> etichetteOsservabilita = acquisisciStringaEtichetteOsservabilita(rete);
        String diagnosi = Controller.distillaDiagnosi(dizionarioParzialeIncrementale, etichetteOsservabilita);
        if (diagnosi != null) {
            System.out.println(diagnosi);
        } else {
            //TO-DO: messaggio errore: SE DIAGNOSI == NULL  o non e' uno stato finale, oppure e' andato storto qualcosa;
        }
    }

}
