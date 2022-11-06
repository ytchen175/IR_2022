import re
import math
import nltk

from nltk.corpus import stopwords
from collections import Counter
from tqdm.auto import tqdm

nltk.download('stopwords')

# initialize nltk tokenizer
nltk.download('punkt')
sent_segmenter = nltk.data.load('tokenizers/punkt/english.pickle')

word_tokenizer = nltk.tokenize.regexp.WordPunctTokenizer()

stemmer = nltk.stem.porter.PorterStemmer()

inverted_index = eval(open('all_docs_freq_all_tokens_dict.txt','r').read())

# ref: https://mathpretty.com/10661.html

NUM_OF_ALL_DOCS = 13173 # number of all documents (processed)

def calculate_weight(raw_count_in_doc, term_doc_in_all_document):
    """
    raw_count_in_doc : appear times of term in the doc (raw count)
    term_doc_in_all_document : number of docs which contains the term (processed)
    """
    idf = NUM_OF_ALL_DOCS / term_doc_in_all_document
    w_i_j = (1 + math.log10(raw_count_in_doc)) * math.log10(idf)

    return w_i_j

def cosine_similarity(vec_a, vec_b):
    assert len(vec_a) == len(vec_b), "Vectors needs to be same length"

    dot = sum(i * j for i, j in zip(vec_a, vec_b))
    a_norm = sum(i**2 for i in vec_a) ** 0.5
    b_norm = sum(i**2 for i in vec_b) ** 0.5

    cos_sim = dot / (a_norm * b_norm)

    return cos_sim

