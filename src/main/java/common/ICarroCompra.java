/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author manel
 */
@Remote
public interface ICarroCompra {
    
    /***
     * Valida el client
     * @param login
     * @return id de sessió
     * @throws common.CompraException si el client no existetx
     */
    public String getSessio(String login) throws CompraException;
    
    /***
     * Tanca la sessió en curs
     * Si hi ha una compra en curs, es perd 
     */
    public void tancaSessio();
    
    /***
     * Afegeix un article a la compra
     * @param a 
     * @throws common.CompraException si el client no ha iniciat sessió correctament
     */
    public void addArticle(Article a) throws CompraException;
    
    /***
     * Finalitza i grava la compra
     * @return identificador de la compra
     * @throws common.CompraException si el client no ha iniciat sessió correctament
     */
    public long finalitzaCompra() throws CompraException;
    
    /***
     * Retorna al client un missatge de la cua de pendents o null si no hi ha més missatges pendents
     * @return 
     */
    public String getMessage();
    
    /***
     * Retorna el nombre de missatges pendents que hi ha a la cua
     * @return 
     */
    public int pendingMessages();

}
