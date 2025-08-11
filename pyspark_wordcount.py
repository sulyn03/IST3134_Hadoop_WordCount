from pyspark.sql.functions import udf, explode, split, col
from pyspark.sql.types import StringType

word_counts_df = df.withColumn("CleanedTitle", preprocess_udf(col("Title"))) \
                  .withColumn("word", explode(split(col("CleanedTitle"), " "))) \
                  .filter(col("word") != "") \
                  .groupBy("word") \
                  .count() \

                  .sort(col("count").desc())
