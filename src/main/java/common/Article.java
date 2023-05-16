/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.PositiveOrZero;

/**
 * 
 * @author manel
 */
@Entity
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String descripcio;
    
    @PositiveOrZero (message="El preu no pot ser un valor negatiu") //BEAN Validation: https://javaee.github.io/tutorial/bean-validation002.html
    private float preuEuros;

    public Article() {
    }

    public Article(Long id, String descripcio, float preuEuros) {
        this.id = id;
        this.descripcio = descripcio;
        this.preuEuros = preuEuros;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public float getPreuEuros() {
        return preuEuros;
    }

    public void setPreuEuros(float preuEuros) {
        this.preuEuros = preuEuros;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Article)) {
            return false;
        }
        Article other = (Article) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Article{id=").append(id);
        sb.append(", descripcio=").append(descripcio);
        sb.append(", preuEuros=").append(preuEuros);
        sb.append('}');
        return sb.toString();
    }
}
