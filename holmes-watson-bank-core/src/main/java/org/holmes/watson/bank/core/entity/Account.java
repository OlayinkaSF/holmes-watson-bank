/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Olayinka
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findByAccountnum", query = "SELECT a FROM Account a WHERE a.accountnum = :accountnum"),
    @NamedQuery(name = "Account.findByAccountbalance", query = "SELECT a FROM Account a WHERE a.accountbalance = :accountbalance"),
    @NamedQuery(name = "Account.findByStatus", query = "SELECT a FROM Account a WHERE a.status = :status")})
public class Account implements Serializable {

    public static final int STATUS_OPEN = 0;
    public static final int STATUS_CLOSED = 1;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    private String accountnum;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    private BigDecimal accountbalance;
    @Basic(optional = false)
    @NotNull
    private int status;
    @JoinColumn(name = "CLIENTID", referencedColumnName = "CLIENTID")
    @ManyToOne(optional = false)
    private Client clientid;

    public Account() {
    }

    public Account(String accountnum) {
        this.accountnum = accountnum;
    }

    public Account(String accountnum, BigDecimal accountbalance, int status) {
        this.accountnum = accountnum;
        this.accountbalance = accountbalance;
        this.status = status;
    }

    public String getAccountnum() {
        return accountnum;
    }

    public void setAccountnum(String accountnum) {
        this.accountnum = accountnum;
    }

    public BigDecimal getAccountbalance() {
        return accountbalance;
    }

    public void setAccountbalance(BigDecimal accountbalance) {
        this.accountbalance = accountbalance;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Client getClientid() {
        return clientid;
    }

    public void setClientid(Client clientid) {
        this.clientid = clientid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountnum != null ? accountnum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.accountnum == null && other.accountnum != null) || (this.accountnum != null && !this.accountnum.equals(other.accountnum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.holmes.watson.bank.core.entity.Account[ accountnum=" + accountnum + " ]";
    }

}
