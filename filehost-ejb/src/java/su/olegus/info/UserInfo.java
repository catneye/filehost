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
public class UserInfo implements Serializable {

    private Integer id;
    private String name;
    private String secret;
    private String newsecret;
    private String role;
    private String login;
    private String email;
    private String phone;
    private String capthca;

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
     * @return the secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * @param secret the secret to set
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the capthca
     */
    public String getCapthca() {
        return capthca;
    }

    /**
     * @param capthca the capthca to set
     */
    public void setCapthca(String capthca) {
        this.capthca = capthca;
    }

    /**
     * @return the newsecret
     */
    public String getNewsecret() {
        return newsecret;
    }

    /**
     * @param newsecret the newsecret to set
     */
    public void setNewsecret(String newsecret) {
        this.newsecret = newsecret;
    }
}
