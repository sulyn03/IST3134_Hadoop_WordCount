from pyspark.sql.functions import udf, explode, split, col
from pyspark.sql.types import StringType

# Read CSV file from S3 with header and schema inference
df = spark.read.csv("s3a://stacksample/Questions.csv", header=True, inferSchema=True)

word_counts_df = df.withColumn("CleanedTitle", preprocess_udf(col("Title"))) \
                  .withColumn("word", explode(split(col("CleanedTitle"), " "))) \
                  .filter(col("word") != "") \
                  .groupBy("word") \
                  .count() \

                  .sort(col("count").desc())

