package net.novazero.lib.ikbus;

/**
 * Class used for representing the static descriptions of I/K-bus modules.
 * 
 * @author  Daan Scheerens
 */
public class IKBusModuleDescriptor {
	
	/** The module identifier/address. */
	private final byte id;
	
	/** Symbolic name of the module. */
	private final String code;
	
	/** Human readable name of the module. */
	private final String name;
	
	/**
	 * Creates a new IKBusModuleDescriptor with the specified properties.
	 * 
	 * @param  id    The module identifier/address.
	 * @param  code  Symbolic name of the module.
	 * @param  name  Human readable name of the module.
	 */
	public IKBusModuleDescriptor(byte id, String code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
	}
	
	/**
	 * Returns the module identifier/address.
	 * @return  The module identifier/address.
	 */
	public byte getId() {
		return id;
	}
	
	/**
	 * Returns the symbolic name of the module.
	 * 
	 * @return  Symbolic name of the module.
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Returns the human readable name of the module.
	 * 
	 * @return  Human readable name of the module.
	 */
	public String getName() {
		return name;
	}
}