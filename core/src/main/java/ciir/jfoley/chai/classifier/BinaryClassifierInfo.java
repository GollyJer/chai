package ciir.jfoley.chai.classifier;

import ciir.jfoley.chai.collections.Pair;

import java.io.Serializable;
import java.util.List;

/**
 * @author jfoley
 */
public class BinaryClassifierInfo implements Serializable {
  int numCorrect;
  int numTotal;

  public int numPredTruePositive;
  public int predPositive;
  int numTruePositive;

  int numPredTrueNegative;
  int predNegative;
  int numTrueNegative;

  int numIterations = 0;
  long time = 0; // time in ms.

  public float getAccuracy() {
    return numCorrect / (float) numTotal;
  }

  public float getPositivePrecision() {
    if (predPositive == 0) {
      return 0;
    }
    return numPredTruePositive / (float) predPositive;
  }

  public float getPositiveRecall() {
    return numPredTruePositive / (float) numTruePositive;
  }

  public float getPositiveF1() {
    if(predPositive == 0) return 0;
    float prec = getPositivePrecision();
    float recall = getPositiveRecall();
    return 2.0f * (prec * recall) / (prec + recall);
  }

  public float getNegativePrecision() {
    if(predNegative == 0) return 0;
    return numPredTrueNegative / (float) predNegative;
  }

  public float getNegativeRecall() {
    if(predNegative == 0) return 0;
    return numPredTrueNegative / (float) numTrueNegative;
  }

  public float getNegativeF1() {
    if(predNegative == 0) return 0;
    float prec = getNegativePrecision();
    float recall = getNegativeRecall();
    return 2.0f * (prec * recall) / (prec + recall);
  }

  public int getNumFalsePositives() {
    return predPositive - numPredTruePositive;
  }
  public int getNumFalseNegatives() {
    return predNegative - numPredTrueNegative;
  }

  public void update(boolean plabel, boolean label) {
    numTotal++;

    if (label) numTruePositive++;
    else numTrueNegative++;

    if (plabel) predPositive++;
    else predNegative++;

    if (label == plabel) {
      if (label) numPredTruePositive++;
      else numPredTrueNegative++;
      numCorrect++;
    }
  }

  public String toString() {
    return String.format("\tNumTotal: "+numTotal+" NumIters: "+numIterations+"\n"+
        "\tTP: %d, FP: %d, TN: %d, FN: %d\n" +
        "\tP: %1.3f, R: %1.3f, F1: %1.3f, Acc: %1.3f\n",
        numPredTruePositive,getNumFalsePositives(),numPredTrueNegative,getNumFalseNegatives(),
        getPositivePrecision(),getPositiveRecall(),getPositiveF1(), getAccuracy());
  }

  public static double computeAccuracy(List<Pair<Boolean, Double>> toEval, double cutoff) {
    BinaryClassifierInfo info = new BinaryClassifierInfo();
    info.update(toEval, cutoff);
    return info.getAccuracy();
  }

  public void update(List<Pair<Boolean, Double>> toEval, double cutoff) {
    for (Pair<Boolean, Double> pred : toEval) {
      update(pred.left, pred.right > cutoff);
    }
  }
}
