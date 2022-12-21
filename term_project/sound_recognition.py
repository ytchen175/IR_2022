import requests
import speech_recognition as sr
from pprint import pprint

def recognize_speech_from_mic(recognizer, microphone):
    response = {
        "success": True,
        "error": None,
        "transcription": None
    }

    if not isinstance(microphone, sr.Microphone):
        raise TypeError("`microphone` must be `Microphone` instance")

    with microphone as source:
        recognizer.adjust_for_ambient_noise(source) # handle ambient noise
        audio = recognizer.listen(source)

    try:
        response["transcription"] = recognizer.recognize_google(audio) # recognize voice
    except sr.RequestError:
        # API was unreachable or unresponsive
        response["success"] = False
        response["error"] = "API unavailable"
    except sr.UnknownValueError:
        # speech was unintelligible
        response["error"] = "Unable to recognize speech"

    return response

def query_es(keyword, page_from, page_size):
    ES_URL = "http://localhost:9200/warc/_search"

    j = {
        "query":{
            "match":{
                "title": f"{keyword}"
            }
        },
        "from": page_from,
        "size": page_size
    }

    r = requests.get(ES_URL, json=j)
    print("--- ElasticSearch response ---")
    # pprint(r.json())
    print(r.json())
    print("--- end ---")


if __name__ == "__main__":
    # create recognizer and mic instances
    r = sr.Recognizer()
    m = sr.Microphone()

    page_from = 0 # needs to be changed if we want to go next page
    page_size = 10

    print("Please say something !")

    res = recognize_speech_from_mic(r, m)

    keyword = res['transcription']

    print(f"Keyword `{keyword}` got, start searching...")

    query_es(keyword, page_from, page_size)

