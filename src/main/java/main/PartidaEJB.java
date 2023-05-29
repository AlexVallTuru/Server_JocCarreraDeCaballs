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
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import java.util.Random;
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
import common.EnumCartes.Tipus;
import common.ObjetoPartida;

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

    ArrayList<String> descarte = new ArrayList();

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
    public void aniadirPuntosPartida(int idPartida, int puntosPartida) throws PartidaException {

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
     * Limpia la lista de descartes.
     */
    @Override
    public void limpiaDescartes() {
        descarte.clear();
    }
    
    /**
     * Controla la logica pringipal del juego, sumando, restando y controlando
     * la pila de descartes. Finalmente devuelve el resultsdo al cliente.
     * 
     * @param puntosPartida
     * @param palo
     * @param dificultad
     * @return 
     */
    @Override
    public ObjetoPartida partidaLogica(int puntosPartida, String palo, int dificultad) {
        // Obtener valor aleatorio del enum
        boolean exit;
        Tipus randomTipus;
        ObjetoPartida objeto = new ObjetoPartida();
        log.log(Level.INFO, "Descartadas: {0}", descarte.size());
        if (descarte.size() == 40) { //Si se han descartado todas las cartas termina la partida
            log.log(Level.INFO, "Fin de la baraja");
            limpiaDescartes();
            objeto.setIsFinished(true);
        } else {
            do { // Obtiene una carta que no haya sido descartada
                randomTipus = getRandomTipus();
                exit = iteraDescarte(descarte, randomTipus.getImageName());
            } while (!exit);
            if (exit) {
                descarte.add(randomTipus.getImageName());
            }

            log.log(Level.INFO, "Valor del enum: {0}", randomTipus.getValue());
            log.log(Level.INFO, "Imatge: {0}", randomTipus.getImageName());
            log.log(Level.INFO, "Puntuaci\u00f3: {0}", randomTipus.getScore());
            if (randomTipus.getValue().equals(palo)) {
                // Asignamos los valores al objeto
                objeto.setImageName(randomTipus.getImageName());
                objeto.setMovimiento("+ " + randomTipus.getScore() + " puntos obtenidos!");
                objeto.setScore(randomTipus.getScore() + puntosPartida);
                objeto.setIsFinished(false);
            } else if (dificultad == 1) { // Factor de dificultad
                objeto.setScore(puntosPartida - 3);
                objeto.setMovimiento("La carta no és del palo correcto, te quitamos 3 puntos!");
                objeto.setImageName(randomTipus.getImageName());
                if (objeto.getScore() < 0) {
                    objeto.setScore(0);
                }
            } else {
                objeto.setImageName(randomTipus.getImageName());
                objeto.setScore(puntosPartida -2);
                if (objeto.getScore() < 0) {
                    objeto.setScore(0);
                }
                objeto.setMovimiento("La carta no és del palo correcto, te quitamos 2 puntos!");
                objeto.setIsFinished(false);
            }
            log.log(Level.INFO, "Puntuacio actual: {0}/38", puntosPartida);
            log.log(Level.INFO, "Cartas utilizadas: {0}/40", descarte.size());
            if (puntosPartida >= 38) { // Limpia la pila de descartes al final de la partida
                limpiaDescartes();
            }
        }
        return objeto;
    }

    /**
     * Itera la pila de descartes para confirmar si la carta ya ha salido en la
     * partida.
     *
     * @param descarte
     * @param carta
     * @return
     */
    private static boolean iteraDescarte(ArrayList<String> descarte, String carta) {
        Iterator<String> iterator = descarte.iterator();
        while (iterator.hasNext()) {
            if (carta.equals(iterator.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Obtiene una carta aleatoria del enum de la baraja.
     *
     * @return
     */
    private static Tipus getRandomTipus() {
        Tipus[] tipusValues = Tipus.values();
        int randomIndex = new Random().nextInt(tipusValues.length);
        return tipusValues[randomIndex];
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
             + "AND p.puntuacion > 0 "
             + "ORDER BY p.puntuacion DESC";

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
