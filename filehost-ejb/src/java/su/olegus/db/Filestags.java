/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author plintus
 */
@Entity
@Table(name = "filestags", catalog = "filehost", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Filestags.findAll", query = "SELECT f FROM Filestags f")
    , @NamedQuery(name = "Filestags.findById", query = "SELECT f FROM Filestags f WHERE f.id = :id")
    , @NamedQuery(name = "Filestags.findByIdfiles", query = "SELECT f FROM Filestags f WHERE f.idfiles = :idfiles")
    , @NamedQuery(name = "Filestags.findByIdtags", query = "SELECT f FROM Filestags f WHERE f.idtags = :idtags")})
public class Filestags implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "idfiles")
    private Integer idfiles;
    @Column(name = "idtags")
    private Integer idtags;

    public Filestags() {
    }

    public Filestags(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdfiles() {
        return idfiles;
    }

    public void setIdfiles(Integer idfiles) {
        this.idfiles = idfiles;
    }

    public Integer getIdtags() {
        return idtags;
    }

    public void setIdtags(Integer idtags) {
        this.idtags = idtags;
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
        if (!(object instanceof Filestags)) {
            return false;
        }
        Filestags other = (Filestags) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "su.olegus.db.Filestags[ id=" + id + " ]";
    }
    
}
