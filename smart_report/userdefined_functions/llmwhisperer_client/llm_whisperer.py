import sys
from unstract.llmwhisperer.client import LLMWhispererClient, LLMWhispererClientException

llmw = LLMWhispererClient(base_url="https://llmwhisperer-api.us-central.unstract.com", api_key="2kxyaS_P-RvFrHSdAZlD2ZRAackOky3h6Hi5K6d-e5I")

def error_exit(error_message):
    print(error_message)
    sys.exit(1)

def extract_text_from_pdf(file_path, pages_list=None):
    try:
        result = llmw.whisper(file_path=file_path, pages_to_extract="1",)
        print("Result:", result)
        extracted_text = result["extracted_text"]
        return extracted_text
    except LLMWhispererClientException as e:
        error_exit(e)

def main():
    file_path = "/workspaces/Medisense/smart_report/config_data/lal_reports.pdf"
    extract_text_from_pdf(file_path)


if __name__ == "__main__":
    main()