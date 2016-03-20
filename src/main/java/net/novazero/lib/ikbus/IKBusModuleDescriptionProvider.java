package net.novazero.lib.ikbus;

import java.util.HashMap;
import java.util.Map;

/**
 * Database that can provide module descriptors for I/K-bus modules based on their identifier/address.
 * 
 * @author  Daan Scheerens
 */
public abstract class IKBusModuleDescriptionProvider {
	
	/** The mapping of module identifiers to module descriptors. */
	public final Map<Byte, IKBusModuleDescriptor> modules = new HashMap<>();
	
	/**
	 * Checks whether the module description provider recognizes the module with the specified identifier.
	 *  
	 * @param   moduleId  Identifier of the module for which is to be checked if it is recognized by the description provider.
	 * @return            True if the module was recognized, false if not.
	 */
	public boolean isRecognized(byte moduleId) {
		return modules.containsKey(modules);
	}
	
	/**
	 * Retrieves the symbolic name of the module with the specified identifier.
	 * 
	 * @param   moduleId  Identifier of the module for which the symbolic name is to be retrieved.
	 * @return            The symbolic name of the specified module or {@code null} if the module was not recognized.
	 */
	public String getModuleCode(byte moduleId) {
		IKBusModuleDescriptor module = modules.get(moduleId);
		return module == null ? null : module.getCode();
	}

	/**
	 * Retrieves the (human readable) name of the module with the specified identifier.
	 * 
	 * @param   moduleId  Identifier of the module for which the (human readable) name is to be retrieved.
	 * @return            The (human readable) name of the specified module or {@code null} if the module was not recognized.
	 */
	public String getModuleName(byte moduleId) {
		IKBusModuleDescriptor module = modules.get(moduleId);
		return module == null ? null : module.getName();
	}
	
	/**
	 * Retrieves the module descriptor for the module with the specified identifier.
	 * 
	 * @param   moduleId  Identifier of the module for which the module descriptor is to be retrieved.
	 * @return            The module descriptor of the specified module or {@code null} if the module was not recognized.
	 */
	public IKBusModuleDescriptor getModuleDescriptor(byte moduleId) {
		return modules.get(moduleId);
	}
	
	/**
	 * Registers a new module to the module description provider.
	 * 
	 * @param  id    The module identifier/address.
	 * @param  code  Symbolic name of the module.
	 * @param  name  Human readable name of the module.
	 */
	protected void registerModule(byte id, String code, String name) {
		modules.put(id, new IKBusModuleDescriptor(id, code, name));
	}
}
