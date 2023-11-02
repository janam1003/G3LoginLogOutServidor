package factory;

import Classes.SigninSignup;
import dao.ServerImplementation;

/**
 *
 * @author Janam
 */
public class ServerFactory {

    // Static variable to store the SigninSignup instance.
    private static SigninSignup data;

    /**
     * Get an instance of the SigninSignup interface. If an instance does not
     * exist, it is created.
     *
     * @return An instance of the SigninSignup interface.
     */
    public static SigninSignup getSigninSignup() {

        // Check if the data instance is not previously loaded.
        if (data == null) {

            /**
             * If not loaded, create a new instance of SigninSignup using the
             * ServerImplementation class.
             */
            data = (SigninSignup) new ServerImplementation();

        }

        // Return the SigninSignup instance.
        return data;
    }

}
