import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.ml.regression.LinearRegressionTrainingSummary;
import org.apache.spark.ml.tuning.CrossValidator;
import org.apache.spark.ml.tuning.CrossValidatorModel;
import org.apache.spark.ml.tuning.ParamGridBuilder;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class LinearRegression {

    public static void execute(PCA pca, Dataset<Row> dataset, Dataset<Row> trainingData, Dataset<Row> testData) {

        org.apache.spark.ml.regression.LinearRegression lr = new org.apache.spark.ml.regression.LinearRegression();
        Pipeline pipeline = new Pipeline().setStages(getPipeline(pca, lr, dataset));

        ParamMap[] paramGrid = new ParamGridBuilder()
                .addGrid(pca.k(), new int[] {250, 300, 350})
                .addGrid(lr.maxIter(), new int[] {100})
                .addGrid(lr.regParam(), new double[] {0.25, 0.3, 0.35})
                .addGrid(lr.elasticNetParam(), new double[] {0.05, 0.1, 0.25})
                //.addGrid(lr.aggregationDepth(), new int[] {2})
                .build();

        RegressionEvaluator evaluator = new RegressionEvaluator().setMetricName("r2");

        CrossValidator cv = new CrossValidator()
                .setEstimator(pipeline)
                .setEvaluator(evaluator)
                .setEstimatorParamMaps(paramGrid)
                .setNumFolds(5);

        long millis = Instant.now().toEpochMilli();

        CrossValidatorModel cvModel = cv.fit(trainingData);

        PCA finalPca =  (PCA) ((Pipeline) cvModel.bestModel().parent()).getStages()[5];
        LinearRegressionModel finalLrModel = (LinearRegressionModel) ((PipelineModel) cvModel.bestModel()).stages()[6];

        System.out.println("Time: " + ((Instant.now().toEpochMilli() - millis) / 1000.0) + " segundos");

        //System.out.println("Coefficients: " + finalLrModel.coefficients() + " Intercept: " + finalLrModel.intercept());

        System.out.println("MEJOR CONFIGURACION -----------------------------------------------------------------------");

        System.out.println("K de PCA: " + finalPca.getK());
        System.out.println("maxIter: " + finalLrModel.getMaxIter());
        System.out.println("regParam: " + finalLrModel.getRegParam());
        System.out.println("elasticNetParam: " + finalLrModel.getElasticNetParam());
        //System.out.println("aggregationDepth: " + finalLrModel.getAggregationDepth());
        LinearRegressionTrainingSummary trainingSummary = finalLrModel.summary();
        System.out.println("r2: " + trainingSummary.r2());

        System.out.println("TEST --------------------------------------------------------------------------------------");

        Dataset<Row> transform = cvModel.transform(testData);
        System.out.println("Test: " + evaluator.evaluate(transform));

        System.out.println("-------------------------------------------------------------------------------------------");

        List<Row> testList = testData.select("label").collectAsList();
        List<Row> transformList = transform.select("prediction").collectAsList();

        int acierto = 0;
        int total = 0;

        for (int i = 0; i < testList.size(); i++) {
            Double realValue = (Double) testList.get(i).get(0);
            Double predictValue = (Double) transformList.get(i).get(0);

            if(realValue < 0.5 && realValue > -0.5 && predictValue < 0.5 && predictValue > -0.5) {
                acierto++;
            } else if (realValue >= 0.5 && predictValue >= 0.5) {
                acierto++;
            } else if (realValue <= -0.5 && predictValue <= -0.5) {
                acierto++;
            }

            total++;
        }

        System.out.println(acierto / (double) total);
    }

    private static PipelineStage[] getPipeline(PCA pca, org.apache.spark.ml.regression.LinearRegression lr, Dataset<Row> dataset) {

        List<String> otherAttributes = new ArrayList<>();

        for (String field : dataset.schema().fieldNames()) {
            if(!field.equals("label") && !field.equals("thisTeam") && !field.equals("otherTeam")) {
                otherAttributes.add(field);
            }
        }

        StringIndexer stringIndexer = new StringIndexer()
                .setInputCols(new String[] {"thisTeam", "otherTeam"})
                .setOutputCols(new String[] {"thisTeam-i", "otherTeam-i"});

        OneHotEncoder oneHotEncoder = new OneHotEncoder()
                .setInputCols(new String[] {"thisTeam-i", "otherTeam-i"})
                .setOutputCols(new String[] {"thisTeam-b", "otherTeam-b"})
                .setDropLast(false);

        VectorAssembler vectorAssembler1 = new VectorAssembler()
                .setInputCols(otherAttributes.toArray(new String[0]))
                .setOutputCol("assembledColumns");

        StandardScaler scaler = new StandardScaler().setInputCol("assembledColumns")
                .setOutputCol("scaledFeatures")
                .setWithStd(true)
                .setWithMean(true);

        List<String> finalFields = new ArrayList<>();
        finalFields.add("thisTeam-b");
        finalFields.add("otherTeam-b");
        finalFields.add("scaledFeatures");

        VectorAssembler vectorAssembler2 = new VectorAssembler()
                .setInputCols(finalFields.toArray(new String[0]))
                .setOutputCol("finalFeatures");

        return new PipelineStage[] {
                stringIndexer,
                oneHotEncoder,
                vectorAssembler1,
                scaler,
                vectorAssembler2,
                pca,
                lr
        };
    }
}
