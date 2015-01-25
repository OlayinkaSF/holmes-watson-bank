/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.core.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Olayinka
 */
@Entity
@Table(catalog = "", schema = "HOLMESWATSONHQ")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Agency.findAll", query = "SELECT a FROM Agency a"),
    @NamedQuery(name = "Agency.findByAgencyid", query = "SELECT a FROM Agency a WHERE a.agencyid = :agencyid"),
    @NamedQuery(name = "Agency.findByAddress", query = "SELECT a FROM Agency a WHERE a.address = :address")})
public class Agency implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(nullable = false, length = 3)
    private String agencyid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String address;

    private static Agency AGENCY;

    public static void setAgency(Agency AGENCY) {
        Agency.AGENCY = AGENCY;
    }

    public static Agency getAgency() {
        return AGENCY;
    }

    public Agency() {
    }

    public Agency(String agencyid) {
        this.agencyid = agencyid;
    }

    public Agency(String agencyid, String address) {
        this.agencyid = agencyid;
        this.address = address;
    }

    public String getAgencyid() {
        return agencyid;
    }

    public void setAgencyid(String agencyid) {
        this.agencyid = agencyid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (agencyid != null ? agencyid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Agency)) {
            return false;
        }
        Agency other = (Agency) object;
        if ((this.agencyid == null && other.agencyid != null) || (this.agencyid != null && !this.agencyid.equals(other.agencyid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.holmes.watson.bank.core.entity.Agency[ agencyid=" + agencyid + " ]";
    }

}
