package graph;

import java.util.ArrayList;
import java.util.List;

public class IndexDomainVertex {
    public List<Integer> domain;
    public int index;

    public IndexDomainVertex(IndexDomainVertex other) {
        this.domain = new ArrayList<>(other.domain);
        this.index = other.index;
    }

    public IndexDomainVertex(int index, List<Integer> domain) {
        this.domain = domain;
        this.index = index;
    }

    public int hashCode() {
        return index;
    }

    public String toString() {
        return String.format("%d: %s", index, domain);
    }

    public boolean equals(Object o) {
        return (o instanceof IndexDomainVertex) && (index == ((IndexDomainVertex) o).index);
    }

}
