/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.List;
import view.Parametri;

/**
 *
 * @author alber
 */
public class StatoFDA {
    private List<StatoReteRidenominato> stato;
    private String osservabilita;

    public StatoFDA(List<StatoReteRidenominato> stato, String osservabilita) {
        this.stato = stato;
        this.osservabilita = osservabilita;
    }
    
    
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(Parametri.PARENTESI_TONDA_A);
        for(StatoReteRidenominato statoRete: stato){
            s.append(statoRete.toString());
        }
        s.append(Parametri.PARENTESI_TONDA_C);
        
        
        return s.toString();
        
        
        
    }
    
    
    
}
