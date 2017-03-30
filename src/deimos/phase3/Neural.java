package deimos.phase3;

import java.sql.Connection;

/*
 * Encog(tm) Java Examples v3.3
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-examples
 *
 * Copyright 2008-2014 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

/**
 * XOR: This example is essentially the "Hello World" of neural network
 * programming.  This example shows how to construct an Encog neural
 * network to predict the output from# the XOR operator.  This example
 * uses backpropagation to train the neural network.
 * 
 * This example attempts to use a minimum of Encog features to create and
 * train the neural network.  This allows you to see exactly what is going
 * on.  For a more advanced example, that uses Encog factories, refer to
 * the  example.
 * 
 */
public class Neural
{
	
	public class User {
		
		String fName, lName, publicIP;
		int yearOfBirth;
		
	}

	public static String printData(double[] row)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < row.length; i++)
		{
			sb.append(String.format("%.2f", row[i]));
			if(i!=row.length-1)
				sb.append(", ");
		}

		return sb.toString();
	}

	/**
	 * The input necessary for XOR.
	 */
	public static double INPUT[][] = { NeuralConstants.getRandomInputDataRow(),
			NeuralConstants.getRandomInputDataRow(),
			NeuralConstants.getRandomInputDataRow(),
			NeuralConstants.getRandomInputDataRow(),
			NeuralConstants.getRandomInputDataRow(),
			NeuralConstants.getRandomInputDataRow()};
	// public static double INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 }, { 0.0, 1.0 }, { 1.0, 1.0 } };



	/**
	 * The ideal data necessary for XOR.
	 */
	public static double IDEAL[][] = { NeuralConstants.YOUNG_MALE_IDEAL,
			NeuralConstants.YOUNG_FEMALE_IDEAL,
			NeuralConstants.MID_MALE_IDEAL,
			NeuralConstants.MID_FEMALE_IDEAL,
			NeuralConstants.OLD_MALE_IDEAL,
			NeuralConstants.OLD_FEMALE_IDEAL};
	// public static double IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };



	/**
	 * The main method.
	 * @param args No arguments are used.
	 */

	/** Create Statements and preparedStatements on this connection. */
	private static Connection db_conn;

	private static BasicNetwork network;

	private static MLDataSet trainingSet;

	static {
		initializeNetwork();
	}

	private static void initializeNetwork()
	{
		// create a neural network, without using a factory
		network = new BasicNetwork();

		// BasicLayer(ActivationFunction activationFunction, boolean hasBias, int neuronCount)
		network.addLayer(new BasicLayer(null, true, NeuralConstants.NODES_INPUT));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 20));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 20));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, NeuralConstants.NODES_OUTPUT));
		network.getStructure().finalizeStructure();
		network.reset();
	}

	public static void train(int user_id)
	{
		// create training data
		trainingSet = new BasicMLDataSet(INPUT, IDEAL);

		// train the neural network
		// final Backpropagation train = new Backpropagation(network, trainingSet);
		final ResilientPropagation train = new ResilientPropagation(network, trainingSet);

		int epoch = 1;

		do
		{
			train.iteration();
			System.out.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		}
		while(train.getError() > 0.01);
		train.finishTraining();

		predict();
	}

	private static void predict() {
		// TODO Auto-generated method stub

		// test the neural network
		System.out.println("Neural Network Results:");

		for(MLDataPair pair: trainingSet )
		{
			final MLData output = network.compute(pair.getInput());


			System.out.println("actual = " + printData(output.getData()) + 
					", ideal = " + printData(pair.getIdeal().getData()));
		}

	}

	public static void shutdown() {
		Encog.getInstance().shutdown();
	}

	/** Testing */
	public static void main(final String args[]) {

		train(1);
		// train(1);
		shutdown();		
	}
}