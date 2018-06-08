package hwr.sem4.csa.comparators;

import hwr.sem4.csa.util.Participator;

import java.util.Comparator;

public class ParticipatorFlattyComparator implements Comparator {

    public int compare(Object obj1, Object obj2)
    {
        // Necessary Typecheck
        if(!(obj1 instanceof Participator) || !(obj2 instanceof Participator)) {
            System.out.println("Type mismatch while trying to compare");
            return 0;
        }

        return ((Participator) obj2).getBalance() - ((Participator) obj1).getBalance();
    }
}
