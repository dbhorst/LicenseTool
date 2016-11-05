package com.beckerhorst.db;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The {@link JpaRepository} for the {@link License} class.
 * 
 * @author dirk
 */
public interface LicenseRepository extends JpaRepository<License, Long> {

	/**
	 * Finds all licenses with the given license name.
	 * 
	 * @param licenseName
	 *            The license name
	 * @return List of licenses
	 */
	List<License> findByLicenseNameContainsIgnoreCase(String licenseName);

	/**
	 * Finds all licenses with the given license name and start date date.
	 * 
	 * @param licenseName
	 *            The license name
	 * @param expirationDateFrom
	 *            The start date
	 * @return List of licenses
	 */
	List<License> findByLicenseNameStartsWithIgnoreCaseAndExpirationDateGreaterThan(String licenseName,
			Calendar expirationDateFrom);

	/**
	 * Finds all licenses with the given license name end date.
	 * 
	 * @param licenseName
	 *            The license name
	 * @param expirationDateTo
	 *            The end date
	 * @return List of licenses
	 */
	List<License> findByLicenseNameStartsWithIgnoreCaseAndExpirationDateLessThan(String licenseName,
			Calendar expirationDateTo);

	/**
	 * Finds all licenses with the given license name between a start and end
	 * date.
	 * 
	 * @param licenseName
	 *            The license name
	 * @param expirationDateFrom
	 *            The start date
	 * @param expirationDateTo
	 *            The end date
	 * @return List of licenses
	 */
	List<License> findByLicenseNameStartsWithIgnoreCaseAndExpirationDateGreaterThanAndExpirationDateLessThan(
			String licenseName, Calendar expirationDateFrom, Calendar expirationDateTo);
}
