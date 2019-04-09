/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;

/**
 *
 * @author Alb
 */
public class Evento implements Serializable{
    private String descrizione;
    
    public Evento(String descrizione){
        this.descrizione=descrizione;
    }
    
    public String toString(){
        return descrizione;
    }
    
    public boolean equals(Evento e2){
        return descrizione.equals(e2.getDescrizione());
    }

    public String getDescrizione() {
        return descrizione;
    }
    
    
}
