/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

/**
 *
 * @author alex
 */
@Entity
public class PartidaJuego implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPartida;

    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "mail")
    private Usuari usuari;

    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    private int nivelDificultad;
    private int puntuacion;

    public PartidaJuego() {
    }

    public PartidaJuego(Usuari usuari, int nivelDificultad, int puntuacion) {
        this.usuari = usuari;
        this.nivelDificultad = nivelDificultad;
        this.puntuacion = puntuacion;
    }

    public int getIdPartida() {
        return idPartida;
    }

    @PrePersist
    public void prePersistDate() {
        fechaInicio = new Date(); // Establecer la fecha actual
    }

    public Usuari getUsuari() {
        return usuari;
    }

    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(int nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }
}
