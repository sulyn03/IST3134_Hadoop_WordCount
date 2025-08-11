import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    // Comprehensive stopword list (same as in your original)
    private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList(
        // --- Common English Stopwords ---
        "a", "an", "and", "are", "as", "at", "am", "be", "been", "but", "by", "can", "could",
        "did", "do", "does", "doing", "don", "down", "for", "from", "had", "has", "have",
        "having", "he", "her", "here", "hers", "herself", "him", "himself", "his", "how",
        "i", "if", "in", "into", "is", "it", "its", "itself", "just", "me", "more", "most",
        "must", "my", "myself", "no", "nor", "not", "of", "on", "only", "or", "other", "our",
        "ours", "ourselves", "out", "over", "own", "same", "she", "should", "so", "some",
        "such", "than", "that", "the", "their", "theirs", "them", "themselves", "then", "there",
        "these", "they", "this", "those", "through", "to", "too", "try", "trying", "tried", "under", "until", "up", "very",
        "was", "we", "were", "what", "when", "where", "which", "while", "who", "whom", "why",
        "will", "with", "would", "you", "your", "yours", "yourself", "yourselves", "way",

        // --- Informal / Contractions & Personal Pronouns ---
        "want", "like", "get", "got", "see", "know", "think", "im", "ive", "u", "am",
        "dont", "doesnt", "cant", "cannot", "sure",

        // --- HTML/XML Tags & Attributes ---
        "p", "br", "div", "span", "li", "ul", "ol", "pre", "code", "var", "img", "td", "tr", "table",

        // --- HTML Entities & Encoding Artifacts ---
        "amp", "lt", "gt", "quot", "apos", "nbsp", "copy", "reg", "ltdiv", "ltdivgt", "ltinput", "ltlt", "ampamp",

        // --- Web & URL Noise ---
        "www", "com", "org", "net",

        // --- Placeholder / Low-Value Words ---
        "one", "two", "first", "last", "next", "end", "now", "also", "well", "yes", "ok", "etc", "vs",

        // --- Stack Overflow & Programming Noise ---
        "stackoverflow", "question", "answer", "post", "vote", "comment", "tag",
        "file", "line", "input", "output", "run", "print", "code", "error", "issue", "problem",
        "help", "thanks", "thank", "hello", "hi",

        // --- Generic Tech/Product Names ---
        "java", "python", "javascript", "csharp", "php", "ruby", "windows", "linux", "mac", "sql", "mysql",
        "oracle", "android", "ios", "chrome", "firefox", "edge", "git", "github",

        // --- Redundant Terms ---
        "app", "application", "project",

        // --- Junk Data / Artefacts ---
        "tznahow",

        // --- Single Letters / Likely Noise ---
        "x", "y", "s", "d", "f", "l", "e", "n", "t", "b", "v", "i", "j", "k"
    ));

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();

        // Skip header row
        if (key.get() == 0 && line.startsWith("Id,OwnerUserId,CreationDate,ClosedDate,Score,Title,Body")) {
            return;
        }

        // Split CSV into fields (limit to 7 to preserve Body as last field)
        String[] fields = line.split(",", 7);

        // Ensure at least 6 fields exist (Title is index 5)
        if (fields.length < 6) {
            return;
        }

        String title = fields[5]; // Extract Title column only

        // Remove HTML tags
        title = title.replaceAll("<[^>]+>", " ");

        // Keep only letters and spaces, then lowercase
        title = title.replaceAll("[^a-zA-Z\\s]", " ").toLowerCase();

        // Tokenize and filter
        StringTokenizer tokenizer = new StringTokenizer(title);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (token.isEmpty() || STOPWORDS.contains(token)) {
                continue;
            }
            word.set(token);
            context.write(word, one);
        }
    }
}
