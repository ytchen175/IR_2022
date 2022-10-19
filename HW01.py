import re
from os import listdir
from os.path import isfile, join
from bs4 import BeautifulSoup
import nltk
from nltk.corpus import stopwords
from collections import Counter
from tqdm.auto import tqdm

def main():
    DOCS_PATH = 'docs'
    PROCESSED_DOCS_PATH = 'processed_docs'

    # ref : https://stackoverflow.com/questions/60269904/split-text-file-after-specific-line-in-python
    SECTION_START = re.compile(r'<!DOCTYPE html')
    SECTION_END = re.compile(r'</html>')

    def split_docs_iter(stream):
        def inner(stream):
            # Yields each line until an end marker is found (or EOF)
            for line in stream:
                if line and not SECTION_END.match(line):
                    yield line
                    continue
                break

        # Find a start marker, then break off into a nested iterator
        for line in stream:
            if line:
                if SECTION_START.match(line):
                    yield inner(stream)
                continue
            break
    filename = "03.warc"

    # split docs
    with open(filename, 'r', encoding="ISO-8859-1") as fh_in:
        for (i, nested_iter) in enumerate(split_docs_iter(fh_in)):
            with open('./docs/docID_{:05d}'.format(i), 'w', encoding='UTF-8') as fh_out:
                for line in nested_iter:
                    fh_out.write(line)

    files = [f for f in listdir(DOCS_PATH) if isfile(join(DOCS_PATH, f))]

    for file in files:
        try:
            with open(f"{DOCS_PATH}/{file}", 'r', encoding="ISO-8859-1") as f:
                soup = BeautifulSoup(f, "html.parser")

                # get text in <body>
                body_text = soup.find('body').getText()
                # Remove newline characters, Home\nHi -> Home Hi
                concatenated_body_text = " ".join(body_text.split())
                # Case folding, A -> a, additional character -> ""
                processed_concatenated_body_text = re.sub(r"[^A-Za-z0-9]+", ' ', concatenated_body_text).lower()

                with open(f"{PROCESSED_DOCS_PATH}/{file}_processed", mode="w", encoding="utf-8", errors='strict', buffering=1) as f1:
                    f1.write(processed_concatenated_body_text)
        # skip the docs which not have <body>
        except (OSError, AttributeError) as e:
            print(f"{file} don't have body")

    # download stopwords
    nltk.download('stopwords')

    # initialize nltk tokenizer
    nltk.download('punkt')
    sent_segmenter = nltk.data.load('tokenizers/punkt/english.pickle')

    word_tokenizer = nltk.tokenize.regexp.WordPunctTokenizer()

    stemmer = nltk.stem.porter.PorterStemmer()

    # all docs with all tokens dict
    all_docs_all_tokens_dict = {}
    # final key name
    each_term_in_each_doc_freq_dict = {}

    processed_files = [f for f in listdir(PROCESSED_DOCS_PATH) if isfile(join(PROCESSED_DOCS_PATH, f))]
    # processed_files = ['docID_00000_processed', 'docID_00001_processed'] # for test

    # initialize the progress bar
    p = tqdm(total=len(processed_files), nrows=4, position=0, leave=True)

    # traverse processed
    for p_file in processed_files:
        with open(f"{PROCESSED_DOCS_PATH}/{p_file}", 'r', encoding="ISO-8859-1") as f:
            contents = f.read()

            doc_num = re.findall(r"[0-9]+", p_file)[0]
            single_doc_tokens_dict = {}
            doc_freq = 0

            tokenized = word_tokenizer.tokenize(contents)
            # tokenized = ["usernam", "member", "usernam"] # for test

            tokenized_and_rm_stopwords_and_stemmed = [stemmer.stem(word) for word in tokenized if word not in stopwords.words('english') and not stemmer.stem(word).isnumeric()]
            tokens_dict = Counter(tokenized_and_rm_stopwords_and_stemmed)

            distinct_tokens = tokens_dict.keys()

            for term in distinct_tokens:
                pos_list = [i for i, x in enumerate(tokenized_and_rm_stopwords_and_stemmed) if x == term]
                term_freq = len(pos_list)
                inner_key_format = f"{doc_num},{term_freq}"

                # doc level
                # output format
                # d1 = {
                #  'salaka': [
                #     {'00000,2': [4, 35]}
                #     ],
                #  'time': [
                #     {'00000,2': [9, 98]}
                #     ]
                # }
                if term not in single_doc_tokens_dict:
                    single_doc_tokens_dict[term] = []

                single_doc_tokens_dict[term].append(
                    {inner_key_format: pos_list}
                )

                # all docs level
                # output format
                # d1 = {
                #  'salaka': [
                #     {'00000,2': [4, 35]}, {'00001,1': [36]}
                #     ],
                #  'time': [
                #     {'00000,2': [7, 10]}, {'00001,3': [9, 98, 100]}
                #     ]
                # }
                if term not in all_docs_all_tokens_dict:
                    all_docs_all_tokens_dict[term] = []
                    all_docs_all_tokens_dict.update(single_doc_tokens_dict)
                else:
                    all_docs_all_tokens_dict[term].append(
                        single_doc_tokens_dict[term][0]
                    )

            p.set_description('Files preprocessed needed: ', refresh=True)
            p.update(1) # update progress bar

    p.close()

    # initialize the progress bar
    pb = tqdm(total=len(all_docs_all_tokens_dict), nrows=4, position=0, leave=True)

    # prepare a dict like is: {'usernam': 'usernam, 4', 'member': 'member, 2'}
    for term, doc_and_doc_freq_pos_list in all_docs_all_tokens_dict.items():
        term_in_doc_freq = len(doc_and_doc_freq_pos_list)

        # for doc_and_doc_freq in doc_and_doc_freq_pos_list:
        #     each_doc_key_freq = int(list(doc_and_doc_freq.keys())[0].split(',')[1])
        #     term_in_doc_freq += each_doc_key_freq

        each_term_in_each_doc_freq_dict[term] = f'{term}, {term_in_doc_freq}'

        pb.set_description('Transforming output format: ', refresh=True)
        pb.update(1) # update progress bar

    pb.close()

    # update outer dict key count
    all_docs_freq_all_tokens_dict = dict((each_term_in_each_doc_freq_dict[key], value) for (key, value) in all_docs_all_tokens_dict.items())

    # save as file
    with open('all_docs_freq_all_tokens_dict.txt','w') as t:
        t.write(str(all_docs_freq_all_tokens_dict))

    print("all_docs_freq_all_tokens_dict successfully saved !")

if __name__ == '__main__':
    main()