package hwr.sem4.csa.comparators;

import hwr.sem4.csa.util.Dotos;

import java.util.Comparator;

public class DotosComparator implements Comparator {

    public int compare(Object obj1, Object obj2)
    {
        // Necessary Typecheck
        if(!(obj1 instanceof Dotos) || !(obj2 instanceof Dotos)) {
            System.out.println("Type mismatch while trying to compare");
            return 0;
        }

        // Necessary Typecast
        Dotos d1 = (Dotos) obj1;
        Dotos d2 = (Dotos) obj2;

        // Possible inequalities

        // Id
        if(d1.getId() != d2.getId()) {
            return d1.getId() - d2.getId();
        }

        // Title
        if(d1.getTitle() == null ^ d2.getTitle() == null) {
            return d1.getTitle() == null ? -1 : 1;
        }
        if(d1.getTitle() != null && !d1.getTitle().equals(d2.getTitle())) {
            return d1.getTitle().compareTo(d2.getTitle());
        }

        // Description
        if(d1.getDescription() == null ^ d2.getDescription() == null) {
            return d1.getDescription() == null ? -1 : 1;
        }
        if(d1.getDescription() != null && !d1.getDescription().equals(d2.getDescription())) {
            return d1.getDescription().compareTo(d2.getDescription());
        }

        // Value
        if(d1.getValue() != d2.getValue()) {
            return d1.getValue() - d2.getValue();
        }

        // Duration
        if(d1.getDuration() != d2.getDuration()) {
            return d1.getDuration() - d2.getDuration();
        }

        // AssignedBy
        if(d1.getAssignedBy() == null ^ d2.getAssignedBy() == null) {
            return d1.getAssignedBy() == null ? -1 : 1;
        }
        if(d1.getAssignedBy() != null && !d1.getAssignedBy().equals(d2.getAssignedBy())) {
            return d1.getAssignedBy().compareTo(d2.getAssignedBy());
        }

        // AssignedTo
        if(d1.getAssignedTo() == null ^ d2.getAssignedTo() == null) {
            return d1.getTitle() == null ? -1 : 1;
        }
        if(d1.getAssignedTo() != null && !d1.getAssignedTo().equals(d2.getAssignedTo())) {
            return d1.getAssignedTo().compareTo(d2.getAssignedTo());
        }

        return 0;
    }
}
