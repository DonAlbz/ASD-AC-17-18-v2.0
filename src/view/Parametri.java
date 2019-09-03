/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import Model.StatoReteRidenominato;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Alb
 */
public class Parametri {
   
    public final static String EVENTO_NULLO = "\u0190";
    public final static String INSIEME_VUOTO = "\u00D8";
    public static char STATO_DECORATO_PREFISSO = 'a';
    public final static String NOME_ROOT_DIZIONARIO_INCREMENTALE ="ROOT";
    public final static String SEPARATORE = "-----------------------";
    public final static String NULL = "null";
    public final static String INIZIALIZZAZIONE_RETE = "********* INIZIALIZZAZIONE RETE *********";
    public final static String A_CAPO = System.getProperty("line.separator");
    public final static String SPAZIO_COMPORTAMENTALE_ETICHETTA = "********* SPAZIO COMPORTAMENTALE *********";
    public final static String AUTOMA_RICONOSCITORE_ETICHETTA = "********* AUTOMA RICONOSCITORE *********";
    public final static String SPAZIO_COMPORTAMENTALE_PARZIALE = "********* SPAZIO COMPORTAMENTALE PARZIALE *********";
    public final static String TRAIETTORIE_ETICHETTA = "********* TRAIETTORIE *********";
    public final static String CAMMINI_ETICHETTA = "********* CAMMINI *********";
    public final static String DETERMINIZZAZIONE_ETICHETTA = "********* DETERMINIZZAZIONE *********";
    public final static String DETERMINIZZAZIONE_DIZIONARI = "********* DETERMINIZZAZIONE (FUSIONE DI DUE DIZIONARI) *********";
    public final static String CAMMINI_NUMERO_ETICHETTA = "numero cammino: ";
    public final static String TRAIETTORIA_NUMERO_ETICHETTA = "numero traiettoria: " ;
    public final static String TAB = "\t";
    public final static String PARENTESI_TONDA_A = "(";
    public final static String PARENTESI_TONDA_C = ")";
    public final static String PARENTESI_QUADRA_A = "[";
    public final static String PARENTESI_QUADRA_C = "]";
    public final static String PARENTESI_GRAFFA_A = "{";
    public final static String PARENTESI_GRAFFA_C = "}";
    public final static String CARATTERE_MINORE = "<";
    public final static String CARATTERE_MAGGIORE = ">";
    public final static String SPAZIO = " ";
    public final static String VIRGOLA = ",";
    public final static String FRECCIA = "-->";
    public final static String ASTERISCO = "*";
    public final static String PIU = "+";
    public final static String APICE = "^";
    public final static String TITOLO_MENU_INIZIALE = "AUTOMA A STATI FINITI";
    public final static String VOCI_MENU_INIZIALE[] = {"Importa una nuova rete",
                                                        "Carica una nuova rete"};
    public final static String MESSAGGIO_FINE_PROGRAMMA = "Sei sicuro di voler uscire? ";
    public final static String TITOLO_MENU_RETE = "AUTOMA";
    public final static String VOCI_MENU_RETE[] = {"Stampa informazioni della rete",
                                                    "Esegui compito",
                                                    "Salva rete",
                                                    "Cambia rete",
                                                    "Modifica tempo massimo di esecuzione"};
    public final static String PATH_FILE_INPUT = "./src/FileInput";
    public final static String MESSAGGIO_PATH_FILE_IMPORT = "Il file importato risiede nel seguente path:";
    public final static String OSSERVABILITA = "osservabilita";
    public final static String RILEVANZA = "rilevanza";
    public final static String FORMATO_TXT = ".txt";
    public final static String FORMATO_DAT = ".dat";
    public final static String IMPORT_SELEZIONE = "SELEZIONA IL FILE INPUT USATO COME SORGENTE";
    public final static String CARICAMENTO_SELEZIONE = "SELEZIONE IL FILE DA CARICARE";
    public final static String TITOLO_MENU_INFORMAZIONI_RETE = "INFORMAZIONI DELLA RETE";
    public final static String VOCI_MENU_INFORMAZIONI_RETE [] = {"Stampa nome della rete",
                                                                "Stampa automi che compongono la rete",
                                                                "Stampa stati che compongono la rete",
                                                                "Stampa transizioni che compongono la rete",
                                                                "Stampa globale delle informazioni della rete"};
    public final static String TITOLO_MENU_COMPITI = "MENU COMPITI";
    public final static String VOCI_MENU_COMPITI[] = {"Crea spazio comportamentale",
                                                        "Crea spazio comportamentale decorato",
                                                        "Distilla diagnosi",
                                                        "Distillazione delle diagnosi da dizionario parziale",
                                                        "Creazione incrementale del dizionario",
                                                        "Costruzione di spazi vincolati"};
    public final static String MESSAGGIO_INSERIMENTO_STRINGA_ETICHETTE = "Inserisci una stringa come nell'esempio sottostante per effetturare una ricerca nel dizionario:";
    public final static String ESEMPIO_MESSAGGIO_INSERIMENTO_STRINGA_ETICHETTE = "Esempio di ingresso: <o3,o2,o3,o2>";
    public final static String MESSAGGIO_INSERISCI = "Inserisci la stringa desiderata: ";
    public final static String LEGENDA_ESPRESSIONE_REGOLARE_OSSERVAZIONI = "LEGENDA INSERIMENTO ESPRESSIONE REGOLARE (OSSERVAZIONI)";
    public final static String LEGENDA_ESPRESSIONE_REGOLARE_TRANSIZIONI = "LEGENDA INSERIMENTO ESPRESSIONE REGOLARE (TRANSIZIONI)";
    public final static String VOCI_LEGENDA_ESPRESSIONE_REGOLARE[] = {"^ = apice per l'elevamento a potenza",
                                                                        "* = da mettere in seguito a ^ per definire opzionalità multipla",
                                                                        "+ = da mettere in seguito a ^ per definire multiplicità",
                                                                        "( ) = sono da considerarsi solo le parentesi tonde per definire l'espressione"};
    public final static String ESPRESSIONE_REGOLARE_ESEMPIO_OSSERVAZIONI = "Esempio di espressione regolare: (o2 o3)^+ o3";
    public final static String ESPRESSIONE_REGOLARE_ESEMPIO_TRANSIZIONI = "Esempio di espressione regolare: t3a t2a t3b";
    public final static String MESSAGGIO_INSERISCI_ESPRESSIONE_REGOLARE = "Basandosi come nell'esempio soprastante inserisci l'espressione regolare desiderata > ";
    public final static String FINALE = "Finale";
    public final static String MESSAGGIO_SCP_NULLO = "Non e' stato identificato uno spazio comportamentale parziale in base all'automa riconoscitore. Viene ritornato null";
    public final static String TRAIETTORIE_NON_TROVATE = "L'osservazione inserita non produce traiettorie";
    public final static String MESSAGGIO_ERRORE_DIAGNOSI = "La diagnosi ricavata è nulla";
    public final static String RICHIESTA_CAMBIO_RETE = "Si è sicuri di voler cambiare la rete?";
    public final static String RICHIESTA_SALVATAGGIO_RETE = "Si vuole salvare la rete attuale?";

    public final static String NUMERO_VERTICI_RIMOSSI = "Il numero di stati potati e': ";

    public final static String MESSAGGIO_CAMBIO_LIMITE_TEMPORALE_1 = "(Per una corretta esecuzione il limite minimo temporale è di 1000ms)";
    public final static String MESSAGGIO_CAMBIO_LIMITE_TEMPORALE_2 = "Si inserisca il nuovo tempo limite di esecuzione che si vuole impostare (in ms):";
    public final static String MESSAGGIO_CAMBIO_LIMITE_EFFETTUATO = "Il cambio del tempo di esecuzione minimo è stato completato con successo";
    public final static String MESSAGGIO_ERRORE_TEMPO_INSUFFICIENTE = "Il tempo minimo impostato per il calcolo non è sufficiente per completare il compito";
    public final static String MESSAGGIO_ERRORE_DIAGNOSI_IMPOSSIBILE = "Non è possibile effettuare una diagnosi";
    public final static String MESSAGGIO_RICHIESTA_NUMERO_DIZIONARI = "Quanti dizionari parziali si vogliono inserire? (minimo 2): ";
    public final static String MESSAGGIO_ERRORE_CREAZIONE_DIZIONARIO_INCREMENTALE = "Non è stato possibile creare il dizionario incrementale";
    public final static String MESSAGGIO_ERRORE_CAMMINI_PARZIALI = "È scaduto il tempo di computazione: i cammini calcolati sono parziali e lo spazio comportamentale è incompleto";
    public final static String MESSAGGIO_ERRORE_SPAZIO_COMPORTAMENTALE_CREATO_NO_NUMERATO_NO_RIDENOMINATO = "Lo spazio comportamentale è stato creato ma non è stato ne numerato ne ridenominato";
    public final static String MESSAGGIO_ERRORE_SPAZIO_COMPORTAMENTALE_CREATO_NUMERATO_NO_RIDENOMINATO = "Lo spazio comportamentale è stato creato e numerato ma non ridenominato";
    public final static String MESSAGGIO_ERRORE_SPAZIO_COMPORTAMENTALE_DECORATO = "Non è stato possibile procedere con la costruzione dello spazio comportamentale decorato";
    public final static String MESSAGGIO_ERRORE_SPAZIO_COMPORTAMENTALE_DECORATO_CAMMINI_PARZIALI = "Non è stato possibile completare la costruzione completa dello spazio comportamentale decorato";
    public final static String MESSAGGIO_ERRORE_SPAZIO_COMPORTAMENTALE_DECORATO_VERTICI_PARZIALI = "Non è stato possibile inserire tutti i vertici dello spazio comportamentale decorato";
    public final static String MESSAGGIO_ERRORE_SPAZIO_COMPORTAMENTALE_VERTICI_PARZIALI = "Non è stato possibile inserire tutti i vertici dello spazio comportamentale";
    public final static String MESSAGGIO_ERRORE_NO_STATO_DA_OSSERVAZIONE = "Non e' stato possibile raggiungere alcuno stato con l'osservazione inserita";
    public final static String MESSAGGIO_ERRORE_SPAZIO_COMPORTAMENTALE_CAMMINI_PARZIALI = "Non è stato possibile completare la costruzione completa dello spazio comportamentale";
    public final static String MESSAGGIO_ERRORE_INSERIMENTO_SPAZIO_COMPORTAMENTALE = "Non è stato possibile inserire tutto lo spazio comportamentale";
    public final static String MESSAGGIO_ERRORE_NO_SPAZIO_MANCANZA_TEMPO = "La creazione dello spazio comportamentale è stata interrotta per mancanza di tempo";

        
    
    public final static int NUMERO_MINIMO_INSERIMENTO_DIZIONARI = 2;
    public final static int NUMERO_RIGA_NOME_RETE = 0;
    public final static int NUMERO_RIGA_NOMI_AUTOMI = 3;
    public final static int NUMERO_RIGA_NOMI_LINK = 6;
    public final static int NUMERO_RIGA_NOMI_EVENTI = 9;
    public final static int NUMERO_RIGA_NOMI_OSSERVABILITA = 12;
    public final static int NUMERO_RIGA_NOMI_RILEVANZA = 15;

    public static long tempoEsecuzioneMax=Long.MAX_VALUE;// >=1000
    public static boolean metodoAlternativiCreazioneSpaziDecorati=true;
    public final static long LIMITE_MINIMO_TEMPORALE=1000; // in millisecondi
    

    
    //incrementa di una posizione alfabetica, la lettera prefisso degli stati rete decorati
    public static void incrementaPrefissoStatoDecorato(){
        STATO_DECORATO_PREFISSO = (char) (((int)STATO_DECORATO_PREFISSO )+ 1) ;
    }

    public static void resettaPrefissoStatoDecorato() {
        STATO_DECORATO_PREFISSO = 'a';
    }

    public static char getSTATO_DECORATO_PREFISSO() {
        return STATO_DECORATO_PREFISSO;
    }

    public static void setSTATO_DECORATO_PREFISSO(char STATO_DECORATO_PREFISSO) {
        Parametri.STATO_DECORATO_PREFISSO = STATO_DECORATO_PREFISSO;
    }

    public static long getTempoEsecuzioneMax() {
        return tempoEsecuzioneMax;
    }

    public static void setTempoEsecuzioneMax(long tempoEsecuzioneMax) {
        Parametri.tempoEsecuzioneMax = tempoEsecuzioneMax;
    }

    public static boolean getMetodoAlternativiCreazioneSpaziDecorati() {
        return metodoAlternativiCreazioneSpaziDecorati;
    }

    public static void setMetodoAlternativoCreazioneSpaziDecorati(boolean metodoAlternativiCreazioneSpaziDecorati) {
        Parametri.metodoAlternativiCreazioneSpaziDecorati = metodoAlternativiCreazioneSpaziDecorati;
    }
    
    
    
}
