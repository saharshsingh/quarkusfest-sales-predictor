package com.redhat.quarkusfest.forecast.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.agroal.api.AgroalDataSource;

@ApplicationScoped
public class StoresService {

	private final AgroalDataSource db;

	@Inject
	public StoresService(AgroalDataSource db) {
		this.db = db;
	}

	public List<Integer> getStoreIDs() {

		final List<Integer> ids = new ArrayList<>();

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = db.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT DISTINCT store_nbr FROM `key` ORDER BY store_nbr ASC");

			while (rs.next()) {
				ids.add(rs.getInt(1));
			}

		} catch (final SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException e) {
				throw new RuntimeException(e);
			}
		}

		return ids;

	}

}
