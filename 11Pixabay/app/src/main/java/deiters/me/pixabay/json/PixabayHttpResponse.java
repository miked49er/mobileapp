
package deiters.me.pixabay.json;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PixabayHttpResponse {

    @SerializedName("totalHits")
    @Expose
    private long totalHits;
    @SerializedName("hits")
    @Expose
    private List<Hit> hits = null;
    @SerializedName("total")
    @Expose
    private long total;

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }

    public PixabayHttpResponse withTotalHits(long totalHits) {
        this.totalHits = totalHits;
        return this;
    }

    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    public PixabayHttpResponse withHits(List<Hit> hits) {
        this.hits = hits;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public PixabayHttpResponse withTotal(long total) {
        this.total = total;
        return this;
    }

}
