/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.ws;

import java.io.Serializable;

/**
 *
 * @author plintus
 */
public class UploadInfo implements Serializable{
    private String originalname;
    private String filename;
    private String description;
    private String captcha;

    /**
     * @return the originalname
     */
    public String getOriginalname() {
        return originalname;
    }

    /**
     * @param originalname the originalname to set
     */
    public void setOriginalname(String originalname) {
        this.originalname = originalname;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
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
     * @return the captcha
     */
    public String getCaptcha() {
        return captcha;
    }

    /**
     * @param captcha the captcha to set
     */
    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
