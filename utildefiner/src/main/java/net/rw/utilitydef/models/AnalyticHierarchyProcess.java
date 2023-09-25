package net.rw.utilitydef.models;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.List;

public class AnalyticHierarchyProcess {

	double[][] rankings = null;
	int size = 0;
	double principal_eigvalue = 0;

	public AnalyticHierarchyProcess(int nCriteria) {

		size = nCriteria;
		rankings = new double[nCriteria][nCriteria];

		/* initialize to neutral */
		for (int i = 0; i < nCriteria; i++) {
			for (int j = 0; j < nCriteria; j++) {
				rankings[i][j] = 1.0;
			}
		}
	}

	public void rankPair(int critA, int critB, double ranking) {	
		if (critA != critB) {
			rankings[critA][critB] = ranking;
			rankings[critB][critA] = 1.0/ranking;
		}
	}

	/**
	 * Use this one if you initialized the object with the constructor and used rankPair to
	 * set the rankings.
	 * 
	 * @return
	 */
	public double[] getRankings() {
		double[] ret = null;		
		ret = getRankings(rankings);
		
		return ret;
	}

	public double[][] getPairwiseMatrix() {
		return rankings;
	}

	public double[] getRankings(double[][] ranks)  {			
		/*
		 * compute eigenvalues and eigenvectors
		 */
		EigenDecomposition eig = 
				new EigenDecomposition(new Array2DRowRealMatrix(ranks));

		/*
		 * find eigenvector corresponding to dominant real eigenvalue
		 */
		int m = 0;
		double[] vv = eig.getRealEigenvalues();

		for (int i = 1; i < vv.length; i++) {
			if (vv[i] > vv[m])
				m = i;
		}
		principal_eigvalue = vv[m];
		RealVector ret = eig.getEigenvector(m);
		double norm = ret.getL1Norm();
		ret = ret.mapDivide(norm).map(n -> Math.abs(n));//map(n -> Precision.round(n,3));

		return ret.toArray();
	}

	public boolean isConsistent() {
		double consistency_index = (principal_eigvalue-rankings.length)/(rankings.length-1);
		double consistency_ratio = consistency_index / 0.58; //need to adjust for n != 3
		return (consistency_ratio < 0.1);
	}

	public int size() {
		return size;
	}

	public static double min(double[] input) {
		double ret = input[0];
		for (int i = 1; i < input.length; i++)
			if (input[i] < ret)
				ret = input[i];
		return ret;
	}

	public static double max(double[] input) {
		double ret = input[0];
		for (int i = 1; i < input.length; i++)
			if (input[i] > ret)
				ret = input[i];
		return ret;
	}

	public static <T> void permutationsImpl(List<T> ori, List<List<T>> res, int d) {
		// iterate from current collection and copy 'current' element N times, one for each element
		if (d == ori.size()) {
			return;
		}
		List<T> other_elements = ori.subList(d+1, ori.size());
		for (int i = 0; i < other_elements.size(); i++) {
			T element = other_elements.get(i);
			List<T> copy = new ArrayList<T>();
			copy.add(ori.get(d));
			copy.add(element);
			res.add(copy);
		}
		permutationsImpl(ori, res, d + 1);
	}
}
