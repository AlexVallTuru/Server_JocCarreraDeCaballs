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


/**
 *
 * @author manel
 */
@Entity
public class LiniaCompra implements Serializable, Cloneable{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Long idArticle;
    
    private String descripcio;
    
    private float pvp;

    public LiniaCompra() {
    }

    public Long getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(Long idArticle) {
        this.idArticle = idArticle;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public float getPvp() {
        return pvp;
    }

    public void setPvp(float pvp) {
        this.pvp = pvp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    /***
     * Retorna un clon de la instància
     * @return 
     */
    @Override
    public Object clone()
    {
        LiniaCompra ret = new LiniaCompra();
        
        ret.setId(this.getId());
        ret.setDescripcio(this.getDescripcio());
        ret.setIdArticle(this.getIdArticle());
        ret.setPvp(this.getPvp());
        
        return ret;
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
        if (!(object instanceof LiniaCompra)) {
            return false;
        }
        LiniaCompra other = (LiniaCompra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LiniaCompra{id=").append(id);
        sb.append(", idArticle=").append(idArticle);
        sb.append(", descripcio=").append(descripcio);
        sb.append(", pvp=").append(pvp);
        sb.append('}');
        return sb.toString();
    }
}
