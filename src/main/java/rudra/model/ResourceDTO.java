package rudra.model;

public class ResourceDTO {

	private String name;	
	private long size;
	private String url;

	public ResourceDTO( String name, long size, String url ) {
		this.name = name;
		this.url = url;
		this.size = size;
	}
	
	public ResourceDTO( String name, String url ) {
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MediaData [name=").append(name).append(", size=").append(size).append(", url=").append(url)
				.append("]");
		return builder.toString();
	}
	
	
}
