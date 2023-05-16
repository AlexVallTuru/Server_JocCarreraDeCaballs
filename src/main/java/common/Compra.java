/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

/**
 *
 * @author manel
 */
@Entity
public class Compra implements Serializable, Cloneable{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataCompra;
    
    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_login") //FK a la taula compra representada per un camp anomenat client_login. Si @JoinColumn no es defineix, Hibernate no toca la taula compra i genera una taula relacional idependent
    private Client client;
    
    private float total;
    
    @NotNull (message = "Una compra ha de tenir sempre un id de sessió")
    private String idSessio;
    
    @OneToMany (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_compra") //FK a la taula liniaCompra representada per un camp anomenat id_compra. Si @JoinColumn no es defineix, Hibernate no toca la taula compra i genera una taula relacional idependent
    private List<LiniaCompra> linies;

    public Compra() {
        this.linies = new ArrayList();
    }

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Date dataCompra) {
        this.dataCompra = dataCompra;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public List<LiniaCompra> getLinies() {
        return linies;
    }

    public void setLinies(List<LiniaCompra> linies) {
        this.linies = linies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    
    public String getIdSessio() {
        return idSessio;
    }

    public void setIdSessio(String idSessio) {
        this.idSessio = idSessio;
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
        if (!(object instanceof Compra)) {
            return false;
        }
        Compra other = (Compra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Compra{id=").append(id);
        sb.append(", dataCompra=").append(dataCompra);
        sb.append(", client=").append(client);
        sb.append(", total=").append(total);
        sb.append(", idSessio=").append(idSessio);
        sb.append(", linies=").append(linies);
        sb.append('}');
        return sb.toString();
    }
    
    /***
     * Retorna un clon de la instància
     * @return 
     */
    @Override
    public Object clone()
    {
        Compra ret = new Compra();
        
        ret.setId(this.getId());
        ret.setDataCompra(this.getDataCompra());
        ret.setIdSessio(this.getIdSessio());
        ret.setTotal(this.getTotal());
        ret.setClient((Client)this.getClient().clone());
        this.getLinies().forEach(x -> ret.getLinies().add((LiniaCompra)x.clone()));
        
        return ret;
    }
}
