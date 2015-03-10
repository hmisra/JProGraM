package datastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FactorOperations {

	//TODO change the function names to follow the standard
	/**
	 * @param a Factor 1
	 * @param b Factor 2
	 * @return product of Factor
	 * @author hmisra
	 * @throws Exception
	 * 
	 * create a new factor based on information from previous 2 factors.
	 * create map from Factor A to factor New
	 * create map from Factor B to Factor New
	 * find all possible combination of assignments in new Factor
	 * iterate through all the possible assignment and with the help of map extract the combination of factor A and B
	 * use assignment to index function to compute the index of A and B
	 * multiply the values of those indexes and assign it to the particular assignment.
	 */
	public static Factor FactorProduct(Factor a, Factor b) throws Exception {
		// create variables array from union of 2 variable array

		String [] variables = unionVariables(a.variables, b.variables);

		// find the mappings

		HashMap<Integer, Integer> mapA=new HashMap<Integer,Integer>();
		HashMap<Integer, Integer> mapB=new HashMap<Integer,Integer>();
		for(int i=0;i<a.variables.length;i++)
		{
			for(int j=0;j<variables.length;j++)
			{
				if(a.variables[i].equalsIgnoreCase(variables[j]))
				{
					mapA.put(j, i);
					break;
				}
			}
		}
		for(int i=0;i<b.variables.length;i++)
		{
			for(int j=0;j<variables.length;j++)
			{
				if(b.variables[i].equalsIgnoreCase(variables[j]))
				{
					mapB.put(j, i);
					break;
				}
			}
		}//can be improved/reduced using reverse logic iterate over new variable array and then in inner loop check for A and B 
		//using mappings find the values of domain in the new array

		int[] domain=new int[variables.length];
		for(int i=0;i<variables.length;i++)
		{
			if(mapA.containsKey(i))
			{
				domain[i]=a.domain[mapA.get(i)];
			}
			else
			{
				domain[i]=b.domain[mapB.get(i)];
			}	
		}


		//using the domain values calculate total values in the factor

		int totalValues=1;
		for(int i=0;i<domain.length;i++)
		{
			totalValues=totalValues* domain[i];
		}
		double[] values=new double[totalValues];


		//create template factor
		Factor newFactor=new Factor(new String().concat(a.name+"+"+b.name),values, variables, domain);

		//generate all possible assignments

		ArrayList<int[]> combinations=new ArrayList<int[]>();

		for(int i=0;i<newFactor.values.length;i++)
		{
			combinations.add(newFactor.indexToAssignment(i));
		}
		for(int j=0;j<combinations.size();j++)
		{
			int[] assignmentA=new int[a.variables.length];
			int[] assignmentB=new int[b.variables.length];
			for(int i=0;i<combinations.get(j).length;i++)
			{
				if(mapA.containsKey(i))
				{
					assignmentA[mapA.get(i)]=combinations.get(j)[i];
				}
				if(mapB.containsKey(i))
				{
					assignmentB[mapB.get(i)]=combinations.get(j)[i];
				}
			}
			newFactor.values[j]=a.values[a.assignmentToIndex(assignmentA)]*b.values[b.assignmentToIndex(assignmentB)];
		}

		//return the template factor to the parent method to fill in the values

		return newFactor;


	}
	public static Factor FactorMarginalization(Factor a, String var) throws Exception
	{
		if(Arrays.toString(a.variables).indexOf(var, 0)!=-1)
		{
			//find the index of variable to be removed
			int indexOfMarginalizedVariable=-1;
			for(int i=0;i<a.variables.length;i++)
			{
				if(a.variables[i].equalsIgnoreCase(var))
				{
					indexOfMarginalizedVariable=i;
					break;
				}
			}

			//create a mapping from new to old factor

			HashMap<Integer, Integer> map=new HashMap<Integer, Integer>();

			for(int i=0;i<a.variables.length-1;i++)
			{
				if(i>=indexOfMarginalizedVariable)
				{
					map.put(i, i+1);

				}
				else if(i<indexOfMarginalizedVariable)
				{
					map.put(i, i);
				}

			}
			//create variable arrays and domain arrays with all the variables and domains except that of the variable to be removed
			//create value array with the new domain
			String[] variables=new String[a.variables.length-1];
			int[] domain=new int[variables.length];
			int product =1;
			for(int i=0;i<variables.length;i++)
			{
				variables[i]=a.variables[map.get(i)];
				int temp=a.domain[map.get(i)];
				product=product*temp;
				domain[i]=temp;
			}
			double[] values=new double[product];

			//create template new factor using the above created arrays
			Factor newFactor=new Factor("MAR_"+var+"_"+a.name,values,variables,domain);
			//create assignment for the new factor
			ArrayList<int[]> combinations=new ArrayList<int[]>();

			for(int i=0;i<newFactor.values.length;i++)
			{
				combinations.add(newFactor.indexToAssignment(i));
			}

			//for each assignment in the new factor create n assignments for the old factor by adding all the possible values of the removed variables (using domain)

			for(int i=0;i<combinations.size();i++)
			{
				int[] oldAssignment=new int[a.variables.length];
				double valueForThisAssignment=0;
				for(int j=0;j<combinations.get(i).length;j++)
				{
					oldAssignment[map.get(j)]=combinations.get(i)[j];
				}
				//sum up the values of n assignments and assign it to the row of newFactor

				for(int k=0;k<a.domain[indexOfMarginalizedVariable];k++)
				{
					oldAssignment[indexOfMarginalizedVariable]=k;
					valueForThisAssignment=valueForThisAssignment+a.values[a.assignmentToIndex(oldAssignment)];
				}
				newFactor.values[i]=valueForThisAssignment;

			}
			return newFactor;

		}
		else
		{
			throw new Exception();
		}
	}
	/**
	 * @param a Factor to be Normalized
	 * @author hmisra
	 * Function to normalize the given factor
	 * 
	 * sum up all the values of Input Factors, values array.
	 * divide each value of the values array in the input factor by the value calculated above
	 * return the Normalized Factor
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static Factor FactorNormalization(Factor b) throws Exception
	{
		
		Factor a=createCopy(b);
		double sum=a.sumOfValues();
		//divide each value of the values array in the input factor by the value calculated above

		for(int i=0;i<a.values.length;i++)
		{
			a.values[i]=a.values[i]/sum;
		}
		a.name="Nor_"+a.name;
		return a;
		//return the Normalized Factor

	}
	public static Factor ObserveEvidence(Factor b, String var, int domainValue) throws Exception
	{
		Factor a=createCopy(b);
		//create 
		if(Arrays.toString(a.variables).indexOf(var, 0)!=-1)
		{
			//find the index of variable observed
			int indexOfMarginalizedVariable=-1;
			for(int i=0;i<a.variables.length;i++)
			{
				if(a.variables[i].equalsIgnoreCase(var))
				{
					indexOfMarginalizedVariable=i;
					break;
				}
			}
			//check if the observed value of the variable is in the domain of the variable
			if(domainValue>=a.domain[indexOfMarginalizedVariable]||domainValue<0)
			{
				throw new Exception("Value of the Observed Variable is out of scope of the variable");
			}
			else
			{
				//generate all assignment of the factor
				ArrayList<int[]> combinations=new ArrayList<int[]>();

				for(int i=0;i<a.values.length;i++)
				{
					combinations.add(a.indexToAssignment(i));
				}
				//select assignments where observed variables assignment is equal to the observed value and set it to 0
				for(int i=0;i<combinations.size();i++)
				{
					if(combinations.get(i)[indexOfMarginalizedVariable]!=domainValue)
					{
						a.values[a.assignmentToIndex(combinations.get(i))]=0;
					}
				}
				a.name=var+"="+domainValue+"_"+a.name;
				return a;
				
			}
		}
		else
		{
			throw new Exception("Variable not found");
		}


	}
	
	public static Factor JointDistribution(Factor[] factors) throws Exception
	{
		Factor f1=factors[0];
		for(int i=1;i<factors.length;i++)
		{
			f1=FactorProduct(f1, factors[i]);
		}
		return f1;
	}
	
	/**
	 * @param a Variable array1
	 * @param b Variable array2
	 * find union of 2 variable array
	 * @return
	 */
	private static String[] unionVariables(String[] a, String[] b) {
		ArrayList<String> tempArray=new ArrayList<String>();
		for(int i=0;i<a.length;i++)
		{
			tempArray.add(a[i]);
		}
		for(int i=0;i<b.length;i++)
		{
			if(!tempArray.contains(b[i]))
			{
				tempArray.add(b[i]);
			}
		}

		return tempArray.toArray(new String[tempArray.size()]);

	}
	
	private static Factor createCopy(Factor a) throws Exception
	{
		int[] domain=new int[a.domain.length];
		String[] variables=new String[a.variables.length];
		double[] values=new double[a.values.length];
		for(int i=0;i<domain.length;i++)
		{
			domain[i]=a.domain[i];
			variables[i]=a.variables[i];
		}
		for(int i=0;i<a.values.length;i++)
		{
			values[i]=a.values[i];
		}
		return new Factor(a.name, values, variables, domain);
		
	}
	


}
