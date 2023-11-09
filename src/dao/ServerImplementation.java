package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import Classes.SigninSignup;
import Classes.User;
import Exceptions.EmailAlreadyExistException;
import Exceptions.IncorrectLoginException;
import Exceptions.ServerErrorException;
import Exceptions.UnknownTypeException;
import java.util.logging.Logger;
import pool.Pool;

/**
 *
 * @author Iñigo
 */
public class ServerImplementation implements SigninSignup {

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

    /**
     * Logger object
     */
	  private static final Logger logger = Logger.getLogger(ServerImplementation.class.getName());
      
    @Override
    public User SignIn(User user) throws IncorrectLoginException, ServerErrorException, UnknownTypeException {
		try {
       logger.info("Validating user credentials for signIn.");
				//Establishing connection
				con = Pool.getConnection();
				//Defining query to check if user exists and password is correct
				final String checkUser = "SELECT ru.id, ru.password, rp.name FROM public.res_users AS ru JOIN public.res_partner AS rp ON ru.partner_id = rp.id WHERE ru.login = ?";
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
				} else {
					throw new IncorrectLoginException();
				}
				//Closing connection
				Pool.returnConnection(con);
				return user;
		} catch (IncorrectLoginException e) {
      logger.severe("Incorrect Login: " + e.getMessage());
			throw e;
		} catch (Exception e) {
      logger.severe("Error: " + e.getMessage());
			throw new ServerErrorException(e.getMessage());
		}
	}

	@Override
	public User signUp(User user) throws ServerErrorException, EmailAlreadyExistException, UnknownTypeException {
		try {
       logger.info("Validating user credentials for signUp.");
				//Establishing connection
				con = Pool.getConnection();
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
					if (rs.getString("id") != null)
						throw new EmailAlreadyExistException();
				}
				//Defining create or replace the procedure
				final String createOrReplaceProcedure = ResourceBundle.getBundle("config.config").getString("INSERTUSER");
				stmtCall = con.prepareCall(createOrReplaceProcedure);
				//Executing query
				stmtCall.execute();
				//Defining query to insert user
				final String insertUser = "CALL insert_res_partner_and_user(?, ?, ?, ?, ?, ?)";
				//Preparing statement
				stmtCall = con.prepareCall(insertUser);
				//Setting parameters
				stmtCall.setString(1, user.getName());
				stmtCall.setString(2, user.getMail());
				stmtCall.setString(3, user.getPhone());
				stmtCall.setString(4, user.getPassword());
				stmtCall.setString(5, user.getAddress());
				stmtCall.setInt(6, user.getZip());
				//Executing query
				stmtCall.execute();
				//Closing connection
				Pool.returnConnection(con);
				return user;
		} catch (EmailAlreadyExistException e) {
       logger.severe("Email Exists: " + e.getMessage());
			throw e;
		} catch (Exception e) {
        logger.severe("Error: " + e.getMessage());
			throw new ServerErrorException();
		}

  }

}
