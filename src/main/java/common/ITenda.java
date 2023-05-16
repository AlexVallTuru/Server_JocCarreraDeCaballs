/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author manel
 */
@Remote
public interface ITenda {
    
    /***
     * Retorna la llista d'articles disponibles
     * @return 
     * @throws common.CompraException 
     */
    public List<Article> getArticles() throws CompraException;
    
    /***
     * Retorna una compra pel seu Id
     * @param id
     * @return 
     * @throws common.CompraException 
     */
    public Compra getCompra(long id) throws CompraException;    
  
    
    /***
     * Retorna les compres d'un client entre dues dates
     * @param login
     * @param dataInicial
     * @param dataFinal
     * @return 
     * @throws common.CompraException 
     */
    public List<Compra> getCompra(String login, Date dataInicial, Date dataFinal) throws CompraException;
    
    /***
     * Dona d'alta un nou article
     * @param descripcio
     * @param pvpEuros
     * @return 
     * @throws common.CompraException 
     */
    public Long addArticle(String descripcio, float pvpEuros) throws CompraException;
    
    /***
     * Afegeix un nou client
     * @param login
     * @param nom
     * @throws common.CompraException
     */
    public void addClient(String emailField, String nom) throws CompraException;
    
    /***
     * Valida un client pel seu login
     * @param login
     * @return 
     * @throws common.CompraException 
     */
    public boolean validaClient(String login) throws CompraException;
}
