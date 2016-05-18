package aiproj.hexifence;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class MLModel {

	private Classifier readModel(String path) throws Exception{
		return (Classifier) weka.core.SerializationHelper.read(path);
	}
	
	public static void main(String[] args){
		MLModel m = new MLModel();
		try{
			File f = new File("nb.arff");
			System.out.println("heya");
			Classifier c = (Classifier) m.readModel("naiveBayesModel.model");
			System.out.println("HERE");
			Instance a = m.createInstance();
			System.out.println("HER");
			double prediction = c.classifyInstance(a);
			System.out.println("HE");
			
			String predict = ""+a.classAttribute().value((int) prediction);
			System.out.println(predict);
			
			//probabilities for each of the next moves
			double[] probPred = c.distributionForInstance(a);
			System.out.println("HEREeyy");
			System.out.println(" predicted: " + predict + " " + probPred[(int)prediction]);
			
		}
		catch(Exception e){
			System.out.println(e);
		}
	}


	private Instance createInstance(){
		String[] lines = {"0", "1", "1", "0", "0", "0", "0", "0", "0", "0",
						  "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						  "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						  };
		String[] hex = {"1", "1", "0", "0", "0", "0", "0"};
		
		ArrayList<Attribute> att = new ArrayList<Attribute>();
		List<String> attvalue = new ArrayList<String>();
		attvalue.add("0");
		attvalue.add("1");
		
		for (int i = 0; i < lines.length; i++){
			Attribute a = new Attribute("turn"+(i+1), attvalue);
			att.add(a);
		}
		
		List<String> hexValue = new ArrayList<String>();
		hexValue.add("0");
		hexValue.add("1");
		hexValue.add("2");
		hexValue.add("3");
		hexValue.add("4");
		hexValue.add("5");
		hexValue.add("6");
		
		for (int i = 0; i < hex.length; i++){
			Attribute b = new Attribute("hex"+(i+1), hexValue);
			att.add(b);
		}
		
		List<String> attClass = new ArrayList<String>();
		
		for (int i = 0; i < lines.length; i++){
			attClass.add(""+(i+1));
		}
		att.add(new Attribute("class", attClass));
		
		Instances structure = new Instances("game", att, 10);
		structure.setClassIndex(lines.length + hex.length);
		
		Instance newInst = new DenseInstance(structure.numAttributes());
		newInst.setDataset(structure);
		
		for (int i = 0; i < lines.length; i++){
			newInst.setValue(i, lines[i]);
		}
		
		for (int i = lines.length; i < lines.length + hex.length; i++){
			newInst.setValue(i, hex[i-lines.length]);
		}
		
		return newInst;
	}
}