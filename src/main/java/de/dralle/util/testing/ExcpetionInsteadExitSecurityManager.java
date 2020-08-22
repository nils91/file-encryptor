/**
 * 
 */
package de.dralle.util.testing;

import java.security.Permission;

/**
 * @author Nils Dralle
 *
 */
public class ExcpetionInsteadExitSecurityManager extends SecurityManager {

	@Override
	public void checkPermission(Permission perm) {// allow anything
	}

	@Override
	public void checkPermission(Permission perm, Object context) {// allow anything
	}

	@Override
	public void checkExit(int status) {
		// TODO Auto-generated method stub
		super.checkExit(status);
		throw new ExitException(status);
	}

}
