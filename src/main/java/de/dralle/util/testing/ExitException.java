/**
 * 
 */
package de.dralle.util.testing;

/**
 * @author Nils Dralle
 *
 */
public class ExitException extends SecurityException {
	private int status;

	public ExitException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExitException(int status) {
		super();
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
