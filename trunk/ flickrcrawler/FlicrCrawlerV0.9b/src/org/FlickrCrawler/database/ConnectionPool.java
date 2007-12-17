package org.FlickrCrawler.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provide a connection pool facilitate to allow
 * multiple threads to access a database server in parallel.
 * 
 * The usage convention is: 
 * 
 * 1. The main program construct and initialize the pool
 * 2. Each thread asks for a connection
 * 3. each thread uses its own connection
 * 4. each thread release the connection back to the pool.
 * 
 * @author elizeu
 *
 */
public class ConnectionPool {

	protected static final int DEFAULT_NUMBER_OF_CONNECTIONS = 15;
	
	String url;
	String user;
	String password;
	String driver;
	int numberOfConnections;
	
	Map<Connection, Boolean> pool;	
		
	public ConnectionPool(String _driverToUse, String _url, String _user, String _password, int _nConnections){
		
		this.driver = _driverToUse;
		this.url = _url;				
		this.user = _user;
		this.password = _password;
		this.numberOfConnections = _nConnections;
		
		this.pool = new HashMap<Connection,Boolean>();
		
		try {
			this.init();
		} catch (IllegalAccessException e) {
			
			System.out.println("IMPOSSIBLE TO CREATE THE POOL! ");
			e.printStackTrace();
		} catch (SQLException e) {
			
			System.out.println("IMPOSSIBLE TO CREATE CONNECTIONS! ");
			e.printStackTrace();
		}
	}

	private void init() throws IllegalAccessException, SQLException {
					
		Connection connection; 
		
		try {
			
			Class.forName(this.driver);
			
		} catch (Exception e) {
			throw new IllegalAccessException("ERROR: failed to load JDBC driver " + this.driver);
		}
		
		for (int i = 0; i < this.numberOfConnections; i++) {
			
			connection = this.createDBConnection();
			
			this.pool.put(connection, Boolean.TRUE);
		}
	}
	
	protected Connection createDBConnection() throws IllegalAccessException, SQLException {
		
		return DriverManager.getConnection(url, user, password);
	}

	public Connection getAvailableConnection() throws InterruptedException {
	
		Connection availableConnection = getConnectionIfAvailable();
		
		while ( availableConnection == null ) {
									
			availableConnection = getConnectionIfAvailable();
		}
		
		return availableConnection;
	}

	private Connection getConnectionIfAvailable() throws InterruptedException {		
										
		synchronized (this.pool) {
		
			for ( Connection connection : this.pool.keySet() ) {
									
				if ( pool.get(connection) ) {
				
					this.pool.put(connection, Boolean.FALSE);
														
					return connection;
				}
			}
			
			this.pool.wait();
		}
		
		return null;
	}

	public void releaseConnection(Connection _connection) throws InterruptedException {
		
		synchronized (this.pool) {
		
			this.pool.put(_connection, Boolean.TRUE);
			
			this.pool.notifyAll();
		}
	}

	public void shutdown() throws SQLException {

		for ( Connection connection : this.pool.keySet() ) {
			
			connection.close();
			
		}
		
	}
	
}
