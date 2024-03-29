package ml;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class MachineLearning {

    public static void main(String[] args) {

        SparkConf conf = new SparkConf()
                .setAppName("Main")
                .setMaster("local[2]");
        SparkContext sc = new SparkContext(conf);
        sc.setLogLevel("ERROR");
        SparkSession session = new SparkSession(sc);

        String dataFile = "./temp/transformed/machine-learning/train/player_combinations.csv";
        Dataset<Row> dataset = session.read()
                .option("header", true)
                .option("delimiter", ";")
                .option("inferSchema", true)
                .csv(dataFile);

        Dataset<Row>[] splits = dataset.randomSplit(new double[] {0.7, 0.3});
        Dataset<Row> trainingData = splits[0];
        Dataset<Row> testData = splits[1];

        LinearRegression.execute(dataset, trainingData, testData);
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");
        DecisionTreeRegression.execute(dataset, trainingData, testData);
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");
        RandomForestRegression.execute(dataset, trainingData, testData);
    }
}
