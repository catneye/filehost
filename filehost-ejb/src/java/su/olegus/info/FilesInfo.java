/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.info;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author plintus
 */
public class FilesInfo implements Serializable{
    
    private Integer id;
    private String filepath;
    private String name;
    private String realname;
    private String description;
    private String filemime;
    private Date adddate;

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
     * @return the filepath
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * @param filepath the filepath to set
     */
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the realname
     */
    public String getRealname() {
        return realname;
    }

    /**
     * @param realname the realname to set
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the filemime
     */
    public String getFilemime() {
        return filemime;
    }

    /**
     * @param filemime the filemime to set
     */
    public void setFilemime(String filemime) {
        this.filemime = filemime;
    }

    /**
     * @return the adddate
     */
    public Date getAdddate() {
        return adddate;
    }

    /**
     * @param adddate the adddate to set
     */
    public void setAdddate(Date adddate) {
        this.adddate = adddate;
    }
}
