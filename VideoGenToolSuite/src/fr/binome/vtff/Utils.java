package fr.binome.vtff;

import java.util.Collections;
import java.util.List;

public class Utils {

	public  boolean equalLists(List<String> a, List<String> b){     
	    // Check for sizes and nulls

	    if (a == null && b == null) return true;


	    if ((a == null && b!= null) || (a != null && b== null) || (a.size() != b.size()))
	    {
	        return false;
	    }

	    // Sort and compare the two lists          
	    Collections.sort(a);
	    Collections.sort(b);      
	    return a.equals(b);
	}
}
