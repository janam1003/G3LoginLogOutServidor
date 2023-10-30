package dao;

import Classes.SigninSignup;
import Classes.User;
import Exceptions.EmailAlreadyExistException;
import Exceptions.IncorrectLoginException;
import Exceptions.MaxUserException;
import Exceptions.ServerErrorException;
import Exceptions.UnknownTypeException;

/**
 *
 * @author Janam
 */
public class ServerImplementation implements SigninSignup{

    @Override
    public User SignIn(User user) throws IncorrectLoginException, ServerErrorException, UnknownTypeException, MaxUserException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User signUp(User user) throws ServerErrorException, EmailAlreadyExistException, UnknownTypeException, MaxUserException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
