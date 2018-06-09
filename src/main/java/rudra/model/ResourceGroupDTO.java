package rudra.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResourceGroupDTO {

	private String groupName;

	private String groupPath;
	
	@JsonIgnore
	private Date lastModified;
	
	private List<ResourceDTO> mediaDataList = new ArrayList<>();


	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupPath() {
		return groupPath;
	}

	public void setGroupPath(String groupPath) {
		this.groupPath = groupPath;
	}

	public List<ResourceDTO> getMediaDataList() {
		return mediaDataList;
	}

	public void setMediaDataList(List<ResourceDTO> mediaDataList) {
		this.mediaDataList = mediaDataList;
	}
	
	public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public void addMediaData( ResourceDTO mediaData ) {
		if ( mediaData == null ) return;
		
		mediaDataList.add( mediaData );
	}

	public void addMediaData( List<ResourceDTO> mediaData ) {
		if ( mediaData == null ) return;
		
		mediaDataList.addAll( mediaData );
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MediaGroup [groupName=").append(groupName).append(", groupPath=").append(groupPath).append("]");
		return builder.toString();
	}
}
