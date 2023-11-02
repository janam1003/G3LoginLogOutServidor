package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mysql.cj.jdbc.CallableStatement;

import Classes.SigninSignup;
import Classes.User;
import Exceptions.EmailAlreadyExistException;
import Exceptions.IncorrectLoginException;
import Exceptions.MaxUserException;
import Exceptions.ServerErrorException;
import Exceptions.UnknownTypeException;
import pool.Pool;

/**
 *
 * @author Iñigo
 */
public class ServerImplementation implements SigninSignup{

	/**
	 * Connection to database
	 */
    private Connection con;
	/**
	 * Statement to execute query
	 */
    private PreparedStatement stmt;
	/**
	 * Statement to execute query
	 */
	private java.sql.CallableStatement stmtCall;
	/**
	 * Result of query
	 */
    private ResultSet rs;

    @Override
    public User SignIn(User user) throws IncorrectLoginException, ServerErrorException, UnknownTypeException, MaxUserException {
		try {
				//Establishing connection
				con = Pool.obteinConnection();
				//Defining query to check if user exists and password is correct
				final String checkUser = "SELECT id, password, name FROM public.res_users WHERE login = ?";
				//Preparing statement
				stmt = con.prepareStatement(checkUser);
				//Setting parameters
				stmt.setString(1, user.getMail());
				//Executing query
				rs = stmt.executeQuery();
				//Checking if user exists and password is correct
				if (rs.next()) {
					String id = rs.getString("id");
					String password = rs.getString("password");
					//Checking if password is correct and setting name
					if (id != null && password.equals(user.getPassword())) {
						user.setName(rs.getString("name"));
					} else {
						throw new IncorrectLoginException();
					}
				}
				//Closing connection
				Pool.freeConnection(con);
				return user;
		} catch (Exception e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public User signUp(User user) throws ServerErrorException, EmailAlreadyExistException, UnknownTypeException, MaxUserException {
		try {
				//Establishing connection
				con = Pool.obteinConnection();
				//Defining query to check if user exists
				final String checkLogin = "SELECT id FROM public.res_users WHERE login = ?";
				//Preparing statement
				stmt = con.prepareStatement(checkLogin);
				//Setting parameters
				stmt.setString(1, user.getMail());
				//Executing query
				rs = stmt.executeQuery();
		
				if (rs.next()) {
					//Checking if login already exists
					if (rs.getString("id") == null)
						throw new EmailAlreadyExistException();
				}
				//Defining query to insert user
				final String insertUser = "CALL insert_res_partner_and_user(?, ?, ?, ?, ?, ?)";
				//Preparing statement
				stmtCall = con.prepareCall(insertUser);
				//Setting parameters
				stmt.setString(1, user.getName());
				stmt.setString(2, user.getMail());
				stmt.setString(3, user.getPhone());
				stmt.setString(4, user.getPassword());
				stmt.setString(5, user.getAddress());
				stmt.setInt(6, user.getZip());
				//Executing query
				stmtCall.execute();
				//Checking if there was an exception
				stmtCall.getMoreResults();
				//Closing connection
				Pool.freeConnection(con);
				return user;
		} catch (Exception e) {
			throw new ServerErrorException();
		}
    }
    
}
