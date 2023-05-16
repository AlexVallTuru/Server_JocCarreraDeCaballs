package main;

import common.Client;
import common.CompraException;
import common.Article;
import common.Compra;
import common.ITenda;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * Classe @Stateless que ens proporciona mètodes que realitzen diverses funcions
 * Es comporta exactament igual que una classe amb mètodes estàtics. No permet
 * mantenir les dades entre una crida i la següent.
 */
// https://docs.wildfly.org/26/Developer_Guide.html// https://docs.wildfly.org/26/Developer_Guide.html
@Stateless
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER) //Simply put, in container-managed concurrency, the container controls how clients' access to methods
@TransactionManagement(value = TransactionManagementType.BEAN)
public class TendaEJB implements ITenda {

    @PersistenceContext(unitName = "Exemple1PersistenceUnit")
    private EntityManager em;

    @Inject
    private UserTransaction userTransaction;

    private static final Logger log = Logger.getLogger(TendaEJB.class.getName());

    @Override
    @Lock(LockType.READ)
    public List<Article> getArticles() throws CompraException {

        log.log(Level.INFO, "Retornant articles...");

        List<Article> ret = em.createQuery("SELECT a FROM Article a", Article.class).getResultList();

        return ret;
    }

    @Override
    @Lock(LockType.READ)
    public Compra getCompra(long id) throws CompraException {

        Compra ret = null;

        Compra c = em.find(Compra.class, id);

        if (c != null) {
            /*
            Problema: Si es vol retornar la compra amb una llista de tipus LiniesCompra, dona el següent error al client: ERROR: javax.ejb.EJBException: Failed to read response
            El problema està relacionat amb la deserialització de les dades al costat client (a la part del servidor tot funciona perfectament)
            Si el que s'ha de serialitzar és un sol objecte sense cap col.lecció, llavors funciona perfectament
            Si el que s'ha de serialitzar conté alhora una col.lecció, peta.
            Zona del client on peta: https://github.com/wildfly/jboss-ejb-client/blob/4.0/javax/src/main/java/org/jboss/ejb/protocol/remote/EJBClientChannel.java ---> ExceptionResultProducer o bé MethodCallResultProducer 
            Solució: clonem els objectes just abans d'enviar-los pel canal de sortida
            TODO: investigar si hi ha una solució que no passi per clonar*/

            //ret = clonaCompra(c);
            ret = (Compra) c.clone();

            log.log(Level.INFO, "Retornant compra {0}...", ret.toString());

        }

        return ret;
    }

    @Override
    @Lock(LockType.READ)
    public List<Compra> getCompra(String login, Date dataInicial, Date dataFinal) throws CompraException {

        List<Compra> ret = new ArrayList<>();

        if (login == null || dataInicial == null || dataFinal == null) {
            throw new CompraException("Dades de consulta no vàlides");
        }

        log.log(Level.INFO, "Retornant compres...");

        List<Compra> lc = em.createQuery("SELECT c FROM Compra c WHERE c.client.login = :p1 AND c.dataCompra BETWEEN :d1 AND :d2")
                .setParameter("p1", login)
                .setParameter("d1", dataInicial)
                .setParameter("d2", dataFinal)
                .getResultList();

        lc.forEach(x -> ret.add((Compra) x.clone()));

        return ret;

    }

    @Override
    @Lock(LockType.WRITE)
    public Long addArticle(String descripcio, float pvpEuros) throws CompraException {

        Article a = new Article();

        log.log(Level.INFO, "Desant article...");
        a.setDescripcio(descripcio);
        a.setPreuEuros(pvpEuros);

        a = (Article) persisteixAmbTransaccio(a);

        return a.getId();
    }

    @Override
    @Lock(LockType.WRITE)
    public void addClient(String login, String nom) throws CompraException {

        if (login == null || login.isBlank() || login.isEmpty()) {
            throw new CompraException("Client no vàlid");
        }

        Client c = new Client();

        c.setLogin(login);
        c.setNom(nom);

        log.log(Level.INFO, "Desant client id= {0} ...", c.getLogin());

        persisteixAmbTransaccio(c);
    }

    @Override
    @Lock(LockType.READ)
    public boolean validaClient(String login) throws CompraException {

        boolean ret;

        if (login == null || login.isBlank() || login.isEmpty()) {
            throw new CompraException("Id de client no vàlid");
        } else {
            ret = (em.find(Client.class, login) != null);
        }

        return ret;
    }

    /**
     * *
     * Valida regles de negoci anotades (veure anotacions al BEAN +
     * https://javaee.github.io/tutorial/bean-validation002.html) i controla
     * transacció
     *
     * @param ob
     * @return
     * @throws CompraException
     */
    private Object persisteixAmbTransaccio(Object ob) throws CompraException {
        List<String> errors = Validadors.validaBean(ob);

        if (errors.isEmpty()) {
            try {

                userTransaction.begin();
                em.persist(ob);
                userTransaction.commit();

            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                String msg = "Error desant: " + errors.toString();
                log.log(Level.INFO, msg);
                throw new CompraException(msg);
            }

        } else {
            String msg = "Errors de validació: " + errors.toString();
            log.log(Level.INFO, msg);
            throw new CompraException(msg);
        }

        return ob;
    }
}
