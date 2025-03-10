from presidio_analyzer import AnalyzerEngine
from presidio_anonymizer import AnonymizerEngine



def presidio_pii(text):
    analyzer = AnalyzerEngine()
    results = analyzer.analyze(text=text,
                            entities=["EMAIL_ADDRESS", "PHONE_NUMBER", "PERSON"],
                            language='en')
    anonymizer = AnonymizerEngine()
    anonymized_text = anonymizer.anonymize(text=text,analyzer_results=results)
    return anonymized_text.text
