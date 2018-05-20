package rudra.model;

public class ResourceDTO {

    private String name;
    private long size;
    private String url;
    private String thumbnail;

    public ResourceDTO(String name, long size, String url, String thumbnail) {
        this.name = name;
        this.url = url;
        this.size = size;
        this.thumbnail = thumbnail;
    }

    public ResourceDTO(String name, long size, String url) {
        this.name = name;
        this.url = url;
        this.size = size;
    }

    public ResourceDTO(String name, String url) {
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append( "ResourceDTO [name=" ).append( name ).append( ", size=" ).append( size ).append( ", url=" )
            .append( url ).append( ", thumbnail=" ).append( thumbnail ).append( "]" );
        return builder.toString();
    }

}
