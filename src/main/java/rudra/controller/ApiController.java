package rudra.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import rudra.model.ResourceGroupDTO;
import rudra.service.ResourceStore;

@RestController
public class ApiController {

	@Autowired
	private ResourceStore resourceStore;

	@RequestMapping("/api")
	public List<String> home() {
		return resourceStore.loadClientNames();
	}

	@RequestMapping(value = "/api/{clientName}", method = RequestMethod.GET)
	public @ResponseBody List<String> listMainGroups(@PathVariable String clientName) {
		return resourceStore.loadMainResourceGroupNames(clientName);
	}

	@RequestMapping(value = "/api/{clientName}/home", method = RequestMethod.GET)
	public @ResponseBody Collection<ResourceGroupDTO> listHomeMediaDetails(@PathVariable String clientName) {
		return resourceStore.loadSubResourceGroups(clientName, "Home");
	}

	@RequestMapping(value = "/api/{clientName}/other", method = RequestMethod.GET)
	public @ResponseBody ResourceGroupDTO mediaDataByType(@PathVariable String clientName, HttpServletRequest request) {
		String groupName = request.getParameter("groupName");
		return resourceStore.loadResourceGroup(clientName, groupName);
	}

	@RequestMapping(path = "/api/{clientName}/resource", method = RequestMethod.GET)
	public void getStaticData(@PathVariable String clientName, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String resourceKey = request.getParameter( "key" );
		resourceStore.loadResource( response.getOutputStream(), clientName, resourceKey);
		response.getOutputStream().close();
	}
}
