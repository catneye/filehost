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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author plintus
 */
@Entity
@Table(name = "tags", catalog = "filehost", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tags.findAll", query = "SELECT t FROM Tags t")
    , @NamedQuery(name = "Tags.findById", query = "SELECT t FROM Tags t WHERE t.id = :id")
    , @NamedQuery(name = "Tags.findByName", query = "SELECT t FROM Tags t WHERE t.name = :name")})
public class Tags implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Size(max = 256)
    @Column(name = "name", length = 256)
    private String name;

    public Tags() {
    }

    public Tags(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof Tags)) {
            return false;
        }
        Tags other = (Tags) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "su.olegus.db.Tags[ id=" + id + " ]";
    }
    
}
