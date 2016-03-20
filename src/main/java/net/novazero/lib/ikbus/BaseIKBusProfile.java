package net.novazero.lib.ikbus;

/**
 * Base implementation of an I/K-bus profile.
 * 
 * @author  Daan Scheerens
 */
public class BaseIKBusProfile implements IKBusProfile {
	
	/** Name of the I/K-bus profile. */
	private final String profileName;
	
	/** Module description provider for the I/K-bus profile. */
	private final IKBusModuleDescriptionProvider moduleDescriptionProvider;
	
	/** I/K-bus message parser for the profile. */
	private final IKBusMessageParser messageParser;
	
	/**
	 * Creates a new I/K-bus profile with the specified properties.
	 * 
	 * @param  profileName                Name of the I/K-bus profile.
	 * @param  moduleDescriptionProvider  Module description provider for the I/K-bus profile.
	 * @param  messageParser              I/K-bus message parser for the profile.
	 */
	public BaseIKBusProfile(String profileName, IKBusModuleDescriptionProvider moduleDescriptionProvider, IKBusMessageParser messageParser) {
		this.profileName = profileName;
		this.moduleDescriptionProvider = moduleDescriptionProvider;
		this.messageParser = messageParser;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProfileName() {
		return profileName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IKBusModuleDescriptionProvider getModuleDescriptionProvider() {
		return moduleDescriptionProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IKBusMessageParser getMessageParser() {
		return messageParser;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getProfileName();
	}
	
}
