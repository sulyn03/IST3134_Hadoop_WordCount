import sys, re, nltk
from nltk.corpus import stopwords
from pyspark.sql.functions import udf, explode, split, col
from pyspark.sql.types import StringType

# Download stopwords and define the set
nltk.download('stopwords')
stopwords_set = set(stopwords.words('english'))

# Preprocessing UDF
def preprocess_text(text):
    if text is None: return ""
    text = re.sub(r'<[^>]+>', '', text).lower()
    text = re.sub('[^a-zA-Z\s]', ' ', text)
    tokens = text.split()
    filtered_tokens = [word for word in tokens if word not in stopwords_set and len(word) >= 3]
    return " ".join(filtered_tokens)

preprocess_udf = udf(preprocess_text, StringType())