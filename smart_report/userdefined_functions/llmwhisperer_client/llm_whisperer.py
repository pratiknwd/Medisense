from unstract.llmwhisperer import LLMWhispererClientV2

# Provide the base URL and API key explicitly
llmw = LLMWhispererClientV2(base_url="https://llmwhisperer-api.us-central.unstract.com", api_key="2kxyaS_P-RvFrHSdAZlD2ZRAackOky3h6Hi5K6d-e5I")

def extract_text_from_pdf(file_path, pages_list=None):
    try:
        result = llmw.whisper(file_path=file_path, pages_to_extract=pages_list)
        extracted_text = result["extracted_text"]
        return extracted_text
    except LLMWhispererClientException as e:
        error_exit(e)

def main():
    file_path = "/workspaces/Medisense/smart_report/config_data/sterling-sample-report-1-3.pdf"
    pages_list = [1]
    extract_text_from_pdf(file_path, pages_list)


if __name__ == "__main__":
    main()