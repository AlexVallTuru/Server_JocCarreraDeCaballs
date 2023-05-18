/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common;

import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author alex
 */
@Remote
public interface IPartida {

    //Actualment no retorna res, pero més endevant retornara la data de creació de la partida
    public int createPartida(int Dificultad,String mail,String nick) throws PartidaException;
    
    public void añadirPuntosPartida(int idPartida, int puntosPartida)throws PartidaException;
    
    public List<String> MostrarDatos() throws PartidaException;
}
