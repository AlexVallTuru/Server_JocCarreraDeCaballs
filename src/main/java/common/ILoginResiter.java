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

    public void addUsuari(String mail, String nick);
    
    public void getSessio(String mail);

}
