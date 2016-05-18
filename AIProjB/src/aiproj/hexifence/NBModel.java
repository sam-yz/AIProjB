package aiproj.hexifence;

import weka.classifiers.Classifier;
import weka.core.Instance;

public class NBModel {

	Classifier model;
	
	NBModel(String path){
		try{
			this.model = (Classifier) weka.core.SerializationHelper.read(path);
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	private Instance createInstance(ArrayList<Move> m){
		
	}
	
	
	
}
