package graph;

import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultEdge;

import java.util.List;

public class RelationshipEdge extends DefaultEdge {
    private String label;

    /**
     * Constructs a relationship edge
     *
     * @param label the label of the new edge.
     *
     */
    public RelationshipEdge(String label)
    {
        this.label = label;
    }

    /**
     * Gets the label associated with this edge.
     *
     * @return edge label
     */
    public String getLabel()
    {
        return label;
    }

//    @Override
//    public Integer getSource() {
//        Pair<Integer, List<Integer>> source = (Pair<Integer, List<Integer>>) super.getSource();
//        return source.getFirst();
//    }
//
//    @Override
//    public Integer getTarget() {
//        Pair<Integer, List<Integer>> target = (Pair<Integer, List<Integer>>) super.getTarget();
//        return target.getFirst();
//    }

    @Override
    public Integer getSource() {
        return ((IndexDomainVertex) super.getSource()).index;
    }

    @Override
    public Integer getTarget() {
        return ((IndexDomainVertex) super.getTarget()).index;
    }

    public List<Integer> getTargetDomain() {
        return ((IndexDomainVertex) super.getTarget()).domain;
    }

    public IndexDomainVertex getTargetObject() {
        return ((IndexDomainVertex) super.getTarget());
    }

    @Override
    public String toString()
    {
        return "(" + getSource() + " : " + getTarget() + " : " + label + ")";
    }
}
