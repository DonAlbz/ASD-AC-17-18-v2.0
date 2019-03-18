/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;



/**
 *
 * @author Alb
 */
public class Transizione {

    private int linkIn; //Link in ingresso
    private Evento eventoRichiesto; //evento richiesto sul link in ingresso
    private Evento[] linkOut; // eventi in uscita, la posizione di ciascun evento indica su quale link viene posizionato
    private String rilevanza;
    private String osservabilita;
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
    
    public Transizione(String s, Stato destinazione, int linkIn,
            Evento eventoRichiesto, Evento[] linkOut, String _rilevanza, String _osservabilita){
        this(s, destinazione, linkIn, eventoRichiesto, linkOut);
        rilevanza=_rilevanza;
        osservabilita=_osservabilita;
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
        StringBuilder s= new StringBuilder();
        s.append(descrizione);
//        s.append(" + ");
//        if(rilevanza!=null){
//            for(String e:rilevanza){
//                s.append(e);
//                s.append(" ");
//            }        
//        }
//         if(osservabilita!=null){
//             for(String e:osservabilita){
//                s.append(e);
//                s.append(" ");
//            }        
//         }        
        return s.toString();
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

    public String getRilevanza() {
        return rilevanza;
    }

    public String getOsservabilita() {
        return osservabilita;
    }
    
    
}
