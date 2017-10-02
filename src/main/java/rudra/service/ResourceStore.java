package rudra.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import rudra.model.ResourceGroupDTO;

public interface ResourceStore {

	List<String> loadClientNames();

	List<String> loadMainResourceGroupNames( String clientName );

	Collection<ResourceGroupDTO> loadSubResourceGroups( String clientName, String mainGroupName );

	ResourceGroupDTO loadResourceGroup( String clientName, String groupName );

	void loadResource( OutputStream out, String clientName, String resourceKey ) throws IOException;
}
