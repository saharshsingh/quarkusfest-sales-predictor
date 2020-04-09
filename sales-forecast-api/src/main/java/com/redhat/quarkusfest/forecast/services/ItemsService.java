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
public class ItemsService {

	private final AgroalDataSource db;
	private final Cache cache = new Cache();

	@Inject
	public ItemsService(AgroalDataSource db) {
		this.db = db;
	}

	public List<Integer> getItemIDs() {

		if (cache.itemIDs != null) {
			return cache.itemIDs;
		}

		synchronized (cache) {
			if (cache.itemIDs == null) {
				cache.itemIDs = getItemIDsFromDatabase();
			}
		}
		return cache.itemIDs;
	}

	private List<Integer> getItemIDsFromDatabase() {

		final List<Integer> ids = new ArrayList<>();

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = db.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT DISTINCT item_nbr FROM `train` ORDER BY item_nbr ASC");

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

	private static class Cache {
		private List<Integer> itemIDs = null;
	}

}
