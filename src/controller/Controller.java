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
import java.util.stream.Collectors;
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
//        View.stampaCammini(cammini);
//        List<Cammino> traiettorie = potatura(cammini);
//        traiettorie = ridenominazione(traiettorie, statiSpazioC);
//        View.stampaTraiettorie(traiettorie);
//        SpazioComportamentale spazioC = new SpazioComportamentale();
//        spazioC = inserisciVerticiSpazioComportamentale(spazioC, traiettorie, statiSpazioC);
//        spazioC = inserisciLatiSpazioComportamentale(spazioC, traiettorie);
        SpazioComportamentale spazioC = new SpazioComportamentale();
        spazioC = creaSpazioDaCammini(spazioC, cammini, statiSpazioC);
        spazioC = potatura3(spazioC, statiSpazioC);
        if (!spazioC.isEmpty()) {
            spazioC = numeraStati(spazioC, statiSpazioC);
            spazioC = ridenominaStati(spazioC);
        }

        rete.setSpazioC(spazioC);
//        System.out.println(spazioC.toString());
//        SpazioComportamentale spazioComportamentaleDecorato = creaSpazioComportamentaleDecorato(spazioC);
//        stampaCammini(camminiDecorati);
        System.out.println(spazioC.toString());
    }

    public static SpazioComportamentale creaSpazioComportamentaleDecorato(Rete rete) {
        List<Cammino> camminiDecorati;
        LinkedList<StatoReteAbstract> statiDecoratiSpazioC = new LinkedList<>();
        if(rete.getSpazioC()==null){
            creaSpazioComportamentale(rete);
        }
        camminiDecorati = trovaCamminiDecorati(rete.getSpazioC(), statiDecoratiSpazioC);
//        View.stampaCammini(camminiDecorati);
        SpazioComportamentale spazioComportamentaleDecorato = new SpazioComportamentale();
        spazioComportamentaleDecorato = inserisciVerticiSpazioComportamentale(spazioComportamentaleDecorato, camminiDecorati, statiDecoratiSpazioC);
        spazioComportamentaleDecorato = inserisciLatiSpazioComportamentale(spazioComportamentaleDecorato, camminiDecorati);
//        spazioComportamentaleDecorato = creaSpazioDaCammini(spazioComportamentaleDecorato, camminiDecorati, statiDecoratiSpazioC);
//        spazioComportamentaleDecorato = potatura3(spazioComportamentaleDecorato, statiDecoratiSpazioC);
//        
        spazioComportamentaleDecorato = etichettaOsservabilita(spazioComportamentaleDecorato);
        rete.setSpazioComportamentaleDecorato(spazioComportamentaleDecorato);
        System.out.println(spazioComportamentaleDecorato.toString());
        return spazioComportamentaleDecorato;
    }

    static SpazioComportamentale creaDizionario(Rete rete, SpazioComportamentale spazioComportamentaleDecorato) {
        return determinizzazione(rete, spazioComportamentaleDecorato);
    }

    static String distillaDiagnosi(SpazioComportamentale dizionario, List<String> osservazioni) {
        String diagnosi;
        StatoDFA statoRaggiunto = raggiungiStatoOsservabile(dizionario, osservazioni);
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
                //Se un cammino ciclico ha come ultimo stato uno stato appartente ad una traiettoria, diventa una traiettoria
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

    private static ArrayList<Cammino> potatura2(List<Cammino> cammini) {
        //cammini con stati finali
        ArrayList<Cammino> traiettorie = new ArrayList<>();

        //cammini ciclici
        ArrayList<Cammino> camminiCiclici = new ArrayList<>();

        //cammini NON ciclici, con ultimo stato NON finale
        ArrayList<Cammino> notTraiettoria = new ArrayList<>();

        //traiettoria ricavata da una notTraiettora, e' necessario controllare che non sia uguale ad una delle traiettorie gia' inserite
        ArrayList<Cammino> traiettoriaDaControllare = new ArrayList<>();

        //notTraiettorie che contengono stati finali: da potare
        ArrayList<Cammino> traiettoriaDaPotare = new ArrayList<>();

        for (Cammino c : cammini) {
            if (c.isTraiettoria()) {
                traiettorie.add(c);
            } else {
                if (c.isCiclico()) {
                    camminiCiclici.add(c);
                } else {
                    notTraiettoria.add(c);
                }
            }
        }

        //Viene tolto l'ultimo stato di ogni cammino non ciclico, finche' non viene trovato uno stato finale o appartenente ad
        //un'altra traiettoria
        for (int j = 0; j < notTraiettoria.size(); j++) {
            boolean statoFinaleTrovato = false;
            while (!statoFinaleTrovato && notTraiettoria.get(j).size() > 1) {
                notTraiettoria.get(j).rimuoviUltimoStato();
                if (notTraiettoria.get(j).isTraiettoria()) {
                    statoFinaleTrovato = true;
                    traiettoriaDaControllare.add(notTraiettoria.get(j));
                    notTraiettoria.remove(j);
                    j--;
                }
            }
        }

        for (int j = 0; j < traiettoriaDaControllare.size(); j++) {
            boolean traiettoriaDaScartare = false;
            for (int i = 0; i < traiettorie.size() && !traiettoriaDaScartare; i++) {
                //Se lo stato finale di una traiettoriaDaControllare e' già presente in una traiettoria, allora la traiettoria da controllare e' da scartare
                if (traiettorie.get(i).contains((StatoRete) traiettoriaDaControllare.get(j).getUltimoStato())) {
                    traiettoriaDaScartare = true;
                    traiettoriaDaControllare.remove(j);
                    j--;
                }
            }
            if (!traiettoriaDaScartare) {
                traiettorie.add(traiettoriaDaControllare.get(j));
            }
        }

        for (int j = 0; j < traiettorie.size(); j++) {
            for (int i = 0; i < camminiCiclici.size(); i++) {

                //Se un cammino ciclico ha come ultimo stato uno stato appartente ad una traiettoria, diventa una traiettoria
                if (traiettorie.get(j).contains((StatoRete) camminiCiclici.get(i).getUltimoStato())) {
                    camminiCiclici.get(i).setIsTraiettoria(true);
                    traiettorie.add(camminiCiclici.get(i));
                    camminiCiclici.remove(i);
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
                statoTraiettoria.setNumero(numeroStato);    //rinomina numeroStato
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
        //rimuove da stati tutti gli StatoRete che non sono contenuti nelle traiettorie
        _stati.retainAll(tuttiGliStatiDelleTraiettorie);

        StatoReteAbstract root = _stati.get(0);
        if (root.getClass() == StatoRete.class) {
            root.setNumero(0);
            root = new StatoReteRidenominato(_stati.get(0));
        }

        if (root.getClass() == StatoReteDecorato.class) {

            String nomeRoot = Character.toString(Parametri.STATO_DECORATO_PREFISSO) + Integer.toString(0);
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

                //Rinomina tutti gli stati delle traiettorie
                while (tuttiGliStatiDelleTraiettorie.lastIndexOf(_stati.get(i)) != -1) {
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
//                        statoCorrente.setTransizionePrecedente(statoCorrenteCammino.getTransizionePrecedente());
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
    public static SpazioComportamentale determinizzazione(Rete rete, SpazioComportamentale spazioComportamentaleDecorato) {
        SpazioComportamentale spazioDFA = new SpazioComportamentale();
        Stack<StatoDFA> stack = new Stack<>();
        List<StatoDFA> verticiSpazio = new ArrayList<>();
        List<StatoInterface> statiRaggiunti;
        StatoInterface root = spazioComportamentaleDecorato.getRoot();
        List<String> etichetteOsservabilita = Arrays.asList(rete.getEtichettaOsservabilita());

        //statiRaggiuntiDaOsservabilita e' una lista di liste, contiene, per ogni etichetta di osservabilita contenuta nella rete,
        //la lista degli stati NFA raggiunti attraverso quella etichetta
        //
        //es: statiRaggiuntiDaOsservabilita.get(1) restituisce tutti gli stati NFA dello spazio comportamentale doppiamente decorato
        //raggiunti attraverso l'etichetta di osservabilita che si trova in posizione 1
        //ovvero: rete.getEtichettaOsservabilita()[1]
        List<List<StatoInterface>> statiReggiuntiDaOsservabilita = new ArrayList<List<StatoInterface>>();
        for (String etichettaOsservabilita : etichetteOsservabilita) {
            statiReggiuntiDaOsservabilita.add(new ArrayList<StatoInterface>());
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
                for (StatoInterface statoRaggiunto : statiRaggiunti) {//per ogni stato NDA
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
                List<StatoInterface> statiTemporanei;
                //Per ogni stato raggiunto da una etichetta di osservabilita'
                for (int i = 0; i < statiReggiuntiDaOsservabilita.size(); i++) {
                    if (!statiReggiuntiDaOsservabilita.get(i).isEmpty()) {
                        //prendi l'insieme degli stati DFA raggiunti dall'osservabilita' i 
                        statiTemporanei = statiReggiuntiDaOsservabilita.get(i);
                        //Esegui l'epsilon-CLOSURE sull'insieme di stati considerato
                        statiTemporanei = epsilon_CLOSURE(spazioComportamentaleDecorato, statiTemporanei);
                        //Il nuovo stato DFA e' l'epsilon-CLOSURE CALCOLATO
                        StatoDFA nuovoStato = new StatoDFA(new ArrayList<StatoInterface>(statiTemporanei), etichetteOsservabilita.get(i));
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
//      Creazione del dizionario ordinato
        SpazioComportamentale dizionario = new SpazioComportamentale();
        StatoDFA rootSpazioDFA = (StatoDFA) spazioDFA.getRoot();

        StatoDFA nuovaRoot = new StatoDFA(rootSpazioDFA, Parametri.STATO_DECORATO_PREFISSO + Integer.toString(verticiSpazio.indexOf(rootSpazioDFA)));
        dizionario.setRoot(nuovaRoot);
        for (StatoInterface v : spazioDFA.getVertici()) {
            int numero = verticiSpazio.indexOf(v);
            dizionario.aggiungiVertice(new StatoDFA((StatoDFA) v, Parametri.STATO_DECORATO_PREFISSO + Integer.toString(numero)));
        }

        for (StatoInterface v : spazioDFA.getVertici()) {
            StatoDFA verticeDizionario = new StatoDFA((StatoDFA) v, Parametri.STATO_DECORATO_PREFISSO + Integer.toString(verticiSpazio.indexOf(v)));
            for (StatoInterface vAdj : spazioDFA.getVerticiAdiacenti((StatoDFA) v)) {
                int numero = verticiSpazio.indexOf(vAdj);
                StatoDFA statoDaAggiungere = new StatoDFA((StatoDFA) vAdj, Parametri.STATO_DECORATO_PREFISSO + Integer.toString(numero));
                dizionario.aggiungiLato(verticeDizionario, statoDaAggiungere);
            }
        }
////          NON FUNZIONA, CREDO A CAUSA DI UN PROBLEMA CON GLI HASHCODE MODIFICATI DURANTE L'ESECUZIONE DEL CODICE
//        List<StatoDFA> statiDaRidenominare = new ArrayList<>();       
//        for (StatoInterface v : spazioDFA.getVertici()) {
//            statiDaRidenominare.add((StatoDFA) v);
//            for (StatoInterface vAdj : spazioDFA.getVerticiAdiacenti(v)) {
//                statiDaRidenominare.add((StatoDFA)vAdj);
//            }
//        }
//        for(StatoDFA v : statiDaRidenominare){
//            int numero = verticiSpazio.indexOf(v);
//            v.setNome(Parametri.STATO_DECORATO_PREFISSO + Integer.toString(numero));
//        }

//          STAMPA DEL DIZIONARIO
//        for (StatoDFA s : verticiSpazio) {
//            System.out.println(s.getNome());
//            
//        }
        // NUOVO METODO STAMPA 
        View.stampaDeterminizzazione(verticiSpazio);

//        StatoInterface statoProva = new StatoDFA("a2", "o2");
//        List<StatoInterface> prova = spazioDFA.getVerticiAdiacenti(statoProva);
//        System.out.println(statoProva.getNome() + "\t" + statoProva.hashCode());
//        System.out.println("a2" + "\t" + new StatoDFA("a2", "o2").hashCode());
//        System.out.println(verticiSpazio.get(2).getNome() + "\t" +  verticiSpazio.get(2).hashCode());
        rete.setDizionario(dizionario);
        return dizionario;
    }

    /**
     * Ritorna l'insieme degli stati del DFA ottenuto applicando
     * l'epsilon-CLOSURE ad uno stato del NFA
     *
     * @param spazioComportamentale
     * @param statoSpazio
     * @return
     */
    private static List<StatoInterface> epsilon_CLOSURE(SpazioComportamentale spazioComportamentale, StatoInterface statoSpazio) {
        List<StatoInterface> insiemeStati = new ArrayList<>();
        Stack<StatoInterface> stack = new Stack<>();
        stack.push(statoSpazio);
        while (!stack.isEmpty()) {
            StatoInterface statoAnalizzato = stack.pop();

            if (statoAnalizzato.getClass() == StatoReteRidenominato.class) {
                insiemeStati.add((StatoReteRidenominato) statoAnalizzato);
            }
            if (statoAnalizzato.getClass() == StatoDFA.class) {
                insiemeStati.add((StatoDFA) statoAnalizzato);
            }
            List<StatoInterface> statiAdiacenti = spazioComportamentale.getVerticiAdiacenti(statoAnalizzato);

            for (StatoInterface s : statiAdiacenti) {
                if (s.getClass() == StatoReteRidenominato.class) {
                    if (((StatoReteAbstract) s).getOsservabilita() == null && !insiemeStati.contains(s)) {
                        stack.push((StatoReteRidenominato) s);
                    }
                }
                if (s.getClass() == StatoDFA.class) {
                    if (((StatoDFA) s).getOsservabilita() == null && !insiemeStati.contains(s)) {
                        stack.push((StatoDFA) s);
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
    private static List<StatoInterface> epsilon_CLOSURE(SpazioComportamentale spazioComportamentaleDecorato, List<StatoInterface> statiRaggiunti) {
        List<StatoInterface> statiDaRitornare = new ArrayList<StatoInterface>(statiRaggiunti);
        List<StatoInterface> epsilonStati = new ArrayList<StatoInterface>();
        for (StatoInterface stato : statiRaggiunti) {
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

    public static SpazioComportamentale creaDizionarioParziale(Rete rete, String osservazione) {
        // parte relativa alla creazione dell'automa riconoscitore
        SpazioComportamentale automaRiconoscitore = creaRiconoscitoreEspressione(rete, osservazione);

        SpazioComportamentale spazioComportamentaleParziale = creaSpazioComportamentaleParziale(rete, automaRiconoscitore);
        SpazioComportamentale dizionarioParziale = null;
        if (spazioComportamentaleParziale != null) {
            dizionarioParziale = creaDizionario(rete, spazioComportamentaleParziale);
            rete.setDizionarioParziale(dizionarioParziale);
        }
        return dizionarioParziale;
    }
  
    public static SpazioComportamentale creaRiconoscitoreEspressione(Rete rete, String osservazione) {
        SpazioComportamentale automaRiconoscitore = new SpazioComportamentale();
        int nomeStatoDFA = 0;
        StatoDFA root = new StatoDFA(String.valueOf(nomeStatoDFA), null);   // la root non ha etichette di osservabilita
        automaRiconoscitore.setRoot(root);
        automaRiconoscitore.aggiungiVertice(root);

        Stack<StatoDFA> pilaDFA = new Stack<StatoDFA>(); // usata in caso di loop
        pilaDFA.push(root);
        String[] listaOsservazioni = osservazione.split(Parametri.SPAZIO);

        // INIZIO ALGORITMO CREAZIONE AUTOMA RICONOSCITORE DELL'ESPRESSIONE
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
                            if (controllaNumeroOsservazioni == listaOsservazioni.length) {
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
                            if (i > 0) {
                                // se si collego l'ultimo stato fuori parentesi
                                int temp = i - 1;
                                if (listaOsservazioni[temp].contains(Parametri.PARENTESI_TONDA_C) && listaOsservazioni[temp].contains(Parametri.ASTERISCO)) {
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
                    if (!singolaOsservazione.contains(Parametri.ASTERISCO) && !singolaOsservazione.contains(Parametri.PIU)) {
                        nomeStatoDFA++;
                        statoDaAggiungere = new StatoDFA(String.valueOf(nomeStatoDFA), singolaOsservazione);
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
                        if (statoPrecedenteOpzionale != null) {
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
                if (controlloUltimaOsservazione == listaOsservazioni.length) {
                    // se è l'ultimo della lista deve essere finale
                    statoDaAggiungere.setIsFinale(true);
                }
                statoPrecedente = statoDaAggiungere;
            }
        }
        
        System.out.println(automaRiconoscitore.toStringAutomaRiconoscitore());
        return automaRiconoscitore;
    }

    private static SpazioComportamentale creaSpazioComportamentaleParziale(Rete rete, SpazioComportamentale automaRiconoscitore) {

        List<Cammino> cammini;
        List<StatoReteAbstract> statiSpazioC = new ArrayList<>();
        SpazioComportamentale spazioComportamentaleParzialeDecorato = null;
        cammini = trovaCamminiParziali(rete, statiSpazioC, automaRiconoscitore);

        View.stampaCammini(cammini);
//        List<Cammino> traiettorie = potatura2(cammini);
//        View.stampaTraiettorie(traiettorie);
//        traiettorie = ridenominazione(traiettorie, statiSpazioC);
//        View.stampaTraiettorie(traiettorie);
        SpazioComportamentale spazioC = new SpazioComportamentale();
        spazioC = creaSpazioDaCammini(spazioC, cammini, statiSpazioC);
        spazioC = potatura3(spazioC, statiSpazioC);
        if (!spazioC.isEmpty()) {
            spazioC = numeraStati(spazioC, statiSpazioC);
            spazioC = ridenominaStati(spazioC);
//        statiSpazioC.get(17).setNome(Parametri.STATO_DECORATO_PREFISSO + Integer.toString(statiSpazioC.get(17).getNumero()));
//        statiSpazioC.set(17,new StatoReteRidenominato(statiSpazioC.get(17)));
//        spazioC = inserisciVerticiSpazioComportamentale(spazioC, traiettorie, statiSpazioC);
//        spazioC = inserisciLatiSpazioComportamentale(spazioC, traiettorie);
            spazioComportamentaleParzialeDecorato = creaSpazioComportamentaleDecorato(spazioC);

//        System.out.println(spazioC.toString());
//        SpazioComportamentale spazioComportamentaleDecorato = creaSpazioComportamentaleDecorato(spazioC);
//        stampaCammini(camminiDecorati);
            System.out.println(spazioComportamentaleParzialeDecorato.toString());
        } else {
            View.traiettorieNonTrovate();
        }
        return spazioComportamentaleParzialeDecorato;
    }

    private static ArrayList<StatoInterface> getTerminazioni(SpazioComportamentale spazioComportamentaleParziale) {
        ArrayList<StatoInterface> terminazioni = new ArrayList<>();
        StatoInterface root = spazioComportamentaleParziale.getRoot();
        Stack<StatoInterface> pila = new Stack<>();
        pila.push(root);

        while (!pila.empty()) {
            StatoInterface stato = pila.pop();
            List<StatoInterface> verticiAdiacenti = spazioComportamentaleParziale.getVerticiAdiacenti(stato);
            if (verticiAdiacenti.size() == 0) {
                terminazioni.add(stato);
            } else {
                for (StatoInterface vertice : verticiAdiacenti) {
                    pila.push(vertice);
                }
            }
        }

        return terminazioni;
    }

    private static boolean controlloPresenzaNuoviStati(ArrayList<StatoInterface> statiInseriti, SpazioComportamentale spazioComportamentaleParziale) {
        boolean controllo = false;
        StatoInterface root = spazioComportamentaleParziale.getRoot();
        ArrayList<StatoInterface> temp = (ArrayList<StatoInterface>) statiInseriti.clone();
        Stack<StatoInterface> pila = new Stack<>();
        pila.push(root);
        int conta = 0;

        while (!pila.empty()) {
            StatoInterface stato = pila.pop();
            List<StatoInterface> verticiAdiacenti = spazioComportamentaleParziale.getVerticiAdiacenti(stato);
            for (StatoInterface vertice : verticiAdiacenti) {
                pila.push(vertice);
                conta++;
            }
        }
        if (temp.size() != conta) {
            controllo = true;
        }

        return controllo;
    }

    private static StatoInterface cercaInSpazioComportamentaleIntero(String nomeStato, SpazioComportamentale spazioComportamentaleIntero) {
        StatoInterface statoCercato = null;
        StatoInterface root = spazioComportamentaleIntero.getRoot();
        ArrayList<StatoInterface> visitati = new ArrayList<>();
        Stack<StatoInterface> pila = new Stack<>();
        pila.push(root);

        StatoReteAbstract verticeABSroot = (StatoReteAbstract) root;
        if (nomeStato.equalsIgnoreCase(verticeABSroot.getNome())) {
            return root;
        } else {
            visitati.add(root);
            boolean controllo = false;
            while (!controllo) {
                StatoInterface stato = pila.pop();
                StatoReteAbstract verticeABS = (StatoReteAbstract) stato;
                List<StatoInterface> verticiAdiacenti = spazioComportamentaleIntero.getVerticiAdiacenti(stato);
                for (StatoInterface vertice : verticiAdiacenti) {
                    verticeABS = (StatoReteAbstract) vertice;
                    if (!visitati.contains(verticeABS)) {
                        if (verticeABS.getNome().equalsIgnoreCase(nomeStato)) {
                            statoCercato = vertice;
                            controllo = true;
                            break;
                        } else {
                            visitati.add(vertice);
                            pila.push(vertice);
                        }
                    }
                }
            }
        }

        return statoCercato;
    }

    private static StatoInterface cercaInSpazioComportamentaleParziale(String nomeStato, SpazioComportamentale spazioComportamentaleParziale) {
        StatoInterface statoCercato = null;
        StatoInterface root = spazioComportamentaleParziale.getRoot();
        ArrayList<StatoInterface> visitati = new ArrayList<>();
        Stack<StatoInterface> pila = new Stack<>();
        pila.push(root);

        StatoDFA verticeDFAroot = (StatoDFA) root;
        if (nomeStato.equalsIgnoreCase(verticeDFAroot.getNome())) {
            return root;
        } else {
            visitati.add(root);
            boolean controllo = false;
            while (!controllo) {
                StatoInterface stato = pila.pop();
                StatoDFA verticeDFA = (StatoDFA) stato;
                List<StatoInterface> verticiAdiacenti = spazioComportamentaleParziale.getVerticiAdiacenti(stato);
                for (StatoInterface vertice : verticiAdiacenti) {
                    verticeDFA = (StatoDFA) vertice;
                    if (!visitati.contains(verticeDFA)) {
                        if (verticeDFA.getNome().equalsIgnoreCase(nomeStato)) {
                            statoCercato = vertice;
                            controllo = true;
                            break;
                        } else {
                            visitati.add(vertice);
                            pila.push(vertice);
                        }
                    }
                }
            }
        }

        return statoCercato;
    }

    private static SpazioComportamentale epsilon_CLOSURE_SCP(SpazioComportamentale spazioComportamentaleParziale, SpazioComportamentale spazioComportamentaleIntero, StatoInterface stato, StatoInterface statoDFA_SCP, String nomeAR) {
        SpazioComportamentale spazioModificato = spazioComportamentaleParziale;
        Stack<StatoInterface> pila = new Stack<>();
        Stack<StatoDFA> pilaDFA = new Stack<>();
        StatoInterface statoPrecedente = stato;
        StatoDFA statoPrecedenteSCP = (StatoDFA) statoDFA_SCP;
        pilaDFA.push(statoPrecedenteSCP);
        pila.push(statoPrecedente);

        while (!pila.isEmpty()) {
            statoPrecedente = pila.pop();
            statoPrecedenteSCP = pilaDFA.pop();
            List<StatoInterface> verticiAdiacenti = spazioComportamentaleIntero.getVerticiAdiacenti(statoPrecedente);
            for (StatoInterface vertice : verticiAdiacenti) {
                StatoReteAbstract verticeABS = (StatoReteAbstract) vertice;
                if (verticeABS.getOsservabilita() == null) {
                    StatoDFA statoDaAggiungere = new StatoDFA(verticeABS.getNome(), null);
                    System.out.println("STATO DA AGGIUNGERE: " + statoDaAggiungere.getNome());
                    try {
                        spazioComportamentaleParziale.aggiungiVertice(statoDaAggiungere);
                        spazioComportamentaleParziale.aggiungiLato(statoPrecedenteSCP, statoDaAggiungere);
                    } catch (NullPointerException e) {
                        System.out.println("stato già presente");
//                        StatoInterface verticeGiaPresente = cercaInSpazioComportamentaleParziale(verticeABS.getNome(), spazioComportamentaleParzialeDecorato);
//                        System.out.println(verticeGiaPresente.getNome());
//                        StatoDFA verticeGiaPresenteDFA = (StatoDFA) verticeGiaPresente;
//                        spazioComportamentaleParzialeDecorato.aggiungiLato(statoPrecedenteSCP, verticeGiaPresenteDFA);
                    }
                    statoDaAggiungere.setStatoRiconoscitoreEspressione(nomeAR);
                    if (verticeABS.isFinale()) {
                        statoDaAggiungere.setIsFinale(true);
                    }
                    pila.push(vertice);
                    pilaDFA.push(statoDaAggiungere);
                }
            }
        }

        return spazioModificato;
    }

    private static ArrayList<StatoInterface> getStatiDaArchiEpsilon(SpazioComportamentale spazioComportamentaleParziale, StatoInterface stato) {
        ArrayList<StatoInterface> stati = new ArrayList<>();
        Stack<StatoInterface> pila = new Stack<>();
        StatoInterface statoPrecedente = stato;
        pila.push(statoPrecedente);

        while (!pila.isEmpty()) {
            statoPrecedente = pila.pop();
            List<StatoInterface> verticiAdiacenti = spazioComportamentaleParziale.getVerticiAdiacenti(statoPrecedente);
            for (StatoInterface vertice : verticiAdiacenti) {
                StatoDFA verticeDFA = (StatoDFA) vertice;
                if (verticeDFA.getOsservabilita() == null) {
                    stati.add(vertice);
                    pila.push(vertice);
                }
            }
        }

        return stati;
    }

    private static Stack<StatoInterface> inserisciTerminazioni(ArrayList<StatoInterface> terminazioni, Stack<StatoInterface> pila) {
        for (int i = 0; i < terminazioni.size(); i++) {
            StatoInterface stato = terminazioni.get(i);
            pila.push(stato);
        }
        return pila;
    }

    private static SpazioComportamentale creaDizionarioParziale(SpazioComportamentale spazioComportamentaleParziale) {
        //TODO Alby
        return null;
    }

    private static List<Cammino> trovaCamminiParziali(Rete rete, List<StatoReteAbstract> stati, SpazioComportamentale automaRiconoscitore) {
        //  TO-DO inserire l'automa riconoscitore 

        List<Cammino> cammini = new ArrayList<>();
        Stack<StatoRete> pilaStato = new Stack<>();//pila dei nuovi stati

        Stack<StatoRete> pilaDiramazioni = new Stack<StatoRete>();//pila degli stati che hanno più di una transizione in uscita

        //Stack<Cammino> pilaCammino = new Stack<>();
        Cammino camminoAttuale = creaNuovoCammino(rete);//il cammino attuale diventa un nuovo cammino con gli stati degli automi e i link azzerati
        int numeroStati = -1;
        StatoDFA statoRadiceRiconoscitore = (StatoDFA) automaRiconoscitore.getRoot();
        StatoRete statoRadice = creaStatoCorrente(rete, numeroStati);
        statoRadice.setStatoAutomaRiconoscitore(statoRadiceRiconoscitore);
        statoRadice.aggiungiStatoAutomaRinocitoreAllaDescrizione();
        pilaStato.push(statoRadice);

        while (!pilaStato.isEmpty()) {
            StatoRete statoAttuale = pilaStato.pop();
            StatoDFA statoAutomaRiconoscitoreAttuale = statoAttuale.getStatoAutomaRiconoscitore();
//            

            if (!stati.contains(statoAttuale)) {//se non è uno stato nuovo
                //statoAttuale.setNumero(stati.size());
                stati.add(statoAttuale);
                camminoAttuale.add(statoAttuale);
                //spazioC.aggiungiVertice(new StatoReteRidenominato(statoAttuale));
//                System.out.println(statoAttuale.toString());
                rete.setRete(statoAttuale);
                if (statoAttuale.isAbilitato(rete.getAutomi(), automaRiconoscitore.getVerticiAdiacenti(statoAutomaRiconoscitoreAttuale))) {
                    List<List<Transizione>> transizioniAbilitate = new ArrayList<List<Transizione>>(rete.getAutomi().size());
                    int numeroTransizioniAbilitate = 0;
                    for (int i = 0; i < rete.getAutomi().size(); i++) {//se nessun automa è già scattato, si itera su tutti gli automi                        
                        rete.getAutomi().get(i).isAbilitato(rete.getLink());
                        ArrayList<Transizione> transizioniAbilitateDaControllare = rete.getAutomi().get(i).getTransizioneAbilitata();
                        /*//controllo che le transizioni abilitate soddisfino l'automa riconoscitore
                        for ( int z=0; z < transizioniAbilitateDaControllare.size(); z++){
                            boolean alitata = transizioniAbilitateDaControllare.get(z).getOsservabilita()==null;
                            
                            
                        }*/
                        transizioniAbilitate.add(transizioniAbilitateDaControllare);//transizione abilitata diventa la transizione abilitata allo scatto nell'automa attuale                   
                        numeroTransizioniAbilitate += transizioniAbilitate.get(i).size();
                    }
                    ArrayList<StatoRete> statiDopoLoScatto = new ArrayList<>();
                    Transizione transizioneEseguita;
                    StatoRete copiaStatoAttuale;
                    if (numeroTransizioniAbilitate == 1) {
                        for (int i = 0; i < rete.getAutomi().size(); i++) {
                            if (rete.getAutomi().get(i).isAbilitato(rete.getLink())) {
                                //la prima e unica transizione abilitata allo scatto
                                Transizione transizioneDaEseguire = rete.getAutomi().get(i).getTransizioneAbilitata().get(0);
                                boolean abilitata = transizioneDaEseguire.getOsservabilita() == null;
                                StatoDFA statoAutomaRiconoscitoreDopoLoScatto = statoAutomaRiconoscitoreAttuale;
                                List<StatoInterface> statiAdiacentiAutomaRiconoscitore = automaRiconoscitore.getVerticiAdiacenti(statoAutomaRiconoscitoreAttuale);

                                for (int z = 0; z < statiAdiacentiAutomaRiconoscitore.size() && !abilitata; z++) {
                                    StatoDFA statoAutomaRiconoscitoreConsiderato = (StatoDFA) statiAdiacentiAutomaRiconoscitore.get(z);
                                    if (transizioneDaEseguire.getOsservabilita().equals(statoAutomaRiconoscitoreConsiderato.getOsservabilita())) {
                                        abilitata = true;
                                        statoAutomaRiconoscitoreDopoLoScatto = statoAutomaRiconoscitoreConsiderato;
                                    }
                                }
                                if (abilitata) {
                                    transizioneEseguita = rete.getAutomi().get(i).scatta(rete.getLink());//l'automa attuale viene fatto scattare e transizione eseguita diventa la transizione che è stata eseguita
//                                statoAttuale.setTransizionePrecedente(transizioneEseguita);//la transizione eseguita viene aggiunta allo StatoRete attuale
                                    StatoRete statoDopoLoScatto = creaStatoCorrente(rete.getAutomi(), rete.getLink(), numeroStati);
                                    statoDopoLoScatto.setTransizionePrecedente(transizioneEseguita);
                                    statoDopoLoScatto.setStatoAutomaRiconoscitore(statoAutomaRiconoscitoreDopoLoScatto);
                                    //lo stato dell'automa riconoscitore viene aggiunto al nome dello stato della rete
                                    statoDopoLoScatto.aggiungiStatoAutomaRinocitoreAllaDescrizione();
                                    statiDopoLoScatto.add(statoDopoLoScatto);
                                    rete.setRete(statoAttuale);
                                }
                            }
                        }
                    } else {
                        //Se sono abilitate piu' transizioni:
                        //Provare a sostituire con creaStatoCorrente(rete)
                        copiaStatoAttuale = creaStatoCorrente(rete.getAutomi(), rete.getLink(), numeroStati);
                        copiaStatoAttuale.setStatoAutomaRiconoscitore(statoAutomaRiconoscitoreAttuale);
                        copiaStatoAttuale.aggiungiStatoAutomaRinocitoreAllaDescrizione();

                        for (int i = 0; i < rete.getAutomi().size(); i++) {
                            for (int j = 0; j < transizioniAbilitate.get(i).size(); j++) {//vengono fatte scattare tutte, ognuna su un nuovo cammino
                                rete.setRete(statoAttuale);
                                Transizione transizioneDaEseguire = transizioniAbilitate.get(i).get(j);

                                boolean abilitata = transizioneDaEseguire.getOsservabilita() == null;
                                StatoDFA statoAutomaRiconoscitoreDopoLoScatto = statoAutomaRiconoscitoreAttuale;
                                List<StatoInterface> statiAdiacentiAutomaRiconoscitore = automaRiconoscitore.getVerticiAdiacenti(statoAutomaRiconoscitoreAttuale);

                                for (int z = 0; z < statiAdiacentiAutomaRiconoscitore.size() && !abilitata; z++) {
                                    StatoDFA statoAutomaRiconoscitoreConsiderato = (StatoDFA) statiAdiacentiAutomaRiconoscitore.get(z);
                                    if (transizioneDaEseguire.getOsservabilita().equals(statoAutomaRiconoscitoreConsiderato.getOsservabilita())) {
                                        abilitata = true;
                                        statoAutomaRiconoscitoreDopoLoScatto = statoAutomaRiconoscitoreConsiderato;
                                    }
                                }

                                if (abilitata) {
                                    transizioneEseguita = rete.getAutomi().get(i).scatta(transizioneDaEseguire, rete.getLink());//viene fatta scattare la transizione da eseguire
                                    copiaStatoAttuale.setTransizionePrecedente(transizioneEseguita);
                                    StatoRete statoDopoLoScatto = creaStatoCorrente(rete.getAutomi(), rete.getLink(), numeroStati);
                                    statoDopoLoScatto.setTransizionePrecedente(transizioneEseguita);
                                    statoDopoLoScatto.setStatoAutomaRiconoscitore(statoAutomaRiconoscitoreDopoLoScatto);
                                    statoDopoLoScatto.aggiungiStatoAutomaRinocitoreAllaDescrizione();
                                    statiDopoLoScatto.add(statoDopoLoScatto);
                                    pilaDiramazioni.add(statoAttuale);
                                }
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
                //Il cammino si interrompe perche' e' stato trovato uno stato gia' visitato, quindi e' un cammino o ciclico
                //o che si ricongiunge nello stato di un altro cammino gia' trovato
                Cammino nuovoCammino = new Cammino();
//                statoAttuale.setNumero(stati.indexOf(statoAttuale));
                StatoRete statoPrecedente = (StatoRete) camminoAttuale.getUltimoStato();
                camminoAttuale.add(statoAttuale);//                
                nuovoCammino.copiaCammino(camminoAttuale);
                nuovoCammino.setIsCiclo(true);
                cammini.add(nuovoCammino);
                if (!pilaDiramazioni.isEmpty()) {
                    //gli ultimi elementi del cammini vengono rimossi finchè non si incontra il primo elemento della coda
                    camminoAttuale.togliFinoAPrimaDelloStato(pilaDiramazioni.pop());
                }

            }
        }
        rete.setCammini(cammini);
        return cammini;

    }

    private static SpazioComportamentale creaSpazioDaCammini(SpazioComportamentale spazioC, List<Cammino> cammini, List<StatoReteAbstract> stati) {
        spazioC.aggiungiVertice(stati.get(0));
        spazioC.setRoot(stati.get(0));
        for (int i = 1; i < stati.size(); i++) {
            spazioC.aggiungiVertice(stati.get(i));
        }

        for (Cammino cammino : cammini) {
            ArrayList<StatoReteAbstract> statiCammino = cammino.getCammino();
            if (statiCammino.size() > 1) {
                for (int i = 1; i < statiCammino.size(); i++) {
                    StatoReteAbstract statoPrecedenteCammino = statiCammino.get(i - 1);
                    //Consente di avere tutti gli stati simili uguali ad un unico stato puntato
//                    StatoRete statoPrecedente = (StatoRete) stati.get(stati.indexOf(statoPrecedenteCammino));
                    StatoRete statoPrecedente = (StatoRete) statoPrecedenteCammino;
//                    statoPrecedente.setNumero(stati.get(stati.indexOf(statoPrecedenteCammino)).getNumero());
//                    StatoReteAbstract statoPrecedente = null;                   
//                        statoPrecedente = new StatoRete(statoPrecedenteCammino);  
                    StatoReteAbstract statoCorrenteCammino = statiCammino.get(i);
                    //Consente di avere tutti gli stati simili uguali ad un unico stato puntato
//                    StatoRete statoCorrente = (StatoRete) stati.get(stati.indexOf(statoCorrenteCammino));
                    StatoRete statoCorrente = (StatoRete) statoCorrenteCammino;
//                    statoCorrente.setNumero(stati.get(stati.indexOf(statoCorrenteCammino)).getNumero());
//                    StatoReteAbstract statoCorrente = null;
//                        statoCorrente = new StatoRete(statoCorrenteCammino);   
                    spazioC.aggiungiLato(statoPrecedente, statoCorrente);
                }
            }
        }
        return spazioC;
    }

    //Questa potatura rimuove gli statoRete foglia non finali da uno spazio comportamentale
    private static SpazioComportamentale potatura3(SpazioComportamentale spazioC, List<StatoReteAbstract> statiSpazioC) {
        ArrayList<StatoInterface> vertici = new ArrayList<>(spazioC.getVertici());

        boolean verticeRimosso = true;
        while (verticeRimosso) {
            verticeRimosso = false;
            for (int i = 0; i < vertici.size() && !verticeRimosso; i++) {
                StatoRete verticeConsiderato = (StatoRete) vertici.get(i);
                if (spazioC.getVerticiAdiacenti(verticeConsiderato) == null || spazioC.getVerticiAdiacenti(verticeConsiderato).size() == 0) {
                    if (!verticeConsiderato.isFinale()) {
                        statiSpazioC.remove(statiSpazioC.indexOf(vertici.get(i)));
                        spazioC.rimuoviVertice(vertici.get(i));
                        vertici.remove(i);
                        verticeRimosso = true;
                    }
                }
            }
        }
        /*
         //Rinomina tutti gli stati delle traiettorie
        while (tuttiGliStatiDelleTraiettorie.lastIndexOf(_stati.get(i)) != -1) {
            tuttiGliStatiDelleTraiettorie.get(tuttiGliStatiDelleTraiettorie.lastIndexOf(_stati.get(i))).setNome(nome);
            tuttiGliStatiDelleTraiettorie.remove(tuttiGliStatiDelleTraiettorie.lastIndexOf(_stati.get(i)));
        }
         */

        return spazioC;
    }

    private static SpazioComportamentale numeraStati(SpazioComportamentale spazioC, List<StatoReteAbstract> statiSpazioC) {

        Set<StatoInterface> visited = new LinkedHashSet<StatoInterface>();
        Stack<StatoInterface> stack = new Stack<StatoInterface>();
        StatoRete root = (StatoRete) spazioC.getRoot();
        stack.push(root);
        while (!stack.isEmpty()) {
            StatoInterface vertex = stack.pop();

            StatoRete verticeConsiderato = (StatoRete) vertex;
            if (verticeConsiderato.getNumero() == -1) {
                verticeConsiderato.setNumero(statiSpazioC.indexOf(verticeConsiderato));
                verticeConsiderato.setNome(Parametri.STATO_DECORATO_PREFISSO + Integer.toString(verticeConsiderato.getNumero()));

            }
            if (!visited.contains(vertex)) {

                visited.add(vertex);
                for (StatoInterface v : spazioC.getVerticiAdiacenti(vertex)) {
                    stack.push(v);
                }
            }
        }
        return spazioC;

    }

    //Crea un nuovo spazioComportamentale con gli statiReteRidonominati
    private static SpazioComportamentale ridenominaStati(SpazioComportamentale spazioC) {
        SpazioComportamentale spazioComportamentaleRidenominato = new SpazioComportamentale();
        StatoInterface rootSpazioC = spazioC.getRoot();
        StatoReteRidenominato rootRidenominata = new StatoReteRidenominato((StatoReteAbstract) rootSpazioC);
        spazioComportamentaleRidenominato.setRoot(rootRidenominata);

        //Trasforma tutti i vertici StatoRete dello spazioComportamentale in StatoReteRidenominato 
        //e aggiunge il vertice al nuovo spazio comportamentale
        ArrayList<StatoInterface> vertici = spazioC.getVertici().stream().collect(Collectors.toCollection(ArrayList::new));
        for (StatoInterface v : vertici) {
            v = new StatoReteRidenominato((StatoReteAbstract) v);
            spazioComportamentaleRidenominato.aggiungiVertice(v);
        }
        for (StatoInterface v : vertici) {
            List<StatoInterface> verticiAdiacenti = spazioC.getVerticiAdiacenti(v);
            StatoReteRidenominato vRidenominato = new StatoReteRidenominato((StatoReteAbstract) v);
            for (StatoInterface verticeAdj : verticiAdiacenti) {
                verticeAdj = new StatoReteRidenominato((StatoReteAbstract) verticeAdj);
                spazioComportamentaleRidenominato.aggiungiLato(vRidenominato, verticeAdj);
            }
        }

        return spazioComportamentaleRidenominato;

    }

    private static SpazioComportamentale creaSpazioComportamentaleDecorato(SpazioComportamentale spazioC) {
        List<Cammino> camminiDecorati;
        LinkedList<StatoReteAbstract> statiDecoratiSpazioC = new LinkedList<>();
        camminiDecorati = trovaCamminiDecorati(spazioC, statiDecoratiSpazioC);
        View.stampaCammini(camminiDecorati);
        SpazioComportamentale spazioComportamentaleDecorato = new SpazioComportamentale();
        spazioComportamentaleDecorato = inserisciVerticiSpazioComportamentale(spazioComportamentaleDecorato, camminiDecorati, statiDecoratiSpazioC);
        spazioComportamentaleDecorato = inserisciLatiSpazioComportamentale(spazioComportamentaleDecorato, camminiDecorati);
        spazioComportamentaleDecorato = etichettaOsservabilita(spazioComportamentaleDecorato);
//        System.out.println(spazioComportamentaleDecorato.toString());

        return spazioComportamentaleDecorato;
    }

    public static SpazioComportamentale unisciDizionari(Rete rete, List<SpazioComportamentale> dizionariParziali) {
        SpazioComportamentale dizionarioIncrementale = new SpazioComportamentale();
        StatoDFA root = new StatoDFA(Parametri.NOME_ROOT_DIZIONARIO_INCREMENTALE, null);
        dizionarioIncrementale.aggiungiVertice(root);
        dizionarioIncrementale.setRoot(root);

        for (SpazioComportamentale daAggiungere : dizionariParziali) {
            StatoDFA rootDaAggiungere = (StatoDFA) daAggiungere.getRoot();
            dizionarioIncrementale.unisciSpazi(daAggiungere);
            dizionarioIncrementale.aggiungiLato(root, rootDaAggiungere);
        }
        dizionarioIncrementale = determinizzaDizionarioIncrementale(rete, dizionarioIncrementale);

        return dizionarioIncrementale;
    }

    public static SpazioComportamentale determinizzaDizionarioIncrementale(Rete rete, SpazioComportamentale dizionarioDaDeterminizzare) {
        SpazioComportamentale spazioDFA = new SpazioComportamentale();
        Stack<StatoDFA> stack = new Stack<>();
        List<StatoDFA> verticiSpazio = new ArrayList<>();
        List<StatoInterface> statiRaggiunti;
        StatoInterface root = dizionarioDaDeterminizzare.getRoot();
        List<String> etichetteOsservabilita = Arrays.asList(rete.getEtichettaOsservabilita());

        //statiRaggiuntiDaOsservabilita e' una lista di liste, contiene, per ogni etichetta di osservabilita contenuta nella rete,
        //la lista degli stati NFA raggiunti attraverso quella etichetta
        //
        //es: statiRaggiuntiDaOsservabilita.get(1) restituisce tutti gli stati NFA dello spazio comportamentale doppiamente decorato
        //raggiunti attraverso l'etichetta di osservabilita che si trova in posizione 1
        //ovvero: rete.getEtichettaOsservabilita()[1]
        List<List<StatoInterface>> statiReggiuntiDaOsservabilita = new ArrayList<List<StatoInterface>>();
        for (String etichettaOsservabilita : etichetteOsservabilita) {
            statiReggiuntiDaOsservabilita.add(new ArrayList<StatoInterface>());
        }
        //creazione dello stato statoDFA root a partire dall'epsilon-CLOSURE
        statiRaggiunti = epsilon_CLOSURE(dizionarioDaDeterminizzare, root);

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
                //rimuovi dalla pila il primo stato DFA e prendi tutti i suoi stati FDA
                statiRaggiunti = statoAnalizzato.getStati();
                for (StatoInterface statoRaggiunto : statiRaggiunti) {//per ogni stato adiacenti
                    //controlla nello spazio doppiamente decorato gli stati adiacenti
                    List<StatoInterface> statiAdiacenti = dizionarioDaDeterminizzare.getVerticiAdiacenti(statoRaggiunto);
                    //per ogni stato adiacente
                    for (StatoInterface statoAdiacente : statiAdiacenti) {
                        //se lo stato raggiunto ha una transizione osservabile
                        if (((StatoDFA) statoAdiacente).getOsservabilita() != null) {
                            int posizioneOsservabilita = etichetteOsservabilita.indexOf(((StatoDFA) statoAdiacente).getOsservabilita());
                            statiReggiuntiDaOsservabilita.get(posizioneOsservabilita).add((StatoDFA) statoAdiacente);
                        }
                    }
                }
                List<StatoInterface> statiTemporanei;
                //Per ogni stato raggiunto da una etichetta di osservabilita'
                for (int i = 0; i < statiReggiuntiDaOsservabilita.size(); i++) {
                    if (!statiReggiuntiDaOsservabilita.get(i).isEmpty()) {
                        //prendi l'insieme degli stati DFA raggiunti dall'osservabilita' i 
                        statiTemporanei = statiReggiuntiDaOsservabilita.get(i);
                        //Esegui l'epsilon-CLOSURE sull'insieme di stati considerato
                        //statiTemporanei = epsilon_CLOSURE(spazioComportamentaleDecorato, statiTemporanei);
                        //Il nuovo stato DFA e' l'epsilon-CLOSURE CALCOLATO
                        StatoDFA nuovoStato = new StatoDFA(new ArrayList<StatoInterface>(statiTemporanei), etichetteOsservabilita.get(i));
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
////      Creazione del dizionario ordinato
//        SpazioComportamentale dizionario = new SpazioComportamentale();
//        StatoDFA rootSpazioDFA = (StatoDFA) spazioDFA.getRoot();
//
//        StatoDFA nuovaRoot = new StatoDFA(rootSpazioDFA, Parametri.STATO_DECORATO_PREFISSO + Integer.toString(verticiSpazio.indexOf(rootSpazioDFA)));
//        dizionario.setRoot(nuovaRoot);
//        for (StatoInterface v : spazioDFA.getVertici()) {
//            int numero = verticiSpazio.indexOf(v);
//            dizionario.aggiungiVertice(new StatoDFA((StatoDFA) v, Parametri.STATO_DECORATO_PREFISSO + Integer.toString(numero)));
//        }
//
//        for (StatoInterface v : spazioDFA.getVertici()) {
//            StatoDFA verticeDizionario = new StatoDFA((StatoDFA) v, Parametri.STATO_DECORATO_PREFISSO + Integer.toString(verticiSpazio.indexOf(v)));
//            for (StatoInterface vAdj : spazioDFA.getVerticiAdiacenti((StatoDFA) v)) {
//                int numero = verticiSpazio.indexOf(vAdj);
//                StatoDFA statoDaAggiungere = new StatoDFA((StatoDFA) vAdj, Parametri.STATO_DECORATO_PREFISSO + Integer.toString(numero));
//                dizionario.aggiungiLato(verticeDizionario, statoDaAggiungere);
//            }
//        }
////          NON FUNZIONA, CREDO A CAUSA DI UN PROBLEMA CON GLI HASHCODE MODIFICATI DURANTE L'ESECUZIONE DEL CODICE
//        List<StatoDFA> statiDaRidenominare = new ArrayList<>();       
//        for (StatoInterface v : spazioDFA.getVertici()) {
//            statiDaRidenominare.add((StatoDFA) v);
//            for (StatoInterface vAdj : spazioDFA.getVerticiAdiacenti(v)) {
//                statiDaRidenominare.add((StatoDFA)vAdj);
//            }
//        }
//        for(StatoDFA v : statiDaRidenominare){
//            int numero = verticiSpazio.indexOf(v);
//            v.setNome(Parametri.STATO_DECORATO_PREFISSO + Integer.toString(numero));
//        }

//          STAMPA (VECCHIA) DEL DIZIONARIO
//        for (StatoDFA s : verticiSpazio) {
//            System.out.println(s.getNome());
//        }
//      STAMPA DEL DIZIONARIO
        View.stampaDeterminizzazioneFusioneDizionari(verticiSpazio);

//        StatoInterface statoProva = new StatoDFA("a2", "o2");
//        List<StatoInterface> prova = spazioDFA.getVerticiAdiacenti(statoProva);
//        System.out.println(statoProva.getNome() + "\t" + statoProva.hashCode());
//        System.out.println("a2" + "\t" + new StatoDFA("a2", "o2").hashCode());
//        System.out.println(verticiSpazio.get(2).getNome() + "\t" +  verticiSpazio.get(2).hashCode());
        rete.setDizionarioParziale(spazioDFA);

        return spazioDFA;
    }

    public static SpazioComportamentale creaAutomaRiconoscitoreScenario(Rete rete, String osservazione) {
        SpazioComportamentale automaRiconoscitore = new SpazioComportamentale();
        int nomeStatoDFA = 0;
        StatoDFA root = new StatoDFA(String.valueOf(nomeStatoDFA), null);   // la root non ha etichette di osservabilita
        automaRiconoscitore.setRoot(root);
        automaRiconoscitore.aggiungiVertice(root);

        Stack<StatoDFA> pilaDFA = new Stack<StatoDFA>(); // usata in caso di loop
        pilaDFA.push(root);
//        String[] listaOsservazioni = osservazione.split(Parametri.SPAZIO);
        String[] listaOsservazioni = osservazione.split(Parametri.SPAZIO);

        // INIZIO ALGORITMO CREAZIONE AUTOMA RICONOSCITORE DELL'ESPRESSIONE
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
                            if (controllaNumeroOsservazioni == listaOsservazioni.length) {
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
                            if (i > 0) {
                                // se si collego l'ultimo stato fuori parentesi
                                int temp = i - 1;
                                if (listaOsservazioni[temp].contains(Parametri.PARENTESI_TONDA_C) && listaOsservazioni[temp].contains(Parametri.ASTERISCO)) {
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
                    if (!singolaOsservazione.contains(Parametri.ASTERISCO) && !singolaOsservazione.contains(Parametri.PIU)) {
                        nomeStatoDFA++;
                        statoDaAggiungere = new StatoDFA(String.valueOf(nomeStatoDFA), singolaOsservazione);
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
                        if (statoPrecedenteOpzionale != null) {
                            automaRiconoscitore.aggiungiLato(statoPrecedenteOpzionale, statoDaAggiungere);
                            statoPrecedenteOpzionale = null;
                        }
                    }
                }
            }
        } else {
            // ALGORITMO LINEARE DELLE TRANSIZIONI
            StatoDFA statoPrecedente = root;
            for (int i = 0; i < listaOsservazioni.length; i++) {
                String singolaOsservazione = listaOsservazioni[i];
                nomeStatoDFA++;
                StatoDFA statoDaAggiungere = new StatoDFA(String.valueOf(nomeStatoDFA), singolaOsservazione);
                automaRiconoscitore.aggiungiVertice(statoDaAggiungere);
                automaRiconoscitore.aggiungiLato(statoPrecedente, statoDaAggiungere);
                int controlloUltimaOsservazione = i + 1;
                if (controlloUltimaOsservazione == listaOsservazioni.length) {
                    // se è l'ultimo della lista deve essere finale
                    statoDaAggiungere.setIsFinale(true);
                }
                statoPrecedente = statoDaAggiungere;
            }
        }

        System.out.println(automaRiconoscitore.toStringAutomaRiconoscitore());
        return automaRiconoscitore;
    }

    static SpazioComportamentale creaDizionarioParzialeVincolato(Rete rete, String osservazione) {
        // parte relativa alla creazione dell'automa riconoscitore
        SpazioComportamentale automaRiconoscitore = creaRiconoscitoreEspressione(rete, osservazione);

        //TODO Alby
        SpazioComportamentale spazioComportamentaleParzialeVincolato = creaSpazioComportamentaleParzialeVincolato(rete, automaRiconoscitore);
        SpazioComportamentale dizionarioParzialeVincolato = null;
        if (spazioComportamentaleParzialeVincolato != null) {
            dizionarioParzialeVincolato = creaDizionario(rete, spazioComportamentaleParzialeVincolato);
            rete.setDizionarioParziale(dizionarioParzialeVincolato);
        }
        return dizionarioParzialeVincolato;
    }

    private static SpazioComportamentale creaSpazioComportamentaleParzialeVincolato(Rete rete, SpazioComportamentale automaRiconoscitore) {

        List<Cammino> cammini;
        List<StatoReteAbstract> statiSpazioC = new ArrayList<>();
        SpazioComportamentale spazioComportamentaleParzialeDecorato = null;
        cammini = trovaCamminiParzialiVincolati(rete, statiSpazioC, automaRiconoscitore);

        View.stampaCammini(cammini);
//        List<Cammino> traiettorie = potatura2(cammini);
//        View.stampaTraiettorie(traiettorie);
//        traiettorie = ridenominazione(traiettorie, statiSpazioC);
//        View.stampaTraiettorie(traiettorie);
        SpazioComportamentale spazioC = new SpazioComportamentale();
        spazioC = creaSpazioDaCammini(spazioC, cammini, statiSpazioC);
        spazioC = potatura3(spazioC, statiSpazioC);
        if (!spazioC.isEmpty()) {
            spazioC = numeraStati(spazioC, statiSpazioC);
            spazioC = ridenominaStati(spazioC);
//        statiSpazioC.get(17).setNome(Parametri.STATO_DECORATO_PREFISSO + Integer.toString(statiSpazioC.get(17).getNumero()));
//        statiSpazioC.set(17,new StatoReteRidenominato(statiSpazioC.get(17)));
//        spazioC = inserisciVerticiSpazioComportamentale(spazioC, traiettorie, statiSpazioC);
//        spazioC = inserisciLatiSpazioComportamentale(spazioC, traiettorie);
            spazioComportamentaleParzialeDecorato = creaSpazioComportamentaleDecorato(spazioC);

//        System.out.println(spazioC.toString());
//        SpazioComportamentale spazioComportamentaleDecorato = creaSpazioComportamentaleDecorato(spazioC);
//        stampaCammini(camminiDecorati);
            System.out.println(spazioComportamentaleParzialeDecorato.toString());
        } else {
            View.traiettorieNonTrovate();
        }
        return spazioComportamentaleParzialeDecorato;
    }

    private static List<Cammino> trovaCamminiParzialiVincolati(Rete rete, List<StatoReteAbstract> stati, SpazioComportamentale automaRiconoscitore) {
        //  TO-DO inserire l'automa riconoscitore 

        List<Cammino> cammini = new ArrayList<>();
        Stack<StatoRete> pilaStato = new Stack<>();//pila dei nuovi stati

        Stack<StatoRete> pilaDiramazioni = new Stack<StatoRete>();//pila degli stati che hanno più di una transizione in uscita

        //Stack<Cammino> pilaCammino = new Stack<>();
        Cammino camminoAttuale = creaNuovoCammino(rete);//il cammino attuale diventa un nuovo cammino con gli stati degli automi e i link azzerati
        int numeroStati = -1;
        StatoDFA statoRadiceRiconoscitore = (StatoDFA) automaRiconoscitore.getRoot();
        StatoRete statoRadice = creaStatoCorrente(rete, numeroStati);
        statoRadice.setStatoAutomaRiconoscitore(statoRadiceRiconoscitore);
        statoRadice.aggiungiStatoAutomaRinocitoreAllaDescrizione();
        pilaStato.push(statoRadice);

        while (!pilaStato.isEmpty()) {
            StatoRete statoAttuale = pilaStato.pop();
            StatoDFA statoAutomaRiconoscitoreAttuale = statoAttuale.getStatoAutomaRiconoscitore();
//            

            if (!stati.contains(statoAttuale)) {//se non è uno stato nuovo
                //statoAttuale.setNumero(stati.size());
                stati.add(statoAttuale);
                camminoAttuale.add(statoAttuale);
                //spazioC.aggiungiVertice(new StatoReteRidenominato(statoAttuale));
//                System.out.println(statoAttuale.toString());
                rete.setRete(statoAttuale);
                if (statoAttuale.isAbilitatoScenario(rete.getAutomi(), automaRiconoscitore.getVerticiAdiacenti(statoAutomaRiconoscitoreAttuale))) {
                    List<List<Transizione>> transizioniAbilitate = new ArrayList<List<Transizione>>(rete.getAutomi().size());
                    int numeroTransizioniAbilitate = 0;
                    for (int i = 0; i < rete.getAutomi().size(); i++) {//se nessun automa è già scattato, si itera su tutti gli automi                        
                        rete.getAutomi().get(i).isAbilitato(rete.getLink());
                        ArrayList<Transizione> transizioniAbilitateDaControllare = rete.getAutomi().get(i).getTransizioneAbilitata();
                        /*//controllo che le transizioni abilitate soddisfino l'automa riconoscitore
                        for ( int z=0; z < transizioniAbilitateDaControllare.size(); z++){
                            boolean alitata = transizioniAbilitateDaControllare.get(z).getOsservabilita()==null;
                            
                            
                        }*/
                        transizioniAbilitate.add(transizioniAbilitateDaControllare);//transizione abilitata diventa la transizione abilitata allo scatto nell'automa attuale                   
                        numeroTransizioniAbilitate += transizioniAbilitate.get(i).size();
                    }
                    ArrayList<StatoRete> statiDopoLoScatto = new ArrayList<>();
                    Transizione transizioneEseguita;
                    StatoRete copiaStatoAttuale;
                    if (numeroTransizioniAbilitate == 1) {
                        for (int i = 0; i < rete.getAutomi().size(); i++) {
                            if (rete.getAutomi().get(i).isAbilitato(rete.getLink())) {
                                //la prima e unica transizione abilitata allo scatto
                                Transizione transizioneDaEseguire = rete.getAutomi().get(i).getTransizioneAbilitata().get(0);
                                boolean abilitata = false;
                                StatoDFA statoAutomaRiconoscitoreDopoLoScatto = statoAutomaRiconoscitoreAttuale;
                                List<StatoInterface> statiAdiacentiAutomaRiconoscitore = automaRiconoscitore.getVerticiAdiacenti(statoAutomaRiconoscitoreAttuale);

                                for (int z = 0; z < statiAdiacentiAutomaRiconoscitore.size() && !abilitata; z++) {
                                    StatoDFA statoAutomaRiconoscitoreConsiderato = (StatoDFA) statiAdiacentiAutomaRiconoscitore.get(z);
                                    //controllo che il nome della transizione corrisponda a quello della transizione selezionata dall'automa
                                    if (transizioneDaEseguire.getDescrizione().equals(statoAutomaRiconoscitoreConsiderato.getOsservabilita())) {
                                        abilitata = true;
                                        statoAutomaRiconoscitoreDopoLoScatto = statoAutomaRiconoscitoreConsiderato;
                                    }
                                }
                                if (abilitata) {
                                    transizioneEseguita = rete.getAutomi().get(i).scatta(rete.getLink());//l'automa attuale viene fatto scattare e transizione eseguita diventa la transizione che è stata eseguita
//                                statoAttuale.setTransizionePrecedente(transizioneEseguita);//la transizione eseguita viene aggiunta allo StatoRete attuale
                                    StatoRete statoDopoLoScatto = creaStatoCorrente(rete.getAutomi(), rete.getLink(), numeroStati);
                                    statoDopoLoScatto.setTransizionePrecedente(transizioneEseguita);
                                    statoDopoLoScatto.setStatoAutomaRiconoscitore(statoAutomaRiconoscitoreDopoLoScatto);
                                    //lo stato dell'automa riconoscitore viene aggiunto al nome dello stato della rete
                                    statoDopoLoScatto.aggiungiStatoAutomaRinocitoreAllaDescrizione();
                                    statiDopoLoScatto.add(statoDopoLoScatto);
                                    rete.setRete(statoAttuale);
                                }
                            }
                        }
                    } else {
                        //Se sono abilitate piu' transizioni:
                        //Provare a sostituire con creaStatoCorrente(rete)
                        copiaStatoAttuale = creaStatoCorrente(rete.getAutomi(), rete.getLink(), numeroStati);
                        copiaStatoAttuale.setStatoAutomaRiconoscitore(statoAutomaRiconoscitoreAttuale);
                        copiaStatoAttuale.aggiungiStatoAutomaRinocitoreAllaDescrizione();

                        for (int i = 0; i < rete.getAutomi().size(); i++) {
                            for (int j = 0; j < transizioniAbilitate.get(i).size(); j++) {//vengono fatte scattare tutte, ognuna su un nuovo cammino
                                rete.setRete(statoAttuale);
                                Transizione transizioneDaEseguire = transizioniAbilitate.get(i).get(j);

                                boolean abilitata = false;
                                StatoDFA statoAutomaRiconoscitoreDopoLoScatto = statoAutomaRiconoscitoreAttuale;
                                List<StatoInterface> statiAdiacentiAutomaRiconoscitore = automaRiconoscitore.getVerticiAdiacenti(statoAutomaRiconoscitoreAttuale);

                                for (int z = 0; z < statiAdiacentiAutomaRiconoscitore.size() && !abilitata; z++) {
                                    StatoDFA statoAutomaRiconoscitoreConsiderato = (StatoDFA) statiAdiacentiAutomaRiconoscitore.get(z);
                                    if (transizioneDaEseguire.getDescrizione().equals(statoAutomaRiconoscitoreConsiderato.getOsservabilita())) {
                                        abilitata = true;
                                        statoAutomaRiconoscitoreDopoLoScatto = statoAutomaRiconoscitoreConsiderato;
                                    }
                                }

                                if (abilitata) {
                                    transizioneEseguita = rete.getAutomi().get(i).scatta(transizioneDaEseguire, rete.getLink());//viene fatta scattare la transizione da eseguire
                                    copiaStatoAttuale.setTransizionePrecedente(transizioneEseguita);
                                    StatoRete statoDopoLoScatto = creaStatoCorrente(rete.getAutomi(), rete.getLink(), numeroStati);
                                    statoDopoLoScatto.setTransizionePrecedente(transizioneEseguita);
                                    statoDopoLoScatto.setStatoAutomaRiconoscitore(statoAutomaRiconoscitoreDopoLoScatto);
                                    statoDopoLoScatto.aggiungiStatoAutomaRinocitoreAllaDescrizione();
                                    statiDopoLoScatto.add(statoDopoLoScatto);
                                    pilaDiramazioni.add(statoAttuale);
                                }
                            }
                        }
                        if (pilaDiramazioni.size() > 0) {
                            pilaDiramazioni.pop();
                        }
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
                //Il cammino si interrompe perche' e' stato trovato uno stato gia' visitato, quindi e' un cammino o ciclico
                //o che si ricongiunge nello stato di un altro cammino gia' trovato
                Cammino nuovoCammino = new Cammino();
//                statoAttuale.setNumero(stati.indexOf(statoAttuale));
                StatoRete statoPrecedente = (StatoRete) camminoAttuale.getUltimoStato();
                camminoAttuale.add(statoAttuale);//                
                nuovoCammino.copiaCammino(camminoAttuale);
                nuovoCammino.setIsCiclo(true);
                cammini.add(nuovoCammino);
                if (!pilaDiramazioni.isEmpty()) {
                    //gli ultimi elementi del cammini vengono rimossi finchè non si incontra il primo elemento della coda
                    camminoAttuale.togliFinoAPrimaDelloStato(pilaDiramazioni.pop());
                }

            }
        }
        rete.setCammini(cammini);
        return cammini;

    }

}
