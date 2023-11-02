package factory;

import Classes.SigninSignup;
import dao.ServerImplementation;

/**
 * The `ServerFactory` class is responsible for creating and providing instances
 * of the `SigninSignup` interface.
 * @author Iñigo
 */
public class ServerFactory {
	/**
	 * Instance of the `SigninSignup` interface.
	 */
    private static SigninSignup server;
		/**
		 * Check and return an instance of the `SigninSignup` interface.
		 * @return An instance of the `SigninSignup` interface.
		 */
		public static SigninSignup getServer() {
			//If there is no instance of the `SigninSignup` interface, create one.
			if (server == null) {
				server = (SigninSignup) new ServerImplementation();
			}
			//Return the instance of the `SigninSignup` interface.
			return server;
		} 
}
