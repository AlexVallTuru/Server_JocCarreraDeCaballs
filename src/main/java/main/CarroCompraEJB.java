package main;

import common.LiniaCompra;
import common.Client;
import common.CompraException;
import common.Article;
import common.Lookups;
import common.Compra;
import common.ICarroCompra;
import common.ITenda;
import java.security.Principal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/***
 * Classe Stateful, que manté l'estat de les dades entre diverses crides als seus mètodes.
 * @author manel
 */
@Stateful
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER) //Simply put, in container-managed concurrency, the container controls how clients' access to methods
@TransactionManagement(value=TransactionManagementType.CONTAINER) // o bé és el contenidor el que gestiona les transaccions a la BBDD o bé és el programador/a manualment
//@TransactionAttribute(TransactionAttributeType.REQUIRED) //https://gerardo.dev/ejb-basics.html
public class CarroCompraEJB implements ICarroCompra {
    
   @Resource
   private SessionContext sessionContext;    
   
   // tenim accès a la funcionalitat "injectada" al BEAN
   @Resource
   private EJBContext ejbContext;
   
   // Si utilitzem el entitymanager al llarg de més d'un mètode, 
   // la transacció es pot extendre al llarg de tot el BEAN i passar de mètode a mètode
   // Aquest paràmetre és necessàri per a un TransactionManagementType = CONTAINER
   // Per defecte es fa commit de la transacció, si s'ha fet un persist, al finalitzar cada mètode.
   // Es fa rollback si abans de sortir del mètode, es produeix una excepció
   @PersistenceContext(unitName = "Exemple1PersistenceUnit",type=PersistenceContextType.EXTENDED)
   private EntityManager em;
   
   // Injecció d'un EJB local. En aquest cas no cal fer lookup.
   @EJB
   AppSingletonEJB singleton;
   
   ITenda tenda;
   
   Compra compra;
   
   String idSessioCompra = null;
   
   Queue<String> missatges;
   
   private static final Logger log = Logger.getLogger(CarroCompraEJB.class.getName());
    
   @PostConstruct
   public void init() {
       
      log.log(Level.INFO, "Inicialitzant CarroCompraEJB: client={0} ; compra={1} ; idSessioCompra={2}; singletonEJB uptime={3}", new Object[]{sessionContext.getCallerPrincipal().getName(), this.compra, this.idSessioCompra, this.singleton.getUptimeUTC().toString()});
     
      try {
          
          log.log(Level.INFO, "Inicalitzant TendaEJB");
          
          missatges = new LinkedList<>();
          
          tenda = Lookups.tendaEJBRemoteLookup();
          
          Principal p = ejbContext.getCallerPrincipal();
          
          log.log(Level.INFO, "Usuari remot: {0}", p.getName());
          log.log(Level.INFO, "Hash: {0}", p.hashCode());
           
       } catch (NamingException ex) {
              log.log(Level.INFO, "ERROR connectant amb:  {0} : {1}", new Object[]{tenda.getClass(), ex.toString()});
       }
   }
   
   @Remove
   @Override
   public void tancaSessio() {
       
       log.log(Level.INFO, "Finalitzant sessió: " + this.idSessioCompra);
   }
   
   @PreDestroy
   public void destroy() {
     log.info("EJB finalitzant...");
   }

    @Override
    public String getSessio(String login) throws CompraException{ 
        
         if (login == null || login.isBlank() || login.isEmpty())
         {
            String msg = "El format del id de client no és vàlid:";
            log.log(Level.WARNING, msg);
            throw new CompraException(msg);
         }
         
         Client c = em.find(Client.class, login);

         if (c == null)
         {
            String msg = "Client no identificat: " + login + ".Impossible obtenir sessió.";
            log.log(Level.WARNING, msg);
            throw new CompraException(msg);
         }

        this.compra = new Compra();

        this.compra.setDataCompra(new Date());

        this.compra.setClient(c);

        this.idSessioCompra =  UUID.randomUUID().toString();

        this.compra.setIdSessio(this.idSessioCompra);

        log.log(Level.INFO, "Creada nova llista de la compra amb id de sessio: {0}", this.idSessioCompra);
        
        return this.idSessioCompra;
    }

    @Override
    public void addArticle(Article a) throws CompraException {

        // verifiquem article
        if (em.find(Article.class, a.getId()) == null)
        {
            String msg = "Article " + a.getId() + " no existeix";
            log.log(Level.WARNING, msg);
            throw new CompraException(msg);
        }
        
        if (this.idSessioCompra != null)
        {
            LiniaCompra l = new LiniaCompra();

            l.setIdArticle(a.getId());
            
            l.setDescripcio(a.getDescripcio());
            
            l.setPvp(a.getPreuEuros());

            log.log(Level.INFO, "Afegint linia de compra: {0}", l);
            
            this.compra.getLinies().add(l);
            
        } else
        {
            String msg = "Sessió no identificada";
            log.log(Level.WARNING, msg);
            throw new CompraException(msg);
        }

    }

    /***
     * Aquest mètode és l'unic que persisteix la compra. La resta treballen en memória.
     * Si falla alguna cosa durant la sessió client, no queda res desat.
     * @return
     * @throws CompraException 
     */
    @Override
    public long finalitzaCompra() throws CompraException {

        // TODO: aquí van les regles de negoci per a acceptar compra
        if (this.idSessioCompra != null)
        {

          log.log(Level.INFO, "Desant compra...");

          // calculem total de la compra
          float total = 0;
          for (LiniaCompra l : this.compra.getLinies()) total += l.getPvp();

          //desem
          this.compra.setTotal(total);

          log.log(Level.INFO, "Total compra: " + this.compra.getTotal());

          // persistim cadascuna de les linies
          this.compra.getLinies().forEach(x -> em.persist(x));

          // persistim la compra
          em.persist(this.compra);

          // si al sortir sortir del mètode volem que no quedi res persistit:
          // this.ejbContext.setRollbackOnly();

        } 
            else
        {
            String msg = "Sessió no identificada";
            log.log(Level.WARNING, msg);
            throw new CompraException(msg);
        }
        
       
        this.missatges.add("COMPRA " + this.compra.getId() + " finalizada. No oblidi consultar els seus punts-descompte !!");
        
        return this.compra.getId();
    }

    /***
     * Retorna el darrer missatge de la cua de missatges
     * @return 
     */
    @Override
    public String getMessage() {
        
        String ret = null;
        
        if (!this.missatges.isEmpty())
        {
            log.log(Level.FINER, "S'ha rebut una petició de missatge");
            ret = this.missatges.remove();
        }
        
        return ret;
    }

    /***
     * Retorna el nombre d'elements de la cua pendents de llegir
     * @return 
     */
    @Override
    public int pendingMessages() {
        log.log(Level.FINER, "S'ha rebut una consulta de missatges pendents");
        return this.missatges.size();
    }

    
    
    
}