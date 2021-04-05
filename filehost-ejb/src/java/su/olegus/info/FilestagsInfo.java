/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.info;

import java.io.Serializable;

/**
 *
 * @author plintus
 */
public class FilestagsInfo implements Serializable{
    
    private Integer id;
    private Integer idfiles;
    private Integer idtags;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the idfiles
     */
    public Integer getIdfiles() {
        return idfiles;
    }

    /**
     * @param idfiles the idfiles to set
     */
    public void setIdfiles(Integer idfiles) {
        this.idfiles = idfiles;
    }

    /**
     * @return the idtags
     */
    public Integer getIdtags() {
        return idtags;
    }

    /**
     * @param idtags the idtags to set
     */
    public void setIdtags(Integer idtags) {
        this.idtags = idtags;
    }
}
