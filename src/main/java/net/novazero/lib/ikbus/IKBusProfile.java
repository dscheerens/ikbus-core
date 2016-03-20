package net.novazero.lib.ikbus;

/**
 * Interface for classes that can provide an I/K-bus profile definition.
 * 
 * @author  Daan Scheerens
 */
public interface IKBusProfile {
	
	/**
	 * Retrieves the name of the I/K-bus profile.
	 * 
	 * @return  The name of the I/K-bus profile.
	 */
	String getProfileName();
	
	/**
	 * Retrieves the module description provider for the I/K-bus profile.
	 * 
	 * @return  The module description provider for the I/K-bus profile.
	 */
	IKBusModuleDescriptionProvider getModuleDescriptionProvider();
	
	/**
	 * Retrieves the message parser for the I/K-bus profile.
	 * 
	 * @return  The message parser for the I/K-bus profile.
	 */
	IKBusMessageParser getMessageParser();

}
