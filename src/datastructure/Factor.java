package datastructure;

public class Factor {
	String name;
	public double []values = null;
	public String [] variables = null;
	public int [] domain=null;
	//TODO implement assignmentToIndex and indexToAssignment such that they can handle variables besides binary variables, current implementation only supports binary variables
	//TODO check the validity of the program after changing the code of above 2 functions
	public Factor(String name,double[] values, String[] variables, int [] domain) throws Exception
	{
		boolean check=sanityCheck(values, variables, domain);
		if (check)
		{
			this.name=name;
			this.values=values;
			this.variables=variables;
			this.domain=domain;
		}
		else
		{
			throw new Exception();
		}
	}


	private boolean sanityCheck(double[] values, String[] variables,
			int[] domain) {
		//check if each variable has a domain

		if(variables.length!=domain.length)
		{
			return false;
		}

//		// check if values are between 0 and 1
//
//		for(int i =0; i< values.length;i++)
//		{
//			if(values[i]<0 || values[i]>1)
//			{
//				return true;
//			}
//		}

		// check if total values are complete
		int size=1;
		for( int i=0;i<domain.length;i++)
		{
			size=size*domain[i];
		}

		if(size!=values.length)
		{
			return false;
		}
		
		
		//TODO
		//duplicate variable names not allowed
		
		//TODO
		//name must be unique
		
		

		return true;
	}
	
	public int assignmentToIndex(int [] assignment) throws Exception
	{
		if(assignment.length!=variables.length) throw new Exception();
		for(int i=0;i<assignment.length;i++)
		{
			if(assignment[i]>=domain[i]||assignment[i]<0) throw new Exception();
		}
		int index=recAssignmentToIndex(assignment,this.variables);
		return index;

	}

	private int recAssignmentToIndex(int[] assignment, String[] variables) {
		if(assignment.length==0)
		{
			return 0;
		}
		@SuppressWarnings("unused")
		int indexDomain=0;
		for(int i=0;i<this.variables.length;i++)
		{
			if(this.variables[i].equalsIgnoreCase(variables[0]))
			{
				indexDomain=i;
			}
		}

		//only works for binary variables for now.. to improve check notes the general logic.
		int []indexes=new int[(int) Math.pow(2, assignment.length-1)];
		int start=(int) (assignment[0]*Math.pow(2, (assignment.length-1)-0));
		//int end=(int) ((assignment[0]*Math.pow(this.domain[indexDomain], (assignment.length-1)-0)) + (Math.pow(2, (assignment.length-1)-0)-1));
		for (int i=0;i<indexes.length;i++)
		{
			indexes[i]=start;
			start++;
		}


		if(assignment.length==1)
		{
			return indexes[0];
		}
		else
		{
			return indexes[recAssignmentToIndex(reducedArray(assignment), reducedVariables(variables))];
		}

	}
	
	public int[] indexToAssignment(int i) throws Exception
	{
		if(i>values.length-1)
		{
			throw new Exception();
		}
		//logic only for binary variables
		String str=Integer.toBinaryString(i);
		int[] assignment=null;
		if(str.length()!=variables.length)
		{
//			System.out.println(str);
			assignment=new int[variables.length];
			for(int j=0;j<variables.length-str.length();j++)
			{
				assignment[j]=0;
				
			}
			for(int k=variables.length-str.length();k<variables.length;k++)
			{
				
				assignment[k]=Integer.parseInt(String.valueOf(str.charAt((k-(variables.length-str.length())))));
				//System.out.println(assignment[k]);
				
			}
			

			return assignment;
			
		}
		else
		{
			assignment=new int[variables.length];
			for(int k=0;k<str.length();k++)
			{
				assignment[k]=Integer.parseInt(str.substring(k, k+1));
				//System.out.print(assignment[k]);
			}

			return assignment;
		}

	}

	private int[] reducedArray(int [] array)
	{
		int [] newArray=new int[array.length-1];
		for(int i=1;i<array.length;i++)
		{
			newArray[i-1]=array[i];
		}
		return newArray;
	}
	
	private String[] reducedVariables(String[] array)
	{
		String [] newArray=new String[array.length-1];
		for(int i=1;i<array.length;i++)
		{
			newArray[i-1]=array[i];
		}
		return newArray;
	}
	
	public void printFactor()
	{
		System.out.println(" Factor : " + this.name);
		System.out.print(" Variables : " );
		for(int i=0;i<this.variables.length;i++)
		{
			System.out.print( " " + this.variables[i]+ " ");
		}
		System.out.println();
		System.out.print(" Domain : ");
		for(int i=0;i<this.domain.length;i++)
		{
			System.out.print( " " + this.domain[i]+ " ");
		}
		System.out.println();
		
		System.out.println(" Values : ");
		for(int i=0;i<this.values.length;i++)
		{
			System.out.println(" "+this.values[i]);
		}
		
		System.out.println("\n");
	}
	
	public double sumOfValues()
	{
		double sum=0.0;
		for(int i=0;i<this.values.length;i++)
		{
			sum=sum+this.values[i];
		}
		return sum;
	}

}
