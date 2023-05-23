/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import common.IPartida;
import common.PartidaException;
import common.PartidaJuego;
import common.Usuari;
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
 *
 * @author alex
 */
@Stateless
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER) //Simply put, in container-managed concurrency, the container controls how clients' access to methods
@TransactionManagement(value = TransactionManagementType.BEAN)
public class PartidaEJB implements IPartida {

    @PersistenceContext(unitName = "Exemple1PersistenceUnit")
    private EntityManager em;

    @Inject
    private UserTransaction userTransaction;

    private static final Logger log = Logger.getLogger(PartidaEJB.class.getName());

    @Override
    @Lock(LockType.WRITE)
    public int createPartida(int Dificultad, String mail, String nick) {

        //True para realizar el persist a la base de datos
        Boolean persistBol = true;

        int idPartida = 0;

        Usuari usuari = new Usuari(mail, nick);

        try {
            PartidaJuego partidaJuego = new PartidaJuego();

            partidaJuego.setUsuari(usuari);
            partidaJuego.prePersistDate();
            partidaJuego.setNivelDificultad(Dificultad);

            persisteixAmbTransaccio(partidaJuego, persistBol);

            idPartida = partidaJuego.getIdPartida();

            return idPartida;

        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | PartidaException ex) {
            Logger.getLogger(PartidaEJB.class.getName()).log(Level.SEVERE, "Error al crear una patirtida, contacte con el administrado:\n", ex);
        } catch (Exception ex) {
            Logger.getLogger(PartidaEJB.class.getName()).log(Level.SEVERE, "Error al crear una patirtida, contacte con el administrado:\n", ex);
        }

        return idPartida;
    }

    @Override
    @Lock(LockType.WRITE)
    public void añadirPuntosPartida(int idPartida, int puntosPartida) throws PartidaException {

        //False para realizar el merge a la base de datos
        Boolean mergeBol = false;

        if (idPartida == 0) {
            String msg = "ERROR AL GENERAR LA PARTIDA.";
            log.log(Level.WARNING, msg);
            throw new PartidaException(msg);
        }
        PartidaJuego partidaJuego = em.find(PartidaJuego.class, idPartida);

        partidaJuego.setPuntuacion(puntosPartida);

        try {

            persisteixAmbTransaccio(partidaJuego, mergeBol);

        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException ex) {
            Logger.getLogger(PartidaEJB.class.getName()).log(Level.SEVERE, "NO SE CONSIGUE PERSISTIR LOS PUNTOS DE LA PARTIDA:", ex);
        }

    }

    /**
     * Función encargada de realizar el proceso de persistencia a la base de
     * datos con transacción o merge.
     *
     * @param partidaJuego La partida que se va a persistir.
     * @throws NotSupportedException
     * @throws SystemException
     * @throws RollbackException
     * @throws HeuristicMixedException
     * @throws HeuristicRollbackException
     * @throws PartidaException Excepción lanzada en caso de error durante la
     * persistencia.
     */
    private void persisteixAmbTransaccio(PartidaJuego partidaJuego, Boolean mergeOrPersist) throws PartidaException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        List<String> errors = Validadors.validaBean(partidaJuego);
        if (mergeOrPersist) {
            if (errors.isEmpty()) {
                try {

                    userTransaction.begin();
                    em.persist(partidaJuego);
                    userTransaction.commit();

                } catch (SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                    String msg = "Error al guardar la partida: " + ex.getMessage();
                    log.log(Level.INFO, msg);
                    throw new PartidaException(msg);
                }
            } else {
                String msg = "Errores de validación: " + errors.toString();
                log.log(Level.INFO, msg);
                throw new PartidaException(msg);
            }
        } else {
            if (errors.isEmpty()) {
                try {

                    userTransaction.begin();
                    em.merge(partidaJuego);
                    userTransaction.commit();

                } catch (SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                    String msg = "Error al guardar la partida: " + ex.getMessage();
                    log.log(Level.INFO, msg);
                    throw new PartidaException(msg);
                }
            } else {
                String msg = "Errores de validación: " + errors.toString();
                log.log(Level.INFO, msg);
                throw new PartidaException(msg);
            }
        }

    }

    /**
     * Metodo que realiza una consulta a la tabla de PartidaJuego i a la tabla
     * de Usuari,para obtener el hall of fame
     *
     * @return una lista de PartidaJuego
     * @throws PartidaException
     */
    @Override
    @Lock(LockType.READ)
    public List<PartidaJuego> ObtenerHallOfFame(int dificultad) throws PartidaException {
        List<PartidaJuego> ret = null;
        try {
            log.log(Level.INFO, "Retornando Hall of fame....");

            String query = "SELECT p.idPartida, p.fechaInicio, p.puntuacion, u.nick "
                    + "FROM PartidaJuego p "
                    + "INNER JOIN p.usuari u "
                    + "WHERE u.mail = p.usuari.mail "
                    + "AND p.nivelDificultad = " + dificultad
                    + " ORDER BY p.puntuacion DESC";

            List<Object[]> result = em.createQuery(query, Object[].class).getResultList();

            ret = new ArrayList<>();
            for (Object[] row : result) {
                PartidaJuego partida = new PartidaJuego();
                partida.setIdPartida((int) row[0]);
                partida.setFechaInicio((Date) row[1]);
                partida.setPuntuacion((int) row[2]);
                partida.setNick((String) row[3]);
                ret.add(partida);
            }
        } catch (Exception ex) {
            log.log(Level.INFO, "Error al hacer la consulta para extraer Hall of Fame");
            // Manejar la excepción o realizar alguna otra acción necesaria
        }

        return ret;
    }

}
