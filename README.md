# IST3134_Hadoop_WordCount
IST3134 Group Assignment Word Frequency Analysis on Stack Overflow Questions using Hadoop MapReduce and PySpark

# 📊 Big Data Analytics in the Cloud – Stack Overflow Word Count Analysis

**Course:** IST3134 – Big Data Analytics in the Cloud  
**Group Assignment | April 2025**  

**Team Members:**  
- Lim Su-Lyn (21032255)  
- Ng Jia Wen (20032231)  
- Yong Mae Jhin (20090759)  

---

## Project Overview

This project compares **Hadoop MapReduce (Java)** and **Apache Spark (PySpark)** for processing large-scale Stack Overflow question titles. The main task is a word count analysis to uncover common programming topics and developer pain points.

---

## Repository Structure
```bash
IST3134-Group-Assignment/
│
├── src/ # Java MapReduce source files
│ ├── WordCountMapper.java
│ ├── WordCountReducer.java
│ └── WordCountDriver.java
│
├── build/ # Compiled classes (generated locally)
├── wordcount.jar # Hadoop job JAR (generated locally)
│
├── scripts/
│ └── pyspark_wordcount.py # PySpark word count script
│
├── data/
│ └── Questions.csv # Sample dataset (not included; see instructions)
│
├── wordcount_final/ # Hadoop MapReduce output directory
│
├── README.md # This file
└── LICENSE # Optional license file
```

---

## Dataset

- **Source:** Kaggle – Stack Overflow Sample Dataset  
- **File:** `Questions.csv` (approx. 1 million records, ~1.9 GB)  
- **Used Field:** `Title` column for word count analysis  
- **Note:** Due to dataset size and licensing, the file is **not included** in this repository.
- **Dataset Link:** [Stack Overflow Sample Dataset on Kaggle](https://www.kaggle.com/datasets/stackoverflow/stacksample?select=Questions.csv)  
- **Download Instructions:**  
  1. Sign up at [Kaggle](https://www.kaggle.com/) and generate an API token (`kaggle.json`).  
  2. Place `kaggle.json` in the `~/.kaggle/` directory.  
  3. Download using the Kaggle API:
     ```bash
     mkdir kaggle
     mv kaggle.json kaggle/
     chmod 600 kaggle/kaggle.jsonexport KAGGLE_CONFIG_DIR=~/kaggle
     pip3 install kaggle --user
     kaggle datasets download -d stackoverflow/stacksample
     unzip stacksample.zip
     
---

## Project Versions

### 1. Hadoop MapReduce (Java)

A classic disk-based distributed computing implementation using Java MapReduce.

- **Files:** Located in `src/`  
- **Key Features:**  
  - Cleans and preprocesses titles (removes HTML, punctuation, stopwords)  
  - Counts words and outputs frequency counts  

### 2. Apache Spark (PySpark)

An in-memory distributed processing version using PySpark for faster performance.

- **Script:** `scripts/pyspark_wordcount.py`  
- **Key Features:**  
  - Uses Spark DataFrame APIs with UDF-based text cleaning  
  - Reads/writes data directly from/to AWS S3  
  - Outputs results as a single CSV  

---

## Setup and Usage

### Hadoop MapReduce

1. **Upload dataset to HDFS**  
   ```bash
   hdfs dfs -mkdir -p /user/hadoop/input
   hdfs dfs -put Questions.csv /user/hadoop/input/

2. **Compile and package Java code**
    ```bash
    mkdir -p build
    javac -classpath $(hadoop classpath) -d build src/*.java
    jar -cvf wordcount.jar -C build/ .
    
3. **Run the MapReduce Job**
    ```bash
    hadoop jar wordcount.jar WordCountDriver /user/hadoop/input/Questions.csv /user/hadoop/output/wordcount_final

4. **View top 100 results**
    ```bash
    hdfs dfs -cat /user/hadoop/output/wordcount_final/part-r-00000 | sort -k2 -nr | head -n 100

5. **Clean Output (if re-running)**
    ```bash
    hdfs dfs -rm -r /user/hadoop/output/wordcount_final

### PySpark Version

1. **Launch PySpark shell with AWS JARs for S3 access**
   ```bash
   pyspark --jars hadoop-aws-3.3.1.jar,aws-java-sdk-bundle-1.11.901.jar

2. **Run the PySpark script**
   ```bash
   python scripts/pyspark_wordcount.py
   
3. **The PySpark job reads from and writes to S3 (ensure proper IAM permissions and bucket paths).**

---

## Environment

- **AWS EC2 Instance:** Single-node setup running Hadoop and Spark.  
- **AWS S3 Bucket:** Used for storing raw dataset and output results.  
- **IAM Permissions:** EC2 instance must have proper S3 read/write access via IAM roles.  
- **Required JARs for Spark:**  
  - `hadoop-aws-3.3.1.jar`  
  - `aws-java-sdk-bundle-1.11.901.jar`

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

*This project was developed as part of the IST3134 course at Sunway University.*
