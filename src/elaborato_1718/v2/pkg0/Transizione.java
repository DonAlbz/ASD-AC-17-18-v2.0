/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elaborato_1718.v2.pkg0;

import java.util.Vector;

/**
 *
 * @author Alb
 */
public class Transizione {

    private int linkIn; //Link in ingresso
    private Evento eventoRichiesto; //evento richiesto sul link in ingresso
    private Evento[] linkOut; // eventi in uscita, la posizione di ciascun evento indica su quale link viene posizionato
    private Stato statoDestinazione;
    private String descrizione;

    /**
     *
     * @param s
     * @param destinazione
     * @param linkIn quale link viene "ascoltato", se non viene ascoltato alcun
     * link, linkIn=-1
     * @param eventoRichiesto evento richiesto sul link numero linkIn
     * @param linkOut array che ha la stessa dimensione dell'array Rete.link,
     * ogni elemento e' un evento o e' null. Se un elemento di linkOut non e'
     * null, allora rappresenta l'evento scatenato dalla transizione e da
     * posizionare sul link che ha per posizione, la posizione dell'elemento in
     * linkOut.
     *
     *
     */
    public Transizione(String s, Stato destinazione, int linkIn,
            Evento eventoRichiesto, Evento[] linkOut) {
        this.descrizione = s;
        this.statoDestinazione = destinazione;
        this.linkIn = linkIn;
        this.eventoRichiesto = eventoRichiesto;
        this.linkOut = linkOut;
    }

    boolean isAbilitato(Evento[] _link) {
        if (linkIn >= 0) {            
            if (_link[linkIn]!=eventoRichiesto) {
                return false;
            }
            /*
            else{
                Rete.setLink(linkIn, null);
            }
             */
        }
        if (linkOut != null) {
            for (int i = 0; i < linkOut.length; i++) {
                if (linkOut[i] != null) {
                    if (_link[i] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    Stato scatta(Evento[] _link) {
        if (linkOut != null) {
            for (int i = 0; i < linkOut.length; i++) {
                if (linkOut[i] != null) {
                    _link[i] = linkOut[i];
                }
            }
        }
        if (linkIn >= 0) {
            _link[linkIn] = null;
        }
        return statoDestinazione;
    }

    public String toString() {
        return descrizione;
    }

    public int getLinkIn() {
        return linkIn;
    }

    public Evento getEventoRichiesto() {
        return eventoRichiesto;
    }

    public Evento[] getLinkOut() {
        return linkOut;
    }

    public Stato getStatoDestinazione() {
        return statoDestinazione;
    }

    public String getDescrizione() {
        return descrizione;
    }
    
    
}
