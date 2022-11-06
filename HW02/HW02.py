import re
import math
import nltk

from nltk.corpus import stopwords
from collections import Counter
from tqdm.auto import tqdm

nltk.download('stopwords')
nltk.download('punkt')

sent_segmenter = nltk.data.load('tokenizers/punkt/english.pickle')
word_tokenizer = nltk.tokenize.regexp.WordPunctTokenizer()
stemmer = nltk.stem.porter.PorterStemmer()

inverted_index = eval(open('all_docs_freq_all_tokens_dict.txt','r').read())

NUM_OF_ALL_DOCS = 13173 # number of all documents (processed)

# ref: https://mathpretty.com/10661.html
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

# input queries
inp = input("Please enter your queries: ")
queries = inp.split()
print(f"queries: {queries}")

'''Calculate weight in term_weight_doc_id_dict'''
term_doc_in_all_document = 0

keys = list(inverted_index.keys())

term_weight_doc_id_dict = dict() # {'usernam':{'13185': 8.79, '13191': 27.10}, 'password':{'13191': 30.52}, 'becom': {'13194': 16.73}}

pb = tqdm(total=len(keys), nrows=4, position=0, leave=True)

for term_df_key in keys:
    term = re.findall('\w+', term_df_key)[0] # 'usernam, 2' -> 'usernam'
    term_doc_in_all_document = int(re.findall(', ([0-9]+)', term_df_key)[0]) # 'usernam, 2' -> 2

    temp_dict = dict()

    for i in range(len(inverted_index[term_df_key])):
        doc_raw_count_and_posting = list(inverted_index[term_df_key][i].keys())[0] # [{'13185,1': [0]}, {'13191,8': ...}...]

        doc_id = re.findall('([0-9]+),', doc_raw_count_and_posting)[0] # '13185,1' -> 13185 #doc的編號
        raw_count_in_doc = int(re.findall(',([0-9]+)', doc_raw_count_and_posting)[0]) # '13185,1' -> 1 #在這個doc出現幾次

        weight_i_j = calculate_weight(raw_count_in_doc, term_doc_in_all_document)

        temp_dict[doc_id] = weight_i_j

    pb.update(1) # update progress bar
    pb.set_description('Calculate weight in term_weight_doc_id_dict: ', refresh=True)

    term_weight_doc_id_dict[term] = temp_dict

pb.close()

terms = list(term_weight_doc_id_dict.keys())

corrected_queries_by_terms = [] # 'user name' -> 'username'
term_weight_for_tokenized_queries = {} # d_1, d_2, ..., d_j, {'13185': (8.79, 0.00), ...}
all_docs_ids_for_tokenized_queries = set() # ('13191', '13200')

q_vector = []
search_results = []

pb1 = tqdm(total=len(queries), nrows=4, position=0, leave=True)

for query in queries:
    query = stemmer.stem(query) # stemmize the query token

    if query in term_weight_doc_id_dict:
        corrected_queries_by_terms.append(query)
    else:
        # get most similar terms for user's query
        search_range = [t for t in terms if t[0] == query[0]] # optimize: reduce the search range by starts with same character

        for i in range(len(search_range)):
            if i == 0:
                edit_distance = nltk.edit_distance(query, search_range[i])
                corrected_queries_by_terms.append(search_range[i])

            tmp = nltk.edit_distance(query, search_range[i]) # find most similar for "correct" query tokens to terms by using Edit distance / Levenshtein distance

            if tmp < edit_distance:
                edit_distance = tmp

                if corrected_queries_by_terms:
                    corrected_queries_by_terms.pop()

                corrected_queries_by_terms.append(search_range[i]) # append most similar token into corrected_queries

    pb1.update(1) # update progress bar
    pb1.set_description(f'Search and preprocess possible terms for query token <{query}>: ', refresh=True)

pb1.close()

# get all query tokens doc id
for item in corrected_queries_by_terms:
    term_docs_weights = term_weight_doc_id_dict[item]
    all_docs_ids_for_tokenized_queries.update(set(term_docs_weights.keys()))


# create dict with all involved doc ids, like {'13191': [], '13185': []}
for doc_id in all_docs_ids_for_tokenized_queries:
    term_weight_for_tokenized_queries.update({doc_id: []})

print(f"corrected_queries_by_terms: {corrected_queries_by_terms}")

# can output {'13185': [8.79, 0.0, 0.0], '13191': [27.10, 30.52, 0.0], '13194': [0.0, 0.0, 16.73]}
pb2 = tqdm(total=len(corrected_queries_by_terms), nrows=4, position=0, leave=True)

for corrected_query in corrected_queries_by_terms:
    if corrected_query in term_weight_doc_id_dict:
        term_docs_weights = term_weight_doc_id_dict[corrected_query] # {'13185': 3.818654696160069, '13191': 7.26724351604199}

        for doc_id, weight in term_docs_weights.items():
            term_weight_for_tokenized_queries[doc_id].append(weight)

        need_to_0_padding = set(all_docs_ids_for_tokenized_queries) - set(term_docs_weights.keys())

        if need_to_0_padding:
            for k in need_to_0_padding:
                term_weight_for_tokenized_queries[k].append(0.0)

    pb2.update(1) # update progress bar
    pb2.set_description(f'Produce doc id weight vector for each query token: ', refresh=True)

pb2.close()

# calculate tokens weight in query
for query in queries:
    tf = queries.count(query)
    q_vector.append(calculate_weight(tf, 1))

for doc_id, d_vector in term_weight_for_tokenized_queries.items():
    similarity = cosine_similarity(d_vector, q_vector)

    search_results.append([doc_id, similarity])
search_results.sort(key=lambda x: x[1], reverse=True)

# print results
for r in search_results:
    print(r[0], r[1])