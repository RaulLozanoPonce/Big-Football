import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.feature.PCA;
import org.apache.spark.ml.feature.StandardScaler;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.regression.RandomForestRegressionModel;
import org.apache.spark.ml.regression.RandomForestRegressor;
import org.apache.spark.ml.tuning.CrossValidator;
import org.apache.spark.ml.tuning.CrossValidatorModel;
import org.apache.spark.ml.tuning.ParamGridBuilder;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.ArrayList;
import java.util.List;

public class RandomForestRegression {

    public static void execute(PCA pca, Dataset<Row> dataset, Dataset<Row> trainingData, Dataset<Row> testData) {

        RandomForestRegressor lr = new RandomForestRegressor();
        Pipeline pipeline = new Pipeline().setStages(getPipeline(pca, lr, dataset));

        ParamMap[] paramGrid = new ParamGridBuilder()
                .addGrid(pca.k(), new int[] {70})
                .addGrid(lr.minInstancesPerNode(), new int[] {1})
                .addGrid(lr.maxDepth(), new int[] {8})
                .addGrid(lr.maxBins(), new int[] {6})
                .addGrid(lr.minInfoGain(), new double[] {0.03})
                .addGrid(lr.minWeightFractionPerNode(), new double[] {0.0})
                .addGrid(lr.checkpointInterval(), new int[] {1})
                .build();

        RegressionEvaluator evaluator = new RegressionEvaluator().setMetricName("r2");

        CrossValidator cv = new CrossValidator()
                .setEstimator(pipeline)
                .setEvaluator(evaluator)
                .setEstimatorParamMaps(paramGrid)
                .setNumFolds(5);

        CrossValidatorModel cvModel = cv.fit(trainingData);

        PCA finalPca =  (PCA) ((Pipeline) cvModel.bestModel().parent()).getStages()[2];
        RandomForestRegressionModel finalLrModel = (RandomForestRegressionModel) ((PipelineModel) cvModel.bestModel()).stages()[3];

        System.out.println("MEJOR CONFIGURACION -----------------------------------------------------------------------");

        System.out.println("K de PCA: " + finalPca.getK());
        System.out.println("minInstancesPerNode: " + finalLrModel.getMinInstancesPerNode());
        System.out.println("maxDepth: " + finalLrModel.getMaxDepth());
        System.out.println("maxBins: " + finalLrModel.getMaxBins());
        System.out.println("minInfoGain: " + finalLrModel.getMinInfoGain());
        System.out.println("minWeightFractionPerNode: " + finalLrModel.getMinWeightFractionPerNode());
        System.out.println("checkpointInterval: " + finalLrModel.getCheckpointInterval());

        System.out.println("TEST --------------------------------------------------------------------------------------");

        System.out.println("Test error: " + evaluator.evaluate(cvModel.transform(testData)));
    }

    private static PipelineStage[] getPipeline(PCA pca, RandomForestRegressor lr, Dataset<Row> dataset) {

        List<String> otherAttributes = new ArrayList<>();

        for (String field : dataset.schema().fieldNames()) {
            if(!field.equals("label")) {
                otherAttributes.add(field);
            }
        }

        VectorAssembler vectorAssembler1 = new VectorAssembler()
                .setInputCols(otherAttributes.toArray(new String[0]))
                .setOutputCol("assembledColumns");

        StandardScaler scaler = new StandardScaler().setInputCol("assembledColumns")
                .setOutputCol("scaledFeatures")
                .setWithStd(true)
                .setWithMean(true);

        return new PipelineStage[] {
                vectorAssembler1,
                scaler,
                pca,
                lr
        };
    }
}
