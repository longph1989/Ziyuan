/**
 * Copyright TODO
 */
package sav.common.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import sav.common.core.iface.HasProbabilityType;

/**
 * @author LLT
 * for centralization
 */
public final class Randomness {
	private Randomness() {}

	public static final long SEED = System.nanoTime();
	public static int totalCallsToRandom = 0;
	static Random random = new Random(SEED);
	
	static int callToDouble = 0;
	static int callToFloat = 0;
	
	private static Random getRandom() {
		totalCallsToRandom++;
		return random;
	}

	public static void reset(long newSeed) {
		random = new Random(newSeed);
	}

	public static boolean nextBoolean() {
		return getRandom().nextBoolean();
	}

	public static float nextFloat() {
		if (callToFloat < 5) {
			callToFloat++;
			return 0.0f;
		} else if (callToFloat < 10) {
			callToFloat++;
			return 0.01f;
		} else if (callToFloat < 20) {
			callToFloat++;
			return -0.01f;
		}
		
		float rangeMin = -100;
		float rangeMax = 100;
		float f = (rangeMin + (rangeMax - rangeMin) * getRandom().nextFloat());
		return f;
	}
	
	public static int nextInt() {
		return getRandom().nextInt();
	}
	
	public static Long nextLong() {
		return getRandom().nextLong();
	}
	
	/**
	 * return random value from 0 to i - 1 
	 */
	public static int nextInt(int i) {
		return getRandom().nextInt(i);
	}
	
	public static int nextInt(int min, int max) {
		return RandomUtils.nextInt(min, max, getRandom());
	}
	
	public static <T> T randomMember(T[] arr) {
		return RandomUtils.randomMember(arr, getRandom());
	}

	public static <T> T randomMember(List<T> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(nextInt(list.size()));
	}

	public static <T> List<T> randomSubList(List<T> allList) {
		return randomSubList(allList, nextInt(allList.size()));
	}
	
	public static <T> List<T> randomSubList(T[] allArr) {
		return randomSubList(Arrays.asList(allArr));
	}
	
	public static <T> List<T> randomSubListKeepOder(List<T> allList, int subSize) {
		List<T> sublist = new ArrayList<T>(allList);
		for (int i = 0; i < subSize; i++) {
			sublist.remove(nextInt(sublist.size()));
		}
		return sublist;
	}
	
	public static <T> List<T> randomSequence(T[] allArr, int seqSize) {
		return randomSequenceFixSize(Arrays.asList(allArr), seqSize);
	}
	
	public static <T> List<T> randomSequenceFixSize(List<T> allList, int seqSize) {
		List<T> seq = new ArrayList<T>(seqSize);
		for (int i = 0; i < seqSize; i++) {
			seq.add(allList.get(nextInt(allList.size())));
		}
		return seq;
	}
	
	public static <T> List<T> randomSequence(List<T> allList, int maxSeqSize) {
		if (allList.isEmpty()) {
			return allList;
		}
		return randomSequenceFixSize(allList, nextInt(maxSeqSize + 1));
	}
	
	public static <T> List<T> randomSubList(List<T> allList, int subSize) {
		Set<Integer> idexies = randomIdxSubList(allList, subSize);
		List<T> sublist = new ArrayList<T>(subSize);
		for (Integer idx : idexies) {
			sublist.add(allList.get(idx));
		}
		return sublist;
	}

	public static <T> Set<Integer> randomIdxSubList(List<T> allList, int subSize) {
		Set<Integer> sublist = new HashSet<Integer>(subSize);
		int n = allList.size();
		Assert.assertTrue(n >= subSize, "subSize is greater than allList size");
		int[] swaps = new int[allList.size()];
		for (int i = 0; i < subSize; i++) {
			int nextIdx = nextInt(n);
			int realIdx = swaps[nextIdx];
			while(realIdx != 0) {
				nextIdx = realIdx - 1;
				realIdx = swaps[realIdx - 1];
			}
			sublist.add(nextIdx);
			swaps[nextIdx] = n;
			n--;
		}
		return sublist;
	}
	
	public static <T> List<T> randomSubList1(List<T> allList, int subSize) {
		List<T> sublist = new ArrayList<T>();
		int n = allList.size();
		for (int i = 0; i < subSize; i++) {
			int nextIdx = nextInt(n);
			T ele = allList.get(nextIdx);
			int eIdx = sublist.indexOf(ele);
			while (eIdx >= 0) {
				ele = allList.get(allList.size() - 1 - eIdx);
				eIdx = sublist.indexOf(ele);
			}
			sublist.add(ele);
			n--;
		}
		return sublist;
	}
	
	public static boolean weighedCoinFlip(double trueProb) {
		return RandomUtils.weighedCoinFlip(trueProb, getRandom());
	}

	public static boolean randomBoolFromDistribution(double trueProb_, double falseProb_) {
		double falseProb = falseProb_ / (falseProb_ + trueProb_);
		return (getRandom().nextDouble() >= falseProb);
	}

	public static <T extends HasProbabilityType> T randomWithDistribution(
			T[] eles) {
		if (CollectionUtils.isEmpty(eles)) {
			return null;
		}
		int sum = 0;
		double[] distr = new double[eles.length];
		for (int i = 0; i < eles.length; i++) {
			sum += eles[i].getProb();
			distr[i] = (double) sum;
		}
		for (int i = 0; i < distr.length; i++) {
			distr[i] = distr[i] / sum;
		}
		double randVal = getRandom().nextDouble();
		for (int i = 0; i < distr.length; i++) {
			if (randVal < distr[i]) {
				return eles[i];
			}
		}
		return eles[eles.length - 1];
	}

	public static void nextBytes(byte[] bytes) {
		getRandom().nextBytes(bytes);
	}

	public static Double nextDouble() {
		if (callToDouble < 5) {
			callToDouble++;
			return 0.0;
		} else if (callToDouble < 10) {
			callToDouble++;
			return 0.01;
		} else if (callToDouble < 20) {
			callToDouble++;
			return -0.01;
		}
		
		double rangeMin = -100;
		double rangeMax = 100;
		
		double d = (rangeMin + (rangeMax - rangeMin) * getRandom().nextDouble());
		return d;
	}

}
