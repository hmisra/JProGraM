package datastructure;
public class FactorTest {
	public static void main(String[] args) throws Exception {
		//cpd_B=struct('var', [1], 'card', [2], 'val', [0.002, 0.998]);
		//cpd_A=struct('var',[1,2,3],'card',[3,2,2],'val',[0.93,0.6,0.85,0.002,0.07,0.4,0.15,0.998]);
		//cpd_J=struct('var',[4,3,6],'card',[2,2,2],'val',[0.45,0.88,0.03,0.02,0.55,0.12,0.97,0.98]);
		String name="Alarm";
		String [] variables={"chor","earthquake","alarm"};
		int [] domain={2,2,2};
		double [] value={0.93,0.6,0.85,0.002,0.07,0.4,0.15,0.998};
		Factor f1=new Factor(name,value, variables, domain);

		f1.printFactor();

		String name1="John";
		String [] variables1={"sleep","alarm","john"};
		int[] domain1={2,2,2};
		double [] value1={0.45,0.88,0.03,0.02,0.55,0.12,0.97,0.98};
		Factor f2=new Factor(name1,value1,variables1, domain1);

		f2.printFactor();


		Factor f3=FactorOperations.FactorProduct(f1, f2);
		f3.printFactor();



		Factor f4=FactorOperations.FactorNormalization(FactorOperations.FactorMarginalization(f1,"alarm"));

		f4.printFactor();
		
		
		Factor f5=FactorOperations.FactorNormalization(FactorOperations.FactorMarginalization(FactorOperations.ObserveEvidence(f1, "alarm", 1), "alarm"));
		f5.printFactor();
		Factor [] BN=new Factor[2];
		BN[0]=f1;
		BN[1]=f2;
		Factor f6=FactorOperations.FactorNormalization(FactorOperations.JointDistribution(BN));
		f6.printFactor();
		

	}
}

