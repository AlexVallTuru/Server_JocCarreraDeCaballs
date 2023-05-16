/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import main.CarroCompraEJB;
import main.LoginRegisterEJB;
import main.TendaEJB;

/**
 * Classe encarregada de fer les connexions amb els EJB remots
 * @author manel
 */
public class Lookups {
    
    private static final String APP_VERSION = "1.0.0";
    
    private static final String wildFlyInitialContextFactory = "org.wildfly.naming.client.WildFlyInitialContextFactory";
    
    private static final String appName = "Server_JocCarreraDeCaballs-" + APP_VERSION;
    
    public static ICarroCompra carroCompraEJBRemoteLookup() throws NamingException
    {
        // "/EJB_Exemple1_Server-1/CarroCompraEJB!common.ICarroCompra?stateful"
        
        String strlookup = "ejb:/" + appName + "/" + CarroCompraEJB.class.getSimpleName() + "!" + ICarroCompra.class.getName()+"?stateful";
            
        Properties jndiProperties = new Properties();

        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,  wildFlyInitialContextFactory);
        
        Context context = new InitialContext(jndiProperties);

        return (ICarroCompra) context.lookup(strlookup);
    }
    
    public static ITenda tendaEJBRemoteLookup() throws NamingException
    {
        String strlookup = "ejb:/" + appName + "/" + TendaEJB.class.getSimpleName() + "!" + ITenda.class.getName();
            
        Properties jndiProperties = new Properties();

        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,  wildFlyInitialContextFactory);
        
        Context context = new InitialContext(jndiProperties);

        return (ITenda) context.lookup(strlookup);
    }
    
    /***
     * Connexió a un EJB amb @remote via local (entre components del mateix servidor)
     * @return
     * @throws NamingException 
     */
    public static ITenda tendaEJBLocalLookup() throws NamingException
    {
        String strlookup = "java:jboss/exported/" + appName + "/" + TendaEJB.class.getSimpleName() + "!" + ITenda.class.getName();
            
        Properties jndiProperties = new Properties();

        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,  wildFlyInitialContextFactory);
        
        Context context = new InitialContext(jndiProperties);

        return (ITenda) context.lookup(strlookup);
    }
    
        public static ILoginResiter LoginRegisterEJBRemoteLookup() throws NamingException
    {
        String strlookup = "ejb:/" + appName + "/" + LoginRegisterEJB.class.getSimpleName() + "!" + ILoginResiter.class.getName();
            
        Properties jndiProperties = new Properties();

        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,  wildFlyInitialContextFactory);
        
        Context context = new InitialContext(jndiProperties);

        return (ILoginResiter) context.lookup(strlookup);
    }
    
    /***
     * Connexió a un EJB amb @remote via local (entre components del mateix servidor)
     * @return
     * @throws NamingException 
     */
    public static ILoginResiter LoginRegisterEJBLocalLookup() throws NamingException
    {
        String strlookup = "java:jboss/exported/" + appName + "/" + LoginRegisterEJB.class.getSimpleName() + "!" + ILoginResiter.class.getName();
            
        Properties jndiProperties = new Properties();

        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,  wildFlyInitialContextFactory);
        
        Context context = new InitialContext(jndiProperties);

        return (ILoginResiter) context.lookup(strlookup);
    }
    
}
