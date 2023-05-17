/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author alex
 */
@Entity
public class Usuari implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "El correo electrónico no es válido")
    private String mail;

    @NotNull(message = "El campo 'nick' no puede estar vacío")
    @Size(min = 3, message = "El campo 'nick' debe tener al menos 3 caracteres")
    @Column(unique = true)
    public String nick;

    public Usuari(String mail, String nick) {
        this.mail = mail;
        this.nick = nick;
    }

    public Usuari() {
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String toString() {
        return "Usuari{" + "mail=" + mail + ", nick=" + nick + '}';
    }

}
