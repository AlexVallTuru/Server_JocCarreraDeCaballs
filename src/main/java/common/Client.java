/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author manel
 */
@Entity
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(length = 10) //defineix el camp de la taula SQL (NVARCHAR)
    @Size(min = 3, max = 10, message = "La longitud ha de ser entre 3 i 10 caracters") //validació a nivell de Bean, que es pot gestionar a posteriori BEAN Validation: https://javaee.github.io/tutorial/bean-validation002.html
    @Pattern(regexp="^[a-zA-Z0-9_]+$", message = "Solament lletres numeros i guió baix")
    private String login;
    
    private String nom;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Client other = (Client) obj;
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        return true;
    }
    
    /***
     * Retorna un clon de la instància
     * @return 
     */
    @Override
    public Object clone()
    {
        Client ret = new Client();
        
        ret.setLogin(this.login);
        ret.setNom(this.nom); 
        
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Client{login=").append(login);
        sb.append(", nom=").append(nom);
        sb.append('}');
        return sb.toString();
    }

    
}
