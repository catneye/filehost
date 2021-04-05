/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author plintus
 */
@Entity
@Table(name = "files", catalog = "filehost", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Files.findAll", query = "SELECT f FROM Files f")
    , @NamedQuery(name = "Files.findById", query = "SELECT f FROM Files f WHERE f.id = :id")
    , @NamedQuery(name = "Files.findByFilepath", query = "SELECT f FROM Files f WHERE f.filepath = :filepath")
    , @NamedQuery(name = "Files.findByName", query = "SELECT f FROM Files f WHERE f.name = :name")
    , @NamedQuery(name = "Files.findByRealname", query = "SELECT f FROM Files f WHERE f.realname = :realname")
    , @NamedQuery(name = "Files.findByDescription", query = "SELECT f FROM Files f WHERE f.description = :description")
    , @NamedQuery(name = "Files.findByFilemime", query = "SELECT f FROM Files f WHERE f.filemime = :filemime")
    , @NamedQuery(name = "Files.findByAdddate", query = "SELECT f FROM Files f WHERE f.adddate = :adddate")
    , @NamedQuery(name = "Files.findByBeforeDate", query = "SELECT f FROM Files f WHERE f.adddate <= :adddate order by f.adddate DESC")})
public class Files implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "adddate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date adddate;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Size(max = 1024)
    @Column(name = "filepath", length = 1024)
    private String filepath;
    @Size(max = 256)
    @Column(name = "name", length = 256)
    private String name;
    @Size(max = 256)
    @Column(name = "realname", length = 256)
    private String realname;
    @Size(max = 1024)
    @Column(name = "description", length = 1024)
    private String description;
    @Size(max = 256)
    @Column(name = "filemime", length = 256)
    private String filemime;

    public Files() {
    }

    public Files(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilemime() {
        return filemime;
    }

    public void setFilemime(String filemime) {
        this.filemime = filemime;
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
        if (!(object instanceof Files)) {
            return false;
        }
        Files other = (Files) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "su.olegus.db.Files[ id=" + id + " ]";
    }

    public Date getAdddate() {
        return adddate;
    }

    public void setAdddate(Date adddate) {
        this.adddate = adddate;
    }

}
