import re
import requests
import argparse
import time
from os import listdir
from os.path import isfile, join
from bs4 import BeautifulSoup
from pprint import pprint
from tqdm.auto import tqdm

def clean_text(text):
    # Remove newline characters and remove pure digit, Home\nHi 2 -> Home Hi
    concatenated_text = re.sub(r"[0-9]+", ' ', " ".join(text.split()))
    # Case folding and additional character -> ""
    processed_concatenated_text = re.sub(r"[^A-Za-z0-9]+", ' ', concatenated_text).lower()

    return processed_concatenated_text


def get_docs(f, docID):
    soup = BeautifulSoup(f, "html.parser")

    doc_info = {
        'docID': docID,
        'title': '',
        'h1': '',
        'body': '',
        'origin_body': ''
    }

    tag_list = ['title', 'h1', 'body', 'origin_body']

    for tag in tag_list:
        if tag == 'origin_body':
            tag = 'body'
            tag_text = soup.find(tag) # get text in <tag>

            if tag_text is None:
                all_text = soup.get_text()
                doc_info.update({'origin_body': all_text}) # all text in html
            else:
                origin_body_text = tag_text.get_text()
                doc_info.update({'origin_body': origin_body_text})
        else:
            tag_text = soup.find(tag) # get text in <tag>

            if tag_text is None:
                processed_text = clean_text(soup.get_text())
                doc_info.update({tag: processed_text}) # all text in html
            else:
                processed_text = clean_text(tag_text.get_text())
                doc_info.update({tag: processed_text})

    return doc_info


def request_and_get_res(method, url, json=None, print_res=False):
    if method == "GET":
        r = requests.get(url)
    elif method == "POST":
        r = requests.post(url, json=json)
    elif method == "PUT":
        r = requests.put(url, json=json)
    elif method == "DELETE":
        r = requests.delete(url)
    else:
        raise ValueError("Method cannot be recognized.")

    if print_res:
        print("--- ElasticSearch response ---")
        pprint(r.json())
        print("--- end ---")

    if r.status_code == 200 or r.status_code == 201:
        if print_res:
            print("*** Operation sucessed. ***")
        return True
    else:
        if print_res:
            print(f"*** Operation failed, got {r.status_code}. ***")
        print("--- ElasticSearch response ---")
        pprint(r.json())
        print("--- end ---")
        return False


def process_command():
    parser = argparse.ArgumentParser()
    parser.add_argument('-init','--initIndex', action="store_true", help='add index [warc] for elasticSearch')
    parser.add_argument('-add','--addData', action="store_true", help='add data [docs/*.html] for elasticSearch')
    return parser.parse_args()


def main(args):
    DOCS_PATH = 'docs'

    ES_URL = "http://localhost:9200/"
    INDEX_NAME = "warc"
    DATA_INGESTION_URL = INDEX_NAME + "/_doc/{}"

    try: 
        if request_and_get_res("GET", ES_URL):
            print('ElasticSearch found.')
        else:
            raise requests.exceptions.ConnectionError
    except requests.exceptions.ConnectionError:
        print('ElasticSearch not found, please start service or install it first.')

    if args.initIndex:
        print("Create index.")
        request_and_get_res("PUT", ES_URL + INDEX_NAME, json=None, print_res=True)

    elif args.addData:
        add_data_url = ES_URL + DATA_INGESTION_URL

        print("Delete index.")
        request_and_get_res("DELETE", ES_URL + INDEX_NAME, json=None, print_res=True)
        print("Create index.")
        request_and_get_res("PUT", ES_URL + INDEX_NAME, json=None, print_res=True)

        files = [f for f in listdir(DOCS_PATH) if isfile(join(DOCS_PATH, f))]
        # files = ['docID_00000.html', 'docID_00001.html'] # for test

        print("Adding data...")

        # initialize the progress bar
        p = tqdm(total=len(files), nrows=4, position=0, leave=True)

        for file in files:
            try:
                with open(f"{DOCS_PATH}/{file}", 'r', encoding="ISO-8859-1") as f:
                    docID = re.findall(r"[0-9]+", file)[0]

                    add_data_url = (ES_URL + DATA_INGESTION_URL).format(docID)
                    doc_info = get_docs(f, docID)

                    time.sleep(0.7)

                    success = request_and_get_res("POST", add_data_url, json=doc_info, print_res=False)

                    retry = 1

                    while not success:
                        if retry == 5:
                            raise requests.exceptions.ConnectionError("Data ingestion process failed at [{file}], passed.")
                        else:
                            time.sleep(2)
                            success = request_and_get_res("POST", add_data_url, json=doc_info, print_res=False)
                            print(f"{add_data_url} request failed, retry {retry} times.")
                        retry += 1
            except:
                print(f"Error occured at [{file}], passed.")

            p.set_description('Files to ElasticSearch: ', refresh=True)
            p.update(1) # update progress bar

        p.close()
        print("All data added.")
    else:
        print("Please specified operation flag [-init, -add] or use -h to get more information.")


if __name__ == "__main__":
    args = process_command()
    main(args)