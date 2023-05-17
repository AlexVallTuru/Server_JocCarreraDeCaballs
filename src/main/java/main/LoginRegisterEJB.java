package main;

import common.LiniaCompra;
import common.Client;
import common.CompraException;
import common.Article;
import common.Lookups;
import common.Compra;
import common.ICarroCompra;
import common.ILoginResiter;
import common.ITenda;
import common.Usuari;
import common.UsuariException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

@Stateless
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER) //Simply put, in container-managed concurrency, the container controls how clients' access to methods
@TransactionManagement(value = TransactionManagementType.BEAN)
public class LoginRegisterEJB implements ILoginResiter {

    @PersistenceContext(unitName = "Exemple1PersistenceUnit")
    private EntityManager em;

    @Inject
    private UserTransaction userTransaction;

    private static final Logger log = Logger.getLogger(TendaEJB.class.getName());

    @Override
    public void getSessio(String mail) throws UsuariException {
        if (mail == null || mail.isBlank() || mail.isEmpty()) {
            String msg = "El mail no es valid: ";
            log.log(Level.WARNING, msg);
            throw new UsuariException(msg);
        }

        Usuari user = em.find(Usuari.class, mail);

        if (user == null) {
            String msg = "Client no identificat: " + user + ". Impossible obtenir sessió.";
            log.log(Level.WARNING, msg);
            throw new UsuariException(msg);
        } else {
            SingletonUsuari singletonUser = SingletonUsuari.getInstance(user.getMail(), user.getNick());
        }
    }

    @Override
    @Lock(LockType.WRITE)
    public void addUsuari(String email, String nick) throws UsuariException {
        Usuari user = new Usuari();
        user.setMail(email);
        user.setNick(nick);
        SingletonUsuari singletonUser = SingletonUsuari.getInstance(user.getMail(), user.getNick());

        try {
            persisteixAmbTransaccio(user);

        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error al persistir el usuario", ex);
        }
    }

    private void persisteixAmbTransaccio(Object ob) throws CompraException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        List<String> errors = Validadors.validaBean(ob);

        userTransaction.begin();
        em.persist(ob);
        userTransaction.commit();

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
    }

    @Override
    public boolean validaUsuariExistent(String mail, String nick) throws UsuariException {
        try {
            if (mail != null && !mail.isBlank() && !mail.isEmpty()) {
                Usuari user = em.find(Usuari.class, mail);
                if (user != null && user.getMail() != null && !user.getMail().isBlank() && !user.getMail().isEmpty()
                        && user.getNick() != null && !user.getNick().isBlank() && !user.getNick().isEmpty()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            String msg = "Error al validar el usuario existente: " + e.getMessage();
            log.log(Level.WARNING, msg);
            throw new UsuariException(msg);
        }
    }
}
