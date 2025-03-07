import json
from ..generative_models.gemini_text import gemini_text
from ..generative_models.gemini_image import gemini_image

def StringtoJson(input_string):
    if input_string:
        input_string = input_string.replace('`', '').replace('json', '')
        input_string = input_string.strip('\n')
    try:
        json_output = json.loads(input_string)
    except:
        json_output = {}
    return json_output


def report_scan(image_file):
    """
    The action here involves parsing the given text and image within the input and generating a response in JSON format.
    """
    prompt = f"""You are Senior Doctor and an expert in analyzing health reports.
    The question is delimited by <input> and </input>. Your task is to generate a response in json with
    - \"test_name\" field MUST be test name mentioned in the report
    - \"test_value\" field MUST be value of respective test_name
    - \"units\": field MUST be units mentioned in report for respective test_name
    - \"bio reference interval\": field MUST be reference interval mentioned for respective test_name
    - \"minimum_value\": field MUST be minimum value of reference interval
     - \"maximum_value\": field MUST be maximum value of reference interval
     - \"explanation\": field MUST be explanation of respective test_name
    "\n\n"
    <input>
    report:{image_file}
    </input>
    answer"""
    return gemini_image(prompt, image_file)


# def report_analyze(df):
#     test_name_list = df['test_name'].tolist()
#     response_list = []
#     for test_name in test_name_list:
#         prompt = """
#         You are Senior Doctor and an expert in analyzing health reports. You are given a test_name
#         The question is delimited by <input> and </input>. Your task is to generate a response in json with
#         - \"test_name\" field MUST be test name mentioned in the list
#         - \"explanation\" field MUST be medical explanation of the respective test_name
#         - \"significance\": field MUST be significance of respective test_name
#         - \"recommendation\": field MUST be recommendation for the respective test_name
#         "\n\n"
#         <input>
#         {test_name_list}
#         </input>
#         answer"""
#         response_list.append(gemini_flash(prompt))
#     return response_list
