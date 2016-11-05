package com.beckerhorst.db;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.commons.lang3.time.DateUtils;

/**
 * @author dirk This class represents the entity of a license
 */
@Entity
public class License {
	@Id
	@GeneratedValue
	@Column(name = "License_Id")
	private Long id;

	@Column(name = "License_name")
	private String licenseName;

	@Column(name = "Purchase_date")
	private Calendar purchaseDate;

	@Transient
	private Date purchaseDateAsDate;

	@Column(name = "Renewal_date")
	private Calendar renewalDate;

	@Transient
	private Date renewalDateAsDate;

	@Column(name = "Expiration_date")
	private Calendar expirationDate;

	@Transient
	private Date expirationDateAsDate;

	/**
	 * Protected Constructor
	 */
	protected License() {

	}

	/**
	 * Public Constructor with name and purchase date.
	 * 
	 * @param licenseName
	 *            Name of the License
	 * @param purchaseDate
	 *            Date of purchase
	 */
	public License(String licenseName, Calendar purchaseDate) {
		this.licenseName = licenseName;
		this.purchaseDate = purchaseDate;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getLicenseName() {
		return licenseName;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	/**
	 * @return the purchaseDate
	 */
	public Calendar getPurchaseDate() {
		return purchaseDate;
	}

	/**
	 * @param purchaseDate
	 *            the purchaseDate to set
	 */
	public void setPurchaseDate(Calendar purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	/**
	 * @return the renewalDate
	 */
	public Calendar getRenewalDate() {
		return renewalDate;
	}

	/**
	 * @param renewalDate
	 *            the renewalDate to set
	 */
	public void setRenewalDate(Calendar renewalDate) {
		this.renewalDate = renewalDate;
	}

	/**
	 * @return the expirationDate
	 */
	public Calendar getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate
	 *            the expirationDate to set
	 */
	public void setExpirationDate(Calendar expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the purchaseDateAsDate
	 */
	public Date getPurchaseDateAsDate() {
		if (purchaseDate != null) {
			purchaseDateAsDate = purchaseDate.getTime();
		}
		return purchaseDateAsDate;
	}

	/**
	 * @param purchaseDateAsDate
	 *            the purchaseDateAsDate to set
	 */
	public void setPurchaseDateAsDate(Date purchaseDateAsDate) {
		this.purchaseDateAsDate = purchaseDateAsDate;
		this.purchaseDate = DateUtils.toCalendar(purchaseDateAsDate);
	}

	/**
	 * @return the renewalDateAsDate
	 */
	public Date getRenewalDateAsDate() {
		if (renewalDate != null) {
			renewalDateAsDate = renewalDate.getTime();
		}
		return renewalDateAsDate;
	}

	/**
	 * @param renewalDateAsDate
	 *            the renewalDateAsDate to set
	 */
	public void setRenewalDateAsDate(Date renewalDateAsDate) {
		this.renewalDateAsDate = renewalDateAsDate;
		this.renewalDate = DateUtils.toCalendar(renewalDateAsDate);
	}

	/**
	 * @return the expirationDateAsDate
	 */
	public Date getExpirationDateAsDate() {
		if (expirationDate != null) {
			expirationDateAsDate = expirationDate.getTime();
		}
		return expirationDateAsDate;
	}

	/**
	 * @param expirationDateAsDate
	 *            the expirationDateAsDate to set
	 */
	public void setExpirationDateAsDate(Date expirationDateAsDate) {
		this.expirationDateAsDate = expirationDateAsDate;
		this.expirationDate = DateUtils.toCalendar(expirationDateAsDate);
	}

	@Override
	public String toString() {
		return String.format("License[id=%d, name='%s', purchaseDate='%s']", id, licenseName, purchaseDate);
	}
}
