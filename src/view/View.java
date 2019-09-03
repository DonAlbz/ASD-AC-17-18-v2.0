/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import Model.*;
import java.util.*;

/**
 *
 * @author alber
 */
public class View {

    public static void stampaCammini(List<Cammino> daStampare) {

        System.out.println(Parametri.CAMMINI_ETICHETTA);
        System.out.println();
        for (int i = 0; i < daStampare.size(); i++) {
            int numeroCammino = i + 1;
            System.out.println(Parametri.CAMMINI_NUMERO_ETICHETTA + numeroCammino);
            System.out.println();
            System.out.println(daStampare.get(i).toString());
            System.out.println();
        }
    }

    public static void stampaTraiettorie(List<Cammino> traiettorie) {
        System.out.println(Parametri.TRAIETTORIE_ETICHETTA);
        System.out.println();
        for (int i = 0; i < traiettorie.size(); i++) {
            int numeroCammino = i + 1;
            System.out.println(Parametri.TRAIETTORIA_NUMERO_ETICHETTA + numeroCammino);
            System.out.println();
            System.out.println(traiettorie.get(i).toString());
            System.out.println();
            System.out.println();
        }
    }

    public static void stampaFileTxt(List<String> fileTxt) {
        for (String str : fileTxt) {
            System.out.println(str);
        }
    }

    public static String stampaNomeRete(Rete rete) {
        String nomeRete = rete.getDescrizione();
        System.out.println("Il nome della rete è: " + nomeRete);
        System.out.println("");
        return nomeRete;
    }

    public static void stampaAutomi(Rete rete) {
        List<Automa> automi = rete.getAutomi();
        int i = 1;
        System.out.println("La rete è composta da " + automi.size() + " automi/a:");
        for (Automa aut : automi) {
            System.out.println("Automa " + i + " nella rete è: " + aut.getDescrizione());
            i++;
        }
        System.out.println("");
    }

    public static void stampaStati(Rete rete) {
        List<Automa> automi = rete.getAutomi();
        for (Automa aut : automi) {
            List<Stato> stati = aut.getStati();
            System.out.println("L'automa " + aut.getDescrizione() + " è composto dei seguenti stati:");
            int i = 1;
            for (Stato st : stati) {
                System.out.println("Stato " + i + ": " + st.getDescrizione());
                i++;
            }
            System.out.println("");
        }
    }

    public static void stampaTransizioni(Rete rete) {
        List<Automa> automi = rete.getAutomi();
        for (Automa aut : automi) {
            List<Stato> stati = aut.getStati();
            for (Stato sta : stati) {
                System.out.print("Lo stato " + sta.getDescrizione() + " facente parte dell'automa " + aut.getDescrizione());
                System.out.println(" possiede le seguenti transizioni:");
                List<Transizione> transizioni = sta.getTransizioni();
                for (Transizione tra : transizioni) {
                    System.out.println("La transizione " + tra.getDescrizione() + " ha come destinazione: " + tra.getStatoDestinazione().getDescrizione());
                }
            }
            System.out.println("");
        }
    }

    public static void stampaGlobaleRete(Rete rete) {
        stampaNomeRete(rete);
        stampaAutomi(rete);
        stampaStati(rete);
        stampaTransizioni(rete);
    }

    public static void stampaEtichettaOsservabilita(Rete rete) {
        String[] etichette = rete.getEtichettaOsservabilita();
        System.out.println("Le etichette di osservabilità sono:");
        for (String str : etichette) {
            System.out.println(str);
        }
    }

    public static void stampaEtichettaRilevanza(Rete rete) {
        String[] etichette = rete.getEtichettaRilevanza();
        System.out.println("Le etichette di rilevanza sono:");
        for (String str : etichette) {
            System.out.println(str);
        }
    }

    public static void messaggioImportCorretto(Rete rete) {
        System.out.println("");
        System.out.println("L'import della rete " + rete.getDescrizione() + " è avvenuto in maniera corretta");
        System.out.println("");
    }

    public static void messaggioSalvataggioCorretto(Rete rete) {
        System.out.println("");
        System.out.println("Il salvataggio della rete è avvenuto in maniera corretta");
        System.out.println("");
    }

    public static void messaggioCaricamentoCorretto(Rete rete) {
        System.out.println("");
        System.out.println("Il caricamento della rete " + rete.getDescrizione() + " è avvenuto in maniera corretta");
        System.out.println("");
    }

    public static void messaggioAquisizioneEtichettaOsservabilita() {
        System.out.println(Parametri.MESSAGGIO_INSERIMENTO_STRINGA_ETICHETTE);
        System.out.println(Parametri.ESEMPIO_MESSAGGIO_INSERIMENTO_STRINGA_ETICHETTE);
    }

    public static void stampaLegendaEspressioneRegolareOsservazioni(Rete rete) {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.SEPARATORE);
        System.out.println(Parametri.LEGENDA_ESPRESSIONE_REGOLARE_OSSERVAZIONI);
        System.out.print(Parametri.A_CAPO);
        System.out.print("L'alfabeto è composto da queste etichette: [ ");
        String[] etichetteOsservabilita = rete.getEtichettaOsservabilita();
        for (String etichet : etichetteOsservabilita) {
            System.out.print(etichet + " ");
        }
        System.out.print("]" + Parametri.A_CAPO);
        for (String legenda : Parametri.VOCI_LEGENDA_ESPRESSIONE_REGOLARE) {
            System.out.println(legenda);
        }
        System.out.println(Parametri.ESPRESSIONE_REGOLARE_ESEMPIO_OSSERVAZIONI);
        System.out.println(Parametri.SEPARATORE);
        System.out.print(Parametri.A_CAPO);
    }

    public static void traiettorieNonTrovate() {
        System.out.println(Parametri.TRAIETTORIE_NON_TROVATE);
    }

    public static void stampaDiagnosi(String diagnosi) {
        System.out.println(diagnosi);
    }

    public static void stampaDeterminizzazione(List<StatoDFA> vertici) {
        System.out.println(Parametri.DETERMINIZZAZIONE_ETICHETTA);
        // modifico il nome del vertice lasciando soltanto il numero e togliendo la "a" e aggiungendo una unità al nome
        List<String> daStampare = new ArrayList<>();
        for (StatoDFA s : vertici) {
            String nomeStato = s.getNome();
            String[] listaNomi = nomeStato.split(" ");
            if (listaNomi.length == 1) {
                String nomeDaStampare = listaNomi[0];
                nomeDaStampare = nomeDaStampare.replace("a", "");
                nomeDaStampare = nomeDaStampare.replace("b", "");
//                int num = Integer.parseInt(nomeDaStampare);
//                num++;
//                nomeDaStampare = String.valueOf(num);
                daStampare.add(nomeDaStampare);
            } else {
                String totale = "";
                for (int i = 0; i < listaNomi.length; i++) {
                    String nomeSingolo = listaNomi[i];
                    nomeSingolo = nomeSingolo.replace("a", "");
                    nomeSingolo = nomeSingolo.replace("b", "");
                    nomeSingolo = nomeSingolo.replace(Parametri.TAB, "");
//                    int num = Integer.parseInt(nomeSingolo);
//                    num++;
//                    nomeSingolo = String.valueOf(num);
                    totale = totale.concat(nomeSingolo + " ");
                }
                daStampare.add(totale);
            }
        }

        // stampa
        int count = 0;
        String stampa = "";
        String ultimoStato = "";
        for (String s : daStampare) {
            if (count == 0) {
                ultimoStato = s;
                count++;
            } else {
                stampa = stampa.concat(ultimoStato);
                stampa = stampa.concat(Parametri.TAB);
                stampa = stampa.concat(Parametri.TAB);
                if (ultimoStato.length() < 10) {
                    stampa = stampa.concat(Parametri.TAB);
                }
                if (ultimoStato.length() < 15) {
                    stampa = stampa.concat(Parametri.TAB);
                }
//                stampa = stampa.concat(Parametri.TAB);
                stampa = stampa.concat(Parametri.FRECCIA);
                stampa = stampa.concat(" " + vertici.get(count).getOsservabilita() + " ");
                stampa = stampa.concat(Parametri.FRECCIA);
                stampa = stampa.concat(Parametri.TAB);
                stampa = stampa.concat(Parametri.TAB);
//                stampa = stampa.concat(Parametri.TAB);
                stampa = stampa.concat(s);
                stampa = stampa.concat(Parametri.A_CAPO);
                count++;
                ultimoStato = s;
            }
        }
        System.out.println(stampa);
    }

    public static void stampaDeterminizzazioneFusioneDizionari(List<StatoDFA> vertici) {
        System.out.println(Parametri.DETERMINIZZAZIONE_DIZIONARI);
        List<String> daStampare = new ArrayList<>();
        int count = 0;
        String stampa = "";
        String ultimoStato = "";
        for (StatoDFA s : vertici) {
            String nomeStato = s.getNome();
            if (count == 0) {
                ultimoStato = nomeStato;
                count++;
            } else {
                stampa = stampa.concat(ultimoStato);
                stampa = stampa.concat(Parametri.TAB);
                if (ultimoStato.length() < 10) {
                    stampa = stampa.concat(Parametri.TAB);
                }
                stampa = stampa.concat(Parametri.TAB);
                stampa = stampa.concat(Parametri.FRECCIA);
                stampa = stampa.concat(" " + vertici.get(count).getOsservabilita() + " ");
                stampa = stampa.concat(Parametri.FRECCIA);
                stampa = stampa.concat(Parametri.TAB);
                stampa = stampa.concat(Parametri.TAB);
//                stampa = stampa.concat(Parametri.TAB);
                stampa = stampa.concat(s.getNome());
                stampa = stampa.concat(Parametri.A_CAPO);
                count++;
                ultimoStato = nomeStato;
            }
        }
        System.out.println(stampa);
    }

    public static void stampaLegendaEspressioneRegolareTransizioni(Rete rete) {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.SEPARATORE);
        System.out.println(Parametri.LEGENDA_ESPRESSIONE_REGOLARE_TRANSIZIONI);
        System.out.print(Parametri.A_CAPO);
        System.out.print("L'alfabeto è composto da queste transizioni: [ ");
        List<Automa> automi = rete.getAutomi();
        for (Automa automa : automi) {
            List<Stato> stati = automa.getStati();
            for (Stato stato : stati) {
                List<Transizione> transizioni = stato.getTransizioni();
                for (Transizione transizione : transizioni) {
                    System.out.print(transizione.getDescrizione() + " ");
                }
            }
        }
        System.out.print("]" + Parametri.A_CAPO);
        for (String legenda : Parametri.VOCI_LEGENDA_ESPRESSIONE_REGOLARE) {
            System.out.println(legenda);
        }
        System.out.println(Parametri.ESPRESSIONE_REGOLARE_ESEMPIO_TRANSIZIONI);
        System.out.println(Parametri.SEPARATORE);
        System.out.print(Parametri.A_CAPO);
    }

    public static void messaggioErroreDiagnosiImpossibile() {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.MESSAGGIO_ERRORE_DIAGNOSI_IMPOSSIBILE);
        System.out.print(Parametri.A_CAPO);
    }

    public static void stampaLimiteTemporale() {
        System.out.print(Parametri.A_CAPO);
        System.out.println("Il tempo di esecuzione massimo impostato nel progetto è di: " + Parametri.tempoEsecuzioneMax + "ms");
    }

    public static void messaggioErroreTempoInsufficiente() {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.MESSAGGIO_ERRORE_TEMPO_INSUFFICIENTE);
        System.out.print(Parametri.A_CAPO);
    }

    public static void messaggioImpossibileDiagnosi() {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.MESSAGGIO_ERRORE_DIAGNOSI_IMPOSSIBILE);
        System.out.print(Parametri.A_CAPO);
    }

    public static void messaggioImpossibileCreareDizionarioIncrementale() {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.MESSAGGIO_ERRORE_CREAZIONE_DIZIONARIO_INCREMENTALE);
        System.out.print(Parametri.A_CAPO);
    }

    public static void messaggioErroreCamminiParziali() {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.MESSAGGIO_ERRORE_CAMMINI_PARZIALI);
        System.out.print(Parametri.A_CAPO);
    }

    public static void messaggioErroreCreazioneSpazioCompNoNumeratoNoRidenominato() {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.MESSAGGIO_ERRORE_SPAZIO_COMPORTAMENTALE_CREATO_NO_NUMERATO_NO_RIDENOMINATO);
        System.out.print(Parametri.A_CAPO);
    }

    public static void messaggioErroreCreazioneSpazioCompNumeratoNoRidenominato() {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.MESSAGGIO_ERRORE_SPAZIO_COMPORTAMENTALE_CREATO_NUMERATO_NO_RIDENOMINATO);
        System.out.print(Parametri.A_CAPO);
    }

    public static void messaggioErroreSpazioComportamentaleDecorato() {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.MESSAGGIO_ERRORE_SPAZIO_COMPORTAMENTALE_DECORATO);
        System.out.print(Parametri.A_CAPO);
    }

    public static void messaggioErroreSpazioComportamentaleDecoratoCamminiParziali() {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.MESSAGGIO_ERRORE_SPAZIO_COMPORTAMENTALE_DECORATO_CAMMINI_PARZIALI);
        System.out.print(Parametri.A_CAPO);
    }

    public static void messaggioErroreSpazioComportamentaleCamminiParziali() {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.MESSAGGIO_ERRORE_SPAZIO_COMPORTAMENTALE_CAMMINI_PARZIALI);
        System.out.print(Parametri.A_CAPO);
    }

    public static void messaggioErroreSpazioComportamentaleDecoratoVerticiParziali() {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.MESSAGGIO_ERRORE_SPAZIO_COMPORTAMENTALE_DECORATO_VERTICI_PARZIALI);
        System.out.print(Parametri.A_CAPO);
    }

    public static void messaggioErroreSpazioComportamentaleVerticiParziali() {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.MESSAGGIO_ERRORE_SPAZIO_COMPORTAMENTALE_VERTICI_PARZIALI);
        System.out.print(Parametri.A_CAPO);
    }

    public static void messaggioErroreNoSpazioDaOsservazione() {
        System.out.println(Parametri.MESSAGGIO_ERRORE_NO_STATO_DA_OSSERVAZIONE);
    }

    public static void messaggioErroreInserimentoSpazioComportamentale() {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.MESSAGGIO_ERRORE_INSERIMENTO_SPAZIO_COMPORTAMENTALE);
        System.out.print(Parametri.A_CAPO);
    }

    public static void messaggioErroreCreazioneSpazioMancanzaTempo() {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.MESSAGGIO_ERRORE_INSERIMENTO_SPAZIO_COMPORTAMENTALE);
        System.out.print(Parametri.A_CAPO);
    }

    public static void numeroVerticiRimossi(int numeroVerticiRimossi) {
        System.out.print(Parametri.NUMERO_VERTICI_RIMOSSI);
        System.out.print(numeroVerticiRimossi);
        System.out.println();
        System.out.println();
    }

    public static void messaggioConfermaSpazioComportamentaleDecoratoCreato() {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.MESSAGGIO_SPAZIO_COMPORTAMENTALE_DECORATO_CREATO);
        System.out.print(Parametri.A_CAPO);
    }

    public static void messaggioMetodoAlternativoAttivo() {
        System.out.print(Parametri.A_CAPO);
        System.out.println(Parametri.MESSAGGIO_METODO_ALTERNATIVO_ATTIVO);
        System.out.print(Parametri.A_CAPO);
    }

    public static void stampaSpazioComportamentale(SpazioComportamentale spazio) {

        StringBuilder s = new StringBuilder();
        s.append(Parametri.SPAZIO_COMPORTAMENTALE_ETICHETTA);
        s.append(Parametri.A_CAPO);
        for (StatoInterface statoPartenza : spazio.getVertici()) {
            s.append(Parametri.PARENTESI_QUADRA_A);
            s.append(statoPartenza.getNome());
            s.append(Parametri.PARENTESI_QUADRA_C);
            s.append(Parametri.TAB);
            s.append(((StatoReteAbstract) statoPartenza).getDescrizione());

            // aggiunta della decorazione
//            StatoReteAbstract statoReteAbstract = (StatoReteAbstract) statoPartenza;
//            List<String> decorazioni = statoReteAbstract.getDecorazione();
//            for(String d : decorazioni){
//                s.append(d);
//            }
//            s.append(statoReteAbstract.getDecorazione());
            s.append(Parametri.TAB);
            s.append(Parametri.FRECCIA);
            s.append(Parametri.TAB);
            int i = 0;
            for (StatoInterface statoArrivo : spazio.getVerticiAdiacenti(statoPartenza)) {
                if (i > 0) {
                    s.append(Parametri.VIRGOLA);
                    s.append(Parametri.SPAZIO);
                }
                s.append(Parametri.PARENTESI_QUADRA_A);
                s.append(((StatoReteAbstract) statoArrivo).getTransizionePrecedente().getDescrizione());
                s.append(Parametri.SPAZIO);
                s.append(Parametri.PARENTESI_TONDA_A);
//                if (((StatoReteAbstract) statoArrivo).getOsservabilita() != null) {
//                    s.append(((StatoReteAbstract) statoArrivo).getOsservabilita());
//                } else {
//                    s.append(Parametri.EVENTO_NULLO);
//                }
//                    s.append(Parametri.SPAZIO);
                if (statoArrivo.getNome() != null) {
                    s.append(statoArrivo.getNome());
                } else {
                    s.append(((StatoRete) statoArrivo).getNumero());
                }
//                s.append(((StatoReteAbstract) statoArrivo).toStringShort());//                
                s.append(Parametri.PARENTESI_TONDA_C);
                s.append(Parametri.PARENTESI_QUADRA_C);
                i++;
            }
            s.append(Parametri.A_CAPO);

        }
        System.out.println(s.toString());
    }

    public static void stampaSpazioComportamentaleDecorato(SpazioComportamentale spazio) {

        StringBuilder s = new StringBuilder();
        s.append(Parametri.SPAZIO_COMPORTAMENTALE_ETICHETTA);
        s.append(Parametri.A_CAPO);
        for (StatoInterface statoPartenza : spazio.getVertici()) {
            s.append(Parametri.PARENTESI_QUADRA_A);
            s.append(statoPartenza.getNome());
            s.append(Parametri.PARENTESI_QUADRA_C);
            s.append(Parametri.TAB);
            s.append(((StatoReteAbstract) statoPartenza).getDescrizione());

            // aggiunta della decorazione
//            StatoReteAbstract statoReteAbstract = (StatoReteAbstract) statoPartenza;
//            List<String> decorazioni = statoReteAbstract.getDecorazione();
//            for(String d : decorazioni){
//                s.append(d);
//            }
//            s.append(statoReteAbstract.getDecorazione());
            s.append(Parametri.TAB);
            s.append(Parametri.FRECCIA);
            s.append(Parametri.TAB);
            int i = 0;
            for (StatoInterface statoArrivo : spazio.getVerticiAdiacenti(statoPartenza)) {
                if (i > 0) {
                    s.append(Parametri.VIRGOLA);
                    s.append(Parametri.SPAZIO);
                }
                s.append(Parametri.PARENTESI_QUADRA_A);
                s.append(((StatoReteAbstract) statoArrivo).getTransizionePrecedente().getDescrizione());
                s.append(Parametri.SPAZIO);
                s.append(Parametri.PARENTESI_TONDA_A);
//                if (((StatoReteAbstract) statoArrivo).getOsservabilita() != null) {
//                    s.append(((StatoReteAbstract) statoArrivo).getOsservabilita());
//                } else {
//                    s.append(Parametri.EVENTO_NULLO);
//                }
//                    s.append(Parametri.SPAZIO);
                if (statoArrivo.getNome() != null) {
                    s.append(statoArrivo.getNome());
                } else {
                    s.append(((StatoRete) statoArrivo).getNumero());
                }
//                s.append(((StatoReteAbstract) statoArrivo).toStringShort());//                
                s.append(Parametri.PARENTESI_TONDA_C);
                s.append(Parametri.PARENTESI_GRAFFA_A);
                if (((StatoReteRidenominato) statoArrivo).getDecorazione() == null) {
                    s.append(Parametri.INSIEME_VUOTO);
                } else {
                    int j = 0;
                    for (String decorazione : ((StatoReteRidenominato) statoArrivo).getDecorazione()) {
                        if (j > 0) {
                            s.append(Parametri.VIRGOLA);
                            s.append(Parametri.SPAZIO);
                        }
                        s.append(decorazione);
                        j++;
                    }
                }
                s.append(Parametri.PARENTESI_GRAFFA_C);
                s.append(Parametri.PARENTESI_QUADRA_C);
                i++;
            }
            s.append(Parametri.A_CAPO);

        }
        System.out.println(s.toString());
    }

}
