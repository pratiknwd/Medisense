import google.generativeai as genai

def gemini_text(prompt, temprature=0, top_p=1, top_k=1, max_tokens=8000):
    genai.configure(api_key="AIzaSyB5h_FCHGPN_Fp-egPHqqxKU4NfHf6eqgs")
    generation_config = {
    "temperature": temprature,
    "top_p": top_p,
    "top_k": top_k,
    "max_output_tokens": max_tokens,
    }
    safety_settings = [
    {
        "category": "HARM_CATEGORY_HARASSMENT",
        "threshold": "BLOCK_NONE"
    },
    {
        "category": "HARM_CATEGORY_HATE_SPEECH",
        "threshold": "BLOCK_NONE"
    },
    {
        "category": "HARM_CATEGORY_SEXUALLY_EXPLICIT",
        "threshold": "BLOCK_NONE"
    },
    {
        "category": "HARM_CATEGORY_DANGEROUS_CONTENT",
        "threshold": "BLOCK_NONE"
    },
    ]
    model = genai.GenerativeModel(
        model_name="gemini-1.5-flash",
        generation_config=generation_config,
        safety_settings=safety_settings)
    response = model.generate_content(prompt)
    return response.text