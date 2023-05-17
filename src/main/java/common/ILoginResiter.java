/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common;

import javax.ejb.Remote;

/**
 *
 * @author alex
 */
@Remote
public interface ILoginResiter {

    public void addUsuari(String mail, String nick)throws UsuariException;

    public void getSessio(String mail)throws UsuariException;

    public boolean validaUsuariExistent(String mail,String nick)throws UsuariException;

}
